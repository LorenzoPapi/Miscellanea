from sympy.solvers import solve
from sympy import Symbol

intervals = []    
T = Symbol('t')

def x_a(t): return 30*t
def x_b(t): return 100 - 20*t

def x(i, t):
    if i == 1:
        return 40*t
    prev_t = times(i-1)
    return x(i - 1, prev_t) + ((-40)**(i-1))*(t - prev_t)

def times(i):
    global intervals, T
    if len(intervals) < i:
        ti = solve((x_b(T) if i & 1 else x_a(T)) - x(i, T), T)[0]
        intervals.append(ti)
    else:
        ti = intervals[i-1]
    return ti
        
times(50)
intervals = [0] + intervals
