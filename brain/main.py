from fastapi import FastAPI
import service
from schemas import EtaRequest, EtaResponse, EvaluateModelResponse

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
    uvicorn.run("main:app", host="127.0.0.1", port=5500, reload=True)