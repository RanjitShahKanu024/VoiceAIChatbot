#!/usr/bin/env python3
import speech_recognition as sr
import sys

def listen():
    r = sr.Recognizer()
    with sr.Microphone() as source:
        print("Listening...", flush=True)
        try:
            audio = r.listen(source, timeout=5, phrase_time_limit=10)
            print("Processing...", flush=True)
            text = r.recognize_google(audio)
            print(text, flush=True)
        except sr.WaitTimeoutError:
            print("timeout", flush=True)
        except sr.UnknownValueError:
            print("could_not_understand", flush=True)
        except Exception as e:
            print(f"error: {str(e)}", flush=True)

if __name__ == "__main__":
    listen()