import pandas as pd
import tensorflow as tf
import numpy as np
from tensorflow.keras.utils import to_categorical
from sklearn.model_selection import train_test_split

data = pd.read_csv('gests\sign_mnist_train.csv')

# метки и пиксельные данные
labels = data['label'].values
pixels = data.drop('label', axis=1).values

pixels = pixels.astype('float32') / 255.0
images = pixels.reshape((-1, 28, 28, 1))
labels = to_categorical(labels)

x_train, x_test, y_train, y_test = train_test_split(images, labels, test_size=0.2, random_state=42)

model = tf.keras.models.Sequential([
    tf.keras.layers.Conv2D(32, (3, 3), activation='relu', input_shape=(28, 28, 1)),
    tf.keras.layers.MaxPooling2D(2, 2), # decreasing size and saving imp feat

    tf.keras.layers.Conv2D(64, (3, 3), activation='relu'),
    tf.keras.layers.MaxPooling2D(2, 2),

    tf.keras.layers.Flatten(),
    tf.keras.layers.Dense(128, activation='relu'), # get all the features and turn it into final results
    tf.keras.layers.Dense(labels.shape[1], activation='softmax')
])

model.compile(optimizer='adam', loss='categorical_crossentropy', metrics=['accuracy'])

model.fit(x_train, y_train, validation_data=(x_test, y_test), epochs=10)

loss, accuracy = model.evaluate(x_test, y_test)
print(f'Точность модели: {accuracy * 100:.2f}%')

model.save('model.keras')
print("Модель сохранена")
