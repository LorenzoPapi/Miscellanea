//package com.lorenzopapi.github.music;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.KeyEvent;
//import java.awt.event.KeyListener;
//import java.awt.event.WindowEvent;
//import java.awt.event.WindowListener;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//import static java.awt.event.KeyEvent.VK_1;
//import static java.awt.event.KeyEvent.VK_Q;
//
//public class KeyboardPianoInstrument extends BasicInstrument implements KeyListener {
//
//	long keyPressedMillis;
//	long keyPressLength;
//	int octave = 4; //Middle Octave
//	int lastOctaveWritten;
//	List<Integer> alreadyPressed = new ArrayList<>();
//	ConcurrentHashMap<KPINote, Integer> octaveToNote = new ConcurrentHashMap<>();
//	File output = new File("output_test.kpi");
//	ByteArrayOutputStream bout;
//
//	static Thread thread = new Thread(new KeyboardPianoInstrument());
//
//	public static void main(String[] args) {
//		 thread.start();
//	}
//
//	@Override
//	public void run() {
//		try {
//			super.run();
//			if (output.exists())
//				output.delete();
//			output.createNewFile();
//			bout = new ByteArrayOutputStream();
//			setTempo(120);
//			setInstrument(InstrumentType.PIANO);
//			JFrame frame = new JFrame("Test");
//			frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
//			frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//			frame.addWindowListener(new WindowListener() {
//				@Override
//				public void windowOpened(WindowEvent e) {
//				}
//
//				@Override
//				public void windowClosing(WindowEvent e) {
//				}
//
//				@Override
//				public void windowClosed(WindowEvent e) {
//					try {
//						bout.close();
//						synth.close();
//					} catch (Exception ex) {
//						ex.printStackTrace();
//					}
//				}
//
//				@Override
//				public void windowIconified(WindowEvent e) {
//				}
//
//				@Override
//				public void windowDeiconified(WindowEvent e) {
//				}
//
//				@Override
//				public void windowActivated(WindowEvent e) {
//				}
//
//				@Override
//				public void windowDeactivated(WindowEvent e) {
//				}
//			});
//			frame.setVisible(true);
//			frame.addKeyListener(this);
//			while (frame.isDisplayable()) {
//				frame.repaint();
//			}
//		} catch (Exception ignored) {}
//	}
//
//	@Override
//	public void keyPressed(KeyEvent ke) {
//		try {
//			int pressed = ke.getKeyCode();
//
//			if (pressed == VK_1)
//				octave = (octave + 1 == 10) ? 0 : octave + 1;
//
//			if (pressed == VK_Q)
//				octave = (octave - 1 == -1) ? 9 : octave - 1;
//
//			for (KPINote note : KPINote.values()) {
//				if (pressed == note.code && !alreadyPressed.contains(pressed)) {
//					keyPressedMillis = System.currentTimeMillis();
//					if (lastOctaveWritten != octave + 118) {
//						bout.write(octave + 118);
//						lastOctaveWritten = octave + 118;
//					}
//					bout.write(note.note.getBytes());
//					//bout.write(new byte[]{0, 0, 0, 0, 0, 0, 0, 0});
//					//System.out.println(Arrays.toString(bout.toByteArray()));
//					noteOn((octave + note.octave) + note.note);
//					octaveToNote.put(note, octave + note.octave);
//				}
//			}
//
//			if (!alreadyPressed.contains(pressed))
//				alreadyPressed.add(pressed);
//
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//	}
//
//	@Override
//	public void keyReleased(KeyEvent e) {
//		try {
//			int released = e.getKeyCode();
//			for (Map.Entry<KPINote, Integer> entry : octaveToNote.entrySet()) {
//				if (released == entry.getKey().code && alreadyPressed.contains(released)) {
//					//TODO t h i n k
//					//bout.write(ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putLong(keyPressLength).array());
//					keyPressLength = System.currentTimeMillis() - keyPressedMillis;
//					noteOff(entry.getValue() + entry.getKey().note);
//					octaveToNote.remove(entry.getKey());
//					alreadyPressed.remove(Integer.valueOf(released));
//				}
//			}
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//	}
//
//	@Override
//	public void keyTyped(KeyEvent e) {}
//
//}
