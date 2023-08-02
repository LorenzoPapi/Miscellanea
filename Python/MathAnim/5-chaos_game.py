from manim import *
import numpy as np
from random import randint

times = [1/2, 1/3, 2/3, 1/4, 3/4, 1/5, 2/5, 3/5, 4/5]
colors = [WHITE, RED, YELLOW, BLUE, GREEN, PURPLE, ORANGE, PINK, TEAL, GRAY]
dots = []

class ChaosGame(Scene):
    def replace_animation(self, old, new, replacements, indexes, animations1 = [], animations2 = []):
        temp = []
        for r in replacements:
            temp.append(r.copy())
        self.add(*temp)
        for i in indexes:
            new[i].set_fill(opacity=0)
        self.play(TransformMatchingTex(old, new), *animations1)
        for i in indexes:
            self.remove(new[i])
            new[i].set_fill(opacity=1)
        self.play(*[ReplacementTransform(temp[index], new[item]) for (index, item) in enumerate(indexes)], *animations2)

    def replace_animation_no_copy(self, old, new, replacements, indexes, animations1 = [], animations2 = []):
        for i in indexes:
            new[i].set_fill(opacity=0)
        self.play(TransformMatchingTex(old, new), *animations1)
        for i in indexes:
            self.remove(new[i])
            new[i].set_fill(opacity=1)
        self.play(*[ReplacementTransform(replacements[index], new[item]) for (index, item) in enumerate(indexes)], *animations2)

    def construct(self):
        for sides in range(3,8):
            shape = RegularPolygon(n=sides).scale(2.5)
            shape.z_index = 3
            verts = shape.get_vertices()
            dot = Dot(ORIGIN, radius = 0.02)
        
            self.play(SpinInFromNothing(shape))
            k = 0
            for t in times:
                text = Text(f"{t}").move_to(2*UL)
                self.add(text)
                '''for i in range(0,3):
                    vert = verts[randint(0,len(verts)-1)]
                    l1 = Line(dot, vert)
                    self.play(GrowFromPoint(l1, dot),run_time=0.5)
                    dot = Dot((1-t)*dot.get_center()+t*vert, radius = 0.02)
                    self.play(FadeIn(dot),run_time=0.5)
                    self.play(FadeOut(l1),run_time=0.5)
                '''
                for i in range(0,2000):
                    vert = verts[randint(0,len(verts)-1)]
                    dot = Dot((1-t)*dot.get_center()+t*vert, radius = 0.04, color = colors[k%len(colors)])
                    dots.append(dot)
                g = Group(*dots)
                self.add(g)
                self.wait(0.5)
                self.remove(g)
                self.remove(text)
                dots.clear()
                k+=1
            self.remove(shape)
                
