from schemas import EtaRequest, EtaResponse, EvaluateModelResponse
import numpy as np
import joblib
import pandas as pd
from sklearn.metrics import mean_absolute_error

model = joblib.load('data/ducketa_brain.pkl')

def predict(request: EtaRequest):
    features = np.array([[request.distance_km, request.local_time, request.queue_size]])
    eta = model.predict(features)[0]

    mean_error = evaluate_model().mean_absolute_error_minutes

    return EtaResponse(
        min=int(eta - mean_error),
        medium=int(eta),
        max=int(eta + mean_error)
    )

def evaluate_model():
    df = pd.read_csv('data/dataset.csv', sep=';')

    X = df[['distance_km', 'local_time', 'queue_size']]
    y = df['delivery_time']

    y_pred = model.predict(X)
    mae = mean_absolute_error(y, y_pred)

    return EvaluateModelResponse(
        mean_absolute_error_minutes=mae
    )