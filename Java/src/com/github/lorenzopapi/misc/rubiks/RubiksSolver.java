package com.github.lorenzopapi.misc.rubiks;

import java.util.ArrayList;
import java.util.Collections;

public class RubiksSolver {
	private final int[] CUBE;
	private final RubiksCube cube;

	public RubiksSolver(RubiksCube cube) {
		this.CUBE = cube.getCube();
		this.cube = cube;
	}

	public void solveStupidly() {
		if (!cube.getCurrentMoves().isEmpty()) {
			ArrayList<MoveObject> opposites = new ArrayList<>();
			ArrayList<MoveObject> moves = new ArrayList<>(cube.getCurrentMoves());
			Collections.reverse(moves);
			moves.forEach(move -> opposites.add(new MoveObject(move.index, move.reverse, !move.CCW)));
			for (MoveObject m : opposites) {
				Move.values()[m.index].moveInterface.doMove(CUBE, m.reverse, m.CCW);
				try {
					Thread.sleep(100);
				} catch (InterruptedException interruptedException) {
					interruptedException.printStackTrace();
				}
			}
			cube.getCurrentMoves().clear();
		}
	}
}
