from sklearn.datasets import load_iris
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestClassifier, GradientBoostingClassifier
from sklearn.metrics import accuracy_score, f1_score, roc_auc_score, precision_score, recall_score
from sklearn.model_selection import cross_val_score
import matplotlib.pyplot as plt

data = load_iris()
X = data.data
y = data.target

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.3, random_state=42)

rf = RandomForestClassifier(random_state=42)
rf.fit(X_train, y_train)
rf_pred = rf.predict(X_test)

gb = GradientBoostingClassifier(random_state=42)
gb.fit(X_train, y_train)
gb_pred = gb.predict(X_test)

print("Random Forest Accuracy:", accuracy_score(y_test, rf_pred))
print("Gradient Boosting Accuracy:", accuracy_score(y_test, gb_pred))
print("Random Forest F1-Score:", f1_score(y_test, rf_pred, average='weighted'))
print("Gradient Boosting F1-Score:", f1_score(y_test, gb_pred, average='weighted'))
rf_probs = rf.predict_proba(X_test)
gb_probs = gb.predict_proba(X_test)
print("Random Forest AUC:", roc_auc_score(y_test, rf_probs, multi_class='ovr'))
print("Gradient Boosting AUC:", roc_auc_score(y_test, gb_probs, multi_class='ovr'))
print("Random Forest Precision:", precision_score(y_test, rf_pred, average='weighted'))
print("Gradient Boosting Precision:", precision_score(y_test, gb_pred, average='weighted'))
print("Random Forest Recall:", recall_score(y_test, rf_pred, average='weighted'))
print("Gradient Boosting Recall:", recall_score(y_test, gb_pred, average='weighted'))
rf_cv = cross_val_score(rf, X, y, cv=5, scoring='accuracy')
gb_cv = cross_val_score(gb, X, y, cv=5, scoring='accuracy')
print("Random Forest Cross-validation accuracy:", rf_cv.mean())
print("Gradient Boosting Cross-validation accuracy:", gb_cv.mean())

models = ['Random Forest', 'Gradient Boosting']
accuracy = [accuracy_score(y_test, rf_pred), accuracy_score(y_test, gb_pred)]

plt.bar(models, accuracy)
plt.title("Model Comparison")
plt.ylabel("Accuracy")
plt.show()