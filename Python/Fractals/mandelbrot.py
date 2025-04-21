import numpy as np
import matplotlib.pyplot as plt
import matplotlib.animation as animation

def mandelbrot(x, y, tres):
    c = complex(x, y)
    z = complex(0, 0)

    for i in range(tres):
        z = z**2 + c
        if abs(z) > lim:
            return i
        
    return tres - 1

def sucaggio(x, y, tres):
    c = complex(x, y)
    z = complex(0, 0)

    for i in range(tres):
        z = z**6 + c
        if abs(z) > lim:
            return i
        
    return tres - 1

def drawset(i):
    ax.clear()
    ax.set_xticks([], [])
    ax.set_yticks([], [])

    X = np.empty((len(re), len(im)))

    tres = round(1.15**(i + 1))
    print(len(re)*len(im)*tres)

    for i in range(len(re)):
        for j in range(len(im)):
            X[i, j] = fractal(re[i], im[j], tres)
    
    img = ax.imshow(X.T, interpolation="bicubic", cmap="magma")
    return [img]

if __name__ == "__main__":
    x0, y0 = -2, -1.5
    w, h = 3, 3
    d = 250
    re = np.linspace(x0, x0 + w, w * d)
    im = np.linspace(y0, y0 + h, h * d)
    fractal = None

    fig = plt.figure(figsize=(10, 10))
    ax = plt.axes()

    fractal = sucaggio
    lim = 4

    anim = animation.FuncAnimation(fig, drawset, frames=30, interval=120, blit=True)
    plt.show()
    #anim.save("anim.gif", "pillow")


