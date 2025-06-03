import pandas as pd
import numpy as np
import tensorflow as tf
from sklearn.metrics import accuracy_score

test_data = pd.read_csv('gests\sign_mnist_test.csv')

if 'label' in test_data.columns:
    test_labels = test_data['label'].values
    test_pixels = test_data.drop('label', axis=1).values
else:
    test_labels = None
    test_pixels = test_data.values

test_pixels = test_pixels.astype('float32') / 255.0

test_images = test_pixels.reshape(-1, 28, 28, 1)

model = tf.keras.models.load_model('model.keras')
print("Модель загружена")

predictions = model.predict(test_images)

predicted_classes = np.argmax(predictions, axis=1)

print("Предсказанные классы:")
print(predicted_classes)

if test_labels is not None:
    accuracy = accuracy_score(test_labels, predicted_classes)
    print(f"Точность на тестовом наборе: {accuracy * 100:.2f}%")
