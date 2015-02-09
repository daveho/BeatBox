package io.github.daveho.beatbox.compositions;

import static io.github.daveho.beatbox.EventGroup.group;
import io.github.daveho.beatbox.EventGroup;
import io.github.daveho.beatbox.PlaySampleEvent;
import io.github.daveho.beatbox.Player;
import io.github.daveho.beatbox.SampleBank;
import io.github.daveho.gervill4beads.GervillUGen;

import java.util.Collections;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

public class Ambient1 extends Player {
	static final int BPM = 12;
	static final float MEASURE_LEN_MS = 1100.0f;
	static final int NUM_TRACKS = 1;

	// Samples
	static PlaySampleEvent s_hihat2 = new PlaySampleEvent(Samples.HIHAT_2, 0.2f);
	static PlaySampleEvent s_hihat3 = new PlaySampleEvent(Samples.HIHAT_3, 0.2f);
	static PlaySampleEvent s_kick = new PlaySampleEvent(Samples.KICK_1, 0.2f);
	
	// Measure patterns
	static EventGroup g_basicHihats; // actually a double-measure pattern
	
	public Ambient1() {
		super(BPM, MEASURE_LEN_MS, NUM_TRACKS);
		SampleBank.preload(Samples.class);

		addReverbToTrack(0, .7f, .5f, .1f);
		
		g_basicHihats = group(
				0, s_hihat3,
				2, s_hihat3,
				4, s_hihat3,
				6, s_hihat3,
				8, s_hihat3,
				10, s_hihat3,
				12, s_hihat2,
				14, s_hihat3,
				16, s_hihat2,
				18, s_hihat3,
				20, s_hihat3,
				22, s_hihat3
		);
	}

	int addBasicRhythm(int m) {
		for (; m < 24; m++) {
			seq.atBeat(0, m*BPM*2, g_basicHihats);
		}
		return 16;
	}
	
	public static void main(String[] args) throws MidiUnavailableException, InvalidMidiDataException {
		Ambient1 composition = new Ambient1();
		int m = 0;
		m = composition.addBasicRhythm(m);

		/*
		SquareWavePolySynth synth = new SquareWavePolySynth(composition.seq, 0, 10);
		composition.liveSynth(synth, true);
		*/
		
		GervillUGen synth = new GervillUGen(composition.ac, Collections.emptyMap());
		
		
		composition.liveGervillSynth(synth, 78);
		
		/*
		TriangleWavePolySynth synth = new TriangleWavePolySynth(composition.seq, 0.1f, 0, 10);
		PlayBackInputEvents playback = new PlayBackInputEvents(ominousIntro);
		playback.addAll(composition.seq, 0, 0, synth);
		 */
		
		composition.play();
	}
}
