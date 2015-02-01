package io.github.daveho.beatbox;

import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.data.Pitch;
import net.beadsproject.beads.ugens.Static;
import net.beadsproject.beads.ugens.WavePlayer;

public class TriangleWaveInstrument extends AbstractInstrument {
	private Static freqValue;
	
	public TriangleWaveInstrument(Sequencer seq, int trackIndex) {
		super(new WavePlayer(seq.getDesk().getAc(), new Static(seq.getDesk().getAc(), 0f), Buffer.TRIANGLE));
		freqValue = (Static) ((WavePlayer)ugen).getFrequencyUGen();
	}
	
	@Override
	public void on() {
		super.on();
		float note = getParam(ParamType.NOTE);
		freqValue.setValue(Pitch.mtof(note));
	}

	@Override
	public void off() {
		super.off();
		freqValue.setValue(0f);
	}

}
