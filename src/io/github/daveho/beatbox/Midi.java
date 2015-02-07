package io.github.daveho.beatbox;

import javax.sound.midi.MidiMessage;

import net.beadsproject.beads.core.Bead;

/**
 * Helper methods for working with midi messages.
 */
public class Midi {
	public static int getNote(MidiMessage msg) {
		return msg.getMessage()[1];
	}
	
	public static int getVelocity(MidiMessage msg) {
		return msg.getMessage()[2];
	}

	public static boolean hasMidiMessage(Bead bead) {
		return bead instanceof MidiMessageSource;
	}

	public static MidiMessage getMidiMessage(Bead message) {
		return ((MidiMessageSource)message).getMessage();
	}

	public static long getMidiTimestamp(Bead message) {
		return ((MidiMessageSource)message).getTimeStamp();
	}
}
