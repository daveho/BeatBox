package io.github.daveho.beatbox;

import net.beadsproject.beads.data.Pitch;

public class SquareWavePolySynth extends AbstractPolySynth<PlayLiveSquareWave> {
	public SquareWavePolySynth(Sequencer seq, float maxGain) {
		super(seq, maxGain);
	}

	@Override
	protected PlayLiveSquareWave startNote(int note, int velocity) {
		float freq = Pitch.mtof(note);
		float gain = (velocity/128.0f) * maxGain;
		PlayLiveSquareWave player = new PlayLiveSquareWave(seq.getDesk().getAc(), freq, gain, seq.getDesk().getTrack(0));
		player.start();
		return player;
	}

	@Override
	protected void endNote(int note, PlayLiveSquareWave player) {
		player.stop();
	}

}
