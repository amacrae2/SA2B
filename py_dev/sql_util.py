__author__ = 'alecmacrae'

import MySQLdb


def get_sql_db(host="localhost", user="root", passwd="", db="Chao"):
    return MySQLdb.connect(host=host, user=user, passwd=passwd, db=db)

def get_sql_cursor(db):
    return db.cursor()