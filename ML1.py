import pandas as pd
from sklearn.preprocessing import MinMaxScaler

df = pd.read_csv('german_credit_data.csv')
pd.set_option('display.max_columns', None)
print(df.head())

missing_values = df.isnull().sum()
print(missing_values)

df.dropna(subset=['Saving accounts'], inplace=True)
df.dropna(subset=['Checking account'], inplace=True)
df['Saving accounts'].fillna(df['Saving accounts'].mode() [0])
df['Checking account'].fillna(df['Checking account'].mode() [0])

df = pd.get_dummies(df, columns=['Sex', 'Job', 'Housing', 'Saving accounts', 'Checking account', 'Purpose'], drop_first=True)

cols = ['Age', 'Credit amount', 'Duration']
scaler = MinMaxScaler()
df[cols] = scaler.fit_transform(df[cols])
df.reset_index(drop=True, inplace=True)
print(df.head())

