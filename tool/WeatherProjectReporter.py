#!/usr/bin/env python
# -*- coding: utf-8 -*-

import os, sys, re
import python_send

todayFile  = "today.txt"
gitLogFile = "gitlog.txt"

# mail setting
SENDFROM = "zhanglintc@163.com"
USERNAME = "zhanglintc@163.com"
SMTPSERV = "smtp.163.com"
PASSWORD = None
MAILLIST = [
    "zhanglintc623@foxmail.com",
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
def read3Lines(line, f):
    for i in range(2):
        line += f.readline()

    return line

# main function
def main():
    cd_command = "cd {0} &&".format(sys.path[0])
    os.system('{0} git pull'.format(cd_command))
    os.system('{0} git log -100 > {1}'.format(cd_command, gitLogFile))
    
    # get date of today
    os.system('{0} date > {1}'.format(cd_command, todayFile))
    fdate = open(todayFile)
    today = fdate.read()[:10]
    os.remove(todayFile)

    # open gitlog
    flog  = open(gitLogFile)

    commitCounter = {}
    line = True
    while line:
        line = flog.readline()
        cmb = re.search("^commit", line)
        if cmb: # commit block
            commitBlock = read3Lines(line, flog)
            regex = "Author: (.*) <(.*)>\nDate:\s*(.{10})" # group(1): user | group(2): email | group(3): date
            cmi = re.search(regex, commitBlock) # commit info
            if cmi:
                user  = cmi.group(1).lower()
                email = cmi.group(2)
                date  = cmi.group(3)

                if date.replace(" ", "") == today.replace(" ", ""):
                    commitCounter[user] = commitCounter.get(user, 0) + 1

    # convert commitCounter to list and make it sorted
    commitCounter = sorted(commitCounter.items(), key = lambda d: d[1], reverse = True)

    # fill up sendContent
    sendContent = "今日项目贡献排行:\n\n"
    idx = 1
    for item in commitCounter:
        sendContent += ("{0}. {1:12} {2} {3}\n".format(idx, item[0], item[1], "commit" if item[1] <= 1 else "commits"))
        idx += 1
    sendContent += "\n"
    sendContent += "https://github.com/zhanglintc/weather"

    # remove gitLogFile
    os.remove(gitLogFile)

    # debug use only
    print sendContent

    # send email
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



