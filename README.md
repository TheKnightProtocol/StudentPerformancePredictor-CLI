

# StudentPerformancePredictor-CLI

**Java (CLI) + Python (ML)** project that predicts whether a student will PASS or FAIL based on:
- Marks (percentage)
- Attendance (percentage)
- Study hours per day

Java handles standard input and invokes the Python ML script. The Python script returns a JSON response that Java prints on the console.



---

# 🎓 Student Performance Predictor CLI

This project combines **Advanced Java and Python** to predict whether a student will **PASS or FAIL** based on internal marks, external marks, and study hours.

---

## ⚙️ Tech Stack
- **Java (CLI Frontend)**
- **Python (Machine Learning Backend using scikit-learn)**
- **Integration via System Command Line**

---

## 📁 Project Structure

---

## Project files

- `student_predict.py` — Python script that trains a small Logistic Regression model and predicts PASS/FAIL.
- `StudentPredictCLI.java` — Java program (CLI) that reads input from user, calls Python, and prints the result.
- `requirements.txt` — Python dependencies
- `.gitignore`
- `.github/workflows/ci.yml` — optional CI that runs tests and compiles Java

---

## Setup (Windows / macOS / Linux)

1. Clone repo:
```bash
git clone https://github.com/YOUR_USERNAME/StudentPerformancePredictor-CLI.git
cd StudentPerformancePredictor-CLI
