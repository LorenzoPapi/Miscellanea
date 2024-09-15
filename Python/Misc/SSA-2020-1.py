# https://codereview.stackexchange.com/questions/277384/python-fractal-tree-generators

import matplotlib.pyplot as plt
import numpy as np
from matplotlib.collections import LineCollection
from PIL import Image

def cos(d): return np.cos(np.radians(d))
def sin(d): return np.sin(np.radians(d))
def tan(d): return np.tan(np.radians(d))

def next_node(pos1, pos2, cosa, sina, length):
    x1, y1 = pos1
    x2, y2 = pos2
    dx = x2 - x1
    dy = y2 - y1
    r = (dx*dx + dy*dy)**.5
    L = length / r
    cosaL, sinaL = cosa*L, sina*L
    x3 = dx * cosaL - dy * sinaL + x2
    y3 = dy * cosaL + dx * sinaL + y2
    return (x3, y3)

def fractal_tree(iterations, span, branches, ratio=0.75, unit=1, initial_branches=1):
    assert branches >= 2
    assert initial_branches >= 1
    half = span / 2
    angles = [-half]
    diff = span / (branches - 1)
    for i in range(1, branches - 1):
        angles.append(diff * i - half)
    angles.append(half)
    trigos = [(cos(angle), sin(angle)) for angle in angles]
    segments = [[(0, 0), (0, unit)]]
    if initial_branches == 2:
        segments.append([(0, 0), (0, -unit)])
    
    elif initial_branches > 2:
        rotation = 360 / initial_branches
        for i in range(1, initial_branches):
            segments.append([(0, 0), (unit*cos(90+i*rotation), unit*sin(90+i*rotation))])
    
    cur_level = segments.copy()
    levels = [cur_level]
    for i in range(1, iterations):
        next_level = []
        L = unit*ratio**i
        for pos1, pos2 in cur_level:
            for cosa, sina in trigos:
                pos3 = next_node(pos1, pos2, cosa, sina, L)
                segments.append([pos2, pos3])
                next_level.append([pos2, pos3])
        
        levels.append(next_level)
        cur_level = next_level
    
    return {'segments': segments, 'levels': levels}

def plot_fractal_tree(iterations, span, branches=2, ratio=0.75, unit=1, initial_branches=1, width=1920, height=1080, show=True, random_colors=True, default_color="#875cff", control_width=False):
    assert ratio >= 0 and ratio <= 1
    fig = plt.figure(figsize=(width/100, height/100),
                     dpi=100, facecolor='black')
    ax = fig.add_subplot(111)
    ax.set_axis_off()
    segments, levels = fractal_tree(iterations, span, branches, ratio, unit, initial_branches).values()
    colors = default_color
    if random_colors:
        colors = np.random.random((len(segments), 3))
    if not control_width:
        collection = LineCollection(segments, edgecolors=colors)
        ax.add_collection(collection)
    else:
        for i, level in enumerate(levels):
            collection = LineCollection(level, edgecolors=colors, lw=iterations-i)
            ax.add_collection(collection)
    
    plt.axis('scaled')
    fig.subplots_adjust(left=0, bottom=0, right=1, top=1, wspace=0, hspace=0)
    fig.canvas.draw()
    image = Image.frombytes(
        'RGB', fig.canvas.get_width_height(), fig.canvas.tostring_rgb())
    if not show:
        plt.close(fig)
    else:
        plt.show()
    return image

plot_fractal_tree(8, 90, ratio=0.5) #span è alpha, ratio è rho