package io.github.daveho.beatbox;

import javax.sound.midi.MidiMessage;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.Bead;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.core.UGenChain;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.data.Pitch;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Static;
import net.beadsproject.beads.ugens.WavePlayer;

public class SquareWaveInstrument extends UGenChain {

	private Gain out;
	private Static freq;
	private Builder b;

	public SquareWaveInstrument(AudioContext ac, float gain) {
		super(ac, 1, 1);
		
		this.out = new Gain(ac, 1, gain);
		
		this.freq = new Static(ac, 0f);
		
		b = Builder.build(ac, out)
				.prepend(new LinearRise(ac, 120.0f)).label("rise")
				.prepend(new Static(ac, 0f)).label("noteGain")
				.prepend(new WavePlayer(ac, freq, Buffer.SQUARE)).label("waveplayer");
		
		addToChainOutput(out);
		UGen uGen = b.get("waveplayer");
		if (uGen == null) {
			throw new IllegalStateException("WTF?");
		}
		drawFromChainInput(uGen);
	}
	
	@Override
	protected void messageReceived(Bead message) {
		if (Midi.hasMidiMessage(message)) {
			MidiMessage msg = Midi.getMidiMessage(message);
			
			if (msg.getStatus() == Midi.STATUS_KEY_DOWN) {
				// Start playing
				float noteGain = Midi.getVelocity(msg) / 128.0f;
				b.get("noteGain").setValue(noteGain);
				freq.setValue(Pitch.mtof(Midi.getNote(msg)));
			} else if (msg.getStatus() == Midi.STATUS_KEY_UP) {
				// Stop playing
				// TODO: could set a trigger for a dynamic fade, for now just stop abruptly
				freq.setValue(0f);
			}
		}
	}

}
