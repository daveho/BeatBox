package io.github.daveho.beatbox;

/**
 * Interface to be implemented by audio generators which
 * (potentially) play in response to real-time events,
 * e.g., {@link InputEvent}s.
 */
public interface PlayLive {
	/**
	 * Start playing.
	 */
	public void start();

	/**
	 * Stop playing.
	 */
	public void stop();

}