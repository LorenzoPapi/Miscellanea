import speech_recognition as sr
import time

def callback(recognizer, audio):
    try:
        print("Parla ora")
        text = recognizer.recognize_google(audio, language="it-IT").lower().replace(" punto interrogativo", "?").replace(" punto esclamativo", "!").replace(" punto e virgola", ";").replace(" punto", ".").replace(" virgola", ",").replace(" due punti", ":")
        print(text.capitalize(), file=open('output.txt', 'a'))
    except sr.UnknownValueError:
        print("Google Speech Recognition could not understand audio")
    except sr.RequestError as e:
        print("Could not request results from Google Speech Recognition service; {0}".format(e))

r = sr.Recognizer()
m = sr.Microphone()
with m as source:
    r.adjust_for_ambient_noise(source)

stop_listening = r.listen_in_background(m, callback)

while True:
    time.sleep(1)
