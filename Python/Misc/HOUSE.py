from bs4 import BeautifulSoup
from requests import get, Session
import re, zipfile, io

URL = 'https://archive.org'
user_agent = 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/34.0.1847.131 Safari/537.36'
s = Session()
s.headers['User-Agent'] = user_agent

soup = BeautifulSoup(get("https://archive.org/details/HouseMDSeason1treve").content, 'html.parser')
urls = [u['href'] for u in soup.find_all('a', attrs={'href': re.compile(r'.+.mp4'), 'class': 'stealth download-pill'})]

playlist = open('HOUSE.m3u8', 'w')
playlist.write('#EXTM3U\n')
for i,u in enumerate(urls):
    playlist.write(s.head(URL + u, allow_redirects=True).url + '\n')
playlist.close()