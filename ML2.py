import pandas as pd
from sklearn.preprocessing import StandardScaler
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LogisticRegression, LinearRegression
from sklearn.metrics import classification_report, confusion_matrix
import seaborn as sns
import matplotlib.pyplot as plt
import numpy as np

df = pd.read_csv('Iris.csv')

x = df.drop(columns=['Id', 'Species'])
y = df['Species']

x_train, x_test, y_train, y_test = train_test_split(x, y, test_size=0.2, random_state=42)

scaler = StandardScaler()
x_train = scaler.fit_transform(x_train)
x_test = scaler.transform(x_test)

model = LogisticRegression()
model.fit(x_train, y_train)
y_pred = model.predict(x_test)

print("Отчет по классификации:")
print(classification_report(y_test, y_pred))

conf_matrix = confusion_matrix(y_test, y_pred)

sns.heatmap(conf_matrix, annot=True, fmt='d', cmap='magma')
plt.xlabel('Предсказанный класс')
plt.ylabel('Истинный класс')
plt.title('Матрица ошибок - Логистическая регрессия')
plt.show()


encoding = {"Iris-setosa": 0, "Iris-versicolor": 1, "Iris-virginica": 2}

y_train_encoded = y_train.map(encoding)
y_test_encoded  = y_test.map(encoding)

lin_model = LinearRegression()
lin_model.fit(x_train, y_train_encoded)
y_pred_lin_cont = lin_model.predict(x_test)

y_pred_lin = np.rint(y_pred_lin_cont).astype(int)
y_pred_lin = np.clip(y_pred_lin, 0, 2)

print("Отчет по классификации (Линейная регрессия, после округления):")
print(classification_report(y_test_encoded, y_pred_lin))

conf_matrix_lin = confusion_matrix(y_test_encoded, y_pred_lin)
sns.heatmap(conf_matrix_lin, annot=True, fmt='d', cmap='magma')
plt.xlabel('Предсказанный класс')
plt.ylabel('Истинный класс')
plt.title('Матрица ошибок - Линейная регрессия')
plt.show()