import numpy as np

np.random.seed(42)
N = 100

X = np.random.randint(0, 2, size=(N, 12))

threshold = 6
Y = np.zeros((N, 2), dtype=int)
for i, sample in enumerate(X):
    if sample.sum() >= threshold:
        Y[i] = [1, 0]
    else:
        Y[i] = [0, 1]

np.savetxt('dataIn.txt', X, fmt='%d')
np.savetxt('dataOut.txt', Y, fmt='%d')
