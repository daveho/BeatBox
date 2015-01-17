package org.cloudcoder.beatbox;

import static org.cloudcoder.beatbox.EventGroup.group;

public class BeatBox extends Player {
	static final int BPM = 16;

	private PlaySampleEvent kick;
	private PlaySampleEvent hihat1;
	private PlaySampleEvent hihat2;
	private PlaySampleEvent snare1;
	
	public BeatBox() {
		super(BPM);
		Samples.loadAll();
		kick = new PlaySampleEvent(Samples.KICK_1, 0.4f);
		hihat1 = new PlaySampleEvent(Samples.HIHAT_1, 0.3f);
		hihat2 = new PlaySampleEvent(Samples.HIHAT_2, 0.3f);
		snare1 = new PlaySampleEvent(Samples.SNARE_1, 0.5f);
	}
	
	private EventGroup kicks() {
		return group(
				0, kick,
				2, kick,
				8, kick
				);
	}
	
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
	
	private int addRhythm(int m) {
		int start = m*BPM;
		seq.atBeats(start, 12, BPM, kicks());
		seq.atBeats(start+4*BPM, 8, BPM, hihats());
		seq.atBeats(start+4*BPM, 2, BPM, snare1());
		seq.atBeats(start+6*BPM, 2, BPM, snare2());
		seq.atBeats(start+8*BPM, 2, BPM, snare1());
		seq.atBeats(start+10*BPM, 2, BPM, snare2());
		return m+12;
	}
	
	private int addBasicKicks(int m) {
		int start = m*BPM;
		seq.atBeats(start+0, 4, BPM, kicks2());
		return m+4;
	}
	
	private int addRhythm3(int m) {
		int start = m*BPM;
		seq.atBeats(start, 12, BPM, kicks());
		seq.atBeats(start+4*BPM, 8, BPM, hihats2());
		seq.atBeats(start+4*BPM, 2, BPM, snare1());
		seq.atBeats(start+6*BPM, 2, BPM, snare2());
		seq.atBeats(start+8*BPM, 2, BPM, snare1());
		seq.atBeats(start+10*BPM, 2, BPM, snare2());
		return m+12;
	}
	
	public static void main(String[] args) {
		BeatBox beatBox = new BeatBox();

		int m = 0;
		
		m = beatBox.addBasicKicks(m);
		m = beatBox.addRhythm(m);
		m = beatBox.addBasicKicks(m);
		m = beatBox.addRhythm3(m);
		
		beatBox.play();
	}
}
