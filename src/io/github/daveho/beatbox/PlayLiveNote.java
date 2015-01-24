package io.github.daveho.beatbox;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.WavePlayer;

/**
 * Base class for playing notes using a Buffer to generate
 * the note waveform.
 */
public abstract class PlayLiveNote implements PlayLive {
	private AudioContext ac;
	private float freq;
	private float gain;
	private UGen out;
	private Buffer buffer;
	private UGen gen;

	/**
	 * Constructor.
	 *
	 * @param ac    the AudioContext
	 * @param freq  frequency
	 * @param gain  gain
	 * @param out   the UGen to which the output should be sent
	 * @param buffer the buffer generating the note waveform (e.g., Buffer.SQUARE)
	 */
	public PlayLiveNote(AudioContext ac, float freq, float gain, UGen out, Buffer buffer) {
		this.ac = ac;
		this.freq = freq;
		this.gain = gain;
		this.out = out;
		this.buffer = buffer;
	}

	@Override
	public void start() {
		this.gen = new WavePlayer(ac, freq, buffer);
		Gain g = new Gain(ac, 1, gain);
		g.addInput(gen);
		out.addInput(g);
		gen.start();
	}
	
	@Override
	public void stop() {
		gen.kill();
	}
}
