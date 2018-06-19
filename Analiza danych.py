import json
import pandas as pd
import glob
import numpy as np
from pyramid.arima import auto_arima
import matplotlib.pyplot as plt



with open('AUD.json') as f:
    AUD =json.load(f)
    # I created and dropped a column, because I had to inizialize data frame :P
    dframe = pd.DataFrame.from_dict(AUD)
    dframe = dframe.drop('AUD', 1)
for filename in glob.glob('*.json'):
    with open(filename) as f:
        if (filename!='pJY.json' and filename!='pUS.json' and filename!='pGB.json' and filename!='pEU.json' and filename!='pCH.json'):
            d=json.load(f)
            df2=pd.DataFrame.from_dict(d).astype(float)
            df=dframe
            dframe=pd.concat([df,df2],axis=1)
dfpairs= pd.DataFrame.from_dict(AUD)

dfpairs['USDJPY']=dframe['USD']/dframe['JPY']
dfpairs['EURUSD']=dframe['EUR']/dframe['USD']
dfpairs['USDCHF']=dframe['USD']/dframe['CHF']
dfpairs['GBPUSD']=dframe['GBP']/dframe['USD']
dfpairs['EURGBP']=dframe['EUR']/dframe['GBP']
dfpairs['EURJPY']=dframe['EUR']/dframe['JPY']
dfpairs['GBPJPY']=dframe['GBP']/dframe['JPY']
dfpairs['EURCHF']=dframe['EUR']/dframe['CHF']
dfpairs['CHFJPY']=dframe['CHF']/dframe['JPY']
dfpairs['GBPCHF']=dframe['GBP']/dframe['CHF']

modell=auto_arima(dfpairs.CHFJPY, start_p=1, start_q=1,
                           max_p=5, max_q=5,
                           seasonal=False,
                           d=0, D=0, trace=False,
                           stepwise=True)

with open('pJY.json') as f:
    d = json.load(f)
    df2=[]
    df2 = pd.DataFrame.from_dict(d).astype(float)
with open('pUS.json') as f:
    d = json.load(f)
    df3 = pd.DataFrame.from_dict(d).astype(float)
with open('pGB.json') as f:
    d = json.load(f)
    df4 = pd.DataFrame.from_dict(d).astype(float)
with open('pEU.json') as f:
    d = json.load(f)
    df5 = pd.DataFrame.from_dict(d).astype(float)
with open('pCH.json') as f:
    d = json.load(f)
    df6 = pd.DataFrame.from_dict(d).astype(float)
dfpredict=pd.concat([df3,df2,df4,df5,df6],axis=1)
#print(dfpredict)

future_forecast = modell.predict(n_periods=30)
forecast=pd.DataFrame({'col':future_forecast})

USDDJPYY=dfpredict['USD']/dfpredict['JPY']
GBPPJPYY=dfpredict['GBP']/dfpredict['JPY']
EURRJPYY=dfpredict['EUR']/dfpredict['JPY']
CHFFJPYY=dfpredict['CHF']/dfpredict['JPY']
pd.concat([CHFFJPYY,forecast],axis=1).plot()
plt.show()


x=USDDJPYY.values.tolist()
y=GBPPJPYY.values.tolist()
plt.xcorr(USDDJPYY,GBPPJPYY,maxlags=5)
plt.show()

'''


dfpairs['GBP/USD']=dframe['GBP']/dframe['USD']
dfscipy.signal.correlate2d.pairs['GBP/CHF']=dframe['GBP']/dframe['CHF']
dfpairs['USD/NOK']=dframe['USD']/dframe['NOK']
dfpairs['USD/CAD']=dframe['USD']/dframe['CAD']
dfpairs['CAD/NOK']=dframe['CAD']/dframe['NOK']
dfpairs['EUR/NOK']=dframe['EUR']/dframe['NOK']
'''

dfpairs=dfpairs.drop('AUD',1)
print(dfpairs)

np.savetxt(r'd:\python project\correlation2.txt', dframe.corr(), fmt='%f')
np.savetxt(r'd:\python project\correlation.txt', dfpairs.corr(), fmt='%f')


#print(dfpairs.corr())
plt.plot(dfpairs['USDJPY'])
plt.show()
#pd.plotting.autocorrelation_plot(dfpairs['USDJPY']);
#plt.acorr(dfpairs['USDJPY'].diff,maxlags=10)
plt.show()
plt.xcorr(dfpairs['USDJPY'],dfpairs['GBPCHF'],maxlags=120)
plt.show()
#zrobic prophet,