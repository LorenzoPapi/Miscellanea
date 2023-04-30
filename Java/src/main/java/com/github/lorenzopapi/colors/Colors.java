package com.github.lorenzopapi.colors;

import java.util.Arrays;

public class Colors {

	public static float[] rgb2hsv(int r, int g, int b) {
		r /= 255;
		g /= 255;
		b /= 255;
		int value = Math.max(r, Math.max(g, b));
		int cMin = Math.min(r, Math.min(g, b));

		int delta = value - cMin;

		int hue = 0;
		if (delta == 0);
		else if (value == r) hue = 60 * (((g - b)/delta) % 6);
		else if (value == g) hue = 60 * (((b - r)/delta) + 2);
		else hue = 60 * (((r - g)/delta) + 4);

		int saturation = 0;
		if (value != 0) saturation = delta / value;

		return new float[]{hue, saturation, value};
	}


	private static boolean isBetween(double x, double low, double up) {
		return low <= x && x < up;
	}

	public static void main(String[] args) {
		System.out.println(Arrays.toString(rgb2hsv(255, 0, 0)));
	}
}
