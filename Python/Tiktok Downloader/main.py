from colorama import Fore, init
from bs4 import BeautifulSoup
from time import sleep
import re, os, sys, requests

'''
I just wanted to say, Snaptik.app, if you see this, please enhance your security measures.
It was surprisingly easy to reverse engineer the rather ineffective "protection" you're currently employing.
Author: cxstles on github
Date: Sep 29th, 2023.

Compacted and ported to python by me!
'''

class Decode():
    def __init__(self, video):
        self.url = video
    
    def decode_page(self):
        token = None
        try:
            token = BeautifulSoup(session.get(URL).text, features="html.parser").find("input", {"name": "token"})['value']
        except Exception as e:
            print(f"Token request error: {e}")
            return None
        
        variable = []
        try:
            headers = {
                'authority': 'snaptik.app',
                'accept': '*/*',
                'accept-language': 'it-IT,it;q=0.9,en-US;q=0.8,en;q=0.7',
                'dnt': '1',
                'origin': URL,
                'referer': f"{URL}/",
                'sec-ch-ua': '"Opera";v="99", "Chromium";v="113", "Not-A.Brand";v="24"',
                'sec-ch-ua-mobile': '?0',
                'sec-ch-ua-platform': '"Windows"',
                'sec-fetch-dest': 'empty',
                'sec-fetch-mode': 'cors',
                'sec-fetch-site': 'same-origin'
            }
            response = session.post(f"{URL}/abc2.php", headers=headers, data=dict(url=self.url, token=token))
            _var = re.findall(r'\(\".*?,.*?,.*?,.*?,.*?.*?\)', response.text)
            for v in (_var[0].split(",")):
                variable.append(str(v).replace("(", "").replace(")", "").replace('"', ""))
        except Exception as e:
            print(f"Page decoding error: {e}")
            return None
        
        try:
            h, u, n, t, e, r = str(variable[0]), int(str(variable[1])), str(variable[2]), int(str(variable[3])), int(str(variable[4])), int(str(variable[5]))

            result, i = "", 0
            while i < len(h):
                s = ""
                while h[i] != n[e]:
                    s += h[i]
                    i += 1
                for j in range(0, len(n)):
                    s = s.replace(n[j], str(j))
                result += chr(int(self.decodeString(s, e, 10)) - t)
                i += 1

            return result # decodeURIComponent(escape(result))
        except Exception as e:
            print(f"Decode error: {e}")
            return None
    
    def decodeString(self, d, e, f): 
        baseChars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ+/"
        charArray = list(baseChars[:e])
        outputChars = list(baseChars[:f])

        a, d = 0, list(d[::-1])
        for c in range(0, len(d)):
            index = charArray.index(str(d[c]))
            a += 0 if index == -1 else index * pow(e, c)

        k = ""
        while a:
            k = outputChars[a % f] + k
            a //= f
    
        return k if k else "0"


def get_video(video:str):
    fail_string = f">> [{Fore.RED}{video}{Fore.RESET}] Failed"
    succ_string = f">> [{Fore.GREEN}{video}{Fore.RESET}] Completed"

    page = Decode(video).decode_page()
    if not page: return None, fail_string
    soup = BeautifulSoup(page, features="html.parser")
    
    url = None
    render_button = soup.find('button', attrs={'data-token': re.compile(r".*")})
    download_button = soup.find(lambda tag: tag.name=='a' and 'Download' in tag.text)
    
    if render_button: # Photo-book
        print('Downloading photo-book')
        token = render_button['data-token']
        task = session.get(f"{URL}/render.php?token={token[2:-2]}").json()['task_url']
        task_json = session.get(task).json()
        progress = task_json['progress']
        
        while progress < 100:
            task_json = session.get(task).json()
            if task_json['status'] != 0:
                print('Error downloading', video)
                return None, fail_string
            else:
                sleep(1)
                progress = task_json['progress']
                print("Progress", progress)
                
        url = task_json['download_url']
    elif download_button: # Normal video
        url = download_button['href'][2:-2]
    else: # default fallback
        return None, fail_string
    
    print('URL:', url)
    return session.get(url), succ_string

def get_videos(list, folder=''):
    global broken
    for i, l in enumerate(list):
        file_path = os.path.join(folder, f"{i+1:03}.mp4")
        if os.path.exists(file_path):
            ans = input(f"The file {file_path} is already present. Overwrite? [y/N]").lower()
            if (ans != "y"):
                print(f"Skipping download of {l}.")
                continue
            else:
                print("Overwriting!")

        response, res_string = get_video(l)
        print(f"{res_string} | {i+1}")

        if response and response.ok:
            with open(file_path, "wb") as file:
                file.write(response.content)
        else:
            broken.append(l)

if __name__ == "__main__":
    os.chdir(os.path.dirname(__file__))

    try: os.mkdir("downloaded")
    except: pass

    text_file_name, links, broken = "tiktoks.txt", [], []

    if len(sys.argv) == 1:
        print(f"No arguments passed. To get help, add the flag `-h`.")
    
    if "-h" in sys.argv:
        print(f"Welcome to the tiktok downloader!\nUsage: python3 {os.path.basename(sys.argv[0])} [FILE_PATH]\n\t python3 {os.path.basename(sys.argv[0])} [OPTIONS] LINK [LINK...] \nDownloads tiktok links from FILE_PATH or a single link.")
        exit(0)

    if len(sys.argv) >= 2:
        if "https://" not in sys.argv[1]:
            text_file_name = sys.argv[1]
        else:
            links = sys.argv[1:]
    
    if len(links) == 0:
        print(f"Trying to read file {text_file_name}...")
        if not os.path.exists(text_file_name):
            print(f"{text_file_name} file is missing!")
            exit(1)

        with open(text_file_name, "r") as tiktoks:
            links = tiktoks.read().splitlines() 
    
    session = requests.Session()
    URL = "https://snaptik.app"
    init(autoreset=True)

    print(f"Preparing to download {len(links)} files.")
    get_videos(links, "downloaded/")
    
    if len(broken) > 0:
        print(f"Found {len(broken)} broken links.")
        with open("broken_links.txt", "w") as bf:
            for b in broken:
                bf.write(f"{b}\n")

    print("Done!")