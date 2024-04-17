import numpy as np
from flask import Flask, request, jsonify
import torch
from PIL import Image
import io
from torchvision import transforms
import os
from ultralytics import YOLO

# Flask app initialization
app = Flask(__name__)

# Model loading (assuming the model file is in the same directory)
model_path = os.path.abspath('/Users/khan/PycharmProjects/mini_project/pythonProject/best.pt')
model = torch.load(model_path)

# Function to preprocess an image for prediction

def preprocess_image(img):
    transform = transforms.Compose([
        transforms.Resize((256, 256)),  # Resize to a specific size
        transforms.ToTensor(),  # Convert to PyTorch tensor
        transforms.Normalize(mean=[0.485, 0.456, 0.406], std=[0.229, 0.224, 0.225]),  # Normalize for better training
    ])
    return transform(img).unsqueeze(0)  # Add a batch dimension for the model


# Route for handling imagfe prediction requests
@app.route('/predict', methods=['POST'])
def predict():
    if 'image' not in request.files:
        return jsonify({'error': 'No file part in the request'})

    file = request.files['image']
    if file.filename == '':
        return jsonify({'error': 'No selected file'})

    try:
        # Read image file
        #img= file['image']
        img = Image.open(io.BytesIO(file.read()))

        #img = file.read()
        # Preprocess image
        img_tensor = preprocess_image(img)

        min_value = torch.min(img_tensor)
        max_value = torch.max(img_tensor)
        normalized_tensor = (img_tensor - min_value) / (max_value - min_value)

        # Make prediction (disable gradient calculation for efficiency)
        with torch.no_grad():
            yolo = YOLO('/Users/khan/PycharmProjects/mini_project/pythonProject/best.pt')
            print("Hello")
            prediction = yolo.predict(normalized_tensor)
            #print(prediction)
            # Access probabilities and class names (adjust based on your YOLO class structure)
            probs = prediction[0].probs
            #print(probs)
            # highest_prob_index = torch.argmax(probs)
            name_dict = prediction[0].names  # Assuming names are stored in a 'names' attribute
            #print(name_dict)
            # Print the class with the highest probability
            probs = prediction[0].probs.top1
            #print(probs)

            highest_prob_name = name_dict[np.argmax(probs)]
            print(f"Image likely belongs to class: {highest_prob_name}")
            #print("Bello")
        # Extract disease name
        # Return prediction result in JSON format
        return jsonify({'disease': highest_prob_name})

    except Exception as e:
        return jsonify({'error': str(e)})

# Run the Flask app in debug mode (optional, for development)
if __name__ == '__main__':
    app.run(debug=True)
