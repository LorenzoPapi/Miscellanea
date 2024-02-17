package com.github.lorenzopapi.maths;

import java.util.Arrays;

public class SumOfThreeCubes {
	public static void main(String[] args) throws InterruptedException {
		System.out.println("x^3 + y^3 + z^3 = 114");
		System.out.println("We suppose z = 0 (2-adic analysis implies that one of them has to be 0)");
		System.out.println("And we only solve x^3 + y^3 = 114");
		Thread.sleep(3000);
		System.out.println("Let's go!");
		int N = 4;
		int[] x = new int[N];
		int[] y = new int[N];
		x[0] = 1;
		y[0] = 1;
		for (int i = 1; i < N; i++) {
		
		}
		System.out.println(Arrays.toString(x));
		System.out.println(Arrays.toString(y));
	}
}
