package com.github.lorenzopapi.encoding;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class Breadifier {
	private static final String[] breads = {"null pointer exception", "\uD83E\uDD6A", "\uD83E\uDD56", "\uD83E\uDD59", "\uD83E\uDED3", "\uD83E\uDD6F"};
	private static final String spacerBread = "\uD83C\uDF5E";
	private static final String times10Bread = "\uD83E\uDD50";
	private static final String[] chars;
	private static final String spacerChar;
	private static final String times10Char;
	private static final HashMap<Character, String> characterMap = new HashMap<>();

	static {
		spacerChar = Character.toString((char) (55));
		times10Char = Character.toString((char) (56));

		chars = new String[7];
		chars[0] = "null pointer exception";
		for (int i = 0; i < 6; i++)
			chars[i + 1] = Character.toString((char) (48 + i));

		characterMap.put('\uDD6A', "\uD83E\uDD6A");
		characterMap.put('\uDD56', "\uD83E\uDD56");
		characterMap.put('\uDD59', "\uD83E\uDD59");
		characterMap.put('\uDED3', "\uD83E\uDED3");
		characterMap.put('\uDD6F', "\uD83E\uDD6F");
		characterMap.put('\uDF5E', "\uD83C\uDF5E");
		characterMap.put('\uDD50', "\uD83E\uDD50");
	}

	public static void main(String[] args) throws IOException {
		System.out.println(fromBread(toBread("Hello", true), true));
		//System.out.println(fromBread(toBread("I don't not know this cryptic series of dots dashes and slashes you speak in.", true), false));
	}

	public static String toBread(String input, boolean useBread) {
		String[] storageVals = useBread ? breads : chars;
		String times10 = useBread ? times10Bread : times10Char;
		String spacer = useBread ? spacerBread : spacerChar;
		StringBuilder result = new StringBuilder();
		for (byte b : input.getBytes()) {
			String num = Byte.toString(b);
			int charsRemaining = num.length();
			for (char c : num.toCharArray()) {
				int val = Character.getNumericValue(c);
				while (val > 0) {
					if (val < storageVals.length) {
						result.append(storageVals[val]);
						break;
					} else {
						result.append(storageVals[storageVals.length - 1]);
						val -= storageVals.length - 1;
					}
				}
				if (charsRemaining-- > 1) {
					result.append(times10);
				}
			}
			result.append(spacer);
		}
		return result.toString();
	}

	public static String fromBread(String input, boolean useBread) throws IOException {
		String[] storageVals = useBread ? breads : chars;
		String times10 = useBread ? times10Bread : times10Char;
		String spacer = useBread ? spacerBread : spacerChar;

		char[] chars = input.toCharArray();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int byteToWrite = 0;
		for (int i = 0; i < chars.length; i += useBread ? 2 : 1) {
			try {
				char c2 = useBread ? chars[i + 1] : chars[i];
				String str = useBread ? characterMap.getOrDefault(c2, Character.toString(c2)) : Character.toString(c2);
				if (str.equals(times10)) {
					byteToWrite *= 10;
				} else if (str.equals(spacer)) {
					out.write((byte) (byteToWrite));
					byteToWrite = 0;
				} else {
					int index = 0;
					for (String str1 : storageVals) {
						if (str1.equals(str)) {
							byteToWrite += index;
							break;
						}
						index++;
					}
				}
			} catch (Throwable ignored) {
			}
		}
		String result = out.toString();
		out.close();
		return result;
	}
}
