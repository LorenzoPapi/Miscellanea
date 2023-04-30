from manim import *
import numpy as np


class EToThePiIPlusOneEqualsZero(Scene):
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
        euler = [
            MathTex(r"e^{i\theta}", "=", r"\cos(", r"\theta", ")" , "+", "\sin(", r"\theta", ")"),
            MathTex(r"e^{i\pi}", "=", r"\cos(", r"\pi", ")" , "+", "\sin(", r"\pi", ")"),
            MathTex(r"e^{i\pi}", "=", "-1", "+", "0"),
            MathTex(r"e^{i\pi}", "+", "1", "=", "0")
        ]
        sub_eq = MathTex(r"\theta=\pi").shift(UP)
        pis = VGroup(*[MathTex(r"\pi") for i in range(0,3)]).arrange_submobjects().shift(UP)
        self.play(Write(euler[0]))
        self.wait()
        self.play(GrowFromPoint(sub_eq, sub_eq.get_center()))
        self.play(Indicate(sub_eq), *[Indicate(i) for i in euler[0] if i.get_tex_string().find(r"\theta") != -1])
        self.play(sub_eq.animate().shift(UP), *[FadeIn(p) for p in pis])
        self.play(*[ReplacementTransform(pis[i], euler[1][[0,3,7][i]]) for i in range(0,3)], TransformMatchingTex(euler[0], euler[1]), ShrinkToCenter(sub_eq))
        self.wait()
        self.play(TransformMatchingTex(euler[1], euler[2]))
        self.wait()
        self.play(TransformMatchingTex(euler[2], euler[3]), run_time=2)
        self.wait(2)
        self.play(FadeOut(euler[3]))
        self.wait()

