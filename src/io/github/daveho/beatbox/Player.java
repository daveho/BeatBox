package io.github.daveho.beatbox;

import io.github.daveho.gervill4beads.GervillUGen;
import io.github.daveho.gervill4beads.MidiMessageSource;
import io.github.daveho.gervill4beads.ReceivedMidiMessageSource;

import java.io.File;
import java.io.IOException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.ShortMessage;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.Bead;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.Clock;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.RecordToFile;
import net.beadsproject.beads.ugens.Reverb;

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

	/**
	 * Allow live playing.
	 * 
	 * @param synth   the {@link Bead} to which midi input events should
	 *                be sent: e.g. a synth
	 * @param record  true if input events should be recorded (TODO: allow destination)
	 * @throws MidiUnavailableException
	 */
	public void liveSynth(Bead synth, boolean record) throws MidiUnavailableException {
		ReceivedMidiMessageSource messageSource = new ReceivedMidiMessageSource(ac);
		final MidiDevice device = CaptureMidiEvents.getMidiInput(messageSource);
		seq.addShutdownHook(new Runnable() {
			@Override
			public void run() {
				System.out.println("Done, shutting down midi device");
				device.close();
			}
		});
		messageSource.addMessageListener(synth);
	}
	
	public MidiMessageSource liveGervillSynth(UGen synth, int patch) throws MidiUnavailableException, InvalidMidiDataException {
		ReceivedMidiMessageSource messageSource = new ReceivedMidiMessageSource(ac);
		final MidiDevice device = CaptureMidiEvents.getMidiInput(messageSource);
		seq.addShutdownHook(new Runnable() {
			@Override
			public void run() {
				System.out.println("Done, shutting down midi device");
				device.close();
			}
		});
		
		ac.out.addInput(synth);
		
		messageSource.addMessageListener(synth);
		
		messageSource.send(new ShortMessage(ShortMessage.PROGRAM_CHANGE, patch, 0), -1);
		
		return messageSource;
	}

	/**
	 * Add reverb to a track.
	 * 
	 * @param trackNumber            the track
	 * @param damping                damping
	 * @param earlyReflectionsLevel  early reflections level
	 * @param lateReverbLevel        late reverb level
	 */
	public void addReverbToTrack(int trackNumber, float damping, float earlyReflectionsLevel, float lateReverbLevel) {
		// The track feeds into the reverb splitter, which
		// feeds into both the AudioContext's output (implicitly,
		// via the call to setTrack), and the reverb's output
		// (which in turn is fed into the AudioContext's
		// output).  So, the eventual output is a mix of both
		// the wet and dry signals.
		Gain reverbSplit = new Gain(seq.getDesk().getAc(), 1);
		Reverb reverb = new Reverb(seq.getDesk().getAc());
		reverb.setDamping(damping);
		reverb.setEarlyReflectionsLevel(earlyReflectionsLevel);
		reverb.setLateReverbLevel(lateReverbLevel);
		
		reverb.addInput(reverbSplit);
		seq.getDesk().setTrack(trackNumber, reverbSplit);
		seq.getDesk().getAc().out.addInput(reverb);
	}

}