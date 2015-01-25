package io.github.daveho.beatbox;

/**
 * Sequencer beat number and timestamp.
 * Useful for determining the time offset of an event relative
 * to the current beat.
 */
public class SequencerBeat {
	private final int beat;
	private final long nano;
	
	/**
	 * Constructor.
	 * 
	 * @param beat  the beat number
	 * @param nano  the beat timestamp, as determined by System.nanoTime()
	 */
	public SequencerBeat(int beat, long nano) {
		this.beat = beat;
		this.nano = nano;
	}
	
	/**
	 * Get the beat number.
	 * 
	 * @return the beat number
	 */
	public int getBeat() {
		return beat;
	}
	
	/**
	 * Get the beat timestamp, as determined by System.nanoTime().
	 * 
	 * @return the beat timestamp
	 */
	public long getNano() {
		return nano;
	}
}
