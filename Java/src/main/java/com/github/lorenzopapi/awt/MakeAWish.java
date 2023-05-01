package com.github.lorenzopapi.awt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.concurrent.atomic.AtomicBoolean;

public class MakeAWish extends JComponent {

	static int frames = 0;
	static JFrame frame = new JFrame("Esprimi un desiderio!");

	public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		AtomicBoolean clicked = new AtomicBoolean(false);
		JFrame mainFrame = new JFrame("Esprimi un desiderio!");

		JPanel globalPanel = new JPanel();
		JPanel wishPanel = new JPanel();
		wishPanel.setSize(640, 320);
		JTextField writeWish = new JTextField();
		writeWish.setMinimumSize(new Dimension(640, 320));
		wishPanel.add(writeWish);

		JPanel makeItPanel = new JPanel();
		JButton makeItButton = new JButton("Fallo avverare!");
		makeItButton.addActionListener(e -> clicked.set(true));
		makeItPanel.setLocation(0, -100);
		makeItPanel.add(makeItButton);

		JPanel logPanel = new JPanel();
		logPanel.setSize(200, 200);
		logPanel.setMinimumSize(logPanel.getSize());
		logPanel.setMaximumSize(logPanel.getSize());
		TextArea log = new TextArea();
		log.setBackground(new Color(12632256));
		log.setEditable(false);
		logPanel.add(log);

		globalPanel.add(wishPanel);
		globalPanel.add(makeItPanel);
		globalPanel.add(logPanel);

		mainFrame.add(globalPanel);
		mainFrame.pack();
		mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);

		while (mainFrame.isVisible()) {
			while (!clicked.get());

			clicked.set(false);

			try {
				if (writeWish.getText().contains("Nerico"))
					throw new RuntimeException("Error: ImpossibleWishAsked\nDesiderio impossibile da avverare.");
				else {
					log.setText("");
					log.setForeground(Color.WHITE);
					log.append("Il desiderio si avvererà.\n\n\n\nForse.");
				}
			} catch (Throwable err) {
				log.setText("");
				log.setForeground(Color.RED);
				log.append("\n" + err.getMessage() + "\n");
			}
		}
	}

	public static void main2(String[] args) throws InterruptedException {
		frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());
		frame.add(new MakeAWish());
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setVisible(true);
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(ke -> {
			synchronized (MakeAWish.class) {
				if (ke.getID() == KeyEvent.KEY_PRESSED && ke.getKeyCode() == KeyEvent.VK_W) {
					System.exit(0);
				}
				return false;
			}
		});
		while (frame.isDisplayable()) {
			frame.repaint();
			Thread.sleep(50);
		}
	}

	public void paint2(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		/*g.setColor(Color.RED);
		g.fillRect(0, 0, 100, 100);
		g.setColor(Color.GREEN);
		g.fillRect(10, 10, 75, 75);
		g.setColor(Color.YELLOW);
		g.fillOval(200, 20, 100, 200);
		g2d.setColor(Color.BLACK);
		g2d.translate(frame.getWidth() / 2, frame.getHeight() / 2);
		g2d.rotate(Math.toRadians(45));
		g2d.fillRect(-50, -50, 100, 100);
		g2d.setColor(Color.RED);
		g2d.fillRect((int) (Math.cos(Math.toRadians(frames)) * 100), 0, 20, 20);
		Image img = null;
		try {
			img = ImageIO.read(new File("src\\resources\\splash.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		g2d.drawImage(img, 0, 0, (img1, infoflags, x, y, width, height) -> true);*/

		g2d.setColor(Color.RED);
		g2d.translate(frame.getWidth() / 2, frame.getHeight() / 2);
		g2d.drawRect(10, 100, 100, 10);
		g2d.setColor(Color.YELLOW);
		g2d.drawRect(-10, -100, 10, 100);
		g2d.setColor(Color.BLACK);
		g2d.drawRect(-10, -100, 10, 100);

		frames++;
	}
}
