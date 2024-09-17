from colorama import Fore, init
import requests as req
from bs4 import BeautifulSoup
import re, os, subprocess
from time import sleep

s = req.Session()
URL = "https://snaptik.app"

def get_token():
    try:
        response = s.get(URL)
        soup = BeautifulSoup(response.text, features="html.parser")
        for _input in soup.find_all("input"):
            if _input.get("name") == "token":
                token = _input.get("value")
        return token
    except req.exceptions.RequestException as e:
        print(f"Token Request error: {e}")
        return None

def decode_page(video: str):
    try:
        headers = {
            'authority': 'snaptik.app',
            'accept': '*/*',
            'accept-language': 'it-IT,it;q=0.9,en-US;q=0.8,en;q=0.7',
            'dnt': '1',
            'origin': URL,
            'referer': URL + "/",
            'sec-ch-ua': '"Opera";v="99", "Chromium";v="113", "Not-A.Brand";v="24"',
            'sec-ch-ua-mobile': '?0',
            'sec-ch-ua-platform': '"Windows"',
            'sec-fetch-dest': 'empty',
            'sec-fetch-mode': 'cors',
            'sec-fetch-site': 'same-origin'
        }
        response = s.post(URL + "/abc2.php", headers=headers, data=dict(url=video, token=get_token()))
        _var = re.findall(r'\(\".*?,.*?,.*?,.*?,.*?.*?\)', response.text)
        base = []
        for e in (_var[0].split(",")):
            base.append(str(e).replace("(", "").replace(")", "").replace('"', ""))
        return decode(base)
    except req.exceptions.RequestException as e:
        print(f"Page decoding error: {e}")
        return None

def get_video(video:str):
    page = decode_page(video)
    if (not page):
        return None, f">> [{Fore.RED}{video}{Fore.RESET}] fail"
    soup = BeautifulSoup(page, features="html.parser")
    
    url = None
    render_button = soup.find('button', attrs={'data-token': re.compile(r".*")})
    download_button = soup.find(lambda tag:tag.name=='a' and 'Download' in tag.text)
    if render_button:
        token = render_button['data-token']
        task = s.get(URL + f"/render.php?token={token[2:-2]}").json()['task_url']
        task_json = s.get(task).json()
        progress = task_json['progress']
        print('Downloading photo-book')
        while progress < 100:
            task_json = s.get(task).json()
            if task_json['status'] != 0:
                print('ERROR with video', video)
                return None, f">> [{Fore.RED}{video}{Fore.RESET}] fail"
            else:
                print("Progress", progress)
                sleep(1)
                progress = task_json['progress']
        print("Progress", progress)
        url = task_json['download_url']
    elif download_button:
        url = download_button['href'][2:-2]
    else:
        return None, f">> [{Fore.RED}{video}{Fore.RESET}] fail"
    print('URL:', url)
    return req.get(url), f">> [{Fore.GREEN}{video}{Fore.RESET}] success"

def decode(variable: list):
    try:
        output = subprocess.check_output([
            'node', 'decode.js',
            str(variable[0]), str(variable[1]), str(variable[2]), str(variable[3]), str(variable[4]), str(variable[5])
        ])
        result = (output).decode("utf-8")
        return result
    except Exception as e:
        print(f"Decode error: {e}")
        return None

def get_videos(list, folder=''):
    broken = []
    for i, l in enumerate(list):
        id = f"{i+1}".zfill(3) + ".mp4"
        file_path = os.path.join(folder, id)
        if os.path.exists(file_path): continue #check stupidino

        response, res = get_video(l)
        print(res + f" | {i+1}")
        if (response and response.ok):
            with open(file_path, "wb") as file:
                file.write(response.content)
        else:
            broken.append(l)
    return broken

if __name__ == "__main__":
    os.chdir(os.path.dirname(__file__))
    if not os.path.exists("tiktoks.txt"):
        print("tiktoks.txt file missing!")
        exit(1)
    
    try: os.mkdir("downloaded")
    except: pass

    bad = []
    init(autoreset=True)
    
    with open("tiktoks.txt", "r") as tiktoks:
        links = tiktoks.readlines()
        bad = get_videos(links, "downloaded/")
    with open("broke_toks.txt", "w") as broken_file:
        for b in bad:
            broken_file.write(b + "\n")
