package io.github.daveho.beatbox;

import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.Pitch;

public class SquareWavePolySynth extends AbstractPolySynth {
	public SquareWavePolySynth(Sequencer seq, float maxGain, int trackIndex, int maxPoly) {
		super(seq, maxGain, trackIndex, maxPoly);
		for (int i = 0; i < maxPoly; i++) {
			instruments[i] = new SquareWaveInstrument(seq, trackIndex);
		}
	}

	@Override
	public void close() {
		for (Instrument instrument : instruments) {
			instrument.close();
		}
	}
}
