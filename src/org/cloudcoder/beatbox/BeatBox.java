package org.cloudcoder.beatbox;

import static org.cloudcoder.beatbox.EventGroup.group;

public class BeatBox extends Player {
	static final int BPM = 16;

	private PlaySampleEvent kick;
	private PlaySampleEvent hihat1;
	private PlaySampleEvent snare1;
	
	public BeatBox() {
		super(BPM);
		Samples.loadAll();
		kick = new PlaySampleEvent(Samples.KICK_1, 0.4f);
		hihat1 = new PlaySampleEvent(Samples.HIHAT_1, 0.3f);
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
	
	private EventGroup snare1() {
		return group(8, snare1, 12, snare1, 14, snare1);
	}
	
	private EventGroup snare2() {
		return group(8, snare1, 14, snare1);
	}
	
	private void addRhythm(int start) {
		start *= BPM;
		seq.atBeats(start+0, 12, BPM, kicks());
		seq.atBeats(start+4*BPM, 8, BPM, hihats());
		seq.atBeats(start+4*BPM, 2, BPM, snare1());
		seq.atBeats(start+6*BPM, 2, BPM, snare2());
		seq.atBeats(start+8*BPM, 2, BPM, snare1());
		seq.atBeats(start+10*BPM, 2, BPM, snare2());
	}
	
	private void addRhythm2(int start) {
		start *= BPM;
		seq.atBeats(start+0, 4, BPM, kicks2());
	}
	
	public static void main(String[] args) {
		BeatBox beatBox = new BeatBox();

		int m = 0;
		
		beatBox.addRhythm(0);
		beatBox.addRhythm2(12);
		beatBox.addRhythm(16);
		beatBox.addRhythm2(20);
		
		beatBox.play();
	}
}
