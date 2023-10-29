import numpy as np

function = np.array([[0, 0, 1, 1],
                     [0, 0, 1, 1],
                     [0, 0, 0, 1],
                     [0, 1, 1, 1]])

#function = np.random.randint(0,2,(4,4))
print(function)
print()

padded = np.array([[0, *function[3], 0],
                   [function[0,3], *function[0], function[0,0]],
                   [function[1,3], *function[1], function[1,0]],
                   [function[2,3], *function[2], function[2,0]],
                   [function[3,3], *function[3], function[3,0]],
                   [0, *function[0], 0]])
result = np.zeros((4,4))

for i in range(1, 5):
    for j in range(1, 5):
        kernel = np.zeros((3,3))
            
        #kernel[0,1] = padded[i-1,j] #& ~(padded[i-1,j-1] & padded[i,j-1]) & ~(padded[i-1,j+1] & padded[i,j+1])
        #kernel[1,0] = padded[i,j-1] #& ~(padded[i-1,j-1] & padded[i-1,j]) & ~(padded[i+1,j-1] & padded[i+1,j])
        #kernel[1,2] = padded[i,j+1] #& ~(padded[i-1,j+1] & padded[i-1,j]) & ~(padded[i+1,j+1] & padded[i+1,j])
        #kernel[2,1] = padded[i+1,j] #& ~(padded[i-1,j+1] & padded[i-1,j]) & ~(padded[i+1,j+1] & padded[i,j+1])
        kernel[0,1] = padded[i,j]
        kernel[1,0] = padded[i,j]
        kernel[1,2] = padded[i,j]
        kernel[2,1] = padded[i,j]

        kernel[0,0] = -(padded[i-1,j-1] & padded[i,j-1] & padded[i-1,j] & padded[i,j]) 
        kernel[0,2] = -(padded[i-1,j+1] & padded[i,j+1] & padded[i-1,j] & padded[i,j]) 
        kernel[2,0] = -(padded[i+1,j-1] & padded[i,j-1] & padded[i+1,j] & padded[i,j]) 
        kernel[2,2] = -(padded[i+1,j+1] & padded[i,j+1] & padded[i+1,j] & padded[i,j]) 
        result[i-1,j-1] = sum([kernel[a,b] * padded[a+i-1,b+j-1] for a in range(0,3) for b in range(0,3)])
print(result)
print()

def prodottoscalare(matrice1,matrice2):
            
    val=0

    for i in range(3):
        for j in range(3):
            val+=matrice1[i,j]*matrice2[i,j]
    return val


matrice = padded
'''np.array([[0, 1, 1, 0, 1, 0],
                    [1, 1, 0, 0, 1, 1],
                    [1, 0, 1, 0, 1, 0],
                    [0, 1, 1, 0, 0, 1],
                    [1, 1, 1, 0, 1, 1],
                    [0, 1, 0, 0, 1, 0]])
'''

matrice_r = np.zeros((4,4))
for i in range(0,len(matrice)-3+1):
    for j in range(0,len(matrice)-3+1):
        #print(f"{0+i}:{3+i}, {0+j}:{3+j}")
        sottomatrice = matrice[0+i:3+i, 0+j:3+j]
        #print(sottomatrice)
        matrice_k = np.zeros((3,3))
        matrice_k[0, 0] = -(sottomatrice[0,0] & sottomatrice[1,1] & sottomatrice[1,0] & sottomatrice[0,1])
        matrice_k[0, 1] = sottomatrice[1,1]
        matrice_k[0, 2] = -(sottomatrice[0,2] & sottomatrice[1,1] & sottomatrice[1,2] & sottomatrice[0,1])
        matrice_k[1, 0] = sottomatrice[1,1]
        matrice_k[1, 1] = 1
        matrice_k[1, 2] = sottomatrice[1,1]
        matrice_k[2, 0] = -(sottomatrice[2,0] & sottomatrice[1,1] & sottomatrice[1,0] & sottomatrice[2,1])
        matrice_k[2, 1] = sottomatrice[1,1]
        matrice_k[2, 2] = -(sottomatrice[2,2] & sottomatrice[1,1] & sottomatrice[1,2] & sottomatrice[2,1])
        matrice_r[i,j]=int(prodottoscalare(sottomatrice,matrice_k))
        if (matrice_r[i,j] > 0): matrice_r[i,j] -= 1


print("questa si chiama meravigghia")
print(matrice_r)

