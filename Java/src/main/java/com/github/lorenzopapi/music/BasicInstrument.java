package com.github.lorenzopapi.music;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;
import java.util.Arrays;
import java.util.List;

public class BasicInstrument implements Runnable {
	
	public Synthesizer synth = null;
	private final List<String> notes = Arrays.asList("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B");
	private MidiChannel[] channels;
	private int instrument;
	private int volume;
	private int tempo;
	
	public void setTempo(int tempo) {
		this.tempo = tempo;
	}
	
	public void setInstrument(int instrument) {
		this.instrument = instrument;
	}
	
	/**
	 * Plays the given note for the given duration
	 */
	public void play(String note, long duration) throws InterruptedException {
		channels[instrument].noteOn(id(note), volume);
		rest(duration);
		channels[instrument].noteOff(id(note));
	}
	
	public void noteOn(String note) {
		channels[instrument].noteOn(id(note), volume);
	}
	
	public void noteOff(String note) {
		channels[instrument].noteOff(id(note));
	}
	
	/**
	 * Plays nothing for the given duration
	 */
	public void rest(long duration) throws InterruptedException {
		double ratioReverse = tempo / 60.0;
		Thread.sleep((long) (duration / ratioReverse));
	}
	
	/**
	 * Returns note
	 *
	 * @return the MIDI id for a given note: e.g. 4C -> 60
	 */
	private int id(String note) {
		return notes.indexOf(note.substring(1)) + 12 * (Integer.parseInt(note.substring(0, 1)) + 1);
	}
	
	@Override
	public void run() {
		try {
			synth = MidiSystem.getSynthesizer();
			synth.open();
			channels = synth.getChannels();
			volume = 127;
			System.out.println(Arrays.toString(synth.getLoadedInstruments()));
			channels[0].programChange(0);
			channels[1].programChange(116);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	static class InstrumentType {
		public static final int PIANO = 0;
		public static final int DRUMS = 1;
	}
}
