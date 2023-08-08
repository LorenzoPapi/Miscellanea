from manim import *
import numpy as np


class Limits(Scene):
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

        quote = Tex('\\center{``The only way to discover the {{limits}} of the {{possible}}, is to go {{beyond}} them into the {{impossible}}."}',
                     tex_template=template, font_size=40).to_edge(UP)
        quote.width = min(config["frame_width"], quote.width)
        quote[1].set_color(RED)
        quote[3].set_color(GREEN)
        quote[5].set_color(PURPLE)
        quote[7].set_color(YELLOW)
        quoted = Text("-Arthur C. Clarke", color=BLUE, font_size=30).next_to(quote, DOWN)
        self.play(FadeIn(quote, lag_ratio = 0.05, rate_func = rate_functions.slow_into, run_time = 6))
        self.wait( )
        self.play(Write(quoted, run_time=3))
        self.wait(2)
        self.play(FadeOut(quote), FadeOut(quoted))
        self.wait()

        def_group = VGroup()
        first_def = Tex('\\center{Il {{limite di una funzione}} è un\'operatore che permette di studiare il comportamento di una funzione nell\'{{intorno di un punto}} e grazie al quale si può stabilire il {{valore a cui tende}} la funzione man mano che i valori della variabile dipendente {{si approssimano}} a quel punto.}', tex_template=template, font_size=40).to_edge(UP)
        first_def.width = min(config["frame_width"], first_def.width)
        first_def[1].set_color(RED)
        first_def[3].set_color(GREEN)
        first_def[5].set_color(PURPLE)
        first_def[7].set_color(YELLOW)
        self.play(FadeIn(first_def, lag_ratio = 0.05, rate_func = rate_functions.slow_into, run_time = 6))
        self.wait(2)
        def_group.add(*[first_def[2*i + 1] for i in range(0,4)])
        self.play(*[FadeOut(first_def[2*i]) for i in range(0,5)])
        self.play(def_group.animate.arrange(DOWN, buff=1).shift(LEFT*4))
        limit_example_text = Tex(r'$$ \lim_{x\to a} f(x) = L $$').shift(RIGHT*3)
        self.wait()
        self.play(ReplacementTransform(def_group.copy(), limit_example_text), run_time=2)
        self.wait(2)
        self.play(FadeOut(limit_example_text), FadeOut(def_group))
        self.wait()

        self.play(FadeIn(def_group))
        self.wait()
        
        limit_of_function = Tex(r'Limite di una {{funzione}}').shift(2*UP)
        self.play(ReplacementTransform(def_group[0].copy(), limit_of_function), FadeOut(def_group))
        self.play(Indicate(limit_of_function[1]))
        self.wait()
        function_definition = Tex(r'{{$ f: A\mapsto B $}}{{$ \iff $}}{{$ \forall \ x \in A, $}}{{$\ \exists! \ y \in B $}}{{% : \ y = f(x) $}}')
        function_steps = Tex(r'{{Una funzione $f$ da $A$ a $B$ }}{{è tale se e solo se }}{{per ogni $x$ in $A$ (dominio) }}{{esiste ed è unico $y$ in $B$ (codominio) }}{{tale che $y=f(x)$}}')
        function_steps.scale(0.7).shift(DOWN*2)
        self.play(ReplacementTransform(limit_of_function[1].copy(), function_definition))
        self.wait()
        for i in range(0,5):
            self.play(ReplacementTransform(function_definition[i].copy(), function_steps[i]))
            self.wait(0.1)
            self.play(Indicate(function_steps[i]))
            self.wait(0.5)
        self.play(FadeOut(function_steps), FadeOut(function_definition))
        self.wait()
        why_limit = Tex(r'Ma quindi a che serve il limite? Principalmente permette di assegnare un valore del codominio a valori che sono {{agli estremi del suo dominio originario}}, e sono detti {{punti di frontiera}}, ma ha molti altri usi che verranno studiati più a fondo in seguito. Perora è un semplice strumento.')
        why_limit.width = why_limit.width = min(config["frame_width"], why_limit.width)
        why_limit[1].set_color(RED)
        why_limit[3].set_color(BLUE_D)
        self.play(ReplacementTransform(limit_of_function, why_limit))
        self.wait(2)
        self.play(FadeOut(why_limit))
        self.wait()

        self.play(FadeIn(def_group))
        self.wait()

        
        
        pass

