from manim import *
import numpy as np

class LimitsGraph(MovingCameraScene):
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
        title = Title("Limite a valore finito per $x$ che tende a un valore finito")
        self.add(title)
        example_function = MathTex(r'f(x) = \frac{x^3-1}{x^4-1}')
        self.play(FadeIn(example_function))
        self.play(example_function.animate.scale(0.5).next_to(title, DOWN, buff=0.1).align_to(title, RIGHT)) #.shift(RIGHT*0.4 )
        self.wait()
        grid = Axes(x_range = [-0.5, 6, 0.5], y_range = [-0.25, 1.5, 0.25],
                    x_length = 9, y_length = 5.5,
                    axis_config={"numbers_to_include": np.arange(0, 1.50, 0.25), "font_size": 24},
                    x_axis_config={"numbers_to_include":np.arange(0, 6, 0.5)}
        ).align_to(title, LEFT).shift(0.5*DOWN)
        y_label = grid.get_y_axis_label("y")
        x_label = grid.get_x_axis_label("x")
        grid_labels = VGroup(x_label, y_label)
        graph = grid.plot(lambda x: (x**3-1)/(x**4-1), x_range=[0, 6, 0.001], discontinuities=[1], dt = 0.01, color=GREEN)
        disc = Circle(0.08, color=GREEN).move_to(grid.c2p(1,3/4))
        
        graph_group = VGroup(graph, grid, grid_labels, disc)
        self.add(graph_group)
        not_defined = Tex(r"Al punto $x=1$ la funzione non è definita, infatti $f(1) = \frac{1^3-1}{1^4-1} = \frac{0}{0}$ che è una forma indeterminata.\\ Però dal grafico sembra che questa {{discontinuità}} si possa eliminare, e quindi che ci sia un modo per {{definire}} la funzione a $x=1$. Introduciamo così il limite:", font_size = 30).shift(UP)
        not_defined.width = min(config["frame_width"], not_defined.width)
        not_defined[1].set_color(RED)
        not_defined[3].set_color(YELLOW)
        function_limit = Tex(r"$$\lim_{x\to 1} f(x) = L$$").next_to(not_defined, DOWN)
        first_step = Tex(r"Sfruttiamo il concetto di intorno per definire il concetto di limite: consideriamo", font_size = 30)
        first_step.width = min(config["frame_width"], first_step.width)
        big_I_1 = MathTex(r"I(1,\delta)").set_color(BLUE).next_to(first_step, DOWN)
        second_step = Tex(r"Per gli estremi dell'intorno consideriamo i rispettivi valori che la funzione gli associa", font_size = 30) 
        second_step.width = min(config["frame_width"], second_step.width)
        
        last_graph_group_state = graph_group.save_state()
        self.wait()
        self.play(graph_group.animate.scale(0.5).align_to(title, LEFT).align_on_border(DOWN, buff = 0.5), FadeIn(not_defined))
        self.wait()
        self.play(FadeIn(function_limit))
        self.wait()
        self.play(FadeOut(not_defined), function_limit.animate.next_to(title, DOWN))
        self.wait()
        self.play(AnimationGroup(FadeIn(first_step), Write(big_I_1), lag_ratio = 0.9))
        self.wait(0.5)
        self.play(Restore(last_graph_group_state), FadeOut(first_step), big_I_1.animate.scale(0.6).align_to(example_function, RIGHT).next_to(example_function, DOWN))
        self.wait()

        delta_tracker = ValueTracker(0.6)
        brace_d_1 = always_redraw(
            lambda: BraceBetweenPoints(grid.c2p(1-delta_tracker.get_value(), 0), grid.c2p(1,0), DOWN).shift(DOWN*0.25)
        )
        brace_d_2 = always_redraw(
            lambda: BraceBetweenPoints(grid.c2p(1,0), grid.c2p(1+delta_tracker.get_value(),0), DOWN).next_to(brace_d_1, RIGHT, buff=0)
        )
        delta_1 = brace_d_1.get_tex(r"\delta")
        delta_2 = brace_d_2.get_tex(r"\delta")
        delta_value = MathTex(fr"\delta = {round(delta_tracker.get_value(), 2)}")
        self.play(Write(delta_value), Write(brace_d_1), Write(delta_1), Write(brace_d_2), Write(delta_2))
        self.wait(2)
        self.play(graph_group.animate.scale(0.5).align_to(title, LEFT).align_on_border(DOWN, buff = 0.5), delta_value.animate.scale(0.5).next_to(example_function, LEFT), FadeIn(second_step))
        temp = delta_value
        delta_value = always_redraw(
            lambda: MathTex(fr"\delta = {round(delta_tracker.get_value(), 2)}").scale(0.5).move_to(delta_value.get_center())
        )
        self.add(delta_value)
        self.remove(temp)
        self.wait()
        lines = [always_redraw(
            lambda: grid.get_lines_to_point(graph.get_point_from_function(1-delta_tracker.get_value()))
        ), always_redraw(
            lambda: grid.get_lines_to_point(graph.get_point_from_function(1+delta_tracker.get_value()))
        ),
            grid.get_lines_to_point(grid.c2p(1, 0.75))
        ]
        brace_e_1 = always_redraw(
            lambda: BraceBetweenPoints([grid.c2p(0,0)[0], graph.get_point_from_function(1+delta_tracker.get_value())[1], 0], grid.c2p(0,0.75), LEFT).shift(LEFT*0.5)
        )
        brace_e_2 = always_redraw(
            lambda: BraceBetweenPoints(grid.c2p(0,0.75), [grid.c2p(0,0)[0], graph.get_point_from_function(1-delta_tracker.get_value())[1], 0], LEFT).next_to(brace_e_1, UP, buff=0)
        )
        epsilon_1 = brace_e_1.get_tex(r"\epsilon")
        epsilon_2 = brace_e_2.get_tex(r"\epsilon")
        self.play(Restore(last_graph_group_state), *[Write(l) for l in lines], Write(brace_e_1), Write(epsilon_1), Write(brace_e_2), Write(epsilon_2), FadeOut(second_step))
        self.wait()
        self.play(delta_tracker.animate.set_value(0.2), run_time=3)
        self.play(delta_tracker.animate.set_value(0.7), run_time=3)
        self.wait()
        
        pass
