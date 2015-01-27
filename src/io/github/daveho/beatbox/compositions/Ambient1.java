package io.github.daveho.beatbox.compositions;

import static io.github.daveho.beatbox.EventGroup.group;

import javax.sound.midi.MidiUnavailableException;

import io.github.daveho.beatbox.EventGroup;
import io.github.daveho.beatbox.PlaySampleEvent;
import io.github.daveho.beatbox.Player;
import io.github.daveho.beatbox.SampleBank;
import io.github.daveho.beatbox.SquareWavePolySynth;

public class Ambient1 extends Player {
	static final int BPM = 16;
	static final float MEASURE_LEN_MS = 1100.0f;
	static final int NUM_TRACKS = 1;

	// Samples
	static PlaySampleEvent s_hihat2 = new PlaySampleEvent(Samples.HIHAT_2, 0.2f);
	static PlaySampleEvent s_hihat3 = new PlaySampleEvent(Samples.HIHAT_3, 0.2f);
	
	// Measure patterns
	static EventGroup g_basicHihats; // actually a double-measure pattern
	
	public Ambient1() {
		super(BPM, MEASURE_LEN_MS, NUM_TRACKS);
		SampleBank.preload(Samples.class);

		addReverbToTrack(0, .7f, .5f, .1f);
		
		g_basicHihats = group(0, s_hihat3, 8, s_hihat3, 16, s_hihat2, 24, s_hihat3);
	}

	int addBasicRhythm(int m) {
		for (; m < 8; m++) {
			seq.atBeat(0, m*BPM*2, g_basicHihats);
		}
		return 16;
	}
	
	public static void main(String[] args) throws MidiUnavailableException {
		Ambient1 composition = new Ambient1();
		int m = 0;
		m = composition.addBasicRhythm(m);

		SquareWavePolySynth synth = new SquareWavePolySynth(composition.seq);
		composition.liveSynth(synth, true);

		composition.play();
	}
}
