#!/usr/bin/env python
# coding=utf-8
import sqlite3
import utils

db_name = "st.db"
user_table = "user"
transaction_table = "transcation_history"
active_table = "actives"
friend_table = "friend"
like_table = "like"
comment_table = "comment_table"


class User_column:

    def __init__(self):
        pass

    PHONE = "phone"
    PASSWORD = "password"
    TOKEN = "token"
    TIME = "time"
    BLUE_COIN = "blue_coin"
    ORANGE_COIN = "orange_coin"
    HEAD = "head"
    TITLE = "title"
    PROFILE = "profile"
    TYPE = "type"
    MOBILE = "mobile"
    CARD = "card"


class Transaction_column:

    def __init__(self):
        pass

    PHONE = "phone"
    # pay_type = 1 代表收入, pay_type = 0 代表支出
    PAY_TYPE = "pay_type"
    PAY_REASON = "reson"
    COUNT = "count"
    URL = "url"


class Active_column:

    def __init__(self):
        pass

    ID = "id"
    PHONE = "phone"
    TITLE = "title"
    PROFILE = "profile"
    COST = "cost"
    ADDRESS = "address"
    DETAILS_ADDRESS = "details_address"
    TIME = "time"
    URL = "url"
    TYPE = "type"
    POST_TIME = "post_time"


class Active_like_column:

    def __init__(self):
        pass

    ID = "id"
    PHONE = 'phone'
    NICK_NAME = "nick_name"


class Active_comment_column:

    def __init__(self):
        pass

    ID = "id"
    COMMENT_PEOPLE = "comment_people"
    CONTENT = "content"


class Friend_column:

    def __init__(self):
        pass

    PHONE = "phone"
    R_PHONE = "r_phone"


class SqlHelper(object):

    def __init__(self):
        self.__conn__ = sqlite3.connect(db_name)
        self.__conn__.execute('''CREATE TABLE IF NOT EXISTS %s  \
                (%s TEXT, %s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT)''' %
                              (user_table, User_column.PHONE, User_column.PASSWORD,
                               User_column.TOKEN, User_column.TIME, User_column.BLUE_COIN,
                               User_column.ORANGE_COIN, User_column.TITLE, User_column.PROFILE, User_column.HEAD,
                               User_column.TYPE, User_column.MOBILE, User_column.CARD))

        self.__conn__.execute('''CREATE TABLE IF NOT EXISTS %s  \
                        (%s TEXT, %s TEXT,%s TEXT,%s TEXT,%s TEXT)''' %
                              (transaction_table, Transaction_column.PHONE, Transaction_column.PAY_REASON,
                               Transaction_column.COUNT, Transaction_column.PAY_TYPE, Transaction_column.URL))

        self.__conn__.execute('''CREATE TABLE IF NOT EXISTS %s  \
                                (%s integer PRIMARY KEY autoincrement ,
                                 %s TEXT, %s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT)''' %
                              (active_table, Active_column.ID, Active_column.PHONE, Active_column.TITLE,
                               Active_column.PROFILE, Active_column.COST, Active_column.ADDRESS,
                               Active_column.DETAILS_ADDRESS, Active_column.TIME
                               , Active_column.URL, Active_column.TYPE, Active_column.POST_TIME))

        self.__conn__.execute('''CREATE TABLE IF NOT EXISTS %s  \
                                (%s TEXT, %s TEXT)''' % (friend_table, Friend_column.PHONE, Friend_column.R_PHONE))

        self.__conn__.execute('''CREATE TABLE IF NOT EXISTS %s  \
                                        (%s TEXT, %s TEXT, %s TEXT)''' % (like_table,
                                                                          Active_like_column.ID,
                                                                          Active_like_column.PHONE,
                                                                          Active_like_column.NICK_NAME))

        self.__conn__.execute('''CREATE TABLE IF NOT EXISTS %s  \
                                        (%s TEXT, %s TEXT, %s TEXT)''' % (comment_table, Active_comment_column.ID,
                                                                          Active_comment_column.COMMENT_PEOPLE,
                                                                          Active_comment_column.CONTENT))


class UserHelper(SqlHelper):

    def save_user(self, phone, password, token, time):
        self.__conn__.execute('INSERT INTO %s (%s,%s,%s,%s,%s,%s) VALUES (?,?,?,?,?,?)' \
                              % (user_table, User_column.PHONE, User_column.PASSWORD,
                                 User_column.TOKEN, User_column.TIME, User_column.BLUE_COIN, User_column.ORANGE_COIN),
                              (phone, password, token, time, 0.00, 0.00))
        self.__conn__.commit()

    def save_user_info(self, title, profile, head, type, mobile, card, token):
        self.__conn__.execute("UPDATE  %s set %s = '%s',%s = '%s',%s='%s',%s='%s',%s='%s',%s='%s' where %s like '%s' "
                              % (user_table, User_column.TITLE, title,
                                 User_column.PROFILE, profile,
                                 User_column.HEAD, head,
                                 User_column.TYPE, type,
                                 User_column.MOBILE, mobile,
                                 User_column.CARD, card,
                                 User_column.TOKEN, token))
        self.__conn__.commit()

    def save_user_token(self, phone, token):
        self.__conn__.execute("UPDATE  %s set %s = '%s' where %s like '%s' " \
                              % (user_table, User_column.TOKEN, token, User_column.PHONE, phone))
        self.__conn__.commit()

    def get_user(self, phone):
        cur = self.__conn__.execute("SELECT * from %s where %s = %s" %
                                    (user_table, User_column.PHONE, phone))
        return list(cur)

    def get_user_by_token(self, token):
        cur = self.__conn__.execute("SELECT * from %s where %s like '%s'" %
                                    (user_table, User_column.TOKEN, token))
        return list(cur)

    def save_user_coin(self, token, blue_coin, orange_coin):
        self.__conn__.execute("UPDATE  %s set %s = %s , %s=%s  where %s like '%s' " \
                              % (user_table, User_column.BLUE_COIN, blue_coin, User_column.ORANGE_COIN, orange_coin,
                                 User_column.TOKEN, token))
        self.__conn__.commit()

    def save_transaction_history(self, phone, reason, count, pay_type, url):
        self.__conn__.execute('INSERT INTO %s (%s,%s,%s,%s,%s) VALUES (?,?,?,?,?)' \
                              % (transaction_table, Transaction_column.PHONE,
                                 Transaction_column.PAY_REASON,
                                 Transaction_column.COUNT, Transaction_column.PAY_TYPE, Transaction_column.URL),
                              (phone, reason, count, pay_type, url))
        self.__conn__.commit()

    def get_transaction_history(self, phone):
        cur = self.__conn__.execute("SELECT * from %s where %s like '%s'" %
                                    (transaction_table, Transaction_column.PHONE, phone))
        return list(cur)

    def save_friend(self, phone, r_phone):
        sql = 'INSERT INTO %s (%s,%s) VALUES (?,?)' % (friend_table, Friend_column.PHONE,
                                                       Friend_column.R_PHONE)
        self.__conn__.execute(sql, (phone, r_phone))
        self.__conn__.commit()

    def get_friend(self, phone):
        cur = self.__conn__.execute("SELECT * from %s where %s like '%s'" %
                                    (friend_table, Friend_column.PHONE, phone))
        return list(cur)

    def save_active(self, phone, title, profile, cost, address, details_address, time, url, active_type):
        self.__conn__.execute('INSERT INTO %s (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s) VALUES (?,?,?,?,?,?,?,?,?,?,?)' \
                              % (active_table, Active_column.ID, Active_column.PHONE, Active_column.TITLE,
                                 Active_column.PROFILE, Active_column.COST, Active_column.ADDRESS,
                                 Active_column.DETAILS_ADDRESS, Active_column.TIME
                                 , Active_column.URL, Active_column.TYPE, Active_column.POST_TIME),
                              (None, phone, title, profile,
                               cost, address, details_address, time, url, active_type, str(utils.get_current_time())))
        self.__conn__.commit()

    def get_active(self, active_type):
        if int(active_type) == 0:
            cur = self.__conn__.execute("SELECT * from %s" %
                                        active_table)
        else:
            cur = self.__conn__.execute("SELECT * from %s where %s like '%s'" %
                                        (active_table, Active_column.TYPE, active_type))
        return list(cur)

    def get_active_with_area(self, active_type, city):
        if int(active_type) == 0:
            cur = self.__conn__.execute("SELECT * from %s where %s like '%s' " %
                                        (active_table, Active_column.ADDRESS, city))
        else:
            cur = self.__conn__.execute("SELECT * from %s where %s like '%s' and %s like '%s' " %
                                        (active_table, Active_column.TYPE, active_type, Active_column.ADDRESS, city))
        return list(cur)

    def get_my_friend_active(self, phone):
        # select active_table from active_table where phone in (select rphone from friend where phone like phone
        sql = "SELECT * from %s,%s where %s IN (SELECT %s from %s where %s like '%s') and %s like %s " % \
              (active_table, user_table, active_table + "." + Active_column.PHONE, Friend_column.R_PHONE, friend_table,
               Friend_column.PHONE, phone, active_table + "." + Active_column.PHONE,
               user_table + "." + User_column.PHONE)
        print sql
        cur = self.__conn__.execute(sql)
        return list(cur)

    def like_active(self, active_id, phone, nick_name):
        sql = 'INSERT INTO %s (%s,%s,%s) VALUES (?,?,?)' % (like_table, Active_like_column.ID,
                                                            Active_like_column.PHONE, Active_like_column.NICK_NAME)
        self.__conn__.execute(sql, (active_id, phone, nick_name))
        self.__conn__.commit()

    def get_active_like(self, active_id):
        sql = "SELECT * from %s where %s like '%s'" % \
              (like_table, Active_like_column.ID, active_id)
        cur = self.__conn__.execute(sql)
        return list(cur)

    def comment_active(self, active_id, phone, content):
        sql = 'INSERT INTO %s (%s,%s,%s) VALUES (?,?,?)' % (comment_table, Active_comment_column.ID,
                                                            Active_comment_column.COMMENT_PEOPLE,
                                                            Active_comment_column.CONTENT)
        self.__conn__.execute(sql, (active_id, phone, content))
        self.__conn__.commit()

    def get_active_comment(self, active_id):
        sql = "SELECT * from %s where %s like '%s'" % \
              (comment_table, Active_comment_column.ID, active_id)
        cur = self.__conn__.execute(sql)
        return list(cur)


if __name__ == "__main__":
    db = UserHelper()
    print db.get_friend(18589072870)
    print db.get_my_friend_active("18589072870")
