package io.github.daveho.beatbox;

import net.beadsproject.beads.data.Pitch;

public class TriangleWavePolySynth extends AbstractPolySynth<PlayLiveTriangleWave> {
	public TriangleWavePolySynth(Sequencer seq, float maxGain) {
		super(seq, maxGain);
	}

	@Override
	protected PlayLiveTriangleWave startNote(int note, int velocity) {
		float freq = Pitch.mtof(note);
		float gain = (velocity/128.0f) * maxGain;
		PlayLiveTriangleWave player = new PlayLiveTriangleWave(seq.getDesk().getAc(), freq, gain, seq.getDesk().getTrack(0));
		player.start();
		return player;
	}

	@Override
	protected void endNote(int note, PlayLiveTriangleWave player) {
		player.stop();
	}

}
