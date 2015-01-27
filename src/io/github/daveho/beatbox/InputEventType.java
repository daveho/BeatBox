package io.github.daveho.beatbox;

/**
 * Types of input events.
 */
public enum InputEventType {
	/** Key down (i.e., played). */
	KEY_DOWN,
	/** Key up (i.e., released), */
	KEY_UP,;

	/**
	 * Convert an ordinal to an {@link InputEventType} value.
	 * 
	 * @param i the ordinal
	 * @return the {@link InputEventType} value
	 */
	public static InputEventType forOrdinal(int i) {
		return values()[i];
	}
}
