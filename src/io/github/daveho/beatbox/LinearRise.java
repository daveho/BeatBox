package io.github.daveho.beatbox;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;

/**
 * Timed linear rise gain UGen.
 */
public class LinearRise extends UGen {
	private final int totalSamples;
	private int processedSamples;
	private boolean finished;

	public LinearRise(AudioContext ac, float riseTimeMs) {
		super(ac, 1, 1);
		// Determine the number of samples in the rise time
		this.totalSamples = (int) ac.msToSamples(riseTimeMs);
		// Keep track of how many samples have been processed
		this.processedSamples = 0;
		// If all samples have been processed, then the rise is done
		// and we just copy samples from the input to the output
		this.finished = false;
	}

	@Override
	public void calculateBuffer() {
		if (finished) {
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
				finished = true;
			}
		}
	}

}
