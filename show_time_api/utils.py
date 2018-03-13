#!/usr/bin/env python
# coding=utf-8
import hashlib
import time
import datetime


def get_md5(from_str):
    m2 = hashlib.md5()
    m2.update(from_str)
    return m2.hexdigest()


def get_current_time():
    return long(round(time.time() * 1000))