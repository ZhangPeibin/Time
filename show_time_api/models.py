# coding=utf-8
from werkzeug.security import generate_password_hash
from werkzeug.security import check_password_hash
from flask_login import UserMixin
import json
import uuid

PROFILE_FILE = "profiles.json"


class User(UserMixin):
    """
        username = [password_hash,id]
    """
    def __init__(self, username):
        self.username = username
        self.id = self.get_id()
        self.password_hash = None

    @property
    def password(self):
        raise AttributeError('password is not readable')

    @password.setter
    def password(self, password):
        self.password_hash = generate_password_hash(password)
        with open(PROFILE_FILE, 'w+') as f:
            try:
                profiles = json.load(f)
            except:
                profiles = {}

            profiles[self.username] = [self.password_hash, self.id]
            f.write(json.dumps(profiles))

    def verify_password(self, password):
        """ 验证用户输入的账号密码
        :param password:
        :return:
        """
        password_hash = self.get_password_hash()
        if password_hash is None:
            return False
        return check_password_hash(password_hash, password)

    def get_password_hash(self):
        try:
            with open(PROFILE_FILE, 'r+') as fp:
                user_profiles = json.load(fp)
                user_info = user_profiles.get(self.username, None)
                if user_info is not None:
                    return user_info[0]
        except IOError:
            return None
        except ValueError:
            return None
        return None

    def get_id(self):
        if self.username is not None:
            try:
                with open(PROFILE_FILE) as f:
                    user_profiles = json.load(f)
                    if self.username in user_profiles:
                        return user_profiles[self.username][1]
            except:
                pass

        return unicode(uuid.uuid4())

    @staticmethod
    def get(user_id):
        if not user_id:
            return None

        try:
            with open(PROFILE_FILE) as f:
                user_profiles = json.load(f)
                for name, info in user_profiles.iteritems():
                    if info[1] == user_id:
                        return User(name)
        except:
            return None
        return None


if __name__ == '__main__':
    a = generate_password_hash('123456')
    b = generate_password_hash('123456')
    print check_password_hash(a, '123456')
    print check_password_hash(b, '123456')
