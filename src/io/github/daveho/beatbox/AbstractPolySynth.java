package io.github.daveho.beatbox;

import io.github.daveho.gervill4beads.Midi;

import java.util.Arrays;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;

import net.beadsproject.beads.core.Bead;
import net.beadsproject.beads.core.UGen;

/**
 * Abstract base class for polysynths that receive input events and
 * play notes in response.  Each in-progress note is represented
 * by a {@live PlayLive} object.
 */
public abstract class AbstractPolySynth extends Bead {
	protected final Sequencer seq;
	protected final int trackIndex;
	protected final int maxPoly;
	protected final UGen[] instruments;
	protected final int[] notes;
	
	/**
	 * Constructor.
	 * 
	 * @param seq         the {@link Sequencer}
	 * @param trackIndex  index of track to which audio output should be sent
	 * @param maxPoly     maximum degree of polyphony
	 */
	public AbstractPolySynth(Sequencer seq, int trackIndex, int maxPoly) {
		this.seq = seq;
		this.trackIndex = trackIndex;
		this.maxPoly = maxPoly;
		//this.noteMap = new HashMap<>();
		this.instruments = new UGen[maxPoly];
		this.notes = new int[maxPoly];
		Arrays.fill(this.notes, -1);
	}
	
	@Override
	protected void messageReceived(Bead message) {
		
		// TODO: when selecting an "available" instrument,
		// we should select the least-recently-used one, in case
		// an instrument is playing the end of a note following a
		// STATUS_KEY_DOWN event.
		
		if (Midi.hasMidiMessage(message)) {
			MidiMessage msg = Midi.getMidiMessage(message);
			if (msg.getStatus() == ShortMessage.NOTE_ON) {
				int index = findFree();
				if (index >= 0) {
					notes[index] = Midi.getNote(msg);
					// Notify the instrument of the message
					instruments[index].message(message);
					System.out.printf("Start playing note %d on instrument %d\n", notes[index], index);
				}
			} else if (msg.getStatus() == ShortMessage.NOTE_OFF) {
				int note = Midi.getNote(msg);
				int index = findPlaying(note);
				if (index >= 0) {
					// Notify the instrument of the message
					instruments[index].message(message);
					System.out.printf("Stop playing note %d on instrument %d\n", note, index);
					notes[index] = -1;
				}
			}
		}
	}

	private int findFree() {
		// FIXME: need to implement an LRU scheme
		for (int i = 0; i < notes.length; i++) {
			if (notes[i] < 0) {
				return i;
			}
		}
		return -1;
	}

	private int findPlaying(int note) {
		for (int i = 0; i < notes.length; i++) {
			if (notes[i] == note) {
				return i;
			}
		}
		return -1;
	}
}
