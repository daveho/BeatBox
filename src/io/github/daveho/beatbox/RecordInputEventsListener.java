package io.github.daveho.beatbox;

import java.util.ArrayList;
import java.util.List;

public class RecordInputEventsListener implements InputEventListener {
	private Sequencer seq;
	private List<RecordedInputEvent> recordedEvents;
	private InputEventListener delegate;
	
	public RecordInputEventsListener(Sequencer seq) {
		this.seq = seq;
		this.recordedEvents = new ArrayList<>();
	}
	
	public void setDelegate(InputEventListener delegate) {
		this.delegate = delegate;
	}

	@Override
	public void onInputEvent(InputEvent inputEvent) {
		long nano = System.nanoTime();
		SequencerBeat seqBeat = seq.getSequencerBeat();
		long nanoOffset = nano - seqBeat.getNano();
		recordedEvents.add(new RecordedInputEvent(inputEvent, seqBeat.getBeat(), nanoOffset));
		if (delegate != null) {
			delegate.onInputEvent(inputEvent);
		}
	}

	public List<RecordedInputEvent> getRecordedEvents() {
		return recordedEvents;
	}
}
