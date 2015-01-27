package io.github.daveho.beatbox;

import java.util.ArrayList;
import java.util.List;

public class PlayBackInputEvents {
	private List<RecordedInputEvent> recordedEvents;
	
	public PlayBackInputEvents(long[][] record) {
		this.recordedEvents = new ArrayList<>();
		
		for (long[] data : record) {
			RecordedInputEvent recordedEvent = new RecordedInputEvent(
					new InputEvent(InputEventType.forOrdinal((int)data[0]), (int)data[1], (int)data[2]),
					(int)data[3], data[4]);
			recordedEvents.add(recordedEvent);
		}
	}
	
	public void addAll(Sequencer seq, int trackIndex, int startBeat, InputEventListener sink) {
		for (RecordedInputEvent evt : recordedEvents) {
			seq.atBeat(trackIndex, startBeat + evt.getBeat(), new PlayRecordedInputEvent(evt, sink));
		}
	}
}
