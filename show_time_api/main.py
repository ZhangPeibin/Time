#!/usr/bin/env python
# coding=utf-8
import sys
import sql
import utils
from flask import Flask, url_for, request, jsonify, render_template, redirect
from werkzeug.utils import secure_filename
from os import path, mkdir

reload(sys)
sys.setdefaultencoding("utf-8")

NET_ADDRESS = "http://192.168.1.146:5000"
ALLOWED_EXTENSIONS = set(['png', 'jpg', 'jpeg', 'gif'])
UPLOAD_FOLDER = './static/uploads'
if not path.exists(UPLOAD_FOLDER):
    mkdir(UPLOAD_FOLDER)

app = Flask(__name__)
app.debug = True
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER
app.config['MAX_CONTENT_LENGTH'] = 16 * 1024 * 1024
token_time = 60 * 1000 * 60 * 24


"""
    resp code :
     0  :   success
     -1 :   error 
     -2 :   no_login or no_register
"""


@app.route("/weblogin", methods=['GET', 'POST'])
def web_login():
    error = None
    if request.method == 'POST':
        username = request.form['username']
        passwd = request.form['password']
        print username
        print passwd
        return redirect(url_for('list'))
    return render_template("login.html")


@app.route("/list")
def list():
    return '<h1>list</h1>'


@app.route("/", methods=['GET', 'POST'])
def home():
    return '<h1>Home</h1>'


@app.route("/register", methods=['GET'])
def api_register():
    if "phone" not in request.args:
        error = {"status": -1, "message": u"非法请求(未找到phone字段)"}
        resp = jsonify(error)
        return resp

    if "password" not in request.args:
        error = {"status": -1, "message": u"非法请求(未找到password字段)"}
        resp = jsonify(error)
        return resp

    phone = request.args['phone']
    psd = request.args['password']
    db = sql.UserHelper()
    user = db.get_user(phone)
    ct = utils.get_current_time()
    if len(user) == 0:
        time = ct + token_time
        token = utils.get_md5(phone + psd + str(time))
        db.save_user(phone, psd, token, time)
        res = {"token": token, "time": time, "message": u"注册成功", "phone": phone}
        db.save_user_coin(token, 30, 0)
        return jsonify(res)
    else:
        error = {"status": -1, "message": u"账号已存在"}
        return jsonify(error)


@app.route("/login", methods=['GET'])
def api_login():
    if "phone" not in request.args:
        error = {"status": -1, "message": u"非法请求(未找到phone字段)"}
        resp = jsonify(error)
        return resp

    if "password" not in request.args:
        error = {"status": -1, "message": u"非法请求(未找到password字段)"}
        resp = jsonify(error)
        return resp

    phone = request.args['phone']
    psd = request.args['password']
    db = sql.UserHelper()
    user = db.get_user(phone)
    ct = utils.get_current_time()
    if len(user) == 0:
        res = {"status": -1, "message": u"用户不存在"}
        return jsonify(res)
    else:
        user = user[0]
        phone = user[0]
        password = user[1]
        token = user[2]
        time = user[3]
        title = user[6]
        profile = user[7]
        head = user[8]
        if not password == psd:
            error = {"status": -1, "message": u"密码错误"}
            return jsonify(error)
        else:
            if ct > long(time):
                time = ct + token_time
                token = utils.get_md5(phone + psd + str(time))
                db.save_user_token(phone, token)
                res = {"token": token, "time": time, "message": "登录成功", "phone": phone,
                       "title": title, "profile": profile, "head": head}
                return jsonify(res)
            else:
                res = {"token": token, "time": time, "message": "登录成功", "phone": phone,
                       "title": title, "profile": profile, "head": head}
                return jsonify(res)


@app.route("/user/coin", methods=['GET'])
def get_user_coin():
    if "token" not in request.args:
        error = {"status": -1, "message": u"非法请求(未找到token字段)"}
        resp = jsonify(error)
        return resp

    token = request.args['token']
    db = sql.UserHelper()
    user = db.get_user_by_token(str(token))
    if len(user) == 0:
        error = {"status": -2, "message": u"用户不存在,请重新注册"}
        resp = jsonify(error)
        return resp
    user = user[0]
    if len(user) > 5:
        blue_coin = user[4]
        orange_coin = user[5]
        title = user[6]
        profile = user[7]
        head = user[8]
        if blue_coin is None:
            blue_coin = 0.00
        if orange_coin is None:
            orange_coin = 0.00
        res = {"status": 0, "blue_coin": blue_coin, "orange_coin": orange_coin,
               "title": title, "profile": profile, "head": head}
        return jsonify(res)
    else:
        res = {"status": 0, "blue_coin": 0.00, "orange_coin": 0.00}
        return jsonify(res)


@app.route("/user/daikuan", methods=['GET'])
def daikuan_user_coin():
    if "token" not in request.args:
        error = {"status": -1, "message": u"非法请求(未找到token字段)"}
        resp = jsonify(error)
        return resp

    if "url" in request.args:
        url = request.args['url']
    else:
        url = None
    token = request.args['token']
    db = sql.UserHelper()
    user = db.get_user_by_token(str(token))
    if len(user) == 0:
        error = {"status": -2, "message": u"用户不存在,请重新登录"}
        resp = jsonify(error)
        return resp
    user = user[0]
    print user
    blue_coin = float(user[4])
    orange_coin = float(user[5])
    if blue_coin is None:
        blue_coin = 0.00
    if orange_coin is None:
        orange_coin = 0.00
    blue_coin = blue_coin + 30.00
    orange_coin = orange_coin + 30.00
    db.save_user_coin(token, blue_coin, orange_coin)
    db.save_transaction_history(user[0], u"时间贷款", 30, 1, url)
    res = {"status": 0, "blue_coin": blue_coin, "orange_coin": orange_coin, "message": u"贷款成功"}
    return jsonify(res)


@app.route("/user/history", methods=['GET'])
def user_transactio_history():
    if "token" not in request.args:
        error = {"status": -1, "message": u"非法请求(未找到token字段)"}
        resp = jsonify(error)
        return resp

    token = request.args['token']
    db = sql.UserHelper()
    user = db.get_user_by_token(str(token))
    if len(user) == 0:
        error = {"status": -2, "message": u"用户不存在,请重新登录"}
        resp = jsonify(error)
        return resp
    user = user[0]
    print user
    phone = user[0]
    histories = db.get_transaction_history(phone)
    if len(histories) == 0:
        res = {"status": 0, "list": [], "message": "暂无记录"}
        return jsonify(res)
    else:
        history_list = []
        for value in histories:
            phone = value[0]
            title = value[1]
            count = value[2]
            pay_type = value[3]
            list_item = {"phone": phone, "title": title, "count": count, "type": pay_type}
            history_list.append(list_item)
        res = {"status": 0, "message": "获取成功", "list": history_list}
        return jsonify(res)


@app.route("/user/transfer", methods=['GET'])
def user_transfer():
    if "token" not in request.args:
        error = {"status": -1, "message": u"非法请求(未找到token字段)"}
        resp = jsonify(error)
        return resp

    if "phone" not in request.args:
        error = {"status": -1, "message": u"非法请求(未找到phone字段)"}
        resp = jsonify(error)
        return resp

    if "money" not in request.args:
        error = {"status": -1, "message": u"非法请求(未找到money字段)"}
        resp = jsonify(error)
        return resp

    token = request.args['token']
    to_phone = request.args['phone']
    to_money = float(request.args['money'])
    db = sql.UserHelper()
    user = db.get_user_by_token(str(token))
    if len(user) == 0:
        error = {"status": -2, "message": u"用户不存在,请重新登录"}
        resp = jsonify(error)
        return resp
    user = user[0]
    phone = user[0]
    if phone == to_phone:
        error = {"status": -1, "message": u"不能自己给自己转入时间"}
        resp = jsonify(error)
        return resp

    to_user = db.get_user(to_phone)
    if len(to_user) == 0:
        error = {"status": -1, "message": u"转出的用户不存在"}
        resp = jsonify(error)
        return resp

    blue_coin = float(user[4])
    orange_coin = float(user[5])
    print "谁转的信息%s,%s" % (blue_coin, orange_coin)

    if blue_coin is None:
        blue_coin = 0.0
    if orange_coin is None:
        orange_coin = 0.0

    if blue_coin + orange_coin < to_money:
        error = {"status": -1, "message": u"转出时间超出限额"}
        resp = jsonify(error)
        return resp
    if blue_coin > to_money:
        blue_coin = blue_coin - to_money
        db.save_user_coin(token, blue_coin, orange_coin)
    elif blue_coin < to_money:
        blue_coin = 0.0
        orange_coin = orange_coin + blue_coin - to_money
        db.save_user_coin(token, 0.0, orange_coin)
    else:
        blue_coin = 0.0
        orange_coin = orange_coin
        db.save_user_coin(token, 0.0, orange_coin)

    db.save_transaction_history(phone, str(phone)+u"转账给"+str(to_phone), to_money, 0, None)
    db.save_transaction_history(to_phone, str(to_phone) + u"从"+str(phone)+u"入账", to_money, 1, None)

    to_user = to_user[0]
    to_blue_coin = float(to_user[4])
    to_orange_coin = float(to_user[5])

    if to_blue_coin is None:
        to_blue_coin = 0.00
    if to_orange_coin is None:
        to_orange_coin = 0.00

    db.save_user_coin(to_user[2], to_blue_coin + to_money, to_orange_coin)
    error = {"status": 0, "message": u"转出时间成功", "blue_coin": blue_coin, "orange_coin": orange_coin}
    resp = jsonify(error)
    return resp


@app.route("/user/pay", methods=['GET'])
def user_pay():
    if "token" not in request.args:
        error = {"status": -1, "message": u"非法请求(未找到token字段)"}
        resp = jsonify(error)
        return resp

    if "phone" not in request.args:
        error = {"status": -1, "message": u"非法请求(未找到phone字段)"}
        resp = jsonify(error)
        return resp

    if "money" not in request.args:
        error = {"status": -1, "message": u"非法请求(未找到money字段)"}
        resp = jsonify(error)
        return resp

    if "reason" not in request.args:
        error = {"status": -1, "message": u"非法请求(未找到reason字段)"}
        resp = jsonify(error)
        return resp

    token = request.args['token']
    to_phone = request.args['phone']
    to_money = float(request.args['money'])
    reason = request.args['reason']
    db = sql.UserHelper()
    user = db.get_user_by_token(str(token))
    if len(user) == 0:
        error = {"status": -2, "message": u"用户不存在,请重新登录"}
        resp = jsonify(error)
        return resp
    user = user[0]
    phone = user[0]
    if phone == to_phone:
        error = {"status": -1, "message": u"不能自己给自己付时间"}
        resp = jsonify(error)
        return resp

    to_user = db.get_user(to_phone)
    if len(to_user) == 0:
        error = {"status": -1, "message": u"支付的用户不存在"}
        resp = jsonify(error)
        return resp

    blue_coin = float(user[4])
    orange_coin = float(user[5])
    print "谁转的信息%s,%s" % (blue_coin, orange_coin)

    if blue_coin is None:
        blue_coin = 0.0
    if orange_coin is None:
        orange_coin = 0.0

    if blue_coin + orange_coin < to_money:
        error = {"status": -1, "message": u"支付时间超出限额"}
        resp = jsonify(error)
        return resp
    if blue_coin > to_money:
        blue_coin = blue_coin - to_money
        db.save_user_coin(token, blue_coin, orange_coin)
    elif blue_coin < to_money:
        orange_coin = orange_coin + blue_coin - to_money
        db.save_user_coin(token, 0.0, orange_coin)
        blue_coin = 0.0
    else:
        blue_coin = 0.0
        orange_coin = orange_coin
        db.save_user_coin(token, 0.0, orange_coin)

    db.save_transaction_history(phone, reason+u"活动支出", to_money, 0, None)
    db.save_transaction_history(to_phone, reason+u"活动收入", to_money, 1, None)

    to_user = to_user[0]
    to_blue_coin = float(to_user[4])
    to_orange_coin = float(to_user[5])

    if to_blue_coin is None:
        to_blue_coin = 0.00
    if to_orange_coin is None:
        to_orange_coin = 0.00

    db.save_user_coin(to_user[2], to_blue_coin + to_money, to_orange_coin)
    error = {"status": 0, "message": u"支付时间成功", "blue_coin": blue_coin, "orange_coin": orange_coin}
    resp = jsonify(error)
    return resp


# 收款条码
@app.route("/user/getmoney", methods=['GET'])
def user_get_money():
    if "token" not in request.args:
        error = {"status": -1, "message": u"非法请求(未找到token字段)"}
        resp = jsonify(error)
        return resp

    if "phone" not in request.args:
        error = {"status": -1, "message": u"非法请求(未找到phone字段)"}
        resp = jsonify(error)
        return resp

    if "money" not in request.args:
        error = {"status": -1, "message": u"非法请求(未找到money字段)"}
        resp = jsonify(error)
        return resp

    if "reason" not in request.args:
        error = {"status": -1, "message": u"非法请求(未找到reason字段)"}
        resp = jsonify(error)
        return resp

    token = request.args['token']
    from_phone = request.args['phone']
    from_money = float(request.args['money'])
    reason = request.args['reason']

    if "url" in request.args:
        url = request.args['url']
    else:
        url = None

    db = sql.UserHelper()
    user = db.get_user_by_token(str(token))
    if len(user) == 0:
        error = {"status": -2, "message": u"用户不存在,请重新登录"}
        resp = jsonify(error)
        return resp
    user = user[0]
    phone = user[0]
    if phone == from_phone:
        error = {"status": -1, "message": u"不能自己给自己付时间"}
        resp = jsonify(error)
        return resp

    from_user = db.get_user(from_phone)
    if len(from_user) == 0:
        error = {"status": -1, "message": u"该收款码的用户不存在"}
        resp = jsonify(error)
        return resp
    from_user = from_user[0]
    from_token = from_user[2]
    blue_coin = float(from_user[4])
    orange_coin = float(from_user[5])
    print "谁收的信息%s,%s" % (blue_coin, orange_coin)

    if blue_coin is None:
        blue_coin = 0.0
    if orange_coin is None:
        orange_coin = 0.0

    if blue_coin + orange_coin < from_money:
        error = {"status": -1, "message": u"对方时间额度不足"}
        resp = jsonify(error)
        return resp
    if blue_coin > from_money:
        blue_coin = blue_coin - from_money
        db.save_user_coin(from_token, blue_coin, orange_coin)
    elif blue_coin < from_money:
        blue_coin = 0.0
        orange_coin = orange_coin + blue_coin - from_money
        db.save_user_coin(from_token, 0.0, orange_coin)
    else:
        blue_coin = 0.0
        orange_coin = orange_coin
        db.save_user_coin(from_token, blue_coin, orange_coin)

    db.save_transaction_history(phone, u"二维码收款:"+reason, from_money, 1, url)
    db.save_transaction_history(from_phone, u"二维码付款:"+reason, from_money, 0, url)

    to_blue_coin = float(user[4])
    to_orange_coin = float(user[5])

    if to_blue_coin is None:
        to_blue_coin = 0.00
    if to_orange_coin is None:
        to_orange_coin = 0.00

    db.save_user_coin(token, to_blue_coin + from_money, to_orange_coin)
    error = {"status": 0, "message": u"收时间成功", "blue_coin": to_blue_coin + from_money, "orange_coin": to_orange_coin}
    resp = jsonify(error)
    return resp


@app.route("/active/add", methods=['GET'])
def save_active():
    if "token" not in request.args:
        error = {"status": -1, "message": u"非法请求(未找到token字段)"}
        resp = jsonify(error)
        return resp
    token = request.args['token']
    title = request.args['title']
    profile = request.args['profile']
    cost = request.args['cost']
    address = request.args['address']
    detail_address = request.args['details']
    time = request.args['time']
    url = request.args['url']
    active_type = request.args['kind']
    db = sql.UserHelper()
    user = db.get_user_by_token(str(token))
    if len(user) == 0:
        error = {"status": -2, "message": u"用户不存在,请重新登录"}
        resp = jsonify(error)
        return resp
    user = user[0]
    phone = user[0]
    db.save_active(phone, title, profile, cost, address, detail_address, time, url, active_type)
    error = {"status": 0, "message": u"添加活动成功"}
    resp = jsonify(error)
    return resp


@app.route("/active/get", methods=['GET'])
def get_active():
    if "token" not in request.args:
        error = {"status": -1, "message": u"非法请求(未找到token字段)"}
        resp = jsonify(error)
        return resp
    if "type" not in request.args:
        get_type = 0
    else:
        get_type = request.args['type']

    db = sql.UserHelper()
    if "city" not in request.args:
        actives = db.get_active(get_type)
    else:
        actives = db.get_active_with_area(get_type, request.args['city'])
    if len(actives) == 0:
        res = {"status": 0, "list": []}
        print res
        return jsonify(res)
    else:
        active_list = []
        for value in actives:
            id = value[0]
            phone = value[1]
            title = value[2]
            profile = value[3]
            cost = value[4]
            address = value[5]
            detail = value[6]
            time = value[7]
            url = value[8]
            active_type = value[9]
            list_item = {"id": id, "phone": phone, "title": title, "profile": profile, "cost": cost, "address": address,
                         "detail": detail, "time": time, "url": url, "type": active_type}
            active_list.append(list_item)
        res = {"status": 0, "message": "获取成功", "list": active_list}
        print res
        return jsonify(res)


@app.route("/user/setinfor", methods=['GET'])
def save_user_information():
    if "token" not in request.args:
        error = {"status": -1, "message": u"非法请求(未找到token字段)"}
        resp = jsonify(error)
        return resp
    token = request.args['token']
    db = sql.UserHelper()
    user = db.get_user_by_token(str(token))
    if len(user) == 0:
        error = {"status": -2, "message": u"用户不存在,请重新登录"}
        resp = jsonify(error)
        return resp

    db.save_user_info(request.args['title'],
                      request.args['profile'],
                      request.args['head'],
                      request.args['type'],
                      request.args['mobile'],
                      request.args['card'],
                      token)
    res = {"status": 0, "message": "上传成功"}
    print res
    return jsonify(res)


def allowed_file(filename):
    return '.' in filename and \
           filename.rsplit('.', 1)[1] in ALLOWED_EXTENSIONS


@app.route('/upload', methods=['GET', 'POST'])
def upload_file():
    if request.method == 'POST':
        file = request.files['image']
        if file and allowed_file(file.filename):
            filename = secure_filename(file.filename)
            file_path = path.join(app.config['UPLOAD_FOLDER'], str(utils.get_current_time()) + filename)
            file.save(file_path)
            if file_path.startswith("."):
                file_path_list = list(file_path)  # 将字符串转换为列表，列表的每一个元素为一个字符
                file_path_list[0] = ''  # 修改字符串的第1个字符为z
                file_path = ''.join(file_path_list)  # 将列表重新连接为字符串
                res = {"status": 0, "message": "上传成功", "url": NET_ADDRESS+file_path}
                return jsonify(res)
        else:
            res = {"status": -1, "message": "上传的文件key错误,请使用image"}
            return jsonify(res)
    else:
        res = {"status": -1, "message": "不支持的http请求"}
        return jsonify(res)


@app.route("/user/addfriend", methods=['GET'])
def user_add_friend():
    if "token" not in request.args:
        error = {"status": -1, "message": u"非法请求(未找到token字段)"}
        resp = jsonify(error)
        return resp

    if "rphone" not in request.args:
        error = {"status": -1, "message": u"非法请求(未找到phone字段)"}
        resp = jsonify(error)
        return resp

    token = request.args['token']
    rphone = request.args['rphone']
    db = sql.UserHelper()
    user = db.get_user_by_token(str(token))
    if len(user) == 0:
        error = {"status": -2, "message": u"用户不存在,请重新登录"}
        resp = jsonify(error)
        return resp
    user = user[0]
    phone = user[0]
    # if phone == rphone:
    #     error = {"status": -1, "message": u"不能自己添加自己为好友"}
    #     resp = jsonify(error)
    #     return resp

    to_user = db.get_user(rphone)
    if len(to_user) == 0:
        error = {"status": -1, "message": u"你要添加的好友不存在"}
        resp = jsonify(error)
        return resp

    db.save_friend(phone, rphone)

    error = {"status": 0, "message": u"添加好友成功"}
    resp = jsonify(error)
    return resp


@app.route("/user/getfriend", methods=['GET'])
def user_get_friend():
    if "token" not in request.args:
        error = {"status": -1, "message": u"非法请求(未找到token字段)"}
        resp = jsonify(error)
        return resp

    token = request.args['token']
    db = sql.UserHelper()
    user = db.get_user_by_token(str(token))
    if len(user) == 0:
        error = {"status": -2, "message": u"用户不存在,请重新登录"}
        resp = jsonify(error)
        return resp
    user = user[0]
    phone = user[0]
    friends = db.get_friend(phone)
    active_list = []
    for value in friends:
        phone = value[0]
        rphone = value[1]
        list_item = {"phone": phone, "rphone": rphone}
        active_list.append(list_item)
    res = {"status": 0, "message": "获取成功", "list": active_list}
    print res
    return jsonify(res)


@app.route("/active/like", methods=['GET'])
def active_like():
    if "token" not in request.args:
        error = {"status": -1, "message": u"非法请求(未找到token字段)"}
        resp = jsonify(error)
        return resp

    if "id" not in request.args:
        error = {"status": -1, "message": u"非法请求(未找到ID字段)"}
        resp = jsonify(error)
        return resp

    token = request.args['token']
    active_id = request.args['id']

    db = sql.UserHelper()
    user = db.get_user_by_token(str(token))
    if len(user) == 0:
        error = {"status": -2, "message": u"用户不存在,请重新登录"}
        resp = jsonify(error)
        return resp
    user = user[0]
    phone = user[0]
    nick_name = user[6]
    if nick_name is None:
        nick_name = phone
    db.like_active(active_id, phone, nick_name)
    res = {"status": 0, "message": "点赞成功"}
    return jsonify(res)


@app.route("/active/comment", methods=['GET'])
def active_comment():
    if "token" not in request.args:
        error = {"status": -1, "message": u"非法请求(未找到token字段)"}
        resp = jsonify(error)
        return resp

    if "id" not in request.args:
        error = {"status": -1, "message": u"非法请求(未找到ID字段)"}
        resp = jsonify(error)
        return resp

    token = request.args['token']
    active_id = request.args['id']
    content = request.args['content']

    db = sql.UserHelper()
    user = db.get_user_by_token(str(token))
    if len(user) == 0:
        error = {"status": -2, "message": u"用户不存在,请重新登录"}
        resp = jsonify(error)
        return resp
    user = user[0]
    phone = user[0]
    nick_name = user[6]
    if nick_name is None:
        nick_name = phone
    db.comment_active(active_id, nick_name, content)
    res = {"status": 0, "message": "评论成功"}
    return jsonify(res)


@app.route("/getfriendactives", methods=['GET'])
def user_get_friend_actives():
    if "token" not in request.args:
        error = {"status": -1, "message": u"非法请求(未找到token字段)"}
        resp = jsonify(error)
        return resp

    token = request.args['token']
    db = sql.UserHelper()
    user = db.get_user_by_token(str(token))
    if len(user) == 0:
        error = {"status": -2, "message": u"用户不存在,请重新登录"}
        resp = jsonify(error)
        return resp
    user = user[0]
    phone = user[0]
    friends = db.get_my_friend_active(phone)
    active_list = []
    for value in friends:
        active_id = value[0]
        phone = value[1]
        active_title = value[2]
        active_profile = value[3]
        cost = value[4]
        address = value[5]
        details_address = value[6]
        time = value[7]
        url = value[8]
        active_type = value[9]
        post_time = value[10]
        head = value[17]
        title = value[18]
        profile = value[19]
        list_item = {"id": active_id, "phone": phone,
                     "active_title": active_title, "active_profile": active_profile,
                     "cost": cost, "address": address, "details_address": details_address,
                     "time": time, "url": url, "type": active_type,
                     "post_time": post_time, "head": head,
                     "title": title, "profile": profile}
        likes = db.get_active_like(active_id)
        if len(likes) == 0:
            list_item['likes'] = []
        else:
            likes_item = []
            for like in likes:
                nick_name = like[2]
                likes_item.append(nick_name)
            list_item['likes'] = likes_item

        comments = db.get_active_comment(active_id)
        if len(comments) == 0:
            list_item["comments"] = []
        else:
            comment_item = []
            for comment in comments:
                comment_people = comment[1]
                content = comment[2]
                item = {"name": comment_people, "content": content}
                comment_item.append(item)

            list_item["comments"] = comment_item

        active_list.append(list_item)
    res = {"status": 0, "message": "获取成功", "list": active_list}
    print res
    return jsonify(res)


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)
