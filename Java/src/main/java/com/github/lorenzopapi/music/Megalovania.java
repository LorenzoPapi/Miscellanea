package com.lorenzopapi.github.music;

import static com.lorenzopapi.github.music.BasicInstrument.InstrumentType.*;

public class Megalovania extends BasicInstrument {

	static Thread thread = new Thread(new Megalovania());

	public static void main(String[] args) {
		while (true)
			new Megalovania().run();
	}

	@Override
	public void run() {
		try {
			super.run();
			setTempo(120); //semiminima = 1000
			
			rest(1000);
			setInstrument(DRUMS);
			for (int i = 0; i < 5; i++) {
				play("4D", 250);
			}
			rest(500);
			setInstrument(PIANO);
			for (int i = 0; i < 4; i++) {
				play("4D", 250);
				play("4D", 250);
				repeatingBeat1();
				play("4C", 250);
				play("4C", 250);
				repeatingBeat1();
				play("3B", 250);
				play("3B", 250);
				repeatingBeat1();
				play("3A#", 250);
				play("3A#", 250);
				repeatingBeat1();
				if (i < 3)
					repeatingBeat2Thread(i > 0);
			}
			rest(1000);
			synth.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void repeatingBeat1() throws InterruptedException {
		play("5D", 500);
		play("4A", 500);
		rest(250);
		play("4G#", 250);
		rest(250);
		play("4G", 500);
		play("4F", 500);
		play("4D", 250);
		play("4F", 250);
		play("4G", 250);
	}
	
	private void repeatingBeat11(String note) throws InterruptedException {
		play(note, 250);
		play(note, 250);
		play("6D", 500);
		play("5A", 500);
		rest(250);
		play("5G#", 250);
		rest(250);
		play("5G", 500);
		play("5F", 500);
		play("5D", 250);
		play("5F", 250);
		play("5G", 250);
	}

	private void repeatingBeat2(String[] note) throws InterruptedException {
		play(note[0], 500);
		play(note[0], 500);
		play(note[0], 250);
		play(note[0], 250);
		rest(250);
		play(note[1], 250);
		rest(250);
		play(note[1], 500);
		play(note[1], 500);
		play(note[1], 250);
		play(note[1], 500);
	}

	private void repeatingBeat2Thread(boolean second) {
		new Thread(repeatingBeat2Thread).start();
		if (second) {
			new Thread(repeatingBeat2Thread1).start();
			new Thread(repeatingBeat1Thread).start();
		}
	}
	private final Runnable repeatingBeat2Thread = () -> {
		try {
			repeatingBeat2(new String[]{"3D", "3D"});
			repeatingBeat2(new String[]{"3C", "3C"});
			repeatingBeat2(new String[]{"2B", "2B"});
			repeatingBeat2(new String[]{"2A#", "3C"});
		} catch (InterruptedException ignored) {}
	};
	
	private final Runnable repeatingBeat1Thread = () -> {
		try {
			repeatingBeat11("5D");
			repeatingBeat11("5C");
			repeatingBeat11("4B");
			repeatingBeat11("4A#");
		} catch (InterruptedException ignored) {}
	};
	
	private final Runnable repeatingBeat2Thread1 = () -> {
		try {
			repeatingBeat2(new String[]{"2D", "2D"});
			repeatingBeat2(new String[]{"2C", "2C"});
			repeatingBeat2(new String[]{"1B", "1B"});
			repeatingBeat2(new String[]{"1A#", "2C"});
		} catch (InterruptedException ignored) {}
	};

	public void play(String note, int duration) throws InterruptedException {
		super.play(note, duration);
	}

	public void rest(int duration) throws InterruptedException {
		super.rest(duration);
	}

}
