# coding=utf-8
from models import User


def add_user():
    username = 'admin'
    password = '123456'
    user = User(username)
    user.password = password


if __name__ == '__main__':
    add_user()