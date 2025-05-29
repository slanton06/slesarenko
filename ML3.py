from sklearn.datasets import load_iris
from sklearn.model_selection import train_test_split
from sklearn.tree import DecisionTreeClassifier, plot_tree
from sklearn.metrics import accuracy_score, precision_score, confusion_matrix
import matplotlib.pyplot as plt

data = load_iris()
X = data.data
y = data.target

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.3, random_state=42)

clf = DecisionTreeClassifier(random_state=42)
clf.fit(X_train, y_train)

plt.figure(figsize=(15, 10))
plot_tree(
    clf,
    filled=True,
    feature_names=data.feature_names,
    class_names=data.target_names,
    rounded=True,
    fontsize=10
)
plt.title("Дерево решений")
plt.show()

y_pred = clf.predict(X_test)

accuracy = accuracy_score(y_test, y_pred)
precision = precision_score(y_test, y_pred, average='weighted')
print(f'Точность: {accuracy:.2f}')
print(f'Прецизионность: {precision:.2f}')

conf_matrix = confusion_matrix(y_test, y_pred)
print(f'Матрица ошибок:\n {conf_matrix}')

for true_label, predicted_label in zip(y_test, y_pred):
    print(f'Истинное: {true_label}, Предсказанное: {predicted_label}')
