package com.github.lorenzopapi.misc.rubiks;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RubiksCube {
	private final int[] CUBE = new int[6];
	private int cubeSize = 20;
	private final List<MoveObject> currentMoves = new ArrayList<>();

	private final ThreadGroup group = new ThreadGroup("Rubiks");
	private final Runnable stupidSolverRunnable = () -> new RubiksSolver(this).solveStupidly();

	private final Runnable shufflerRunnable = () -> {
		try {
			Random r = new Random();
			for (int i = 0; i < 20; i++) {
				boolean reverse = r.nextBoolean();
				boolean CCW = r.nextBoolean();
				int index = r.nextInt(3);
				Move.values()[index].moveInterface.doMove(CUBE, reverse, CCW);
				MoveObject move = new MoveObject(index, reverse, CCW);
				currentMoves.add(move);
				Thread.sleep(100);
			}
			Thread.sleep(1000);
		} catch (InterruptedException interruptedException) {
			interruptedException.printStackTrace();
		}

	};

	public RubiksCube() {
		initCube();
	}

	private void initCube() {
		CUBE[0] = 0; //U
		CUBE[1] = 0b001001001001001001001001001; //L
		CUBE[2] = 0b010010010010010010010010010; //F
		CUBE[3] = 0b011011011011011011011011011; //R
		CUBE[4] = 0b100100100100100100100100100; //B
		CUBE[5] = 0b101101101101101101101101101; //D
	}

	public void mouseWheelMoved(MouseWheelEvent event) {
		this.cubeSize *= event.getWheelRotation() > 0 ? 0.8 : 1.25;
		event.consume();
	}

	public void keyPressed(KeyEvent e) {
		boolean CCW = (e.getModifiersEx() & InputEvent.SHIFT_DOWN_MASK) == InputEvent.SHIFT_DOWN_MASK;
		if (e.getKeyCode() == KeyEvent.VK_Q) {
			this.cubeSize *= 1.25;
		} else if (e.getKeyCode() == KeyEvent.VK_W) {
			this.cubeSize *= 0.8;
		} else if (e.getKeyCode() == KeyEvent.VK_F) {
			Move.F.moveInterface.doMove(CUBE, false, CCW);
		} else if (e.getKeyCode() == KeyEvent.VK_R) {
			Move.L.moveInterface.doMove(CUBE, true, CCW);
		} else if (e.getKeyCode() == KeyEvent.VK_L) {
			Move.L.moveInterface.doMove(CUBE, false, CCW);
		} else if (e.getKeyCode() == KeyEvent.VK_B) {
			Move.F.moveInterface.doMove(CUBE, true, CCW);
		} else if (e.getKeyCode() == KeyEvent.VK_U) {
			Move.U.moveInterface.doMove(CUBE, false, CCW);
		} else if (e.getKeyCode() == KeyEvent.VK_D) {
			Move.U.moveInterface.doMove(CUBE, true, CCW);
		} else if (e.getKeyCode() == KeyEvent.VK_I) {
			initCube();
		} else if (e.getKeyCode() == KeyEvent.VK_M) {
			if (group.activeCount() == 0) new Thread(group, shufflerRunnable, "Randomizer Thread").start();
			else System.out.println("Other threads already running!");
		} else if (e.getKeyCode() == KeyEvent.VK_N) {
			if (group.activeCount() == 0) new Thread(group, stupidSolverRunnable, "Stupid Solver Thread").start();
			else System.out.println("Other threads already running!");
		}
		e.consume();
	}

	public int getCubeSize() {
		return cubeSize;
	}

	public int[] getCube() {
		return CUBE;
	}

	public List<MoveObject> getCurrentMoves() {
		return currentMoves;
	}
}

enum Move {
	F("F", "B", (CUBE, reverse, CCW) -> {
		for (int i = 0; i < 3; i++) {
			//6->8->0->0->6
			//7->5->1->3->7
			//8->2->2->6->8
			int colorU = Utils.getColorBits(CUBE[0], reverse ? i*3 : 8-i*3);
			int colorL = Utils.getColorBits(CUBE[1], reverse ? 2-i : i+6);
			int colorR = Utils.getColorBits(CUBE[3], reverse ? i+6 : 2-i);
			int colorD = Utils.getColorBits(CUBE[5], reverse ? 8-i*3 : i*3);

			Utils.setCubeBits(CUBE, 0, reverse ? i*3 : 8-i*3, (CCW != reverse) ? colorR : colorL);
			Utils.setCubeBits(CUBE, 1, reverse ? 2-i : i+6, (CCW != reverse) ? colorU : colorD);
			Utils.setCubeBits(CUBE, 3, reverse ? i+6 : 2-i, (CCW != reverse) ? colorD : colorU);
			Utils.setCubeBits(CUBE, 5, reverse ? 8-i*3 : i*3, (CCW != reverse) ? colorL : colorR);
		}
		Utils.rotateFace(CUBE, reverse ? 4 : 2, CCW);
	}),
	L("L", "R", (CUBE, reverse, CCW) -> {
		for (int i = (reverse ? 6 : 0); i < (reverse ? 9 : 3); i++) {
			int colorU = Utils.getColorBits(CUBE[0], i);
			int colorF = Utils.getColorBits(CUBE[2], i);
			int colorB = Utils.getColorBits(CUBE[4], 8 - i);
			int colorD = Utils.getColorBits(CUBE[5], i);

			Utils.setCubeBits(CUBE, 0, i, (reverse != CCW) ? colorF : colorB);
			Utils.setCubeBits(CUBE, 2, i, (reverse != CCW) ? colorD : colorU);
			Utils.setCubeBits(CUBE, 4, 8 - i, (reverse != CCW) ? colorU : colorD);
			Utils.setCubeBits(CUBE, 5, i, (reverse != CCW) ? colorB : colorF);
		}
		Utils.rotateFace(CUBE, reverse ? 3 : 1, CCW);
	}),
	U("U", "D", (CUBE, reverse, CCW) -> {
		for (int i = (reverse ? 2 : 0); i < 9; i+=3) {
			int colorL = Utils.getColorBits(CUBE[1], i);
			int colorF = Utils.getColorBits(CUBE[2], i);
			int colorR = Utils.getColorBits(CUBE[3], i);
			int colorB = Utils.getColorBits(CUBE[4], i);

			Utils.setCubeBits(CUBE, 1, i, (reverse != CCW) ? colorB : colorF);
			Utils.setCubeBits(CUBE, 2, i, (reverse != CCW) ? colorL : colorR);
			Utils.setCubeBits(CUBE, 3, i, (reverse != CCW) ? colorF : colorB);
			Utils.setCubeBits(CUBE, 4, i, (reverse != CCW) ? colorR : colorL);
		}
		Utils.rotateFace(CUBE, reverse ? 5 : 0, CCW);
	});

	String name;
	String reverse;
	MoveFunction moveInterface;

	Move(String name, String reverse, MoveFunction moveMethod) {
		this.name = name;
		this.reverse = reverse;
		this.moveInterface = moveMethod;
	}
}

enum CubeColor {
	WHITE("white", 0xFFFFFF),
	AQUA("orange", 0xFFA500),
	GREEN("green", 0x00FF00),
	RED("red", 0xFF0000),
	BLUE("blue", 0x0000FF),
	YELLOW("yellow", 0xFFFF00);

	String name;
	int rgb;

	CubeColor(String name, int rgb) {
		this.name = name;
		this.rgb = rgb;
	}
}

class Utils {
	public static void setCubeBits(int[] CUBE, int face, int index, int bits) {
		CUBE[face] = Utils.setBitsThrice(CUBE[face], index, bits);
	}

	public static int getExtractedBits(int number, int count, int start) {
		//Tronca numero sino alla posizione richiesta;
		//Fai AND con "count" bit accesi
		return ((1 << count) - 1) & (number >> start);
	}

	public static int getColorBits(int face, int index) {
		return Utils.getExtractedBits(face, 3, index * 3);
	}

	public static int getColor(int face, int index) {
		return CubeColor.values()[Utils.getColorBits(face, index)].rgb;
	}

	/*public static String toBinaryString(int number) {
		return toBinaryString(number, 32);
	}

	public static String toBinaryString(int number, int pad) {
		return String.format("%"+pad+"s", Integer.toBinaryString(number)).replace(" ", "0");
	}*/

	public static void rotateFace(int[] CUBE, int face, boolean CCW) {
		//0->2; 1->5; 2->8; 3->1; 5->7; 6->0; 7->3; 8->6
		int[] array = new int[9];
		for (int i = 0; i < 9; i++) {
			array[i] = Utils.getExtractedBits(CUBE[face], 3, i*3);
		}

		int[] temp = new int[9];
		System.arraycopy(array, 0, temp, 0, temp.length);

		for (int i = 0; i < 3; i++) {
			array[i] = CCW ? temp[6 - 3 * i] : temp[2 + 3 * i];
			array[6+i] = CCW ? temp[8 - 3 * i] : temp[3 * i];
		}
		array[3] = CCW ? temp[7] : temp[1];
		array[5] = CCW ? temp[1] : temp[7];

		CUBE[face] = 0;
		for (int i = 0; i < 9; i++) {
			CUBE[face] |= (array[i] << 3*i);
		}
	}

	public static int setBitsThrice(int number, int index, int bits) {
		int low = Utils.getExtractedBits(number, index*3, 0);
		int high = Utils.getExtractedBits(number, 24 - index*3, index*3+3);
		return (high << index*3+3) | (bits << index*3) | (low);
	}
}

class MoveObject {
	int index;
	boolean reverse;
	boolean CCW;

	public MoveObject(int index, boolean reverse, boolean CCW) {
		this.index = index;
		this.reverse = reverse;
		this.CCW = CCW;
	}

	@Override
	public String toString() {
		return (reverse ? Move.values()[index].reverse : Move.values()[index].name) + (CCW ? "'" : "");
	}
}

interface MoveFunction {
	void doMove(int[] CUBE, boolean reverse, boolean CCW);
}