package io.github.daveho.beatbox;

import net.beadsproject.beads.core.Bead;

public class SquareWavePolySynth extends AbstractPolySynth {

	public SquareWavePolySynth(Sequencer seq, int trackIndex, int maxPoly) {
		super(seq, trackIndex, maxPoly);
		
		// Create instruments and connect to track output
		for (int i = 0; i < maxPoly; i++) {
			SquareWaveInstrument instr = new SquareWaveInstrument(seq.getDesk().getAc(), 0.1f); // FIXME: hard-coded gain
			instruments[i] = instr;
			seq.getDesk().getTrack(trackIndex).addInput(instr.getOut());
		}
//		seq.addStartupHook(new Runnable() {
//			@Override
//			public void run() {
//				System.out.println("Running startup hook!");
//				for (Bead instr : instruments) {
//					SquareWaveInstrument sq = (SquareWaveInstrument)instr;
//					seq.getDesk().getTrack(trackIndex).addInput(sq.getOut());
//				}
//			}
//		});
	}

}
