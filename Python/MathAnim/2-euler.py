from manim import *
import numpy as np


class EulerFormulaDemonstration(Scene):
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
        
    def build_i3(self, i2):
        eq3_build = [MathTex("i^3="), MathTex("i^3=", r"i\times", "(", "i^2", ")"), MathTex("i^3=", r"i\times", "(", "-1", ")"), MathTex("i^3=", "-", r"i")]
        self.play(GrowFromPoint(eq3_build[0], ORIGIN))
        self.wait(0.5)
        self.play(TransformMatchingTex(eq3_build[0], eq3_build[1]))
        self.wait()
        self.play(Succession(i2.animate().scale(2).move_to(ORIGIN).shift(UP), AnimationGroup(Indicate(i2), Indicate(eq3_build[1][3]))))

        self.replace_animation(eq3_build[1], eq3_build[2], [i2[2]], [3])
        
        self.play(Succession(Restore(i2), TransformMatchingTex(eq3_build[2], eq3_build[3]), lag_time=0.8))
        self.wait()
        self.play(eq3_build[3].animate().scale(0.5).next_to(i2, DOWN))
        eq3_build[3].save_state()
        return eq3_build[3]

    def build_i4(self, i2, i3):
        eq4_build = [MathTex("i^4="), MathTex("i^4=", "(", "i^2", r")\times(", "i^2", ")"), MathTex("i^4=", "(", "-1", r")\times(", "-1", ")"), MathTex("i^4=", "1")]
        self.play(GrowFromPoint(eq4_build[0], ORIGIN))
        self.wait(0.5)
        self.play(TransformMatchingTex(eq4_build[0], eq4_build[1]))
        self.wait()
        self.play(Succession(i2.animate().scale(2).move_to(ORIGIN).shift(UP), AnimationGroup(Indicate(i2), Indicate(eq4_build[1][2]), Indicate(eq4_build[1][4]))))

        self.replace_animation(eq4_build[1], eq4_build[2], [i2[2], i2[2]], [2, 4]) 

        self.play(Succession(Restore(i2), TransformMatchingTex(eq4_build[2], eq4_build[3]), lag_time=0.8))
        self.wait()
        self.play(eq4_build[3].animate().scale(0.5).next_to(i3, DOWN))
        eq4_build[3].save_state()
        return eq4_build[3]

    def build_i5(self, i4):
        eq5_build = [MathTex("i^5="), MathTex("i^5=", "i^4", r"\times", "i"), MathTex("i^5=", "1", r"\times", "i"), MathTex("i^5=", "i")]
        self.play(GrowFromPoint(eq5_build[0], ORIGIN))
        self.wait(0.5)
        self.play(TransformMatchingTex(eq5_build[0], eq5_build[1]))
        self.wait()
        self.play(Succession(i4.animate().scale(2).move_to(ORIGIN).shift(UP), AnimationGroup(Indicate(i4), Indicate(eq5_build[1][1]))))

        self.replace_animation(eq5_build[1], eq5_build[2], [i4[1]], [1])

        self.play(Succession(Restore(i4), TransformMatchingTex(eq5_build[2], eq5_build[3]), lag_time=0.8))
        self.wait()
        self.play(eq5_build[3].animate().scale(0.5).next_to(i4, DOWN))
        eq5_build[3].save_state()
        return eq5_build[3]
        
    def construct(self):
        first_eqs = [MathTex("i^0", "=", "1"), MathTex("i^1", "=", "i"), MathTex("i^2", "=", "-1")]
        second_eqs = [MathTex("i^6", "=", "-1"), MathTex("i^7", "=", "-i"), MathTex("i^8", "=", "1"), MathTex(r"\vdots")]
        
        first_group = VGroup(first_eqs[0], first_eqs[1], first_eqs[2]).set_x(0).arrange(DOWN, buff=0.5)
        self.play(Succession(*[GrowFromPoint(first_eqs[i], ORIGIN) for i in range(2,-1,-1)]))
        self.wait()
        self.play(first_group.animate().scale(0.5).to_edge(UL))
        for i in range(0, len(first_eqs)):
            first_eqs[i].save_state()
        
        all_eqs = [*first_eqs, self.build_i3(first_eqs[2]), None, None, *second_eqs]
        all_eqs[4] = self.build_i4(all_eqs[2], all_eqs[3])
        all_eqs[5] = self.build_i5(all_eqs[4])

        stp = all_eqs[5].get_center()
        for i in range(0, len(second_eqs)):
            second_eqs[i].scale(0.5).move_to([stp[0], stp[1] - 0.5, stp[2]])
            self.play(GrowFromPoint(second_eqs[i], [stp[0], stp[1] - 0.5, stp[2]]))
            stp = second_eqs[i].get_center()
            second_eqs[i].save_state()
        self.wait()
       
        ex_eqs = [
            MathTex("e^", "{x}", "=", r"\sum_{n=0}^{\infty}{\frac{x^n}{n!}}"),
            MathTex("e^", "{x}", "=", "1+", "{x}", r"+\frac{1}{2!}", "x", "^2", r"+\frac{1}{3!}", "x", "^3", r"+\frac{1}{4!}", "x", "^4", r"+\cdots"),
        ]
        et_eqs = [
            MathTex(*ex_eqs[1].get_tex_string().replace("{x}", r"{(i\theta)}").replace("x", r"(i\theta)").split(" ")),
            MathTex("e^", r"{i\theta}", "=", "&(", "1", r"+\frac{1}{2!}", r"(i\theta)", "^2", r"+\frac{1}{4!}", r"(i\theta)", "^4", r"+\frac{1}{6!}", r"(i\theta)", "^6", r"+\cdots", r")+\\",
                                        r"&(i\theta", r"+\frac{1}{3!}", r"(i\theta)", "^3", r"+\frac{1}{5!}", r"(i\theta)", "^5", r"+\frac{1}{7!}", r"(i\theta)", "^7", r"+\cdots", ")"),
            MathTex("e^", r"{i\theta}", "=", "&(", "1", r"+\frac{1}{2!}", "i^2", r"\theta", "^2", r"+\frac{1}{4!}", "i^4", r"\theta", "^4", r"+\frac{1}{6!}", "i^6", r"\theta", "^6", r"+\cdots", r")+\\",
                                        r"&(i\theta", r"+\frac{1}{3!}", "i^3", r"\theta", "^3", r"+\frac{1}{5!}", "i^5", r"\theta", "^5", r"+\frac{1}{7!}", "i^7", r"\theta", "^7", r"+\cdots", ")"),
            MathTex("e^", r"{i\theta}", "=", "&(", "1", r"+\frac{1}{2!}", "i^2", r"\theta", "^2", r"+\frac{1}{4!}", "i^4", r"\theta", "^4", r"+\frac{1}{6!}", "i^6", r"\theta", "^6", r"+\cdots", r")+\\",
                                        r"&i(", r"\theta", r"+\frac{1}{3!}", "i^2", r"\theta", "^3", r"+\frac{1}{5!}", "i^4", r"\theta", "^5", r"+\frac{1}{7!}", "i^6", r"\theta", "^7", r"+\cdots", ")"),
            MathTex("e^", r"{i\theta}", "=", "&(", "1", r"-\frac{1}{2!}", r"\theta", "^2", r"+\frac{1}{4!}", r"\theta", "^4", r"-\frac{1}{6!}", r"\theta", "^6", r"+\cdots", r")+\\",
                                        r"&i(", r"\theta", r"-\frac{1}{3!}", r"\theta", "^3", r"+\frac{1}{5!}", r"\theta", "^5", r"-\frac{1}{7!}", r"\theta", "^7", r"+\cdots", ")"),
            MathTex("e^", r"{i\theta}", "=", "&(", r"\cos(", r"{\theta}", ")", r")+\\",
                                        r"&i(", r"\theta", r"-\frac{1}{3!}", r"\theta", "^3", r"+\frac{1}{5!}", r"\theta", "^5", r"-\frac{1}{7!}", r"\theta", "^7", r"+\cdots", ")"),
            MathTex("e^", r"{i\theta}", "=", "&(", r"\cos(", r"{\theta}", ")", r")+\\",
                                        r"&i(", r"\sin(", r"{\theta}", ")", ")"),
            MathTex("e^", r"{i\theta}", "=", r"\cos(", r"{\theta}", ")", "+", "i", r"\sin(", r"{\theta}", ")")
        ]
        sin_eqs = [
            MathTex(r"\sin(", "{x}", ")", "=", r"\sum_{n=0}^{\infty}{\frac{(-1)^{n}}{(2n+1)!}}x^{2n+1}"),
            MathTex(r"\sin(", "{x}", ")", "=", "x", r"-\frac{1}{3!}", "x", "^3", r"+\frac{1}{5!}", "x", "^5", r"-\frac{1}{7!}", "x", "^7", r"+\cdots"),
            MathTex(r"\sin(", r"{\theta}", ")", "=", r"\theta", r"-\frac{1}{3!}", r"\theta", "^3", r"+\frac{1}{5!}", r"\theta", "^5", r"-\frac{1}{7!}", r"\theta", "^7", r"+\cdots"),
        ]
        cos_eqs = [
            MathTex(r"\cos(", "{x}", ")", "=", r"\sum_{n=0}^{\infty}{\frac{(-1)^{n}}{(2n)!}}x^{2n}"),
            MathTex(r"\cos(", "{x}", ")", "=", "1", r"-\frac{1}{2!}", "x", "^2", r"+\frac{1}{4!}", "x", "^4", r"-\frac{1}{6!}", "x", "^6", r"+\cdots"),
            MathTex(r"\cos(", r"{\theta}", ")", "=", "1", r"-\frac{1}{2!}", r"\theta", "^2", r"+\frac{1}{4!}", r"\theta", "^4", r"-\frac{1}{6!}", r"\theta", "^6", r"+\cdots"),
        ]

        sub_eq = MathTex(r"x=i\theta").shift(UP)
        thetas = VGroup(*[MathTex(r"(i\theta)") for i in range(0,5)]).arrange_submobjects().shift(UP)
        self.play(GrowFromPoint(ex_eqs[0], ORIGIN))
        self.wait()
        self.play(ReplacementTransform(ex_eqs[0], ex_eqs[1]))
        self.wait(0.5)
        self.play(GrowFromPoint(sub_eq, sub_eq.get_center()))
        self.play(Indicate(sub_eq), *[Indicate(i) for i in ex_eqs[1] if i.get_tex_string().find("x") != -1])
        self.wait()
        self.play(sub_eq.animate().shift(UP), *[FadeIn(t) for t in thetas])
        self.replace_animation_no_copy(ex_eqs[1], et_eqs[0], thetas, [1,4,6,9,12], [ShrinkToCenter(sub_eq)])
        self.wait()
        self.play(TransformMatchingTex(et_eqs[0], et_eqs[1]), run_time=2)
        self.wait()
        self.play(TransformMatchingTex(et_eqs[1], et_eqs[2]))
        self.wait()
        self.play(TransformMatchingTex(et_eqs[2], et_eqs[3]))
        self.wait()
        et_eqs[3].save_state()
   
        self.play(et_eqs[3].animate().scale(0.7).shift(UP*2.5), all_eqs[2].animate().scale(2).move_to(ORIGIN), all_eqs[4].animate().scale(2).move_to(ORIGIN).shift(DOWN), all_eqs[6].animate().scale(2).move_to(ORIGIN).shift(DOWN*2))
        self.play(Indicate(all_eqs[2]), Indicate(all_eqs[4]), Indicate(all_eqs[6]), *[Indicate(i) for i in et_eqs[3] if i.get_tex_string() == "i^2" or i.get_tex_string() == "i^4" or i.get_tex_string() == "i^6"])
        self.wait()
        self.play(Restore(et_eqs[3]), Restore(all_eqs[2]), Restore(all_eqs[4]), Restore(all_eqs[6]))
        self.play(TransformMatchingTex(et_eqs[3], et_eqs[4]))
        self.wait()
    
        sub_eq = MathTex(r"x=\theta").shift(DOWN)
        thetas = VGroup(*[MathTex(r"\theta") for i in range(0,4)]).arrange_submobjects().shift(DOWN)
        cos_t = MathTex(r"\cos(", r"{\theta}", ")")
        self.play(Succession(et_eqs[4].animate().scale(0.7).shift(UP*2.5), Write(cos_eqs[0]), lag_time=0.6))
        self.play(ReplacementTransform(cos_eqs[0], cos_eqs[1]))
        self.wait(0.5)
        self.play(GrowFromPoint(sub_eq, sub_eq.get_center()))
        self.play(Indicate(sub_eq), *[Indicate(i) for i in cos_eqs[1] if i.get_tex_string().find("x") != -1])
        self.play(sub_eq.animate().shift(DOWN), *[FadeIn(t) for t in thetas])
        self.play(*[ReplacementTransform(thetas[i], cos_eqs[2][[1,6,9,12][i]]) for i in range(0,4)], TransformMatchingTex(cos_eqs[1], cos_eqs[2]), ShrinkToCenter(sub_eq))
        self.wait(0.5)
        self.play(*[Indicate(et_eqs[4][i]) for i in range(4, 15)], *[Indicate(cos_eqs[2][i]) for i in range(4, 15)])
        self.wait(0.5)
        for i in range(0,3): cos_t[i].move_to(cos_eqs[2][i].get_center())
        self.add(cos_t)
        et_eqs[5].move_to(et_eqs[4].get_center()).scale(0.7)
        self.replace_animation_no_copy(et_eqs[4], et_eqs[5], cos_t, [4,5,6], [FadeOut(cos_eqs[2])])
        self.wait()
        
        sub_eq = MathTex(r"x=\theta").shift(DOWN)
        thetas = VGroup(*[MathTex(r"\theta") for i in range(0,5)]).arrange_submobjects().shift(DOWN)
        sin_t = MathTex(r"\sin(", r"{\theta}", ")").shift(DOWN)
        self.play(Write(sin_eqs[0]))
        self.play(ReplacementTransform(sin_eqs[0], sin_eqs[1]))
        self.wait(0.5)
        self.play(GrowFromPoint(sub_eq, sub_eq.get_center()))
        self.play(Indicate(sub_eq), *[Indicate(i) for i in sin_eqs[1] if i.get_tex_string().find("x") != -1])
        self.play(sub_eq.animate().shift(DOWN), *[FadeIn(t) for t in thetas])
        self.play(*[ReplacementTransform(thetas[i], sin_eqs[2][[1,4,6,9,12][i]]) for i in range(0,5)], TransformMatchingTex(sin_eqs[1], sin_eqs[2]), ShrinkToCenter(sub_eq))
        self.wait(0.5)
        self.play(*[Indicate(et_eqs[5][i]) for i in range(9, 20)], *[Indicate(sin_eqs[2][i]) for i in range(4, 15)])
        self.wait(0.5)
        for i in range(0,3): sin_t[i].move_to(sin_eqs[2][i].get_center())
        self.add(sin_t)
        et_eqs[6].move_to(et_eqs[5].get_center()).scale(0.7)
        self.replace_animation_no_copy(et_eqs[5], et_eqs[6], sin_t, [9,10,11], [FadeOut(sin_eqs[2])], [FadeOut(i) for i in all_eqs])
        self.wait()
        self.play(TransformMatchingTex(et_eqs[6], et_eqs[7]), run_time=2)
        self.wait(2)
        self.play(FadeOut(et_eqs[7]))
        self.wait()

