# LLMOfflineClient
A desktop client app for offline LLM use. Interact with local LLMs (like CodeLlama, Llama 2, Mistral, etc.) via Ollama.

## Features

- Offline LLM Access: Chat with AI models completely locally, no internet required
- Multi-Model Support: Works with any Ollama-supported model (CodeLlama, Llama 2, Mistral, etc.)
- Simple GUI: Clean Java Swing interface for easy prompting
- Cross-Platform: Runs anywhere Java runs (Windows, Mac, Linux)
- Lightweight: Minimal resource usage compared to browser-based options
- Privacy-Focused: All data stays on your machine

## How to Use

1. **Install Ollama**<br>
Download and install Ollama from https://ollama.com/

2. **Download an LLM**<br>
Run this in your terminal:
ollama pull codellama:7b
(Or choose another model from https://ollama.com/library)

3. **Run the App**<br>
- Download LLMOfflineClient.jar from this repo
- Double click to run (needs JDK 17+ installed)

4. **Select Model**<br>
Choose your downloaded LLM (like codellama:7b) from the app's dropdown

5. **Write Your Prompt**<br>
Type your message and hit Send!

## Troubleshooting<br>
Need help? Copy and paste this prompt into ChatGPT, Claude, Gemini, DeepSeek or any AI assistant in your browser:
```
I am trying to use LLMOfflineClient, a java swing based offline application on my computer.

This app uses Ollama to communicate with LLMs, such as codellama:7b.

LLMOfflineClient's github repository instructed me to install Ollama, download an LLM, run LLMOfflineClient via its runnable jar, select the LLM model I downloaded, and send a prompt.

I exerience an issue, help me to resolve it.
```

## License
MIT License. *For commercial use, please credit Gergely Bertalan in documentation or an "About" section.*
