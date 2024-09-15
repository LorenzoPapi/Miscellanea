# coding: utf-8
prev = [0 for i in range(100)]
def plottami(extr, alpha):
    x = np.arange(-extr, extr, alpha)
    y = [k for k in x if (k != 1 and k != -1)]
    plt.plot(x, y)
    y = [a(6,k) for k in x]
    plt.plot(x,y)
    #for i,v in enumerate(x):
    #    print(v, y[i])
    plt.show()
    
import matplotlib.pyplot as plt
plottami(0.005)
plottami(1,0.005)
import numpy as np
plottami(1,0.005)
plottami(2,0.005)
def a(n, k = None):
    if k == None: k = float(input("What k? > "))
    if (n==1):
        prev[1] = k
        return k
    else:
        prev[n] = a(n-1,k)
        return 1/(1-prev[n]) - 1/(1+prev[n])
        
plottami(2,0.005)
