package io.github.daveho.beatbox;

import java.util.HashMap;
import java.util.Map;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;

/**
 * Factory for creating chains of UGens using a fluid API.
 * Chains should be constructed "backwards", from sink to
 * source.  Individual UGens can be labeled with arbitrary
 * strings.
 */
public class Builder {
	private final AudioContext ac;
	private final UGen ugen;
	private final Map<String, UGen> map;
	
	/**
	 * Create a {@link Builder} for a chain of UGens which will sent
	 * output to given sink UGen.
	 * 
	 * @param ac    the AudioContext
	 * @param ugen  the sink UGen
	 */
	private Builder(AudioContext ac, UGen ugen, Map<String, UGen> map) {
		this.ac = ac;
		this.ugen = ugen;
		this.map = map;
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
	
	/**
	 * Create a new {@link Builder) which will send audio
	 * to given sink.
	 * 
	 * @param ac   the AudioContext
	 * @param out  the sink
	 * @return a new {@link Builder}
	 */
	public static Builder build(AudioContext ac, UGen out) {
		return new Builder(ac, out, new HashMap<String, UGen>());
	}
	
	public Builder prepend(UGen ugen) {
		this.ugen.addInput(ugen);
		return new Builder(ac, this.ugen, this.map);
	}
	
	/**
	 * Label the current head of the chain.
	 * The label is set as the UGen's name, and the
	 * returned {@link Builder} will be able to
	 * identify the UGen by its label.
	 * 
	 * @param label the label to apply to the head of the chain
	 */
	public Builder label(String label) {
		HashMap<String, UGen> map = new HashMap<>();
		map.putAll(this.map);
		map.put(label, ugen);
		ugen.setName(label);
		return new Builder(ac, ugen, map);
	}
	
	/**
	 * Retrieve UGen with given label.
	 * 
	 * @param label the label
	 * @return the UGen with the specified label, or null if there is no
	 *         such UGen
	 */
	public UGen get(String label) {
//		System.out.println(map.size() + " entries in map");
		return map.get(label);
	}
	
//	/**
//	 * Prepend a square wave player to the chain.
//	 * 
//	 * @param freq the frequency
//	 * @return a {@link Builder} whose head is the square wave player
//	 */
//	public Builder playSquareWave(float freq) {
//		return prepend(new WavePlayer(ac, freq, Buffer.SQUARE));
//	}
//	
//	/**
//	 * Prepend a triangle wave player to the chain.
//	 * 
//	 * @param freq the frequency
//	 * @return a {@link Builder} whose head is the triangle wave player
//	 */
//	public Builder playTriangleWave(float freq) {
//		return prepend(new WavePlayer(ac, freq, Buffer.TRIANGLE));
//	}
//
//	/**
//	 * Prepend a Gain to the chain.
//	 * 
//	 * @param gain the gain factor
//	 * @return a {@link Builder} whose head is the Gain
//	 */
//	public Builder withGain(float gain) {
//		return prepend(new Gain(ac, 1, gain));
//	}
//	
//	/**
//	 * Prepend a linear rise to the chain.
//	 * 
//	 * @param riseTimeMs rise time in milliseconds
//	 * @return a {@link Builder} whose head is the Gain
//	 */
//	public Builder withLinearRise(float riseTimeMs) {
//		return prepend(new LinearRise(ac, riseTimeMs));
//	}
}
