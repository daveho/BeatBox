package org.cloudcoder.beatbox;

/**
 * Abstract base class for "simple" {@link SequencerEvent}s,
 * which have a no-op implementation of {@link SequencerEvent#onAdd(int, Sequencer)}.
 */
public abstract class SimpleSequencerEvent implements SequencerEvent {
	@Override
	public void onAdd(int trackIndex, int beat, Sequencer seq) {
		// Do nothing
	}
}
