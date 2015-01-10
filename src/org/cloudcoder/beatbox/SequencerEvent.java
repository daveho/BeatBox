package org.cloudcoder.beatbox;

/**
 * A {@link Sequencer} event.
 */
public interface SequencerEvent {
	/**
	 * Fire the event.
	 * 
	 * @param seq the {@link Sequencer}
	 */
	public void fire(Sequencer seq);
}
