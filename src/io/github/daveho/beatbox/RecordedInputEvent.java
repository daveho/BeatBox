package io.github.daveho.beatbox;

public class RecordedInputEvent extends InputEvent {
	private final int beat;
	private final long nanoOffset;
	
	public RecordedInputEvent(InputEvent inputEvent, int beat, long nanoOffset) {
		super(inputEvent.getType(), inputEvent.getNote(), inputEvent.getVelocity());
		this.beat = beat;
		this.nanoOffset = nanoOffset;
	}

	public int getBeat() {
		return beat;
	}
	
	public long getNanoOffset() {
		return nanoOffset;
	}
	
	public String toString() {
		return String.format("{%d, %d, %d, %d, %d}", getType().ordinal(), getNote(), getVelocity(), getBeat(), getNanoOffset());
	}
}
