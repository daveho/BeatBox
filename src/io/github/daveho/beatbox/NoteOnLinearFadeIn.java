package io.github.daveho.beatbox;

import java.util.Arrays;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.Bead;
import net.beadsproject.beads.core.UGen;

/**
 * Timed linear rise gain UGen.
 */
public class NoteOnLinearFadeIn extends UGen {
	private enum State {
		OFF,
		RISING,
		ON,
	}
	
	private final int totalSamples;
	private int processedSamples;
	private State state;

	public NoteOnLinearFadeIn(AudioContext ac, float riseTimeMs) {
		super(ac, 1, 1);
		// Determine the number of samples in the rise time
		this.totalSamples = (int) ac.msToSamples(riseTimeMs);
		this.state = State.OFF;
	}
	
	@Override
	protected void messageReceived(Bead message) {
		System.out.println("NoteOnLinearFadeIn received message");
		
		// Become active when a midi key down event occurs
		if (Midi.hasMidiMessage(message)) {
			MidiMessage msg = Midi.getMidiMessage(message);
			if (msg.getStatus() == ShortMessage.NOTE_ON) {
				state = State.RISING;
				processedSamples = 0;
				System.out.println("Start rise");
			} else if (msg.getStatus() == ShortMessage.NOTE_OFF) {
				// FIXME: abrupt cutoff
				state = State.OFF;
			}
		}
	}

	@Override
	public void calculateBuffer() {
		if (state == State.ON) {
			// Just copy samples from input to output
			System.arraycopy(bufIn[0], 0, bufOut[0], 0, bufferSize);
		} else if (state == State.RISING) {
			int numSamplesToProcess = Math.min(bufferSize, totalSamples - processedSamples);
			// Copy samples, applying computed linear rise gain
			for (int i = 0; i < numSamplesToProcess; i++) {
				float f = (processedSamples + i) / (float) totalSamples;
				bufOut[0][i] = f * bufIn[0][i];
			}
			// In case rise has ended and we need to copy the remaining input samples
			for (int i = numSamplesToProcess; i < bufferSize; i++) {
				bufOut[0][i] = bufIn[0][i];
			}
			// Account for additional processed samples: if all samples have been
			// processed, the rise is finished
			processedSamples += numSamplesToProcess;
			if (processedSamples >= totalSamples) {
				System.out.println("End rise");
				state = State.ON;
			}
		} else if (state == State.OFF) {
			Arrays.fill(bufOut[0], 0f);
		}
	}

}
