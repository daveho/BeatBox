package io.github.daveho.beatbox;

/**
 * A {@link Sequencer} event.
 */
public interface SequencerEvent {
	/**
	 * This method is called when a {@link SequencerEvent} is being
	 * added to a {@link Sequencer} to fire on a particular beat.
	 * "Leaf" events should just do nothing.  Composite events
	 * can use this callback as an opportunity to add other
	 * events.
	 * 
	 * @param trackIndex the track to which the event is being added
	 * @param beat beat on which this event is being added
	 * @param seq  the {@link Sequencer}
	 */
	public void onAdd(int trackIndex, int beat, Sequencer seq);
	
	/**
	 * Fire the event.
	 * 
	 * @param seq the {@link Sequencer}
	 * @param trackIndex the track index
	 */
	public void fire(Sequencer seq, int trackIndex);
}
