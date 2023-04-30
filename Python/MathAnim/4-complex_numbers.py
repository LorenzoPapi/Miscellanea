from manim import *
import numpy as np


class ComplexNumbers(Scene):
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
        template = TexTemplate()
        template.add_to_preamble(r"\usepackage{ragged2e}")
        quote = Tex('\\center{``It is essential that the original determination of the function concept be broadened to a domain of magnitudes which includes both the {{real}} and the {{imaginary}} quantities, under the single designation of {{complex numbers}}."}',
                     tex_template=template, font_size=40).to_edge(UP)
        quote.width = min(config["frame_width"], quote.width)
        quote[1].set_color(RED)
        quote[3].set_color(GREEN)
        quote[5].set_color(YELLOW)
        quoted = Text("-Carl Friedrich Gauss", color=BLUE, font_size=30).next_to(quote, DOWN)
        self.play(FadeIn(quote, lag_ratio = 0.05, rate_func = rate_functions.slow_into, run_time = 6))
        self.wait(2)
        self.play(Write(quoted, run_time=3))
        self.wait(2)
        self.play(FadeOut(quote), FadeOut(quoted))
        self.wait()
        pass

