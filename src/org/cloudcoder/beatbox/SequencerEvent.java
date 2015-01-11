package org.cloudcoder.beatbox;

/**
 * A {@link Sequencer} event.
 */
public interface SequencerEvent {
	/**
	 * Method requesting that the event schedule itself
	 * to be fired at a specified beat.  We give the events
	 * control over adding themselves to allow event groups
	 * (where many events will be added at once, possibly
	 * with an offset from the "base" beat.
	 * 
	 * @param beat the beat when the event should schedule itself to fire
	 * @param seq the {@link Sequencer}
	 */
	public void schedule(int beat, Sequencer seq);
	
	/**
	 * Fire the event.
	 * 
	 * @param seq the {@link Sequencer}
	 */
	public void fire(Sequencer seq);
}
