package com.github.lorenzopapi.encoding;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Random;

public class HideTextInImage {
	public static void main(String[] args) throws IOException {
//		hideFile("Lucretius.txt", "secret_L");
//		show("secret_L", "Lucretius_dec.txt");
		
		hideFile("test.bin", "secret_test");
		show("secret_test", "test_dec.bin");
	}
	
	private static boolean isSpecialPlace(int x, int y, int w, int h) {
		return (x == w - 1 || x == 0) && (y == 0 | y == h - 1);
	}
	
	private static void rotateKeys(byte[] keys) {
		for (int j = 0; j < keys.length; j++) {
			int rotated = ((keys[j] & 0XFF) | ((keys[j] << 8) & 0XFF00)) >>> 1;
			keys[j] = (byte) (rotated & 0xFF);
		}
	}
	
	private static void hideString(String giovanni, String name) throws IOException {
		Random rand = new Random(System.nanoTime());
		byte[] keys = new byte[3];
		rand.nextBytes(keys);
		
		File output = new File("hidden", "%s.png".formatted(name));
		output.getParentFile().mkdirs();
		String toEncrypt = giovanni;
		toEncrypt = toEncrypt.substring(0, 162);
		
		int size = (int) Math.ceil(Math.sqrt(toEncrypt.length() / 3. + 4));
		BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		int w = image.getWidth(), h = image.getHeight(), x = 0, y = 0;
		
		image.setRGB(x++, 0, toEncrypt.length());
		image.setRGB(w - 1, 0, new Color(0x80, 0x80, keys[0] & 0xFF).getRGB());
		image.setRGB(0, h - 1, new Color(0xFF, keys[1] & 0xFF, 0x80).getRGB());
		image.setRGB(w - 1, h - 1, new Color(keys[2] & 0xFF, 0x80, 0xFF).getRGB());
		
		for (int i = 0; i < 3; i++) keys[i] ^= 55;
		
		toEncrypt = toEncrypt.concat(toEncrypt.substring(0, (w * h * 3 - 12) - toEncrypt.length()));
		
		char[] chars = toEncrypt.toCharArray();
		for (int i = 0; i < toEncrypt.length(); i += 3) {
			if (isSpecialPlace(x, y, w, h)) {
				i -= 3;
			} else {
				char[] color = Arrays.copyOfRange(chars, i, Math.min(chars.length, i + 3));
				Color encrypted = new Color((color[0] ^ keys[0]) & 0xFF, (color[1] ^ keys[1]) & 0xFF, (color[2] ^ keys[2]) & 0xFF);
				image.setRGB(x, y, encrypted.getRGB());
				rotateKeys(keys);
			}
			if (++x == w) {
				x = 0;
				y++;
			}
		}
		System.out.println(Arrays.toString(toEncrypt.getBytes()));
		ImageIO.write(image, "png", output);
	}
	
	private static void hideFile(String inputFile, String name) throws IOException {
		BufferedInputStream input = new BufferedInputStream(new FileInputStream(new File("hidden", inputFile)));
		String toEncrypt = new String(input.readAllBytes());
		hideString(toEncrypt, name);
	}

	private static void show(String name, String exit) throws IOException {
		File input = new File("hidden", "%s.png".formatted(name));
		BufferedImage image = ImageIO.read(input);
		int len = image.getRGB(0, 0);
		int w = image.getWidth(), h = image.getHeight();
		byte[] keys = new byte[] {
				(byte) (new Color(image.getRGB(w - 1, 0)).getBlue() ^ 55),
				(byte) (new Color(image.getRGB(0, h - 1)).getGreen() ^ 55),
				(byte) (new Color(image.getRGB(w - 1, h - 1)).getRed() ^ 55)
		};
		boolean end = false;
		ByteBuffer buf = ByteBuffer.allocate(len);
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				if (!isSpecialPlace(x, y, w, h)) {
					int color = image.getRGB(x, y), i = 3;
					while (buf.hasRemaining() && i-- > 0) {
						int dec = ((color >> (8 * i)) ^ keys[2 - i]) & 0xFF;
						buf.put((byte) dec);
					}
					if (!buf.hasRemaining()) end = true;
					rotateKeys(keys);
				}
			}
			if (end) break;
		}
		System.out.println(Arrays.toString(buf.array()));
		if (exit.isEmpty()) System.out.println(new String(buf.array()));
		else {
			BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(new File("hidden", exit)));
			bout.write(buf.array());
		}
	}
}
