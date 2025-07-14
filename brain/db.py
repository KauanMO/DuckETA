import os
from dotenv import load_dotenv
from sqlalchemy import create_engine
import pandas as pd

load_dotenv()

DB_HOST = os.getenv("DB_HOST")
DB_PORT = os.getenv("DB_PORT")
DB_NAME = os.getenv("DB_NAME")
DB_USER = os.getenv("DB_USER")
DB_PASS = os.getenv("DB_PASS")

DATABASE_URL = f"postgresql+psycopg2://{DB_USER}:{DB_PASS}@{DB_HOST}:{DB_PORT}/{DB_NAME}"

engine = create_engine(DATABASE_URL)

def get_feedbacks():
    query = """
    SELECT
      o.distance_km AS distance_km,
      EXTRACT(HOUR FROM o.order_time) AS local_time,
      o.queue_size AS queue_size,
      f.actual_delivery_time AS delivery_time
    FROM app_order o
    JOIN order_feedback f ON o.id = f.order_id
    WHERE f.actual_delivery_time IS NOT NULL
    """
    df = pd.read_sql_query(query, engine)
    
    return df
