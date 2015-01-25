package io.github.daveho.beatbox;

public class InputEvent {
	private final InputEventType type;
	private final int note;
	private final int velocity;
	
	public InputEvent(InputEventType type, int note, int velocity) {
		this.type = type;
		this.note = note;
		this.velocity = velocity;
	}
	
	public InputEventType getType() {
		return type;
	}
	
	public int getNote() {
		return note;
	}
	
	public int getVelocity() {
		return velocity;
	}
}
