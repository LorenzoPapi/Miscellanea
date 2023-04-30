package com.github.lorenzopapi.misc.rubiks;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class RubiksFrame extends JFrame implements MouseWheelListener, KeyListener {
	private final RubiksCube cube = new RubiksCube();

	public RubiksFrame(){
		super("RubiksFrame Cube");
		this.setSize(640, 480);
		this.getContentPane().setBackground(Color.BLACK);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.addMouseWheelListener(this);
		this.addKeyListener(this);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;

		int x = this.getSize().width / 2 - cube.getCubeSize() * 5;
		int y = this.getSize().height / 2 - cube.getCubeSize();

		drawFace(g2d, cube.getCube()[0], x + cube.getCubeSize() * 3, y - cube.getCubeSize() * 3);
		for (int i = 0; i < 4; i++) drawFace(g2d, cube.getCube()[i+1], x + cube.getCubeSize() * 3 * i, y);
		drawFace(g2d, cube.getCube()[5], x + cube.getCubeSize() * 3, y + cube.getCubeSize() * 3);
	}

	public void drawFace(Graphics2D g2d, int face, int topX, int topY) {
		for (int j = 0; j < 3; j++) {
			for (int k = 0; k < 3; k++) {
				g2d.setColor(new Color(Utils.getColor(face, k + j*3)));
				g2d.fill3DRect(topX + cube.getCubeSize() * j, topY + cube.getCubeSize() * k, cube.getCubeSize(), cube.getCubeSize(), true);
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		RubiksFrame rubiks = new RubiksFrame();
		SwingUtilities.invokeLater(() -> rubiks.setVisible(true));
		while (true) {
			rubiks.repaint();
			Thread.sleep(50);
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent event) {
		cube.mouseWheelMoved(event);
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		cube.keyPressed(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {}
}