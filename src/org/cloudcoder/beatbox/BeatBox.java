package org.cloudcoder.beatbox;

import static org.cloudcoder.beatbox.EventGroup.group;

public class BeatBox extends Player {
	static final int BPM = 16;
	static final float MEASURE_LEN_MS = 1100.0f;
	static final float BEAT_LEN_MS = MEASURE_LEN_MS / BPM;
	static final int NUM_TRACKS = 1;

	private PlaySampleEvent kick;
	private PlaySampleEvent kick2;
	private PlaySampleEvent hihat1;
	private PlaySampleEvent hihat2;
	private PlaySampleEvent snare1;
	private PlaySampleEvent tom1;
	private PlaySampleEvent clap1;
	private PlaySampleEvent boing1;
	private PlaySampleEvent cowbell1;
	private PlaySampleEvent cowbell2;
	private PlaySampleEvent cowbell2_loud;
	
	public BeatBox() {
		super(BPM, MEASURE_LEN_MS, NUM_TRACKS);
		Samples.loadAll();
		kick = new PlaySampleEvent(Samples.KICK_1, 0.4f);
		kick2 = new PlaySampleEvent(Samples.KICK_2, 0.4f);
		hihat1 = new PlaySampleEvent(Samples.HIHAT_1, 0.3f);
		hihat2 = new PlaySampleEvent(Samples.HIHAT_2, 0.3f);
		snare1 = new PlaySampleEvent(Samples.SNARE_1, 0.5f);
		tom1 = new PlaySampleEvent(Samples.TOM_1, 0.3f);
		clap1 = new PlaySampleEvent(Samples.CLAP_1, 0.4f);
		boing1 = new PlaySampleEvent(Samples.BOING_1, 0.4f);
		cowbell1 = new PlaySampleEvent(Samples.COWBELL_1, 0.15f);
		cowbell2 = new PlaySampleEvent(Samples.COWBELL_2, 0.25f);
		cowbell2_loud = new PlaySampleEvent(Samples.COWBELL_2, 0.9f);
	}
	
	// paired kicks
	private EventGroup kicks() {
		return group(
				0, kick2,
				2, kick2,
				8, kick
				);
	}
	
	// basic kicks
	private EventGroup kicks2() {
		return group(
				0, kick,
				8, kick
				);
	}
	
	private EventGroup hihats() {
		return group(
				0, hihat1,
				2, hihat1,
				4, hihat1,
				6, hihat1,
				8, hihat1,
				10, hihat1,
				12, hihat1,
				14, hihat1
				);
	}
	
	private EventGroup hihats2() {
		return group(
				0, hihat1,
				2, hihat1,
				4, hihat2,
				6, hihat2,
				8, hihat1,
				10, hihat2,
				12, hihat1,
				14, hihat2
				);
	}
	
	private EventGroup snare1() {
		return group(8, snare1, 12, snare1, 14, snare1);
	}
	
	private EventGroup snare2() {
		return group(8, snare1, 14, snare1);
	}
	
	private EventGroup claps1() {
		return group(12, clap1, 14, clap1);
	}
	
//	private EventGroup claps2() {
//		return group(10, clap1, 16, clap1);
//	}
	
	private EventGroup boings1() {
		return group(0, boing1);
	}
	
	private EventGroup cowbells1() {
		return group(8, cowbell1, /*10, cowbell1,*/ 12, cowbell2);
	}
	
	private EventGroup cowbells2() {
		return group(4, cowbell2, 12, cowbell2_loud);
	}
	
	private SequencerEvent oneBeatSquareWave(float freq) {
		return new PlaySquareWaveEvent(BEAT_LEN_MS, freq, 0.1f);
	}
	
	private int addRhythm(int m) {
		int start = m*BPM;
		seq.atBeats(0, start, 8, BPM, kicks());
		seq.atBeats(0, start+0*BPM, 8, BPM, hihats());
		seq.atBeats(0, start+0*BPM, 2, BPM, snare1());
		seq.atBeats(0, start+2*BPM, 2, BPM, snare2());
		seq.atBeats(0, start+4*BPM, 2, BPM, snare1());
		seq.atBeats(0, start+6*BPM, 2, BPM, snare2());
		return m+8;
	}
	
	private int addBasicKicks(int m) {
		int start = m*BPM;
		seq.atBeats(0, start+0, 4, BPM, kicks2());
		return m+4;
	}
	
	private int addPairedKicks(int m) {
		int start = m*BPM;
		seq.atBeats(0, start+0, 4, BPM, kicks());
		return m+4;
	}
	
	private int addPairedKicksAndBoings(int m) {
		int start = m*BPM;
		seq.atBeats(0, start+0, 4, BPM, kicks());
//		seq.atBeats(0, start+0, 4, BPM, boings1());
		seq.atBeat(0, start+0, boings1());
		return m+4;
	}
	
	private int addRhythm3(int m) {
		int start = m*BPM;
		seq.atBeats(0, start+0, 8, BPM, kicks());
		seq.atBeats(0, start+0*BPM, 8, BPM, hihats2());
		seq.atBeats(0, start+0*BPM, 2, BPM, snare1());
		seq.atBeats(0, start+2*BPM, 2, BPM, snare2());
		seq.atBeats(0, start+4*BPM, 2, BPM, snare1());
		seq.atBeats(0, start+6*BPM, 2, BPM, snare2());
		return m+8;
	}

	private int addBasicKicksAndClaps(int m) {
		int start = m*BPM;
		seq.atBeats(0, start+0, 4, BPM, kicks2());
		seq.atBeats(0, start+0, 4, BPM, claps1());
		return m+4;
	}

	private int addBasicKicksAndClapsWithCowbell(int m) {
		int start = m*BPM;
		seq.atBeats(0, start+0, 4, BPM, kicks2());
		seq.atBeats(0, start+0, 4, BPM, claps1());
		seq.atBeat(0, start+0, cowbells1());
		seq.atBeat(0, start+1*BPM, cowbells2());
		seq.atBeat(0, start+2*BPM, cowbells1());
		seq.atBeat(0, start+3*BPM, cowbells2());
		return m+4;
	}
	
	private int addSquareWavePattern(int m) {
		int start = m*BPM;
		seq.atBeats(0, start+0, 4, BPM, kicks2());
		seq.atBeats(0, start+4, 4, BPM, oneBeatSquareWave(440.0f));
		seq.atBeats(0, start+6, 4, BPM, oneBeatSquareWave(440.0f));
		seq.atBeats(0, start+12, 4, BPM, oneBeatSquareWave(440.0f));
		seq.atBeats(0, start+14, 4, BPM, oneBeatSquareWave(440.0f));
		return m+4;
	}
	
	public static void main(String[] args) {
		BeatBox beatBox = new BeatBox();

		int m = 0;
		
		m = beatBox.addBasicKicks(m);
		m = beatBox.addPairedKicks(m);
		m = beatBox.addRhythm(m);
		m = beatBox.addBasicKicksAndClaps(m);
		m = beatBox.addBasicKicksAndClapsWithCowbell(m);
		m = beatBox.addRhythm3(m);
		m = beatBox.addPairedKicksAndBoings(m);
		m = beatBox.addBasicKicksAndClaps(m);
		m = beatBox.addBasicKicksAndClapsWithCowbell(m);
		m = beatBox.addRhythm3(m);

//		beatBox.recordToFile("beats.wav");
		
		beatBox.play();
	}
}
