package io.github.daveho.beatbox;

/**
 * Listener for {@link InputEvent}s.
 */
public interface InputEventListener {
	/**
	 * Called when an {@link InputEvent} is received.
	 * 
	 * @param inputEvent the received {@link InputEvent}
	 */
	public void onInputEvent(InputEvent inputEvent);
	
	/**
	 * Called when no further events are forthcoming.
	 */
	public void close();
}
