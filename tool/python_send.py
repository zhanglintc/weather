# -*- coding: utf-8 -*-

from email import encoders
from email.header import Header
from email.mime.text import MIMEText
from email.utils import parseaddr, formataddr
import smtplib

to_addr = [
    "mailto@domain.com",
]

from_addr   = "youremail@domain.com"
alias       = "Admin"
password    = "yourpassword"
smtp_server = "smtp.domain.com"

subject = "Python Email Tool"
content = "hello, send by Python..."

def _format_addr(s):
    name, addr = parseaddr(s)
    return formataddr(( \
        Header(name, 'utf-8').encode(), \
        addr.encode('utf-8') if isinstance(addr, unicode) else addr))

# sendEmail(to_addr, from_addr, alias, password, smtp_server, subject, content)
def sendEmail(
        to_addr = to_addr,
        from_addr = from_addr,
        alias = alias,
        password = password,
        smtp_server = smtp_server,
        subject = subject,
        content = content):

    msg = MIMEText(content, 'plain', 'utf-8')
    msg['From']    = _format_addr(u'{0} <{1}>'.format(alias, from_addr))
    msg['To']      = to_addr[0]
    msg['Bcc']     = ','.join(to_addr[1:])
    msg['Subject'] = Header(subject, 'utf-8').encode()

    server = smtplib.SMTP(smtp_server, 25)
    server.login(from_addr, password)
    server.sendmail(from_addr, to_addr, msg.as_string())
    server.quit()

if __name__ == '__main__':
    sendEmail()


