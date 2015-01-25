package io.github.daveho.beatbox;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.Buffer;

/**
 * Play a triangle wave in response to real-time events (e.g.,
 * midi input events.)
 */
public class PlayLiveTriangleWave extends PlayLiveNote {
	/**
	 * Constructor.
	 * 
	 * @param ac    the AudioContext
	 * @param freq  the frequency
	 * @param gain  the gain
	 * @param out   the UGen to which the generated note should be sent
	 */
	public PlayLiveTriangleWave(AudioContext ac, float freq, float gain, UGen out) {
		super(ac, freq, gain, out, Buffer.TRIANGLE);
	}
}
