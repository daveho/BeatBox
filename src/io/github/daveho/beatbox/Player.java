package io.github.daveho.beatbox;

import java.io.File;
import java.io.IOException;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.Bead;
import net.beadsproject.beads.ugens.Clock;
import net.beadsproject.beads.ugens.RecordToFile;

/**
 * Play music using a {@link Sequencer}.
 */
public class Player {
	private class OnTick extends Bead {
		// We wait a bit before actually playing
		boolean playing = false;

		// Beat counter
		int beat = 0;

		@Override
		protected void messageReceived(Bead message) {
			// Avoid playing on the first few beats to avoid
			// noisy audio artifact.
			if (!playing) {
				playing = (beat >= bpm-1);
				if (playing) {
					// Time to start playing!  Reset beat counter.
					beat = 0;
				}
			}
			if (playing) {
				seq.tick(beat);
			}
			beat++;
		}
	}

	/** The AudioContext. */
	protected final AudioContext ac;
	/** The {@link Desk}. */
	protected final Desk desk;
	/** The {@link Sequencer}. */
	protected final Sequencer seq;
	/** Number of beats per measure. */
	protected final int bpm;
	/** Length of a measure in milliseconds. */
	protected final float measureLenMs;
	private RecordToFile rtf;

	/**
	 * Constructor.
	 * 
	 * @param bpm           number of beats per measure
	 * @param measureLenMs  length of one measure in milliseconds
	 * @param numTracks     number of tracks
	 */
	public Player(int bpm, float measureLenMs, int numTracks) {
		this.bpm = bpm;
		this.measureLenMs = measureLenMs;
		this.ac = new AudioContext();
		this.desk = new Desk(ac, numTracks);
		this.seq = new Sequencer(desk);
	}

	/**
	 * Record audio to named file.
	 * 
	 * @param fileName name of file were audio should be recorded.
	 */
	public void recordToFile(String fileName) {
		try {
			this.rtf = new RecordToFile(ac, 2, new File(fileName));
		} catch (IOException e) {
			throw new RuntimeException("Error recording to file", e);
		}
		rtf.addInput(ac.out);
		ac.out.addDependent(rtf);
	}

	/**
	 * Play audio as specified by the events added
	 * to the {@link Sequencer}.
	 */
	public void play() {
		Clock clock = new Clock(ac, measureLenMs);
		Bead onTick = new OnTick();
		clock.addMessageListener(onTick);
		
		// Note that the Clock's class notion of "beat" is actually
		// what sequencer considers to mean "measure".
		clock.setTicksPerBeat(bpm);
		
		ac.out.addDependent(clock);
		
		
		ac.start();
	}

}