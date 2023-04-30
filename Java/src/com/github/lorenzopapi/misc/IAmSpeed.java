package com.github.lorenzopapi.misc;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.awt.event.KeyEvent.*;

public class IAmSpeed implements NativeKeyListener {

	protected final Robot bot;
	protected final List<Integer> pressed;

	public IAmSpeed() throws AWTException {
		bot = new Robot();
		pressed = new ArrayList<>();
	}

	public void nativeKeyPressed(NativeKeyEvent e) {
		if (!pressed.contains(e.getKeyCode())) {
			pressed.add(e.getKeyCode());
			if (e.getKeyCode() == NativeKeyEvent.VC_F1) {
				level0();
			}
		}
	}

	private void level0() {
		bot.keyPress(VK_W);
		pauseBot(549);
		crouchJump(false);
		pauseBot(1120);
		crouchJump(true);
		bot.keyRelease(VK_W);
		moveView(90, 0);
		pauseBot(700); //skip rewind
		bot.keyPress(VK_W);
		pauseBot(400);
		crouchJump(false);
		pauseBot(1020);
		bot.keyRelease(VK_W);
		crouchJump(true);
		pauseBot(700); //skip rewind at 200 fps
		moveView(90, 0);
		bot.keyPress(VK_W);
		pauseBot(450);
		bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		pauseBot(900);
		fullCrouch();
		move(VK_D, 150);
		pauseBot(500);
		moveView(-25, -10);
		pauseBot(200);
		interact();
		moveView(25, 10);
		doubleJump(700);
		moveView(5, 0);
		pauseBot(2000);
		doubleJump(500);
	}

	private void move(int keyCode, long millis) {
		bot.keyPress(keyCode);
		pauseBot(millis);
		bot.keyRelease(keyCode);
	}

	private void fullCrouch() {
		bot.keyPress(VK_CONTROL);
		pauseBot(400);
		bot.keyRelease(VK_CONTROL);
	}

	private void interact() {
		bot.keyPress(VK_E);
		pauseBot(100);
		bot.keyRelease(VK_E);
	}

	private void moveView(int yaw, int pitch) {
		int yawI = yaw * 20;
		int pitchI = pitch * 20;
		bot.mouseMove((int) (MouseInfo.getPointerInfo().getLocation().getX() + yawI), (int) MouseInfo.getPointerInfo().getLocation().getY() - pitchI);
	}

	private void doubleJump(int millisBetweenJumps) {
		jump();
		pauseBot(millisBetweenJumps);
		jump();
	}

	private void crouchJump(boolean release) {
		if (!release) {
			bot.keyPress(VK_SPACE);
			bot.keyPress(VK_CONTROL);
		} else {
			bot.keyRelease(VK_SPACE);
			bot.keyRelease(VK_CONTROL);
		}
	}

	private void jump() {
		bot.keyPress(VK_SPACE);
		pauseBot(100);
		bot.keyRelease(VK_SPACE);
	}

	public void pauseBot(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void nativeKeyReleased(NativeKeyEvent e) {
		if (pressed.contains(e.getKeyCode())) {
			pressed.remove((Integer) e.getKeyCode());
		}
	}

	public void nativeKeyTyped(NativeKeyEvent e) {}

	public static void main(String[] args) throws AWTException {
		Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
		logger.setLevel(Level.OFF);
		logger.setUseParentHandlers(false);
		try {
			GlobalScreen.registerNativeHook();
		} catch (NativeHookException ignored) {}
		IAmSpeed speed = new IAmSpeed();
		GlobalScreen.addNativeKeyListener(speed);
	}
}
