package com.github.lorenzopapi.misc;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static java.awt.event.KeyEvent.*;

public class FNFAI {

	private static BufferedImage img;
	private static Robot bot;
	private static final ArrayList<Integer> pressed = new ArrayList<>();
	public static final int[] keys = new int[]{VK_LEFT, VK_DOWN, VK_UP, VK_RIGHT};
	public static final ArrayList<Triar<Integer, Integer, Integer>> normalMap = new ArrayList<>();

	static {
		try {
			bot = new Robot();
		} catch (AWTException ignored) {}
		final int y = 250;
		normalMap.add(new Triar<>(747, y, 0xFFC24B99));
		normalMap.add(new Triar<>(854, y, 0xFF00FFFF));
		normalMap.add(new Triar<>(969, y, 0xFF12FA05));
		normalMap.add(new Triar<>(1078, y, 0xFFF9393F));
		img = bot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
	}

	private static void screenshot() {
		img = bot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
	}

	public static void main(String[] args) {
		while (true) {
			screenshot();
			check();
			System.gc();
		}
	}

	private static void check() {
		for (int i = 0; i < 4; i++) {
			if (img.getRGB(normalMap.get(i).x, normalMap.get(i).y) == normalMap.get(i).c) {
				doPress(i);
			} else {
				doRelease(i);
			}
		}
	}

	private static void doPress(int index) {
		bot.keyPress(keys[index]);
		pressed.add(keys[index]);
	}

	private static void doRelease(int index) {
		if (pressed.contains(keys[index])) {
			bot.keyRelease(keys[index]);
			pressed.remove((Integer) keys[index]);
		}
	}

	private static class Triar<X, Y, C> {
		X x;
		Y y;
		C c;

		public Triar(X x, Y y, C c) {
			this.x = x;
			this.y = y;
			this.c = c;
		}
	}
}
