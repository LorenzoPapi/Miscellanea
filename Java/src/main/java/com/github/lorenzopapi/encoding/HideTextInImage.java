package com.github.lorenzopapi.encoding;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.Random;
import java.util.zip.DataFormatException;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public class HideTextInImage {
	
	public static void main(String[] args) throws IOException, DataFormatException {
		hideFile("Lucretius.txt", "secret_L");
		show("secret_L", "Lucretius_dec.txt");

		hideFile("test.bin", "secret_test");
		show("secret_test", "test_dec.bin");
	}
	
	private static void printHexArray(byte[] bytes) {
		System.out.print("[");
		int i;
		for (i = 0; i < bytes.length - 1; i++) System.out.printf("%X, ", bytes[i]);
		System.out.printf("%X]%n", bytes[i]);
	}
	
	private static boolean isSpecialPlace(int x, int y, int w, int h) {
		// Is corner?
		return (x == w - 1 || x == 0) && (y == 0 | y == h - 1);
	}
	
	private static void rotateKeys(byte[] keys) {
		// Rotate keys
		for (int j = 0; j < keys.length; j++) {
			int rotated = ((keys[j] & 0XFF) | ((keys[j] << 8) & 0XFF00)) >>> 1;
			keys[j] = (byte) (rotated & 0xFF);
		}
	}

	private static int encryppt(byte[] bytes, byte[] keys) {
		Color c = new Color((bytes[0] ^ keys[0]) & 0xFF, (bytes[1] ^ keys[1]) & 0xFF, (bytes[2] ^ keys[2]) & 0xFF);
		rotateKeys(keys);
		return c.getRGB();
	}
	
	private static void hideByteArray(byte[] inputBytes, String name) throws IOException {
		// Initialize keys
		Random rand = new Random(System.nanoTime());
		byte[] keys = new byte[3];
		rand.nextBytes(keys);
		
		// Compress the bytes
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DeflaterOutputStream dos = new DeflaterOutputStream(baos);
		dos.write(inputBytes);
		dos.flush();
		dos.close();
		int len = baos.size();
		int size = (int) Math.ceil(Math.sqrt(len / 3. + 4));
		byte[] padding = new byte[(size * size * 3 - 12) - len];
		System.arraycopy(baos.toByteArray(), 0, padding, 0, (size * size * 3 - 12) - len);
		baos.write(padding);
		ByteBuffer compressed = ByteBuffer.wrap(baos.toByteArray());
		compressed.position(0);

		// Encode corner pixels
		BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		int x = 0, y = 0;
		image.setRGB(x++, 0, len);
		image.setRGB(size - 1, 0, new Color(0x80, 0x80, keys[0] & 0xFF).getRGB());
		image.setRGB(0, size - 1, new Color(0xFF, keys[1] & 0xFF, 0x80).getRGB());
		image.setRGB(size - 1, size - 1, new Color(keys[2] & 0xFF, 0x80, 0xFF).getRGB());
		
		// XOR keys
		for (int i = 0; i < 3; i++) keys[i] ^= 0x55;

		// Encode bytes in the image
		byte[] color = new byte[3];
		while (compressed.hasRemaining()) {
			if (!isSpecialPlace(x, y, size, size)) {
				compressed.get(color);
				image.setRGB(x, y, encryppt(color, keys));
			}
			if (++x == size) {
				x = 0;
				y++;
			}
		}
		printHexArray(compressed.array());
		// Make sure output folder exists and print out image
		File output = new File("hidden", name.concat(".png"));
		output.getParentFile().mkdirs();
		ImageIO.write(image, "png", output);
	}
	
	private static void hideFile(String inFile, String encName) throws IOException {
		BufferedInputStream input = new BufferedInputStream(new FileInputStream(new File("hidden", inFile)));
		hideByteArray(input.readAllBytes(), encName);
	}

	private static void show(String encName, String decName) throws IOException, DataFormatException {
		// Read image and keys
		BufferedImage image = ImageIO.read(new File("hidden", encName.concat(".png")));
		int size = image.getWidth();
		int len = image.getRGB(0, 0);
		byte[] keys = new byte[] {
				(byte) (new Color(image.getRGB(size - 1, 0)).getBlue() ^ 0x55),
				(byte) (new Color(image.getRGB(0, size - 1)).getGreen() ^ 0x55),
				(byte) (new Color(image.getRGB(size - 1, size - 1)).getRed() ^ 0x55)
		};
		
		// Allocate buffer and de-encode
		ByteBuffer compressed = ByteBuffer.allocate(len);
		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				if (!isSpecialPlace(x, y, size, size)) {
					int color = image.getRGB(x, y), i = 3;
					while (compressed.hasRemaining() && i-- > 0) {
						int enc = color >> (8 * i);
						byte dec = (byte) ((enc ^ keys[2 - i]) & 0xFF);
						compressed.put(dec);
					}
					rotateKeys(keys);
				}
			}
			if (!compressed.hasRemaining()) break;
		}
		printHexArray(compressed.array());
		// Allocate result and decompress
		ByteArrayInputStream bais = new ByteArrayInputStream(compressed.array());
		InflaterInputStream iis = new InflaterInputStream(bais);
		// Print or write to file
		if (decName.isEmpty()) System.out.println(new String(iis.readAllBytes()));
		else {
			BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(new File("hidden", decName)));
			bout.write(iis.readAllBytes());
		}
	}
}
