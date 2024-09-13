import requests, base64, json, shutil, os
import xml.etree.ElementTree as ElementTree
from pprint import pprint
from urllib.parse import urlparse, parse_qs
from urllib3 import disable_warnings
from urllib3.exceptions import InsecureRequestWarning


disable_warnings(InsecureRequestWarning)
#proxy
proxy = 'http://de001-surf.zone2.proxy.allianz:8080'
proxies = {'http' : proxy, 'https' : proxy}


headers = {'Content-type': 'application/x-www-form-urlencoded'}
data = 'code=555&grant_type=authorization_code&scope=openid&client_secret=secret&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Fspring%2Foidc%2Fcallback&client_id=cube'

request_url = 'http://auth-server:8200/oauth2/token'
r = requests.post(request_url, headers=headers, data=data , allow_redirects=False)
#print(r.headers)
print(r.status_code)
print(r.text)
