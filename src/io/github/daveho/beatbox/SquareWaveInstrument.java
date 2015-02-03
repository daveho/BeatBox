package io.github.daveho.beatbox;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Buffer;

/**
 * An instrument which listens for MidiMessages and plays square
 * waves in response.
 */
public class SquareWaveInstrument extends WaveInstrument {

	/**
	 * Constructor.
	 * 
	 * @param ac    the AudioContext
	 * @param gain  the maximum gain
	 */
	public SquareWaveInstrument(AudioContext ac, float gain) {
		super(ac, Buffer.SQUARE, gain);
	}
}
