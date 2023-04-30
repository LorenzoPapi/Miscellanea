from manim import *
import numpy as np

class GeneralTaylorSeries(Scene):
    def __init__(self, func_name="", func=None, full="", plot_range = [-16,16]):
        super().__init__()
        self.f_name = func_name
        self.func = func
        self.full_taylor_tex = full
        self.plot_range = plot_range
    
    def build(self, total_terms=7, name_has_latex_form=False):
        axes = Axes(x_range = [-15,9,2], y_range = [-3,3,1], x_length=15, tips=True, axis_config = {"numbers_to_exclude": [0]}).add_coordinates().shift(UP*0.4).shift(LEFT*1.8)
        
        function_text = Tex(r"Serie di Taylor per ${}{}$".format("\\" if name_has_latex_form else "", self.f_name), font_size=40).next_to(axes, UP).shift(3*LEFT).shift(DOWN*0.3)
        last_taylor_tex = MathTex("g(x)=", color=BLUE, font_size=40).next_to(axes, DOWN).shift(1.8*RIGHT)
        last_taylor_graph = None
        
        self.play(Write(axes), Write(axes.get_axis_labels(x_label = "x", y_label = "y")), Write(function_text), run_time = 2)
        self.wait()
        self.play(Create(axes.plot(self.func, x_range = self.plot_range, color=YELLOW)), Write(MathTex("f(x)={}{}".format("\\" if name_has_latex_form else "", self.f_name), color=YELLOW, font_size=40).next_to(function_text, DOWN)), run_time=2)
        self.play(Write(last_taylor_tex), run_time=1)
        self.wait()
        
        for i in range(0, total_terms+1):
            new_taylor_graph = axes.plot(lambda x: sum([self.taylor(x, j) for j in range(0,i+1)]), x_range = self.plot_range, color=BLUE)
            new_taylor_tex = MathTex("g(x)=", self.full_tex(i), color=BLUE, font_size=40).next_to(axes, DOWN).shift(1.8*RIGHT)
            
            self.play(Create(new_taylor_graph) if last_taylor_graph == None else ReplacementTransform(last_taylor_graph, new_taylor_graph), ReplacementTransform(last_taylor_tex, new_taylor_tex), run_time=1.5)
            
            last_taylor_graph = new_taylor_graph
            last_taylor_tex = new_taylor_tex
            self.wait(0.3)

        self.play(ReplacementTransform(last_taylor_graph, axes.plot(self.func, x_range = self.plot_range, color=BLUE)), ReplacementTransform(last_taylor_tex, MathTex("g(x)=", fr"{self.full_taylor_tex}", color=BLUE, font_size=40).next_to(axes, DOWN*0.5).shift(1.8*RIGHT)), run_time=2.5)
        self.wait()
        self.play(FadeOut(*self.get_top_level_mobjects()), run_time=1)
        self.wait(0.5)

    def full_tex(self, n):
        if (n==0):
            return self.tex(0)
        elif (n < 6):
            return (f"{self.tex(0)}" + "".join([self.tex(i) for i in range(1,n+1)]))
        else:
            return (f"{self.tex(0)}{self.tex(1)[0]}\\cdots " + "".join([self.tex(i) for i in range(n-4,n+1)]))    

class CosTaylorSeries(GeneralTaylorSeries):
    def __init__(self):
        super().__init__("cos(x)", lambda x: np.cos(x), r"\sum_{n=0}^{\infty}{\frac{(-1)^{n}}{(2n)!}}x^{2n}")
    
    def taylor(self, x, i):
        return ((-1)**i) * (x**(2*i)) / np.math.factorial(2*i)

    def tex(self, n):
        return "1" if n==0 else (("+" if n%2==0 else "-") + "\\frac{{x^{{{a}}}}}{{{a}!}}".format(a=2*n))
    
    def construct(self):
        self.build(name_has_latex_form=True)

class ExpTaylorSeries(GeneralTaylorSeries):
    def __init__(self):
        super().__init__("e^x", lambda x: np.exp(x), r"\sum_{n=0}^{\infty}{\frac{x^n}{n!}}", [-15,10])
    
    def taylor(self, x, i):
        return (x**i) / np.math.factorial(i)

    def tex(self, n):
        return "1" if n==0 else ("+x" if n== 1 else (f"+\\frac{{x^{{{n}}}}}{{{n}!}}"))
    
    def construct(self):
        self.build()

class SinTaylorSeries(GeneralTaylorSeries):
    def __init__(self):
        super().__init__("sin(x)", lambda x: np.sin(x), r"\sum_{n=0}^{\infty}{\frac{(-1)^{n}}{(2n+1)!}}x^{2n+1}")
    
    def taylor(self, x, i):
        return ((-1)**i) * (x**(2*i+1)) / np.math.factorial(2*i+1)

    def tex(self, n):
        return "x" if n==0 else (("+" if n%2==0 else "-") + "\\frac{{x^{{{a}}}}}{{{a}!}}".format(a=2*n+1))
    
    def construct(self):
        self.build(name_has_latex_form=True)
