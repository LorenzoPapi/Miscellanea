//Copyright (c) 2021: made by pizza boi can't copy or ded.

package com.github.lorenzopapi.misc;

import javax.sound.midi.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MidiHandler {

	private static final List<String> notes = Arrays.asList("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B");

	public static void main(String[] args) {
		for (MidiDevice.Info info : MidiSystem.getMidiDeviceInfo()) {
			try {
				MidiDevice device = MidiSystem.getMidiDevice(info);
				for (Transmitter transmitter : device.getTransmitters()) {
					transmitter.setReceiver(new MidiInputReceiver());
				}
				device.getTransmitter().setReceiver(new MidiInputReceiver());
				device.open();
			} catch (Throwable ignored) {}
		}
	}

	public static class MidiInputReceiver implements Receiver {
		private final Robot robot;
		private final HashMap<Integer, Integer> keyNoteMap = new HashMap<>();

		public MidiInputReceiver() throws AWTException, MidiUnavailableException {
			this.robot = new Robot();
			// Hollow Knight keys
//			keyNoteMap.put(nameToID("4F"), KeyEvent.VK_I);
//			keyNoteMap.put(nameToID("4F#"), KeyEvent.VK_A);
//			keyNoteMap.put(nameToID("4G"), KeyEvent.VK_Z);
//			keyNoteMap.put(nameToID("4G#"), KeyEvent.VK_S);
//			keyNoteMap.put(nameToID("4A"), KeyEvent.VK_X);
//			keyNoteMap.put(nameToID("4A#"), KeyEvent.VK_D);
//			keyNoteMap.put(nameToID("4B"), KeyEvent.VK_C);
//			keyNoteMap.put(nameToID("5C"), KeyEvent.VK_TAB);
//			keyNoteMap.put(nameToID("5B"), KeyEvent.VK_LEFT);
//			keyNoteMap.put(nameToID("6C"), KeyEvent.VK_UP);
//			keyNoteMap.put(nameToID("6D"), KeyEvent.VK_DOWN);
//			keyNoteMap.put(nameToID("6E"), KeyEvent.VK_RIGHT);
			keyNoteMap.put(nameToID("5C"), KeyEvent.VK_A);
			keyNoteMap.put(nameToID("5E"), KeyEvent.VK_S);
			keyNoteMap.put(nameToID("5F"), KeyEvent.VK_W);
			keyNoteMap.put(nameToID("5A"), KeyEvent.VK_D);
			MidiSystem.getSynthesizer().open();
		}

		@Override
		public void send(MidiMessage msg, long timeStamp) {
			//1st byte = pressed/released; -112 if pressed, -128 if released
			//2nd byte = note id
			//3rd byte = how much the note was pressed; higher number, higher pressure
			//Midi messages are cool and easy
			int note = msg.getMessage()[1];
			if (keyNoteMap.containsKey(note)) {
				if (msg.getMessage()[0] == -112) {
					robot.keyPress(keyNoteMap.get(note));
				} else {
					robot.keyRelease(keyNoteMap.get(note));
				}
				robot.waitForIdle();
			}
		}

		private int nameToID(String id) {
			return Integer.parseInt(id.substring(0, 1)) * 12 + notes.indexOf(id.substring(1));
		}

		@Override
		public void close() {}
	}
}