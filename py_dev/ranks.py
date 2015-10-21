__author__ = 'alecmacrae'

from collections import namedtuple
import ranking

from sql_util import get_sql_cursor, get_sql_db

ChaoSkill = namedtuple("ChaoSkill", "name mu sigma")


def get_ranks(course):
    db = get_sql_db()
    cursor = get_sql_cursor(db)

    cursor.execute("SELECT name, mu, sigma FROM truescore WHERE course = '{}' ORDER BY mu DESC;".format(course))

    numchao = int(cursor.rowcount)
    chao_ranks = []
    mus = []
    sigmas = []
    chao_names = []
    for i in range(0, numchao):
        row = cursor.fetchone()
        chao_ranks.append(ChaoSkill(name=row[0], mu=row[1], sigma=row[2]))
        # chao_names.append(row[0])
        # mus.append(row[1])
        # sigmas.append(row[2])

    return chao_ranks


def print_rank_moves(old_ranks, new_ranks):
    for i in xrange(len(new_ranks)):
        print_rank_move(new_ranks, old_ranks, i, i, "", 0)


def print_rank_move(new_ranks, old_ranks, new_index, old_index, highlight, movement):
    if old_ranks[old_index].name == new_ranks[new_index].name:
        print "{}\t{}\t".format(new_index, new_ranks[new_index].name, new_ranks[new_index].mu, new_ranks[new_index].sigma, highlight+str(movement))
        return True
    elif old_index<0:
        return False
    else:
        status = print_rank_move(new_ranks, old_ranks, new_index, old_index-1, )