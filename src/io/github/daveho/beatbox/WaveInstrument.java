package io.github.daveho.beatbox;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.Bead;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.core.UGenChain;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.data.Pitch;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Static;
import net.beadsproject.beads.ugens.WavePlayer;

/**
 * An instrument which listens for MidiMessages and plays arbitrary
 * waves in response.
 */
public class WaveInstrument extends UGenChain {

	protected Gain out;
	protected float gain;
	protected Static freq;
	protected Builder b;

	/**
	 * Constructor.
	 * 
	 * @param ac    the AudioContext
	 * @param buf   the buffer type (e.g., Buffer.SQUARE)
	 * @param gain  the static gain
	 */
	public WaveInstrument(AudioContext ac, Buffer buf, float gain) {
		super(ac, 1, 1);
		this.out = new Gain(ac, 1);
		this.gain = gain;
		
		this.freq = new Static(ac, 0f);
		
		this.b = Builder.build(ac, out)
				.prepend(new NoteOnLinearFadeIn(ac, 120.0f)).label("rise")
				.prepend(new Gain(ac, 1, 0f)).label("noteGain")
				.prepend(new WavePlayer(ac, freq, buf)).label("waveplayer");
		
		addToChainOutput(out);
	}

	@Override
	protected void messageReceived(Bead message) {
			if (Midi.hasMidiMessage(message)) {
				// Broadcast the message to the other UGens in the signal chain
				for (UGen ugen : b.all()) {
					System.out.println("Notify " + ugen.getClass().getSimpleName());
					ugen.message(message);
				}
				
				MidiMessage msg = Midi.getMidiMessage(message);
				
				UGen noteGainUGen = b.get("noteGain");
	//			System.out.println("noteGain is a " + noteGainUGen.getClass().getSimpleName());
	//			System.out.println("  Its value is " + noteGainUGen.getValue());
				
				if (msg.getStatus() == ShortMessage.NOTE_ON) {
					// Start playing
					float noteGain = (Midi.getVelocity(msg) / 128.0f) * gain;
	//				System.out.println("Note gain is " + noteGain);
					((Gain)noteGainUGen).setGain(noteGain);
					freq.setValue(Pitch.mtof(Midi.getNote(msg)));
				} else if (msg.getStatus() == ShortMessage.NOTE_OFF) {
					// Stop playing
					// TODO: could set a trigger for a dynamic fade, for now just stop abruptly
					freq.setValue(0f);
					noteGainUGen.setValue(0f);
				}
			}
		}

}