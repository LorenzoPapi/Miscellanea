package com.lorenzopapi.github;

import java.io.ByteArrayOutputStream;

public class Decompressor {

	public static void main(String[] args) {
		String src = "hello there";
		src = numbersToString("053023086203120001");
		System.out.println(src);
		src = stringToNumbers(src);
		System.out.println(src);
	}

	public static String numbersToString(String str) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		while (str.length() > 1) {
			stream.write((byte) (Integer.parseInt(str.substring(0, 3)) - 128));
			str = str.substring(3);
		}
<<<<<<< Updated upstream
		return stream.toString();
=======
		return new String(stream.toByteArray());
>>>>>>> Stashed changes
	}

	public static String stringToNumbers(String str) {
		byte[] bytes = str.getBytes();
		StringBuilder builder = new StringBuilder();
		for (Byte b : bytes) {
			builder.append((byte) (b + 128));
		}
		return builder.toString();
	}
}
