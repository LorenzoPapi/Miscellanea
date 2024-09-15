import random

l = 3
def gen_random_perm(k=4):
    return [random.choice(range(10)) for _ in range(k)]

mine = gen_random_perm(l)
#guessed = []
probable = []
histories = [[], [], [], []]

def other_move():
    global l
    print('Yours:', ''.join(map(str, mine)))
    oth = [int(i) for i in input('Other: ')]
    
    d = 0
    for p in zip(mine, oth):
        if p[0] == p[1]:
            d += 1
    if d == l:
        return False
    else:
        print(d)
        return True

def my_move():
    if len(probable) < 2:
        for h in histories:
            h.append(random.choice([r for r in range(10) if r not in h]))
        guess = [h[-1] for h in histories]
    else:
        assert probable[0][1] == 0
        guess = probable[0].copy()
    print('Guess:', ''.join(map(str, guess)))
    corrects = int(input('Response: '))
    if corrects == 0 and len(probable) == 0:
        probable.append((guess, 0))
        return
    else:
        probable.append((guess, corrects))
    print(probable)

while True:
    # my_move()
    # print()
    if not other_move():
        print('HAI PERSO')
        break