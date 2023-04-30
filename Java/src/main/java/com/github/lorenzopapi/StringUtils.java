package com.github.lorenzopapi;

import java.util.Random;

public class StringUtils {

	public static void main(String[] args) {
		System.out.println(reversePerWord("Casso se è difficile", null));
		System.out.println(reverseTotal("Casso se è difficile"));
		System.out.println(randomizeUpperCaseLetters("Casso se è difficile"));
	}

	public static String reversePerWord(String toReverse, String splitter) {
		StringBuilder sb = new StringBuilder();
		if (splitter == null)
			splitter = " ";
		for (String split : toReverse.split(splitter)) sb.append(reverseTotal(split)).append(" ");
		return sb.toString();
	}

	public static String reverseTotal(String toReverse) {
		char[] reversed = new char[toReverse.length()];
		for (int i = 0; i < toReverse.length(); i++) reversed[i] = toReverse.charAt(toReverse.length() - i - 1);
		return new String(reversed);
	}

	public static String randomizeUpperCaseLetters(String toRandomize) {
		char[] randomized = new char[toRandomize.length()];
		Random random = new Random(System.nanoTime());
		for (int i = 0; i < toRandomize.length(); i++) randomized[i] = toRandomize.charAt(i) <= 'z' && toRandomize.charAt(i) >= 'a' && random.nextBoolean() ? (char) (toRandomize.toCharArray()[i] - 32) : toRandomize.toCharArray()[i];
		return new String(randomized);
	}

}
