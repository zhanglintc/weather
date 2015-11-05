#!/usr/bin/env python
# -*- coding: utf-8 -*-

import os, sys, re
import python_send

todayFile  = "today.txt"
gitLogFile = "gitlog.txt"

# return 3 lines as a string
def read3Lines(line, f):
    for i in range(2):
        line += f.readline()

    return line

# main function
def main():
    cd_command = "cd {0} &&".format(sys.path[0])
    os.system('{0} git pull'.format(cd_command))
    os.system('git log -100 > {0}'.format(gitLogFile))
    
    # get date of today
    os.system('date > {0}'.format(todayFile))
    fdate = open(todayFile)
    # today = fdate.read()[:10]
    today = "Tue Nov  3"
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

    sendContent = "今日项目贡献排行({0}):\n\n".format(today)
    idx = 1
    for item in commitCounter:
        sendContent += ("{0}. {1:12} {2} {3}\n".format(idx, item[0], item[1], "commit" if item[1] <= 1 else "commits"))
        idx += 1

    print sendContent
    os.remove(gitLogFile)

if __name__ == '__main__':
    main()



