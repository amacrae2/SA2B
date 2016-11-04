__author__ = 'alecmacrae'

import argparse
import trueskill

from trueskill_util import update_trueskills, find_match_quality
from sql_util import get_sql_cursor, get_sql_db
from ranks import get_ranks, print_rank_moves
from print_colors import bcolors


def update_truescore(course, chaos):
    db = get_sql_db()
    trueskill_ratings = []
    for i in range(0, len(chaos)):
        cursor = get_sql_cursor(db)
        cursor.execute("SELECT name, mu, sigma FROM trueskill WHERE name = '{}' AND course = '{}';".format(chaos[i],
                                                                                                           course))
        numchao = int(cursor.rowcount)
        assert numchao == 1, numchao
        row = cursor.fetchone()
        trueskill_ratings.append(trueskill.Rating(row[1], row[2]))
    match_quality = find_match_quality(trueskill_ratings)
    new_trueskills = update_trueskills(trueskill_ratings)
    cursor = get_sql_cursor(db)
    for i in xrange(len(new_trueskills)):
        cursor.execute(
            "UPDATE trueskill SET mu = {}, sigma = {} WHERE name = '{}' AND course = '{}';".format(
                                                                                        new_trueskills[i].mu,
                                                                                        new_trueskills[i].sigma,
                                                                                        chaos[i],
                                                                                        course))
        db.commit()
    return match_quality


def add_result_to_table(table_name, course, chaos, db_name):
    db = get_sql_db(db=db_name)
    cursor = get_sql_cursor(db)
    cursor.execute("INSERT INTO {} (course,first_place,second_place,third_place,fourth_place)"
                   " VALUES ('{}','{}','{}','{}','{}');".format(*[table_name, course] + chaos))
    db.commit()


if __name__ == "__main__":
    parser = argparse.ArgumentParser(formatter_class=argparse.ArgumentDefaultsHelpFormatter)
    parser.add_argument('course', help="course race took place on")
    parser.add_argument('chao1', help="first place chao")
    parser.add_argument('chao2', help="second place chao")
    parser.add_argument('chao3', help="third place chao")
    parser.add_argument('chao4', help="fourth place chao")
    parser.add_argument('--add', help="adds the results to the records db, should only be done if race was 'random'",
                        action='store_const', const=True)
    parser.add_argument('--omit', help="omits the record from any db", action='store_const', const=True)
    parser.add_argument('--db', help="sql database to use", default="Chao")
    args = parser.parse_args()
    chaos = [args.chao1, args.chao2, args.chao3, args.chao4]

    old_ranks = get_ranks(args.course)
    match_quality = update_truescore(args.course, chaos)
    new_ranks = get_ranks(args.course)
    print_rank_moves(old_ranks, new_ranks, args.course)
    print "{}Match Quality = {}%{}".format(bcolors.OKBLUE, round(match_quality, 2), bcolors.ENDC)

    if not args.omit:
        add_result_to_table("records_all", args.course, chaos, args.db)
        if args.add:
            add_result_to_table("records", args.course, chaos, args.db)
