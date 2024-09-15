from sympy.solvers import solve
from sympy import Symbol
import matplotlib.pyplot as plt
import numpy as np
import matplotlib.animation as animation
from matplotlib.patches import Circle

m = 0.5 # Massa della sfera
R = 0.05 # Raggio della sfera
L0 = 1 # Distanza iniziale tra le sfere
v0 = 50 # Velocità iniziale sfere
H = 50*R # Grandezza della finestra (che soddisfi H >> R)
v1 = v0*2*R/L0 # Velocità finale sfere
xA = 2 # Posizione del primo muro
xB = xA + H # Posizione del secondo muro
am = (v1**2 - v0**2)/(2*H) # Accelerazione media della sfera

XMAX = xB + 1
dt = 0.0005 # Intervallo

class Sfera():
    def __init__(self, x0):
        self.x0 = x0
        self.t1 = abs(xA - x0)/v0
        self.t2 = self.t1 + 2*(H)/(v0+v1)
        self.circle = Circle((x0, 5), R)
    
    def pos(self, t=0):
        x, v = self.x0, v0
        while x < XMAX:
            t += dt
            if t <= self.t1:
                x += v0*dt
            elif t > self.t1 and t <= self.t2:
                x += v*dt
                v += am*dt
                print(v)
            else:
                x += v1*dt
            yield x
    
    def update(self, pos):
        # for each frame, update the data stored on each artist.
        self.circle.set_center((pos, 5))
        return self.circle

    def init(self):
        self.circle.set_center((s.x0, 5))
        return self.circle
    
    def animate(self):
        return animation.FuncAnimation(fig, self.update, self.pos, interval=10000*dt, repeat=False, init_func=self.init)

fig, ax = plt.subplots()
ax.axvline(x=xA)
ax.axvline(x=xB)

ax.set_aspect('equal')
sfere = []
for i in range(5):
    sfere.append(Sfera(-L0*i))
anims = []
for s in sfere:
    ax.add_patch(s.circle)

ax.set_xlim(-xA, XMAX)
ax.set_ylim(4.7, 5.3)
for s in sfere:
    anims.append(s.animate())
plt.show()