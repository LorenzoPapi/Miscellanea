package com.github.lorenzopapi.encoding;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Random;

public class HideTextInImage {
	
	public static final BufferedWriter writer;
	
	static {
		try {
			writer = new BufferedWriter(new FileWriter(new File("hidden", "output_enc.txt")));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void main(String[] args) throws IOException {
//		hideFile("Lucretius.txt", "secret_L");
//		show("secret_L", "Lucretius_dec.txt");
		
		hideFile("test.bin", "secret_test");
		show("secret_test", "test_dec.bin");
		writer.close();
	}
	
	private static void printHexArray(byte[] bytes) {
		System.out.print("[");
		int i;
		for (i = 0; i < bytes.length - 1; i++) System.out.printf("%X, ", bytes[i]);
		System.out.printf("%X]%n", bytes[i]);
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
		int x = 0, y = 0;
		
		image.setRGB(x++, 0, toEncrypt.length());
		image.setRGB(size - 1, 0, new Color(0x80, 0x80, keys[0] & 0xFF).getRGB());
		image.setRGB(0, size - 1, new Color(0xFF, keys[1] & 0xFF, 0x80).getRGB());
		image.setRGB(size - 1, size - 1, new Color(keys[2] & 0xFF, 0x80, 0xFF).getRGB());
		
		for (int i = 0; i < 3; i++) keys[i] ^= 0x55;
		//toEncrypt = toEncrypt.concat(toEncrypt.substring(0, (size * size * 3 - 12) - toEncrypt.length()));
		
		char[] chars = toEncrypt.toCharArray();
		for (int i = 0; i < toEncrypt.length(); i += 3) {
			if (isSpecialPlace(x, y, size, size)) {
				i -= 3;
			} else {
				char[] color = Arrays.copyOfRange(chars, i, Math.min(chars.length, i + 3));
				writer.write("%X -> %X, ".formatted((byte) (color[0]), (color[0] ^ keys[0]) & 0XFF));
				writer.write("%X -> %X, ".formatted((byte) (color[1]), (color[1] ^ keys[1]) & 0XFF));
				writer.write("%X -> %X%n".formatted((byte) (color[2]), (color[2] ^ keys[2]) & 0XFF));
				image.setRGB(x, y, new Color((color[0] ^ keys[0]) & 0xFF, (color[1] ^ keys[1]) & 0xFF, (color[2] ^ keys[2]) & 0xFF).getRGB());
				rotateKeys(keys);
			}
			if (++x == size) {
				x = 0;
				y++;
			}
		}
		writer.write("\n");
		printHexArray(toEncrypt.getBytes());
		ImageIO.write(image, "png", output);
	}
	
	private static void hideFile(String inFile, String encName) throws IOException {
		BufferedInputStream input = new BufferedInputStream(new FileInputStream(new File("hidden", inFile)));
		String toEncrypt = new String(input.readAllBytes());
		hideString(toEncrypt, encName);
	}

	private static void show(String encName, String decName) throws IOException {
		BufferedImage image = ImageIO.read(new File("hidden", "%s.png".formatted(encName)));
		int size = image.getWidth();
		int len = image.getRGB(0, 0);
		byte[] keys = new byte[] {
				(byte) (new Color(image.getRGB(size - 1, 0)).getBlue() ^ 0x55),
				(byte) (new Color(image.getRGB(0, size - 1)).getGreen() ^ 0x55),
				(byte) (new Color(image.getRGB(size - 1, size - 1)).getRed() ^ 0x55)
		};
		ByteBuffer buf = ByteBuffer.allocate(len);
		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				if (!isSpecialPlace(x, y, size, size)) {
					int color = image.getRGB(x, y), i = 3;
					while (buf.hasRemaining() && i-- > 0) {
						int enc = color >> (8 * i);
						byte dec = (byte) ((enc ^ keys[2 - i]) & 0xFF);
						writer.write("%X -> %X, ".formatted((byte) enc, dec));
						buf.put(dec);
					}
					writer.write("\n");
					rotateKeys(keys);
				}
			}
			if (!buf.hasRemaining()) break;
		}
		printHexArray(buf.array());
		if (decName.isEmpty()) System.out.println(new String(buf.array()));
		else {
			BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(new File("hidden", decName)));
			bout.write(buf.array());
		}
	}
}
