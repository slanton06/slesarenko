import numpy as np
import matplotlib.pyplot as plt
from tensorflow import keras
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score
from sklearn.linear_model import LogisticRegression
from sklearn.ensemble import RandomForestClassifier

X = np.loadtxt('dataIn.txt', delimiter=None)
Y = np.loadtxt('dataOut.txt', delimiter=None)

print("X shape:", X.shape)
print("Y shape:", Y.shape)

X_train, X_test, Y_train, Y_test = train_test_split(X, Y, test_size=0.2, random_state=42)

model = keras.Sequential([
    keras.layers.Dense(12, activation='sigmoid', input_shape=(12,)),
    keras.layers.Dense(2, activation='softmax')
])

model.compile(optimizer='adam', loss='categorical_crossentropy', metrics=['accuracy'])

history = model.fit(X_train, Y_train, epochs=40, batch_size=16, validation_data=(X_test, Y_test))

test_loss, test_accuracy = model.evaluate(X_test, Y_test)
print("Accuracy:", test_accuracy * 100, "%")

Y_pred = model.predict(X_test)
predicted_classes = (Y_pred.argmax(axis=1))
true_classes = (Y_test.argmax(axis=1))
print("Accuracy:", accuracy_score(true_classes, predicted_classes))

plt.figure(figsize=(10, 4))

plt.subplot(1, 2, 1)
plt.plot(history.history['loss'], label='Training loss')
plt.plot(history.history['val_loss'], label='Validation loss')
plt.xlabel('Epochs')
plt.ylabel('Loss')
plt.title('Loss curve')
plt.legend()

plt.subplot(1, 2, 2)
plt.plot(history.history['accuracy'], label='Training accuracy')
plt.plot(history.history['val_accuracy'], label='Validation accuracy')
plt.xlabel('Epochs')
plt.ylabel('Accuracy')
plt.title('Accuracy curve')
plt.legend()

plt.tight_layout()
plt.show()

lr = LogisticRegression()
lr.fit(X_train, Y_train.argmax(axis=1))
Y_pred_lr = lr.predict(X_test)
acc_lr = accuracy_score(Y_test.argmax(axis=1), Y_pred_lr)
print("Logistic Regression Accuracy:", acc_lr * 100, "%")

rf = RandomForestClassifier(n_estimators=100)
rf.fit(X_train, Y_train.argmax(axis=1))
Y_pred_rf = rf.predict(X_test)
acc_rf = accuracy_score(Y_test.argmax(axis=1), Y_pred_rf)
print("Random Forest Accuracy:", acc_rf * 100, "%")
