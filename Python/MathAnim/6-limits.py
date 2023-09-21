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
        first_def = Tex('\\center{Il {{limite di una funzione}} è un\'operatore che permette di studiare il comportamento di una funzione nell\'{{intorno di un punto}} e grazie al quale si può stabilire il {{valore a cui tende}} la funzione man mano che i valori della variabile dipendente {{si approssimano al punto}}.}', tex_template=template, font_size=40).to_edge(UP)
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
        limit_example_text = MathTex(r'\lim_{x\to a} f(x) = {{L}}')
        self.wait()
        self.play(ReplacementTransform(def_group.copy(), limit_example_text.shift(RIGHT*3)), run_time=2)
        self.wait(2)
        self.play(FadeOut(limit_example_text), FadeOut(def_group))
        self.wait()

        self.play(FadeIn(def_group))
        self.wait()
        
        limit_of_function = Tex(r'Limite di una {{funzione}}').shift(2*UP)
        self.play(ReplacementTransform(def_group[0].copy(), limit_of_function), FadeOut(def_group))
        self.play(Indicate(limit_of_function[1]))
        self.wait()
        function_definition = MathTex(r'{{f: A\mapsto B }}{{ \iff }}{{ \forall \ x \in A, }}{{\ \exists! \ y \in B }}{{ : \ y = f(x) }}')
        function_steps = Tex(r'{{Una funzione $f$ da $A$ a $B$ }}{{è tale se e solo se }}{{per ogni $x$ in $A$ (dominio) }}{{esiste ed è unico $y$ in $B$ (codominio) }}{{tale che $y=f(x)$}}').scale(0.7).shift(DOWN*2)
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
        why_limit.width = min(config["frame_width"], why_limit.width)
        why_limit[1].set_color(RED)
        why_limit[3].set_color(BLUE_D)
        self.play(ReplacementTransform(limit_of_function, why_limit))
        self.wait(2)
        self.play(FadeOut(why_limit))
        self.wait()

        self.play(FadeIn(def_group))
        self.wait()

        n_o_p = Tex(r'Intorno* di un punto').shift(2*UP)
        n_o_p_spec = Tex(r'*Quando si parla di "intorno" senza specificarne il tipo, si sottintende che sia circolare (o completo), cioè simmetrico rispetto al punto stesso.').scale(0.4).to_corner(DR)
        self.play(ReplacementTransform(def_group[1].copy(), n_o_p), FadeOut(def_group), FadeIn(n_o_p_spec))
        self.play(Indicate(n_o_p))
        self.wait()
        n_def = [
            MathTex(r'{{I(x_0, \epsilon)}}{{ := (x_0 - \epsilon, x_0 + \epsilon) }}'),
            MathTex(r'{{I(x_0, \epsilon)}}:= \{x \in \mathbb{R} \ \big| x_0 - \epsilon < x < x_0 + \epsilon \}'),
            MathTex(r'{{I(x_0, \epsilon)}}:= \{x \in \mathbb{R} \ \big| - \epsilon < x - x_0 < \epsilon \}'),
            MathTex(r'{{I(x_0, \epsilon)}}:= \{x \in \mathbb{R} \ \big| |x - x_0| < \epsilon \}')
        ]
        n_steps = Tex(r"{{L'intorno* di raggio $\epsilon > 0$ del punto $x_0$ }}{{è l'intervallo aperto a destra e a sinistra da $x_0 - \epsilon$ a $x_0 + \epsilon$}}").scale(0.7).shift(DOWN*2)
        self.play(ReplacementTransform(n_o_p.copy(), n_def[0]))
        for i in range(0,2):
            self.play(ReplacementTransform(n_def[0][i].copy(), n_steps[i]))
            self.wait(0.1)
            self.play(Indicate(n_steps[i]))
            self.wait(0.5)
        for i in range(0,3):
            self.play(TransformMatchingTex(n_def[i], n_def[i+1]))
            self.wait(1)
        n_meaning = Tex(r"Data la condizione $\epsilon > 0$, l'intorno può essere reso {{piccolo a piacere}}, caratteristica che sarà fondamentale per la definizione rigorosa del limite di una funzione. In sostanza, noi possiamo rendere questo intervallo (l'intorno) più piccolo possibile, senza mai includere solamente il punto stesso, sfruttando la {{densità di $\mathbb{R}$}}.")
        n_meaning.width = min(config["frame_width"], n_meaning.width)
        n_meaning[1].set_color(RED)
        n_meaning[3].set_color(BLUE_D)
        self.play(FadeOut(n_def[3]), FadeOut(n_steps), FadeOut(n_o_p_spec), ReplacementTransform(n_o_p, n_meaning))
        self.wait(2)
        self.play(FadeOut(n_meaning))
        self.wait()

        self.play(FadeIn(def_group))
        self.wait()

        lim_value = Tex(r'Valore a cui tende').shift(2*UP)
        self.play(ReplacementTransform(def_group[2].copy(), lim_value), FadeOut(def_group))
        self.play(Indicate(lim_value))
        self.wait()
        self.play(ReplacementTransform(lim_value.copy(), limit_example_text.shift(LEFT*3)))
        self.play(Indicate(limit_example_text[1]))
        lim_value_meaning = Tex(r"Se il valore del limite $L$ è un numero reale, il limite si dice {{convergente}} e l'espressione $f(x)$ {{converge ad $L$}} per $x$ che tende ad $a$; altrimenti, può essere $\pm\infty$ ed il limite è {{divergente}}.")
        lim_value_meaning.width = min(config["frame_width"], lim_value_meaning.width)
        lim_value_meaning[1].set_color(YELLOW)
        lim_value_meaning[3].set_color(YELLOW)
        lim_value_meaning[5].set_color(RED)
        self.play(FadeOut(limit_example_text), ReplacementTransform(lim_value, lim_value_meaning))
        self.wait(2)
        self.play(FadeOut(lim_value_meaning))
        self.wait()

        self.play(FadeIn(def_group))
        self.wait()

        approx_value = Tex('Si approsimano al punto').shift(2*UP)
        self.play(ReplacementTransform(def_group[3].copy(), approx_value), FadeOut(def_group))
        self.play(Indicate(approx_value))
        self.wait()
        self.play(ReplacementTransform(approx_value.copy(), limit_example_text))
        self.play(Indicate(limit_example_text[0][3:6]))
        approx_meaning = Tex(r"Per approssimazione al punto $a$, intendiamo {{l'analisi del comportamento della funzione}} per valori che si {{avvicinano arbitrariamente al punto stesso}}: se la funzione non è definita in $a$, non possiamo semplicemente dare alla variabile dipendente questo valore, ma possiamo assegnarle valori arbitrariamente vicini ad esso. Inoltre, $a$ può essere sia un {{numero reale}} sia {{$\pm\infty$}}.")
        approx_meaning.width = min(config["frame_width"], approx_meaning.width)
        approx_meaning[1].set_color(BLUE_D)
        approx_meaning[3].set_color(YELLOW)
        approx_meaning[5].set_color(RED)
        approx_meaning[7].set_color(PURPLE)
        self.play(FadeOut(limit_example_text), ReplacementTransform(approx_value, approx_meaning))
        self.wait(2)
        self.play(FadeOut(approx_meaning))
        self.wait()

        note = Tex('Poichè sia $a$ che $L$ possono assumere valore finito o infinito, in totale abbiamo 4 tipi di limiti:')
        note.width = min(config["frame_width"], note.width)
        texts = []
        for i in ["finito", "infinito"]:
            for j in ["finito", "infinito"]:
                t = Tex(j + " per $x$ che tende a un valore " + i)
                t.width = min(config["frame_width"], t.width)
                texts.append(t)
        texts[0].set_color(RED)
        texts[1].set_color(GREEN)
        texts[2].set_color(PURPLE)
        texts[3].set_color(YELLOW)
        VGroup(limit_example_text, note, *texts).arrange(DOWN)
        self.play(FadeIn(limit_example_text))
        self.wait(0.5)
        self.play(ReplacementTransform(limit_example_text.copy(), note))
        self.play(AnimationGroup(*[FadeIn(texts[i]) for i in range(0,4)], lag_ratio=1.0))
        self.wait(3)
        self.play(*[FadeOut(texts[i]) for i in range(1,4)], FadeOut(note))
        self.play(VGroup(limit_example_text, texts[0]).animate.arrange(DOWN))
        self.wait()

        title = Title("Limite a valore finito per $x$ che tende a un valore finito")
        self.play(FadeOut(limit_example_text), ReplacementTransform(texts[0], title))
        example_function = MathTex(r'f(x) = \frac{x^3-1}{x^4-1}')
        self.play(FadeIn(example_function))
        self.play(example_function.animate.scale(0.5).next_to(title, DOWN, buff=0.3).align_to(title, RIGHT))
        self.wait()
        grid = Axes(x_range = [-0.5, 6, 1], y_range = [-0.5, 1.5, 0.25],
                    x_length = 9, y_length = 5.5, tips = False,
                    axis_config={ "numbers_to_include": np.arange(-0.5,1.75,0.25), "font_size": 24},
                    x_axis_config={ "numbers_to_include": np.arange(-0.5,7,1) }
        ).align_to(title, LEFT).shift(0.5*DOWN)
        y_label = grid.get_y_axis_label("y", edge=LEFT, direction=LEFT, buff=0.4)
        x_label = grid.get_x_axis_label("x")
        grid_labels = VGroup(x_label, y_label)
        graph = grid.plot(lambda x: (x**3-1)/(x**4-1), x_range=[0, 6, 0.001], discontinuities=[1], dt = 0.01, color=GREEN)
        disc = Circle(0.08, color=GREEN).move_to(grid.c2p(1,3/4))
        self.play(Create(graph), Create(grid), Create(grid_labels))
        self.play(Create(disc))
        self.wait(2)

        not_defined = Tex(r"Al punto $x=1$ la funzione non è definita, infatti $f(1) = \frac{1^3-1}{1^4-1} = \frac{0}{0} che è una forma indeterminata. Però dal grafico sembra che questa {{discontinuità}} si possa eliminare, e quindi che ci sia un modo per {{definire}} la funzione a $x=1$. Introduciamo così il limite:")
        function_limit = Tex(r"$\lim_{x\to 1} f(x) = L$")
        
        pass

