
---

## 4) Python script — `student_predict.py`

Put this code in `student_predict.py`. It supports both command-line argument mode (`python student_predict.py 75,80,6`) and interactive mode (`python student_predict.py`).

```python
#!/usr/bin/env python3
"""
student_predict.py
Simple logistic regression model trained on a small synthetic dataset.
Usage:
  python student_predict.py 75,80,6
or
  python student_predict.py
  (then it will prompt for values)
Output:
  JSON string to stdout, e.g. {"prediction": "PASS", "probability": 92.34}
"""

import sys
import json
import numpy as np
from sklearn.linear_model import LogisticRegression

def train_model():
    # Small synthetic dataset (marks, attendance, study_hours) -> pass(1) / fail(0)
    X = np.array([
        [85, 90, 8],
        [70, 65, 5],
        [40, 50, 2],
        [90, 92, 9],
        [60, 75, 4],
        [30, 40, 1],
        [80, 85, 6],
        [55, 60, 3],
        [68, 72, 5],
        [95, 95, 10],
    ])
    y = np.array([1,1,0,1,1,0,1,0,1,1])
    model = LogisticRegression(solver='liblinear')
    model.fit(X, y)
    return model

def parse_input_arg(arg):
    # Accept "marks,attendance,hours"
    try:
        parts = arg.strip().split(',')
        if len(parts) != 3:
            raise ValueError("Expected 3 values: marks,attendance,study_hours")
        marks = float(parts[0])
        attendance = float(parts[1])
        hours = float(parts[2])
        return marks, attendance, hours
    except Exception as e:
        raise ValueError(f"Invalid input format: {e}")

def parse_input_interactive():
    try:
        marks = float(input("Enter marks (percentage 0-100): ").strip())
        attendance = float(input("Enter attendance (percentage 0-100): ").strip())
        hours = float(input("Enter average study hours per day: ").strip())
        return marks, attendance, hours
    except Exception as e:
        raise ValueError(f"Invalid input: {e}")

def main():
    model = train_model()
    # Decide whether to accept CLI arg or use interactive input
    if len(sys.argv) >= 2:
        arg = sys.argv[1]
        try:
            marks, attendance, hours = parse_input_arg(arg)
        except Exception as e:
            print(json.dumps({"error": str(e)}))
            sys.exit(1)
    else:
        try:
            marks, attendance, hours = parse_input_interactive()
        except Exception as e:
            print(json.dumps({"error": str(e)}))
            sys.exit(1)

    X_new = np.array([[marks, attendance, hours]])
    pred = model.predict(X_new)[0]
    prob = model.predict_proba(X_new)[0][1]  # probability of class 1 (pass)

    result = {
        "prediction": "PASS" if int(pred) == 1 else "FAIL",
        "probability": round(float(prob) * 100, 2)
    }
    # Print JSON to stdout (Java will parse this)
    print(json.dumps(result))

if __name__ == "__main__":
    main()
