from fastapi import FastAPI
from pydantic import BaseModel
import joblib
import numpy as np

model = joblib.load('data/ducketa_brain.pkl')

app = FastAPI()

class EtaRequest(BaseModel):
    distance_km: float
    local_time: int
    queue_size: int

class EtaResponse(BaseModel):
    low_traffic: int
    medium_traffic: int
    high_traffic: int

@app.post('/predict', response_model=EtaResponse)
def predict_eta(request: EtaRequest):
    features = np.array([[request.distance_km, request.local_time, request.queue_size]])
    eta = model.predict(features)[0]

    return EtaResponse(
        low_traffic=int(eta * 0.8),
        medium_traffic=int(eta),
        high_traffic=int(eta * 1.3)
    )