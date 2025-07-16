from fastapi import FastAPI
import service
from schemas import EtaRequest, EtaResponse, EvaluateModelResponse
import train_brain

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
    
    train_brain.train_model()
    uvicorn.run("main:app", host="0.0.0.0", port=5500, reload=True)