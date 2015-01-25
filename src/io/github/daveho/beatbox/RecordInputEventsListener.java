package io.github.daveho.beatbox;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link InputEventListener} which captures
 * {@link InputEvent}s, turning them into {@link RecordedInputEvent}s
 * which have precise timing information.  The idea is that
 * the {@link RecordedInputEvent}s can be replayed later
 * via the {@link Sequencer}.  This listener can delegate to
 * another listener, e.g., an actual synthesizer, allowing
 * recording of live playing.
 */
public class RecordInputEventsListener implements InputEventListener {
	private Sequencer seq;
	private List<RecordedInputEvent> recordedEvents;
	private InputEventListener delegate;
	
	/**
	 * Constructor.
	 * 
	 * @param seq the {@link Sequencer} which will provide the timing information
	 */
	public RecordInputEventsListener(Sequencer seq) {
		this.seq = seq;
		this.recordedEvents = new ArrayList<>();
	}
	
	/**
	 * Set the {@link InputEventListener} to which events should be
	 * delegated.
	 * 
	 * @param delegate the delegate
	 */
	public void setDelegate(InputEventListener delegate) {
		this.delegate = delegate;
	}

	@Override
	public void onInputEvent(InputEvent inputEvent) {
		long nano = System.nanoTime();
		SequencerBeat seqBeat = seq.getSequencerBeat();
		if (seqBeat != null) {
			long nanoOffset = nano - seqBeat.getNano();
			recordedEvents.add(new RecordedInputEvent(inputEvent, seqBeat.getBeat(), nanoOffset));
		}
		if (delegate != null) {
			delegate.onInputEvent(inputEvent);
		}
	}

	/**
	 * Get all {@link RecordedInputEvent}s.
	 * 
	 * @return the list of {@link RecordedInputEvent}s
	 */
	public List<RecordedInputEvent> getRecordedEvents() {
		return recordedEvents;
	}
}
