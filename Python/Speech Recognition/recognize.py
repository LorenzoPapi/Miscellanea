import speech_recognition as sr
import os
import getpass
import time
import vlc
import sys
import threading


path = "/home/alessandro/Scrivania/Speech Recognition EE/resources"

# assistant name
botName = "Null"

# your name
userName = getpass.getuser()

theme = vlc.MediaPlayer(path + "/tema.mp3")
attack = vlc.MediaPlayer(path + "/7page.mp3")
torture = vlc.MediaPlayer(path + "/tortura.mp3")
timerTime = -1
threadTimer = None
close = False

greetings = [
    "ehi",
    "buongiorno",
    "salve",
    "ciao"
]


def exitProgram():
    printBot("A r r i v e d e r c i.")
    p = vlc.MediaPlayer(path + "/ciao.mp3")
    p.play()
    time.sleep(.5)

def runTimer():
    global timerTime
    global threadTimer
    if not (threadTimer.exiting):
        while (timerTime > 0):
            time.sleep(1)
            timerTime -= 1
        if (timerTime == 0):
            giornoResponse("Timer Scaduto!!!")
        

def timer(command):
    global timerTime
    global threadTimer
    hours, minutes, seconds = 0, 0, 0
    if ("or") in command:
        hours = command.replace(" ", "").split("ore")[0].split("ora")[0][-2:]
        if not (hours[0].isdigit()):
            hours = hours[-1]
        if (hours == "'"):
            hours = 1
        timerTime += int(hours) * 3600
    if ("minut") in command:
        minutes = command.replace(" ", "").split("minuti")[0].split("minuto")[0][-2:]
        if not (minutes[0].isdigit()):
            minutes = minutes[-1]
        if (minutes == "n"):
            minutes = 1
        timerTime += int(minutes) * 60
    if ("second") in command: 
        seconds = command.replace(" ", "").split("secondi")[0].split("secondo")[0][-2:]
        if not (seconds[0].isdigit()):
            seconds = seconds[-1]
        if (seconds == "n"):
            seconds = 1
        timerTime += int(seconds)
    timerTime += 1
    s, s1, s2 = "", "", ""
    if 'hours' in locals():
        s = "ora" if hours == 1 else "ore"
    else:
        s = "ore"
        hours = 0
    if "minutes" in locals():
        s1 = "minuto" if minutes == 1 else "minuti"
    else:
        s1 = "minuti"
    if "seconds" in locals():
        s2 = "secondo" if seconds == 1 else "secondi"
    else:
        s2 = "secondi"
    giornoResponse("Avvio timer di " + str(timerTime) + " secondi, ovvero:\n" + str(hours) + " " + s + "\n" + str(minutes) + " " + s1 + "\n" + str(seconds) + " " + s2 + ".")
    threadTimer = threading.Thread(target=runTimer, name='timerThread')
    threadTimer.exiting = False
    threadTimer.start()


def giornoResponse(audio):
    printBot(audio)
    os.system("espeak -a 100 -p 50 -s 130 -g 0 -v europe/it+12 " + "\"" + audio + "\"")


def printBot(string):
    print(botName + ": " + string)


def myCommand():
    r = sr.Recognizer()
    with sr.Microphone() as source:
        print("Dì qualcosa...")
        r.pause_threshold = 1
        r.adjust_for_ambient_noise(source, duration=1)
        audio = r.listen(source)
        try:
            command = r.recognize_google(audio, language="it-IT").lower().encode("utf-8")
            print(userName + " ha detto: " + command + "\n")
            # loop back to continue to listen for commands
        except sr.UnknownValueError:
            print("Non ho capito, ripeti")
            command = myCommand()
        return command


def assistant(command):
    global close
    global threadTimer
    if (close == False):
        for word in greetings:
            if word in command:        
                printBot("Buongiorno a te," + userName)
                p = vlc.MediaPlayer(path + "/buongiorno.mp3")
                p.play()
                break
        if ("il tuo tema") in command and not ("ferma" in command):
            giornoResponse("Avvio Il vento d\'oro")
            time.sleep(2)
            theme.play()
        elif ("ferma") in command:
            if ("tema") in command:
                giornoResponse("Stop musica")
                theme.stop()
            elif ('attacco') in command:
                giornoResponse("Stop attacco")
                attack.stop()
            elif ("tortura") in command:
                giornoResponse("Stop tortura")
                torture.stop()
            elif ("timer") in command:
                giornoResponse("Fermo il timer")
                threadTimer.exiting = True
                threadTimer.join()
        elif ("il tuo stand") in command:
            printBot("Il mio stand è GOLDEN EXPERIENCE")
            p = vlc.MediaPlayer(path + "/stand.mp3")
            p.play()
        elif ("stand cry") in command:
            giornoResponse("MUDAAAAA")
            p = vlc.MediaPlayer(path + "/muda.mp3")
            p.play()
        elif ("attacco più forte") in command:
            giornoResponse("7 PAGINE DI MUDA!")
            time.sleep(1)
            attack.play()
        elif ("frase celebre") in command:
            printBot("Io, Giorno Giovanna, ho un sogno")
            p = vlc.MediaPlayer(path + "/sogno.mp3")
            p.play()
        elif ("torturare qualcuno") in command:
            giornoResponse("Usa la canzone preferita: ")
            torture.play()
        elif ("è inutile") in command:
            p = vlc.MediaPlayer(path + "/muda.mp3")
            p.play()
        elif ("aiuto") in command:
            giornoResponse("\nInizia col dire \"ehi\" o \"buongiorno\"\n\nPoi scrivi al mio creatore su WhatsApp perchè già gli è scocciato fare un messaggio di aiuto.")
        elif ("termina") in command or ("arrivederci") in command:
            if (threadTimer != None and threadTimer.is_alive()):
                giornoResponse("Aspetta! Hai un timer attivo, sicuro di voler uscire? Di \"Si\" per chiudere, altrimenti di \"No\"")
                close = True
            if close == False:
                exitProgram()
                sys.exit()
        elif ("timer") in command and not ("ferma") in command:
            timer(command)
    else:
        if "sì" in command:
            exitProgram()
            threadTimer.exiting = True
            threadTimer.join()
            sys.exit()
        else:
            giornoResponse("Chiusura annullata!")
            close = False


botName = "Giorno-Bot"
giornoResponse("Ciao " + userName + ", sono " + botName + ", il tuo assistente personale!\nDì \"aiuto\" per sapere cosa posso fare")


while True:
    assistant(myCommand())
    if (threadTimer != None and threadTimer.is_alive() and timerTime == 0):
        threadTimer.exiting = True
        threadTimer.join()

