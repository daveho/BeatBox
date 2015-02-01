package io.github.daveho.beatbox;


public class TriangleWavePolySynth extends AbstractPolySynth {
	public TriangleWavePolySynth(Sequencer seq, float maxGain, int trackIndex, int maxPoly) {
		super(seq, maxGain, trackIndex, maxPoly);
		for (int i = 0; i < maxPoly; i++) {
			instruments[i] = new TriangleWaveInstrument(seq, trackIndex);
		}
	}

	@Override
	public void close() {
		for (Instrument instrument : instruments) {
			instrument.close();
		}
	}
}
