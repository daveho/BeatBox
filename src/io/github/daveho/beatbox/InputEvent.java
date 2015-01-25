package io.github.daveho.beatbox;

/**
 * An input event.  Intended as a simpler and more meaningful abstraction
 * of MidiMessage.
 */
public class InputEvent {
	private final InputEventType type;
	private final int note;
	private final int velocity;
	
	/**
	 * Constructor.
	 * 
	 * @param type      the {@link InputEventType}
	 * @param note      the midi note
	 * @param velocity  the velocity
	 */
	public InputEvent(InputEventType type, int note, int velocity) {
		this.type = type;
		this.note = note;
		this.velocity = velocity;
	}
	
	/**
	 * @return the {@link InputEventType}
	 */
	public InputEventType getType() {
		return type;
	}
	
	/**
	 * @return the midi note
	 */
	public int getNote() {
		return note;
	}
	
	/**
	 * @return the velocity
	 */
	public int getVelocity() {
		return velocity;
	}
}
