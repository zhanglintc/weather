#!/usr/bin/env python
# -*- coding: utf-8 -*-

import os, sys, re
import python_send

todayFile      = "today.txt"
yesterdayFile  = "yesterday.txt"
gitLogFile     = "gitlog.txt"

# mail setting
SENDFROM = "zhanglintc@163.com"
USERNAME = "zhanglintc@163.com"
SMTPSERV = "smtp.163.com"
PASSWORD = None
MAILLIST = [
    "zhanglintc623@foxmail.com",
    "522081447@qq.com", # yb
    "619847439@qq.com", # ll
]

# try to get password in your user folder
try:
    with open(os.path.expanduser('~') + '/.smpass', 'rb') as fr:
        PASSWORD = fr.read().strip()
except:
    pass

# if failed, try to get password in the script's folder
if not PASSWORD:
    try:
        with open(sys.path[0] + '/.smpass', 'rb') as fr:
            PASSWORD = fr.read().strip()
    except:
        pass

# if still failed, use password in the script
if not PASSWORD:
    PASSWORD = "YOURPASSWORD~"
else:
    pass

# return 3 lines as a string
def read5Lines(line, f):
    for i in range(4):
        line += f.readline()

    return line

# main function
def main():
    cd_command = "cd {0} &&".format(sys.path[0])
    os.system('{0} git pull'.format(cd_command))
    os.system('{0} git log -100 > {1}'.format(cd_command, gitLogFile))
    
    # get date of today
    os.system('{0} date > {1}'.format(cd_command, todayFile))
    fdate = open("{0}/{1}".format(sys.path[0], todayFile))
    today = fdate.read()[:10]
    fdate.close()
    os.remove("{0}/{1}".format(sys.path[0], todayFile))

    # get date of yesterday
    os.system('{0} date -d yesterday > {1}'.format(cd_command, yesterdayFile))
    fdate = open("{0}/{1}".format(sys.path[0], yesterdayFile))
    yesterday = fdate.read()[:10]
    fdate.close()
    os.remove("{0}/{1}".format(sys.path[0], yesterdayFile))

    # open gitlog
    flog  = open("{0}/{1}".format(sys.path[0], gitLogFile))

    commitCounter = {}
    commitDetail  = {}
    line = True
    while line:
        line = flog.readline()
        cmb = re.search("^commit", line)
        if cmb: # commit block
            commitBlock = read5Lines(line, flog)

            # group(1): user | group(2): email | group(3): date | group(4): time | group(5): detail
            regex = "Author: (.*) <(.*)>\nDate:\s*(.{10})\s(.{5}).*\n\s*(.*$)"

            cmi = re.search(regex, commitBlock) # commit info
            if cmi:
                user   = cmi.group(1).lower()
                email  = cmi.group(2)
                date   = cmi.group(3)
                time   = cmi.group(4)
                detail = cmi.group(5)

                if date.replace(" ", "") == yesterday.replace(" ", ""): # "TueNov3" ## debug use only
                    # fill up commitCounter
                    commitCounter[user] = commitCounter.get(user, 0) + 1

                    # fill up commitDetail
                    if not commitDetail.get(user):
                        # initialize commitDetail[user]
                        commitDetail[user] = []
                        
                    commitDetail[user].append("{0} {1}".format(time, detail))

    # convert commitCounter to list and make it sorted
    commitCounter = sorted(commitCounter.items(), key = lambda d: d[1], reverse = True)

    # fill up sendContent
    sendContent = "昨日项目贡献排行({0}):\n\n".format(yesterday)

    if not commitCounter:
        sendContent += "很遗憾今天居然没有人上传代码\n"

    else:
        idx = 1
        for item in commitCounter:
            sendContent += ("No.{0}: {1:2} {2:7} by {3}\n".format(idx, item[1], "commit" if item[1] <= 1 else "commits", item[0]))
            idx += 1
        sendContent += "\n"

        sendContent += "\n个人贡献详情(从新到旧排序):\n"

        for item in commitCounter:
            sendContent += "\n{0}:\n".format(item[0])
            for detail in commitDetail[item[0]]:
                sendContent += "- {0}\n".format(detail)

    sendContent += "\n"
    sendContent += "https://github.com/zhanglintc/weather"

    # remove gitLogFile
    os.remove("{0}/{1}".format(sys.path[0], gitLogFile))

    # debug use only
    print sendContent

    # send email
    if True: # debug switcher
        python_send.sendEmail(
            to_addr = MAILLIST,
            from_addr = SENDFROM,
            alias = "Weather Project Admin".encode("utf-8"),
            password = PASSWORD,
            smtp_server = SMTPSERV,
            subject = "项目每日贡献报告 - {0}".format(today),
            content = sendContent,
        )

if __name__ == '__main__':
    main()



