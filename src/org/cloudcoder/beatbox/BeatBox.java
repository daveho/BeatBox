package org.cloudcoder.beatbox;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.Bead;
import net.beadsproject.beads.ugens.Clock;

import static org.cloudcoder.beatbox.EventGroup.group;

public class BeatBox {
	private static final int BPM = 16;

	private final class OnTick extends Bead {
		// We wait a bit before actually playing
		boolean playing = false;

		// Beat counter
		int beat = 0;

		@Override
		protected void messageReceived(Bead message) {
			// Avoid playing on the first few beats to avoid
			// noisy audio artifact.
			if (!playing) {
				playing = (beat >= BPM-1);
				if (playing) {
					// Time to start playing!  Reset beat counter.
					beat = 0;
				}
			}
			if (playing) {
				seq.tick(beat);
			}
			beat++;
		}
	}

	private AudioContext ac;
	private Sequencer seq;
	private PlaySampleEvent kick;
	private PlaySampleEvent hihat1;
	private PlaySampleEvent snare1;
	
	public BeatBox() {
		ac = new AudioContext();
		seq = new Sequencer(ac);
		Samples.loadAll();
		kick = new PlaySampleEvent(Samples.KICK_1, 0.4f);
		hihat1 = new PlaySampleEvent(Samples.HIHAT_1, 0.3f);
		snare1 = new PlaySampleEvent(Samples.SNARE_1, 0.5f);
	}
	
	private EventGroup kicks() {
		//return group(0, kick, BPM/2, kick);
		return group(0, kick, 2, kick, 8, kick);
	}
	
	private EventGroup hihats() {
		EventGroup g = new EventGroup();
		for (int i = 0; i < BPM; i += BPM/8) {
			g.addEvent(i, hihat1);
		}
		return g;
	}
	
	private EventGroup snare1() {
		return group(12, snare1, 16, snare1);
	}
	
	public void play() {
		Clock clock = new Clock(ac, 1000);
		Bead onTick = new OnTick();
		clock.addMessageListener(onTick);
		
		// Note that the Clock's class notion of "beat" is actually
		// what sequencer considers to mean "measure".
		clock.setTicksPerBeat(BPM);
		
		ac.out.addDependent(clock);
		
		ac.start();
	}
	
	public static void main(String[] args) {
		BeatBox beatBox = new BeatBox();
		
		//beatBox.kicksAndClaps();
		beatBox.seq.atBeats(0, 10, BPM, beatBox.kicks());
		beatBox.seq.atBeats(2*BPM, 8, BPM, beatBox.hihats());
		
		beatBox.seq.atBeats(4*BPM, 6, BPM, beatBox.snare1());
		
		beatBox.play();
	}
}
