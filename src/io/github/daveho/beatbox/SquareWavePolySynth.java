package io.github.daveho.beatbox;

public class SquareWavePolySynth extends AbstractPolySynth {

	public SquareWavePolySynth(Sequencer seq, int trackIndex, int maxPoly) {
		super(seq, trackIndex, maxPoly);
		
		// Create instruments and connect to track output
		for (int i = 0; i < maxPoly; i++) {
			SquareWaveInstrument instr = new SquareWaveInstrument(seq.getDesk().getAc(), 0.05f);
			instruments[i] = instr; // FIXME: hard-coded gain
			seq.getDesk().getTrack(trackIndex).addInput(instr.getOut());
//			instruments[i].start();
		}
	}

}
