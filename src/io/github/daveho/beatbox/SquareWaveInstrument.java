package io.github.daveho.beatbox;

import javax.sound.midi.MidiMessage;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.Bead;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.data.Pitch;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Static;
import net.beadsproject.beads.ugens.WavePlayer;

public class SquareWaveInstrument extends Bead /*extends UGenChain*/ {

	private Gain out;
	private float gain;
	private Static freq;
	private Builder b;

	public SquareWaveInstrument(AudioContext ac, float gain) {
		this.out = new Gain(ac, 1);
		this.gain = gain;
		
		this.freq = new Static(ac, 0f);
		
		
		this.b = Builder.build(ac, out)
//				.prepend(new LinearRise(ac, 120.0f)).label("rise")
				.prepend(new NoteOnLinearFadeIn(ac, 1500.0f)).label("rise")
				.prepend(new Gain(ac, 1, 0f)).label("noteGain")
				.prepend(new WavePlayer(ac, freq, Buffer.SQUARE)).label("waveplayer");
		
//		System.out.println("ng.getValue() = " + ng.getValue());
		
//		out.pause(true);
	}
	
	/**
	 * Get the output UGen of this instrument.
	 * 
	 * @return the output UGen
	 */
	public UGen getOut() {
		return out;
	}
	
	@Override
	public void start() {
		for (UGen u : b.all()) {
			u.start();
		}
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
			System.out.println("noteGain is a " + noteGainUGen.getClass().getSimpleName());
			System.out.println("  Its value is " + noteGainUGen.getValue());
			
			if (msg.getStatus() == Midi.STATUS_KEY_DOWN) {
				// Enable output
				out.start();
				
				// Start playing
				float noteGain = (Midi.getVelocity(msg) / 128.0f) * gain;
//				System.out.println("Note gain is " + noteGain);
				((Gain)noteGainUGen).setGain(noteGain);
				freq.setValue(Pitch.mtof(Midi.getNote(msg)));
			} else if (msg.getStatus() == Midi.STATUS_KEY_UP) {
				// Stop playing
				// TODO: could set a trigger for a dynamic fade, for now just stop abruptly
				freq.setValue(0f);
				noteGainUGen.setValue(0f);
			}
		}
	}
}
