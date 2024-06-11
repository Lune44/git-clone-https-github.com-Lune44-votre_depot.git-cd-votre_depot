from flask import Flask, request
import pywhatkit as kit
import openai
import datetime
import time

app = Flask(__name__)

# Configuration de l'API OpenAI
openai.api_key = 'YOUR_OPENAI_API_KEY'

def get_response_from_openai(message):
    response = openai.Completion.create(
        engine="text-davinci-003",
        prompt=message,
        max_tokens=150
    )
    return response.choices[0].text.strip()

@app.route('/send-message', methods=['POST'])
def send_message():
    data = request.json
    phone_number = data['phone']
    message = data['message']

    # Utiliser OpenAI pour générer une réponse
    response = get_response_from_openai(message)
    
    # Envoi du message via WhatsApp Web à l'aide de pywhatkit
    current_time = datetime.datetime.now()
    kit.sendwhatmsg(phone_number, response, current_time.hour, current_time.minute + 2)

    return {'status': 'Message sent'}

if __name__ == '__main__':
    app.run(port=5000)
