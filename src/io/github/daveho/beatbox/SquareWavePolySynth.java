package io.github.daveho.beatbox;

import net.beadsproject.beads.data.Pitch;

public class SquareWavePolySynth extends AbstractPolySynth {
	public SquareWavePolySynth(Sequencer seq) {
		super(seq);
	}

	@Override
	protected PlayLive startNote(int note, int velocity) {
		float freq = Pitch.mtof(note);
		float gain = (velocity/128.0f) * 0.15f;
		PlayLive player = new PlayLiveSquareWave(seq.getDesk().getAc(), freq, gain, seq.getDesk().getTrack(0));
		player.start();
		return player;
	}

	@Override
	protected void endNote(int note, PlayLive player) {
		player.stop();
	}

}
