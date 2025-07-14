from pydantic import BaseModel

class EtaRequest(BaseModel):
    distance_km: float
    local_time: int
    queue_size: int

class EtaResponse(BaseModel):
    min: int
    medium: int
    max: int

class EvaluateModelResponse(BaseModel):
    mean_absolute_error_minutes: float