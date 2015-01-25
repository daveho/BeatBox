package io.github.daveho.beatbox;

/**
 * A recorded {@link InputEvent}: specifies a beat number and
 * a nanosecond offset within the beat.  Intended to allow
 * storage and replay of {@link InputEvent}s.
 */
public class RecordedInputEvent extends InputEvent {
	private final int beat;
	private final long nanoOffset;
	
	/**
	 * Constructor.
	 * 
	 * @param inputEvent an {@link InputEvent} whose information will be copied
	 * @param beat       the beat number
	 * @param nanoOffset nanosecond offset within the beat
	 */
	public RecordedInputEvent(InputEvent inputEvent, int beat, long nanoOffset) {
		super(inputEvent.getType(), inputEvent.getNote(), inputEvent.getVelocity());
		this.beat = beat;
		this.nanoOffset = nanoOffset;
	}

	/**
	 * @return the beat number
	 */
	public int getBeat() {
		return beat;
	}
	
	/**
	 * @return nanosecond offset within the beat
	 */
	public long getNanoOffset() {
		return nanoOffset;
	}
	
	public String toString() {
		return String.format("{%d, %d, %d, %d, %d}", getType().ordinal(), getNote(), getVelocity(), getBeat(), getNanoOffset());
	}
}
