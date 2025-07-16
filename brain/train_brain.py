from db import get_feedbacks
import pandas as pd
from sklearn.ensemble import RandomForestRegressor
import joblib
import numpy as np
from hour_map import hour_weight_map
from sklearn.metrics import mean_absolute_error
from sklearn.model_selection import train_test_split

def generate_fake_dataset():
    quantity = 1000
    dir = 'data/fake_dataset.csv'

    data = {
        "distance_km": np.round(np.random.uniform(1, 15, quantity), 2),
        "local_time": np.random.randint(0, 24, quantity),
        "queue_size": np.random.randint(0, 30, quantity)
    }

    df = pd.DataFrame(data)

    def calculate_time(row):
        base = row["distance_km"] * 3 + row["queue_size"] * 1.5 + row["local_time"] * 0.5

        if hour_weight_map.get(row["local_time"], 1) == 0:
            return int(base * 0.8)
        elif hour_weight_map.get(row["local_time"], 1) == 1:
            return int(base)
        else:
            return int(base * 1.3)
    
    df["delivery_time"] = df.apply(calculate_time, axis=1)
    df.to_csv(dir, index=False, sep=";")

def generate_dataset():
    generate_fake_dataset()

    fake_df = pd.read_csv('data/fake_dataset.csv', sep=';')
    real_df = get_feedbacks()

    full_df = pd.concat([fake_df, real_df], ignore_index=True)

    full_df.to_csv('data/dataset.csv', index=False, sep=';')
    return full_df

def train_model():
    df = generate_dataset()

    X = df[['distance_km', 'local_time', 'queue_size']]
    y = df['delivery_time']

    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

    model = RandomForestRegressor(n_estimators=100, random_state=42)
    model.fit(X_train, y_train)

    y_pred = model.predict(X_test)
    mae = mean_absolute_error(y_test, y_pred)
    print(f"Mean Absolute Error: {mae:.2f} minutes")

    joblib.dump(model, 'data/ducketa_brain.pkl')

if __name__ == "__main__":
    train_model()