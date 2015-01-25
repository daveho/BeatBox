package io.github.daveho.beatbox;

import net.beadsproject.beads.data.Pitch;

public class SquareWavePolySynth extends AbstractPolySynth<PlayLiveSquareWave> {
	public SquareWavePolySynth(Sequencer seq) {
		super(seq);
	}

	@Override
	protected PlayLiveSquareWave startNote(int note, int velocity) {
		float freq = Pitch.mtof(note);
		float gain = (velocity/128.0f) * 0.15f;
		PlayLiveSquareWave player = new PlayLiveSquareWave(seq.getDesk().getAc(), freq, gain, seq.getDesk().getTrack(0));
		player.start();
		return player;
	}

	@Override
	protected void endNote(int note, PlayLiveSquareWave player) {
		player.stop();
	}

}
