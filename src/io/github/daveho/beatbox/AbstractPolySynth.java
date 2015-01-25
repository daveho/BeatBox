package io.github.daveho.beatbox;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract base class for polysynths that receive input events and
 * play notes in response.  Each in-progress note is represented
 * by a {@live PlayLive} object.
 */
public abstract class AbstractPolySynth implements InputEventListener {
	protected final Sequencer seq;
	private final Map<Integer, PlayLive> noteMap;
	
	public AbstractPolySynth(Sequencer seq) {
		this.seq = seq;
		this.noteMap = new HashMap<>();
	}

	@Override
	public void onInputEvent(InputEvent inputEvent) {
		PlayLive player;
		
		switch (inputEvent.getType()) {
		case KEY_DOWN:
			player = startNote(inputEvent.getNote(), inputEvent.getVelocity());
			noteMap.put(inputEvent.getNote(), player);
			break;
			
		case KEY_UP:
			player = noteMap.get(inputEvent.getNote());
			if (player != null) {
				player.stop();
				noteMap.remove(inputEvent.getNote());
			}
			break;
			
		default:
			System.out.println("Unknown input event type? " + inputEvent.getType());
		}
	}
	
	protected abstract PlayLive startNote(int note, int velocity);
	
	protected abstract void endNote(int note, PlayLive player);
	
	public void close() {
		for (PlayLive player : noteMap.values()) {
			player.stop();
		}
	}
}
