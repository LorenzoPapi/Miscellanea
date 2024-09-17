from requests import Session
from bs4 import BeautifulSoup
from PIL import Image
from io import BytesIO
from yt_dlp import YoutubeDL as YTDL
from pydub import AudioSegment
import speech_recognition as sr
import re, os

def download_images_for_word(word, prefix="", lim:int=1):
    s = Session()
    s.headers = {'user-agent': 'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.28 Safari/537.36'}

    soup = BeautifulSoup((s.get(f"https://www.google.com/search?q={word}").content), "html.parser")
    form_tag = soup.find("div", {"class": "saveButtonContainer"}).find("form")
    cookie_data = {}
    for input_tag in form_tag.findAll("input", {"type": "hidden"}):
        cookie_data[input_tag['name']] = input_tag['value']
    post_req = s.post(form_tag['action'], data=cookie_data).content

    soup = BeautifulSoup(post_req, "html.parser")
    img_soup = None
    for a in soup.findAll("a"):
        if a.string == "Immagini":
            print("https://www.google.com" + a['href'])
            img_soup = BeautifulSoup(s.get("https://www.google.com" + a['href']).content, "html.parser")
            break

    for i, img in enumerate(img_soup.findAll("img")):
        src = img['src']
        if 'https://' in src:
            print(src)
            Image.open(BytesIO(s.get(src).content)).save(f"Images/{prefix}-{word}-{i:02}.png")
        if i == lim: break

def make_audio_to_google_image(audio_file, lang:str="it-IT"):
    total_audio = AudioSegment.from_wav(audio_file)
    word_file = open("wordlist.txt", "w")
    length = 60
    length *= 1000
    for i in range(0, len(total_audio) // length):
        new_audio = total_audio[i*length:(i+1)*length]
        new_audio.export(f"Audios/{i:02}.wav", format="wav")
        r = sr.Recognizer()
        with sr.AudioFile(f"Audios/{i:02}.wav") as source:
            audio = r.record(source)
    
        try:
            word_file.write(r.recognize_google(audio, language=lang).lower())
            word_file.write("\n")
        except sr.UnknownValueError:
            print("Google could not understand audio")
        except sr.RequestError as e:
            print("Google error; {0}".format(e))
    word_file.close()

def make_video_to_google_image(url, lang:str="it-IT"):
    ydl = YTDL({
        "outtmpl":{
            "default": "audio"
        },
        "format": "ba",
        'postprocessors': [{
            'key': 'FFmpegExtractAudio',
            'preferredcodec': 'wav',
        }]
    })
    ydl.download([url])
    make_audio_to_google_image("audio.wav", lang)

def convert_words_to_image(words:list):
    for i, w in enumerate(words):
        download_images_for_word(w, f"{i:03}")

if __name__ == "__main__":
    os.chdir(os.path.dirname(__file__))
    
    try:
        os.mkdir("Audios")
        os.mkdir("Images")
    except:
        pass

    words = open("wordlist.txt", "r").read()
    words = re.findall(r"[A-zÀ-ú]+", words)

    convert_words_to_image(words)
    #make_video_to_google_image("https://www.youtube.com/watch?v=myYqlkhey-A")