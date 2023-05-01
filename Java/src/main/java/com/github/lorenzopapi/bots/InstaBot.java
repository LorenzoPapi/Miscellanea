package com.github.lorenzopapi.bots;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Date;
import java.time.Instant;
import java.util.Timer;
import java.util.TimerTask;

public class InstaBot {
	public static void main(String[] args) throws AWTException {
		TimerTask post = new PostTask();
		Timer timer = new Timer(true);
		post.run();
		timer.scheduleAtFixedRate(post, Date.from(Instant.now().plusSeconds(4500)), 55000);
		while (true);// System.out.println(MouseInfo.getPointerInfo().getLocation());
	}

	static void moveAndClick(Robot bot, int x, int y) {
		moveMouseInterpolated(bot, x, y);
		bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	}

	static void moveMouseInterpolated(Robot bot, int ex, int ey) {
		Point p = MouseInfo.getPointerInfo().getLocation();
		for (double t = 0; t <= 1; t += 0.005) {
			bot.mouseMove(lerp(p.x, ex, t), lerp(p.y, ey, t));
		}
	}
	
	static void pressKey(Robot bot, char key) {
		bot.keyPress(KeyEvent.getExtendedKeyCodeForChar(key));
		bot.keyRelease(KeyEvent.getExtendedKeyCodeForChar(key));
		bot.delay(150);
	}

	static int lerp(int s, int e, double t) {
		return (int) Math.floor(s + (e - s) * t);
	}

	private static class PostTask extends TimerTask {

		@Override
		public void run() {
			try {
				Robot bot = new Robot();
				bot.setAutoWaitForIdle(true);
				
				moveAndClick(bot, 3760, 0);     // Minimize this window if necessary
				moveAndClick(bot, 40, 600);     // Click create
				moveAndClick(bot, 2840, 560);   // Select image folder
				bot.delay(500);
				
				// Press enter to open it
				bot.keyPress(KeyEvent.VK_ENTER);
				bot.keyRelease(KeyEvent.VK_ENTER);
				bot.delay(2000);
				
				// Click and drag photo to upload place
				moveMouseInterpolated(bot, 2260, 150);
				bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
				moveMouseInterpolated(bot, 960, 570);
				bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
				bot.delay(1000);
				
				// No resize nor effects
				moveAndClick(bot, 1280, 245);
				bot.delay(1000);
				moveAndClick(bot, 1450, 245);
				bot.delay(1000);
				
				// Click the description place and start writing
				moveAndClick(bot, 1170, 345);
				BufferedReader read = new BufferedReader(new FileReader("stupid"));
				String num = read.readLine();
				read.close();
				BufferedWriter writer = new BufferedWriter(new FileWriter("stupid"));
				writer.write(String.valueOf(Integer.parseInt(num) + 1));
				writer.close();
				int size = 1 << Integer.parseInt(num);
				for (char c : String.format("Configurazione 'and' %d x %d", size, size).toCharArray()) pressKey(bot, c);
				bot.delay(1000);
				
				// Move to image folder, click and drag first image to the bin
				moveMouseInterpolated(bot, 2260, 150);
				bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
				moveMouseInterpolated(bot, 2000, 360);
				bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
				bot.delay(1000);
				
				// Post image
				moveAndClick(bot, 1445, 245);
				bot.delay(20000);
				moveAndClick(bot, 1900, 165);
				bot.delay(1000);
				
				// Close image folder
				moveAndClick(bot, 3840, 0);
			} catch (Throwable ex) {
				ex.printStackTrace();
			}
		}
	}
}
