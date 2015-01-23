package io.github.daveho.beatbox;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.WavePlayer;

/**
 * Play a square wave in response to real-time events (e.g.,
 * midi input events.)
 */
public class PlayLiveSquareWave {
	private AudioContext ac;
	private float freq;
	private float gain;
	private UGen out;
	private UGen gen;
	
	/**
	 * Constructor.
	 *
	 * @param ac    the AudioContext
	 * @param freq  frequency
	 * @param gain  gain
	 * @param out   the UGen to which the output should be sent
	 */
	public PlayLiveSquareWave(AudioContext ac, float freq, float gain, UGen out) {
		this.ac = ac;
		this.freq = freq;
		this.gain = gain;
		this.out = out;
	}

	/**
	 * Start playing.
	 */
	public void start() {
		this.gen = new WavePlayer(ac, freq, Buffer.SQUARE);
		Gain g = new Gain(ac, 1, gain);
		g.addInput(gen);
		out.addInput(g);
		gen.start();
	}
	
	/**
	 * Stop playing.
	 */
	public void stop() {
		gen.kill();
	}
}
