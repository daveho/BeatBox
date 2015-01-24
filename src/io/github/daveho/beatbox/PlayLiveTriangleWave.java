package io.github.daveho.beatbox;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.Buffer;

public class PlayLiveTriangleWave extends PlayLiveNote {
	public PlayLiveTriangleWave(AudioContext ac, float freq, float gain, UGen out) {
		super(ac, freq, gain, out, Buffer.TRIANGLE);
	}
}
