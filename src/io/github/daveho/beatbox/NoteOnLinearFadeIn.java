package io.github.daveho.beatbox;

import javax.sound.midi.MidiMessage;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.Bead;
import net.beadsproject.beads.core.UGen;

/**
 * Timed linear rise gain UGen.
 */
public class NoteOnLinearFadeIn extends UGen {
	private final int totalSamples;
	private int processedSamples;
	private boolean active;

	public NoteOnLinearFadeIn(AudioContext ac, float riseTimeMs) {
		super(ac, 1, 1);
		// Determine the number of samples in the rise time
		this.totalSamples = (int) ac.msToSamples(riseTimeMs);
		this.active = false;
	}
	
	@Override
	protected void messageReceived(Bead message) {
		System.out.println("NoteOnLinearFadeIn received message");
		
		// Become active when a midi key down event occurs
		if (Midi.hasMidiMessage(message)) {
			MidiMessage msg = Midi.getMidiMessage(message);
			if (msg.getStatus() == Midi.STATUS_KEY_DOWN) {
				active = true;
				processedSamples = 0;
				System.out.println("Start rise");
			}
		}
	}

	@Override
	public void calculateBuffer() {
		if (!active) {
			// Just copy samples from input to output
			System.arraycopy(bufIn[0], 0, bufOut[0], 0, bufferSize);
		} else {
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
				active = false;
			}
		}
	}

}
