package io.github.daveho.beatbox;

import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.Pitch;

public class TriangleWavePolySynth extends AbstractPolySynth {
	public TriangleWavePolySynth(Sequencer seq, float maxGain, int trackIndex) {
		super(seq, maxGain, trackIndex);
	}

	@Override
	protected PlayLive startNote(int note, int velocity) {
		UGen playTriangleWave = Builder.build(seq.getDesk().getAc(), seq.getDesk().getTrack(trackIndex))
			.withLinearRise(100.0f)
			.withGain((velocity/128.0f) * maxGain)
			.playTriangleWave(Pitch.mtof(note))
			.getUgen();
		return new PlayLiveUGen(playTriangleWave);
	}

	@Override
	protected void endNote(int note, PlayLive player) {
		player.stop();
	}

}
