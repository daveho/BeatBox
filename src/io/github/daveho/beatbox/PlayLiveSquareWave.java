package io.github.daveho.beatbox;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.Buffer;

/**
 * Play a square wave in response to real-time events (e.g.,
 * midi input events.)
 */
public class PlayLiveSquareWave extends PlayLiveNote {
	public PlayLiveSquareWave(AudioContext ac, float freq, float gain, UGen out) {
		super(ac, freq, gain, out, Buffer.SQUARE);
	}
}
