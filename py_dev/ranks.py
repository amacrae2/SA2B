__author__ = 'alecmacrae'

from collections import namedtuple
import ranking

from sql_util import get_sql_cursor, get_sql_db
from print_colors import bcolors

ChaoSkill = namedtuple("ChaoSkill", "rank name mu sigma")


def get_ranks(course):
    db = get_sql_db()
    ranks = find_ranks(db, course)
    cursor = get_sql_cursor(db)

    cursor.execute("SELECT name, mu, sigma FROM truescore WHERE course = '{}' ORDER BY mu DESC;".format(course))

    numchao = int(cursor.rowcount)
    assert numchao == len(ranks), "numchao {} != num ranks {}".format(numchao, len(ranks))
    chao_ranks = []
    for i in range(0, numchao):
        row = cursor.fetchone()
        chao_ranks.append(ChaoSkill(rank=ranks[i], name=row[0], mu=row[1], sigma=row[2]))
    return chao_ranks


def find_ranks(db, course):
    cursor = get_sql_cursor(db)

    cursor.execute("SELECT mu FROM truescore WHERE course = '{}' ORDER BY mu DESC;".format(course))

    numchao = int(cursor.rowcount)
    mus = []
    for i in range(0, numchao):
        row = cursor.fetchone()
        mus.append(row[0])
    ranks = list(ranking.Ranking(mus))
    return ranks


def print_rank_moves(old_ranks, new_ranks):
    for i in xrange(len(new_ranks)):
        success = print_rank_move(new_ranks, old_ranks, i, i, bcolors.WARNING, 0)
        if not success:
            increment = 0
            while True:
                increment += 1
                if print_rank_move(new_ranks, old_ranks, i, i+increment, bcolors.FAIL, increment) or \
                   print_rank_move(new_ranks, old_ranks, i, i-increment, bcolors.OKGREEN, increment):
                    break


def print_rank_move(new_ranks, old_ranks, new_index, old_index, highlight, movement):
    if 0 <= old_index < len(old_ranks) and old_ranks[old_index].name == new_ranks[new_index].name:
        print "{}\t{}\t{}\t{}\t{}".format(new_ranks[new_index].rank, new_ranks[new_index].name, new_ranks[new_index].mu,
                                          new_ranks[new_index].sigma, highlight+str(movement)+bcolors.ENDC)
        return True
    else:
        return False
