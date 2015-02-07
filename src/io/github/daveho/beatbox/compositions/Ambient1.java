package io.github.daveho.beatbox.compositions;

import static io.github.daveho.beatbox.EventGroup.group;

import java.util.Collections;

import io.github.daveho.beatbox.EventGroup;
import io.github.daveho.beatbox.PlaySampleEvent;
import io.github.daveho.beatbox.Player;
import io.github.daveho.beatbox.SampleBank;
import io.github.daveho.beatbox.SquareWavePolySynth;
import io.github.daveho.gervill4beads.GervillUGen;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

public class Ambient1 extends Player {
	static final int BPM = 16;
	static final float MEASURE_LEN_MS = 1100.0f;
	static final int NUM_TRACKS = 1;

	// Samples
	static PlaySampleEvent s_hihat2 = new PlaySampleEvent(Samples.HIHAT_2, 0.2f);
	static PlaySampleEvent s_hihat3 = new PlaySampleEvent(Samples.HIHAT_3, 0.2f);
	
	// Measure patterns
	static EventGroup g_basicHihats; // actually a double-measure pattern
	
	// Captured input event data
	static long[][] ominousIntro = {
		{0, 26, 74, 32, 29355378},
		{0, 38, 74, 32, 45553519},
		{0, 40, 91, 65, 23295691},
		{1, 38, 0, 66, 23279732},
		{0, 41, 91, 95, 29089030},
		{1, 40, 0, 96, 16775887},
		{0, 43, 91, 127, 34348064},
		{1, 41, 0, 128, 4167158},
		{1, 43, 0, 156, 55155879},
		{0, 36, 52, 159, 28460275},
		{0, 45, 45, 159, 37592082},
		{0, 38, 87, 224, 53176945},
		{1, 36, 0, 225, 10746510},
		{1, 26, 0, 278, 46210617},
		{1, 38, 0, 278, 53636110},
		{1, 45, 0, 278, 58567588},
	};
	
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
	
	public static void main(String[] args) throws MidiUnavailableException, InvalidMidiDataException {
		Ambient1 composition = new Ambient1();
		int m = 0;
		m = composition.addBasicRhythm(m);

		/*
		SquareWavePolySynth synth = new SquareWavePolySynth(composition.seq, 0, 10);
		composition.liveSynth(synth, true);
		*/
		
		GervillUGen synth = new GervillUGen(composition.ac, Collections.emptyMap());
		
		
		composition.liveGervillSynth(synth, 20);
		
		/*
		TriangleWavePolySynth synth = new TriangleWavePolySynth(composition.seq, 0.1f, 0, 10);
		PlayBackInputEvents playback = new PlayBackInputEvents(ominousIntro);
		playback.addAll(composition.seq, 0, 0, synth);
		 */
		
		composition.play();
	}
}
