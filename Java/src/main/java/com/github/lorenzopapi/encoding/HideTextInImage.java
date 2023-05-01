package com.github.lorenzopapi.encoding;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.Random;

public class HideTextInImage {
	public static void main(String[] args) throws IOException {
		Random rand = new Random(System.nanoTime());
		byte[] keys = new byte[4];
		rand.nextBytes(keys);
		BufferedInputStream input = new BufferedInputStream(new FileInputStream(new File("hidden", "input.txt")));
		hide(new String(input.readAllBytes()), keys);
		show(keys);
	}
	
	private static void hide(String s, byte[] keys) throws IOException {
		File output = new File("hidden", "secret.png");
		output.getParentFile().mkdirs();
		BufferedImage image = new BufferedImage((int) Math.pow(s.length() / 3., .5) + 1, (int) Math.pow(s.length() / 3., .5) + 1, BufferedImage.TYPE_INT_RGB);
		int x = 0, y = 0;
		char[] chars = s.toCharArray();
		for (int i = 0; i < s.length(); i+= 3) {
			char[] color = Arrays.copyOfRange(chars, i, Math.min(chars.length, i + 3));
			Color encrypted = new Color((color[0] ^ keys[0]) & 0xFF, color.length > 1 ? (color[1] ^ keys[1]) & 0xFF : 0, color.length > 2 ? (color[2] ^ keys[2]) & 0xFF : 0);
			image.setRGB(x, y, encrypted.getRGB());
			//System.out.println("%32s".formatted(Integer.toBinaryString(c1.getRGB())).replace(" ", "0") + " -> " + "%32s".formatted(Integer.toBinaryString(encrypted.getRGB())).replace(" ", "0"));
			x++;
			if (x == image.getHeight()) {
				x = 0;
				y++;
			}
		}
		ImageIO.write(image, "png", output);
	}
	
	private static void show(byte[] keys) throws IOException {
		File input = new File("hidden", "secret.png");
		BufferedImage image = ImageIO.read(input);
		StringBuilder sb = new StringBuilder();
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getHeight(); x++) {
				Color encrypted = new Color(image.getRGB(x, y), true);
				if (encrypted.getRed() != 0 ) sb.appendCodePoint((encrypted.getRed() ^ keys[0]) & 0xFF);
				if (encrypted.getGreen() != 0) sb.appendCodePoint((encrypted.getGreen() ^ keys[1]) & 0xFF);
				if (encrypted.getBlue() != 0) sb.appendCodePoint((encrypted.getBlue() ^ keys[2]) & 0xFF);
			}
		}
		System.out.println(sb);
	}
	
//	private static void hideAlpha(String s, int key) throws IOException {
//		File output = new File("hidden", "secret_alpha.png");
//		output.getParentFile().mkdirs();
//		BufferedImage image = new BufferedImage((int) Math.pow(s.length() / 4., .5) + 1, (int) Math.pow(s.length() / 4., .5) + 1, BufferedImage.TYPE_INT_ARGB);
//		int x = 0, y = 0;
//		char[] chars = s.toCharArray();
//		for (int i = 0; i < s.length(); i+= 4) {
//			char[] color = Arrays.copyOfRange(chars, i, Math.min(chars.length, i + 4));
//			// Add a byte that says how many times the key must be xor-ed
//			Color encrypted = new Color((color[0] ^ (key)) & 0xFF, ((color.length > 1 ? color[1] : 0) ^ (key)) & 0xFF, ((color.length > 2 ? color[2] : 0) ^ (key)) & 0xFF, color.length > 3 ? (color[3] ^ (key)) & 0xFF : 0);
//			image.setRGB(x, y, encrypted.getRGB());
//			//System.out.println("%32s".formatted(Integer.toBinaryString(c1.getRGB())).replace(" ", "0") + " -> " + "%32s".formatted(Integer.toBinaryString(encrypted.getRGB())).replace(" ", "0"));
//			x++;
//			if (x == image.getHeight()) {
//				x = 0;
//				y++;
//			}
//		}
//		ImageIO.write(image, "png", output);
//	}
//	private static void showAlpha(int key) throws IOException {
//		File input = new File("hidden", "secret.png");
//		BufferedImage image = ImageIO.read(input);
//		StringBuilder sb = new StringBuilder();
//		for (int y = 0; y < image.getHeight(); y++) {
//			for (int x = 0; x < image.getHeight(); x++) {
//				Color encrypted = new Color(image.getRGB(x, y), true);
//				if (encrypted.getRed() != 0 ) sb.appendCodePoint(encrypted.getRed() ^ (key) & 0xFF);
//				if (encrypted.getGreen() != 0) sb.appendCodePoint(encrypted.getGreen() ^ (key) & 0xFF);
//				if (encrypted.getBlue() != 0) sb.appendCodePoint(encrypted.getBlue() ^ (key) & 0xFF);
//				if (encrypted.getAlpha() != 0) sb.appendCodePoint((encrypted.getAlpha() ^ (key)) & 0xFF);
//			}
//		}
//		System.out.println(sb);
//	}
//
}
