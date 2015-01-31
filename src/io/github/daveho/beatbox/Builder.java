package io.github.daveho.beatbox;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.WavePlayer;

/**
 * Factory for creating chains of UGens using a fluid API.
 * Chains should be constructed "backwards", from sink to
 * source.
 */
public class Builder {
	private final AudioContext ac;
	private final UGen ugen;
	
	/**
	 * Create a {@link Builder} for a chain of UGens which will sent
	 * output to given sink UGen.
	 * 
	 * @param ac    the AudioContext
	 * @param ugen  the sink UGen
	 */
	private Builder(AudioContext ac, UGen ugen) {
		this.ac = ac;
		this.ugen = ugen;
	}
	
	/**
	 * @return the AudioContext
	 */
	public AudioContext getAc() {
		return ac;
	}
	
	/**
	 * @return the sink UGen
	 */
	public UGen getUgen() {
		return ugen;
	}
	
	public static Builder build(AudioContext ac, UGen out) {
		return new Builder(ac, out);
	}
	
	/**
	 * Prepend a square wave player to the chain.
	 * 
	 * @param freq the frequency
	 * @return a {@link Builder} whose head is the square wave player
	 */
	public Builder playSquareWave(float freq) {
		WavePlayer player = new WavePlayer(ac, freq, Buffer.SQUARE);
		ugen.addInput(player);
		return new Builder(ac, player);
	}
	
	/**
	 * Prepend a triangle wave player to the chain.
	 * 
	 * @param freq the frequency
	 * @return a {@link Builder} whose head is the triangle wave player
	 */
	public Builder playTriangleWave(float freq) {
		WavePlayer player = new WavePlayer(ac, freq, Buffer.TRIANGLE);
		ugen.addInput(player);
		return new Builder(ac, player);
	}

	/**
	 * Prepend a Gain to the chain.
	 * 
	 * @param gain the gain factor
	 * @return a {@link Builder} whose head is the Gain
	 */
	public Builder withGain(float gain) {
		Gain g = new Gain(ac, 1, gain);
		ugen.addInput(g);
		return new Builder(ac, g);
	}
}
