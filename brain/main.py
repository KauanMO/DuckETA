from fastapi import FastAPI
import service
from schemas import EtaRequest, EtaResponse, EvaluateModelResponse
import train_brain
from apscheduler.schedulers.background import BackgroundScheduler
from datetime import datetime

app = FastAPI()

@app.post('/predict', response_model=EtaResponse)
def predict_eta(request: EtaRequest):
    predict = service.predict(request)
    return predict

@app.get('/evaluate-model', response_model=EvaluateModelResponse)
def evaluate_model():
    evaluate = service.evaluate_model()
    return evaluate

if __name__ == "__main__":
    import uvicorn
    scheduler = BackgroundScheduler()
    scheduler.add_job(train_brain.train_model, 'cron', hour=0, minute=0)
    scheduler.start()

    train_brain.train_model()
    uvicorn.run("main:app", host="0.0.0.0", port=5500, reload=True)