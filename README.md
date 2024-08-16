
# Plant Disease Detection using YOLOv8

[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Platform: Render](https://img.shields.io/badge/Platform-Render-blue)](https://render.com)
[![API: Flask](https://img.shields.io/badge/API-Flask-red)](https://flask.palletsprojects.com/)

This repository contains a comprehensive project that utilizes the PlantVillage dataset to train a YOLOv8 model for detecting diseases in plant leaves. The project includes a Flask-based API, an Android app for interacting with the model, and deployment on Render.

## Table of Contents

- [Project Overview](#project-overview)
- [Features](#features)
- [Folder Structure](#folder-structure)
- [Installation](#installation)
- [Usage](#usage)
- [API Endpoints](#api-endpoints)
- [Model Training](#model-training)
- [Android App](#android-app)
- [Deployment](#deployment)
- [Contributing](#contributing)
- [License](#license)
## Project Overview

This project aims to detect diseases in plant leaves by leveraging the PlantVillage dataset and YOLOv8 model. The workflow includes data preprocessing, model training, API development, and integration with an Android app.
## Features

- **YOLOv8 Model Training**: Efficient training process using the PlantVillage dataset.
- **Flask API**: Serves the trained model and provides endpoints for prediction.
- **Android App**: A user-friendly interface for capturing images and receiving disease predictions.
- **Render Deployment**: Model and API deployed on Render for seamless access.
## Folder Structure

```plaintext
plant-disease-detection/
├── android-app/
├── api/
├── dataset/
├── model-training/
├── deployment/
├── .gitignore
├── LICENSE
└── README.md
```
- `android-app/`: Contains the Android project files.
- `api/`: Flask API code with configuration, model handling, and utility functions.
- `dataset/`: Raw and processed datasets, including preprocessing scripts.
- `model-training/`: Notebooks, scripts, and model checkpoints for training.
- `deployment/`: Deployment scripts and configurations for Render.
## Installation

- Clone the repository: 
```plaintext
git clone https://github.com/yourusername/plant-disease-detection.git
```
- Navigate to the relevant directory (e.g., `api/`) and install dependencies:
```plaintext
cd api/
pip install -r requirements.txt
```
## Usage

**Running the API**

To start the Flask API locally:
```plaintext
cd api/
python app.py
```
The API will be available at `http://localhost:5000`.

**Running the Android App**
- Open the project in Android Studio.
- Build and run the app on an emulator or physical device.
- Use the app to capture images and predict diseases by connecting to the Flask API.

## API Endpoints

- `/predict`: POST endpoint to send an image and receive predictions.
    - Request: Image file (e.g., `image/jpeg`).
    - Response: JSON object containing the disease and confidence score.
    Example:
    ```plaintext
    curl -X POST -F "file=@leaf.jpg" http://localhost:5000/predict

    ```
    

## Model Training

The model was trained using the PlantVillage dataset with the following steps:

- Data preprocessing and augmentation.
- Training with YOLOv8 using the provided training scripts.
- Evaluation on a validation set.
You can find the training scripts in the `model-training/` directory.


**Key Observations:**

`Training Loss (train/loss)`:
- The training loss decreases steadily from 4.2199 at epoch 0 to 0.02846 at epoch 14.
- This indicates that the model is learning effectively and reducing its error over time.
`Accuracy (metrics/accuracy_top1 and metrics/accuracy_top5)`:
- Top-1 Accuracy increases from 0.9547 to 0.99567, which is excellent and shows the model is correctly identifying the top prediction almost 99.5% of the time by the end.
- Top-5 Accuracy starts at 0.9988 and reaches 0.99991, indicating the model is nearly perfect in having the correct label within its top 5 predictions.
`Validation Loss (val/loss)`:
- Validation loss remains relatively stable, hovering around 38.033 to 39.053. A stable or slightly decreasing validation loss is a good sign, but ideally, you want it to decrease more significantly, similar to training loss.
`Learning Rate (lr/pg0, lr/pg1, lr/pg2)`:
- The learning rate starts at 0.00023538 and decreases gradually to 0.00010139. This is a common approach to avoid overshooting the minimum of the loss function as training progresses.
## Android App

The Android app allows users to capture or upload an image of a plant leaf and receive a disease prediction from the API. The app is built using Kotlin and communicates with the Flask API for inference.

The app's main interface allows users to choose between capturing an image using the camera or selecting an image from the gallery.
Image Capture and Selection:

When the user captures an image or selects one from the gallery, the app converts the image into a byte array for efficient transmission.
API Request:

The app uses the OkHttp library to create an HTTP POST request. The image data is sent as a multipart/form-data request to the server's endpoint (https://plant-disease-detection-mini-1-3.onrender.com/predict).
Response Handling:

The server processes the image and returns a JSON response containing the predicted disease name.
The app parses this JSON response using the Gson library. It extracts the disease name and displays it on the app’s main screen.
Error Handling:

The app includes error handling for network errors and parsing issues, providing feedback to the user if predictions cannot be fetched or if the response format is invalid.
## Deployment

The model and API are deployed on Render. To redeploy or update:

- Modify the `Dockerfile` and `deploy.sh` in the `deployment/render/` directory.
- Push changes to the repository to trigger deployment on Render.
## Licence

This project is licensed under the MIT License. See the LICENSE file for more details.