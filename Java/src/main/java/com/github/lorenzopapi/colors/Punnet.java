package com.github.lorenzopapi.colors;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Punnet {
	static List<String> operators = List.of("and", "or", "xor", "impl", "isimpl", "eq", "mod");
	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in);
		boolean end = false;
		
		while (true) {
			System.out.print("DEBUG? ");
			if (sc.next().equals("true")) {
				String mode = sc.next();
				for (int i = 0; i < 14; i++)
					punnetSquare(i, mode, true);
				break;
			} else {
				System.out.print("Input size: ");
				while (!sc.hasNextInt()) {
					if (sc.next().equals("stop")) {
						end = true;
						break;
					}
					System.out.println("Input an integer size!");
					System.out.print("Input size: ");
				}
				if (end) break;
				int size = Math.max(0, Math.min(15, sc.nextInt()));
				System.out.print("Input operation: ");
				String operation = sc.next();
				punnetSquare(size, operation, true);
			}
		}
	}
	
	private static int color(int seed, int key) {
		Random rand = new Random(seed-key);
		return rand.nextInt();
	}
	
	private static void punnetSquare(int size, String operation, boolean replace) throws IOException {
		if (!operators.contains(operation)) operation = "unk";
		File output = new File("images/%s".formatted(operation), "punnet_%d.png".formatted(size));
		if (replace || !output.exists()) {
			int dim = 1 << size;
			Random rand = new Random(System.nanoTime());
			int key = 412941294; //412941294;
			BufferedImage image = new BufferedImage(dim, dim, BufferedImage.TYPE_INT_RGB);
			System.out.printf("Starting %d x %d '%s' square %n", dim, dim, operation);
			for (int x = 0; x < dim; x++) {
				for (int y = 0; y < dim; y++) {
					int c = switch (operation) {
						case "and" -> x & y;
						case "or" -> x | y;
						case "xor" -> x ^ y;
						case "impl" -> ~x | y;
						case "isimpl" -> x | ~y;
						case "eq" -> ~(x ^ y);
						case "mod" -> x*x+y*y - 1;
						default -> 0;
					};
					image.setRGB(x, y, color(c, key));
				}
			}
			output.getParentFile().getParentFile().mkdirs();
			output.getParentFile().mkdirs();
			ImageIO.write(image, "png", output);
		} else System.out.printf("File %s already existing%n", output.getPath());
	}
}
