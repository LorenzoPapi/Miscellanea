from selenium import webdriver
from selenium.webdriver.common.by import By

browser = webdriver.Firefox()

from PIL import Image
from io import BytesIO
from yt_dlp import YoutubeDL as YTDL
from pydub import AudioSegment
import speech_recognition as sr
import re, os
from time import sleep

def download_images_for_word(word, prefix="", lim:int=1):
    browser.get(f"https://www.google.com/search?q={word}")
    elem = browser.find_elements(By.TAG_NAME, "button")
    for e in elem:
        if "Accetta" in e.accessible_name:
            e.click()
            break
    elem = browser.find_elements(By.TAG_NAME, "a")
    for e in elem:
        if "Immagini" in e.accessible_name:
            e.click()
            break

    divs = browser.find_elements(By.TAG_NAME, "div")
    for d in divs:
        page = d.get_attribute("data-lpage")
        if page:
            alt_name = d.find_element(By.TAG_NAME, "h3").accessible_name
            d.click()
            print(page)
            break
    elem = browser.find_elements(By.TAG_NAME, "div")
    for e in elem:
        if "Condividi" in e.accessible_name:
            e.find_element(By.XPATH, './..').find_element(By.XPATH, './..').click()
            break
    # imgs = browser.find_elements(By.TAG_NAME, "img")
    # sleep(2)
    # for i in imgs:
    #     not_id = not i.get_attribute("id")
    #     alt = i.get_attribute("alt")
    #     src = i.get_attribute("src")
    #     if alt and not src.startswith("data"):
    #         print(alt, alt_name)
    #         print(src)

    #     # href = i.get_attribute("href")
    #     # print(href)
    #     # if href and ("imgres" in href):
    #     #     print(i.get_attribute("href"))
    #     # Image.open(BytesIO(s.get(src).content)).save(f"Images/{prefix}-{word}-{i:02}.png")
    sleep(1000)
    browser.quit()
    exit()

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