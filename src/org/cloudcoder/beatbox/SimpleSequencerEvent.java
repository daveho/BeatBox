package org.cloudcoder.beatbox;

/**
 * Abstract base class for "simple" {@link SequencerEvent}s
 * which simply schedule themselves to be fired at a particular
 * beat.
 */
public abstract class SimpleSequencerEvent implements SequencerEvent {
	@Override
	public void schedule(int beat, Sequencer seq) {
		seq.atBeat(beat, this);
	}
}
