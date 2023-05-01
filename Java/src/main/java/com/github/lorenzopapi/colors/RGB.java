package com.github.lorenzopapi.colors;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;

public class RGB {

	public static void compressToLuigiImage(String fileToCompress, String imageOut) throws Exception {
		File fileToRead = new File(fileToCompress);
		byte[] bytes = Files.readAllBytes(fileToRead.toPath());
		double size = Math.ceil(bytes.length / 3.0);
		BufferedImage img = new BufferedImage((int) Math.ceil(Math.sqrt(size)), (int) Math.ceil(Math.sqrt(size)), BufferedImage.TYPE_INT_RGB);
		int counter = 0;
		for (int y = 0; y < img.getHeight(); y++) {
			for (int x = 0; x < img.getWidth(); x++) {
				int[] pixelValues = new int[]{128, 128, 128};
				if (counter < bytes.length) {
					for (int c = 0; c < 3; c++) {
						int XOR = -128;
						if (counter + c < bytes.length) {
							if (bytes[counter + c] >= 0) {
								XOR = 128;
							}
							pixelValues[c] = (bytes[counter + c] ^ XOR);
						}
					}
					counter = counter + 3;
				}
				int rgb = pixelValues[0] << 16 | pixelValues[1] << 8 | pixelValues[2];
				img.setRGB(x, y, rgb);
			}
		}
		ImageIO.write(img, "png", new File(imageOut + ".png"));
	}

	public static void decompressLuigiImage(String imageName, String destinationFile, boolean text) throws Exception {
		FileOutputStream output = new FileOutputStream(destinationFile);
		BufferedImage img = ImageIO.read(new File(imageName + ".png"));
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		for (int y = 0; y < img.getHeight(); y++) {
			for (int x = 0; x < img.getWidth(); x++) {
				int pixel = img.getRGB(x, y);
				Color color = new Color(pixel, false);
				int red = color.getRed();
				int green = color.getGreen();
				int blue = color.getBlue();

				int[] rgbArray = new int[] {red, green, blue};

				for (int c = 0; c < 3; c++) {
					int XOR = 128;
					if (rgbArray[c] < 128) {
						XOR = -128;
					}
					rgbArray[c] = rgbArray[c] ^ XOR;
				}

				for (int value : rgbArray) {
					if (text && value != -128)
						bout.write(value);
					else if (!text)
						bout.write(value);
				}
			}
		}
		output.write(bout.toByteArray());
		output.close();
	}

	public static void main(String[] args)throws Exception {
		String image = "TOTALCMD64.ZIP";
		//"Luigi" method compresses more
		compressToLuigiImage(image, image + "_compressed");
		decompressLuigiImage(image + "_compressed", "Decompressed.zip", false);
	}
}
