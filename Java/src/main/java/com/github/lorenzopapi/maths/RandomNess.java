package com.github.lorenzopapi.maths;

import java.util.Random;
import java.util.stream.IntStream;

public class RandomNess {
	public static void main(String[] args) {
		Random rand = new Random();
		int size = 20;
		int min = 1;
		IntStream limitedStream = rand.ints(size, min, min + size);
		limitedStream.forEach((value -> System.out.print(value + ", ")));
	}
}
