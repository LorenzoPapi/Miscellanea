import random

def get_nido():
    return 10, 10 

def rnd():
    return random.randint(1, 100)

def senti_feromone(a,b):
    nido = get_nido()
    return 1/(nido[0]-a)*1/(nido[1]-b)

def calcola_pos_des(x_inf, y_inf):
    max_fer = [0, 0, 0]
    for i in [-1, 0, 1]:
        for j in [-1, 0, 1]:
            if i != j:
                new_fer = senti_feromone(x_inf + i, y_inf + j)
                if max_fer[0] < new_fer:
                    max_fer[0] = new_fer
                    max_fer[1] = x_inf
                    max_fer[2] = y_inf
    return max_fer[1], max_fer[2]

def movimento_formica(x_noinf, y_noinf):
    return x_noinf + ((-1) if (rnd() > 50) else 1)*round(rnd()/100), y_noinf + ((-1) if (rnd() > 50) else 1)*round(rnd()/100)

def sposta_cibo(x_t, y_t):
    total_mov_x = total_mov_y = 0
    total_mov_x, total_mov_y += calcola_pos_des(x_t, y_t)
    for _ in range(5):
        total_mov_x, total_mov_y += movimento_formica(x_t, y_t)
    return x_t + total_mov_x, y_t + total_mov_y

