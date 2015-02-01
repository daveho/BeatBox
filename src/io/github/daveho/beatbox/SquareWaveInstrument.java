package io.github.daveho.beatbox;

import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.data.Pitch;
import net.beadsproject.beads.ugens.Static;
import net.beadsproject.beads.ugens.WavePlayer;

public class SquareWaveInstrument extends AbstractInstrument {
	private Static freqValue;
	
	public SquareWaveInstrument(Sequencer seq, int trackIndex) {
		super(new WavePlayer(seq.getDesk().getAc(), new Static(seq.getDesk().getAc(), 0f), Buffer.SQUARE));
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
