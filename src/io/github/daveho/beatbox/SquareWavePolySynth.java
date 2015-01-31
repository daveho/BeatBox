package io.github.daveho.beatbox;

import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.Pitch;

public class SquareWavePolySynth extends AbstractPolySynth {
	public SquareWavePolySynth(Sequencer seq, float maxGain, int trackIndex) {
		super(seq, maxGain, trackIndex);
	}

	@Override
	protected PlayLive startNote(int note, int velocity) {
		UGen playSquareWave = Builder.build(seq.getDesk().getAc(), seq.getDesk().getTrack(trackIndex))
			.withGain((velocity/128.0f) * maxGain)
			.playSquareWave(Pitch.mtof(note))
			.getUgen();
		return new PlayLiveUGen(playSquareWave);
	}

	@Override
	protected void endNote(int note, PlayLive player) {
		player.stop();
	}

}
