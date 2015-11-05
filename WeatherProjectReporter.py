#!/usr/bin/env python
# -*- coding: utf-8 -*-

import os, sys, re

def read3Lines(line, f):
    for i in range(2):
        line += f.readline()

    return line

def main():
    cd_command = "cd {} &&".format(sys.path[0])
    os.system('{} git pull'.format(cd_command))
    os.system('git log -100 > gitlog.txt')
    
    # get date of today
    os.system('date > today.txt')
    fdate = open("today.txt")
    today = fdate.read()[:10].replace(" ", "")
    os.remove('today.txt')

    # open gitlog
    flog  = open("gitlog.txt")

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

                if date.replace(" ", "") == today:
                    commitCounter[user] = commitCounter.get(user, 0) + 1

    sendContent = ""
    for key, val in commitCounter.items():
        sendContent += ("{0}: {1}\n".format(key, val))

    print sendContent
    os.remove("gitlog.txt")

if __name__ == '__main__':
    main()



