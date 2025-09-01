###🎤 VoiceAIChatbot: Where AI Meets Your Voice
###📸 Screenshots:

### System ERD
This diagram shows the Entity-Relationship model for the VoiceAIChatbot, illustrating the main components and data flow.

![System ERD](docs/VoiceAIChatbot_ERD.png)

### Presentation ERD

A more detailed diagram used in presentations to explain the chatbot architecture, interactions, and AI integration.

![Presentation ERD](docs/VoiceAIChatbot_Presentation_ERD.png)



# VoiceAIChatbot
🎤 A voice-enabled AI chatbot in Java using Groq API. Currently supports **text input** and **text-to-speech responses**.



## Features
- Text input chatbot  
- Optional speech responses  
- Environment-variable safe API key usage  
- Clean project structure with compiled code and dependencies separated  



# VoiceAIChatbot

🎤 A voice-enabled AI chatbot in Java using the Groq API.  
Currently supports **text input** and **text-to-speech responses**.

---

## Features

- Text input chatbot  
- Optional speech responses  
- Environment-variable safe API key usage  
- Clean project structure with compiled code and dependencies separated  

---

## Project Structure

```text
VoiceAIChatbot/
├── src/
│   └── VoiceAIChatbot.java                   # Main Java source file
├── JarFiles/
│   └── json-20240303.jar                     # JSON library dependency
├── bin/
│   └── VoiceAIChatbot.class                  # Compiled Java class
├── docs/
│   ├── VoiceAIChatbot_ERD.png                # Entity Relationship Diagram
│   └── VoiceAIChatbot_Presentation_ERD.png   # ERD used in presentation
├── extras/
│   └── voice_recognition.py                  # Optional Python helper script
├── .gitignore                                # Git ignore rules
└── README.md                                 # Project documentation




## Setup Instructions

1. **Clone the repository:**
```bash
git clone https://github.com/RanjitShahKanu024/VoiceAIChatbot.git
cd VoiceAIChatbot



Set your Groq API key (environment variable):
export GROQ_API_KEY="your_api_key_here"



Compile the Java code:
javac -cp "JarFiles/*" -d bin src/VoiceAIChatbot.java



Run the chatbot:
java -cp "JarFiles/*:bin" VoiceAIChatbot






