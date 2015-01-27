package io.github.daveho.beatbox;

import net.beadsproject.beads.core.Bead;
import net.beadsproject.beads.ugens.DelayTrigger;

public class PlayRecordedInputEvent implements SequencerEvent {
	private RecordedInputEvent recordedEvent;
	private InputEventListener sink;
	
	public PlayRecordedInputEvent(RecordedInputEvent recordedEvent, InputEventListener sink) {
		this.recordedEvent = recordedEvent;
		this.sink = sink;
	}

	@Override
	public void onAdd(int trackIndex, int beat, Sequencer seq) {
		// Nothing to do
	}

	@Override
	public void fire(Sequencer seq, int trackIndex) {
		System.out.println("Correct beat for playing recorded input event");
		
		// Compute delay offset in milliseconds
		double delayMs = recordedEvent.getNanoOffset() / 1000000.0;
		System.out.println("  schedule event to play in " + delayMs + " milliseconds");
		
		// Schedule the event to be fired
		DelayTrigger trigger = new DelayTrigger(seq.getDesk().getAc(), delayMs, new Bead(){
			@Override
			protected void messageReceived(Bead message) {
				System.out.println("Play recorded event");
				sink.onInputEvent(recordedEvent);
			}
		});
		seq.getDesk().getAc().out.addDependent(trigger);
	}

}
