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
	
	public BeatBox() {
		ac = new AudioContext();
		seq = new Sequencer(ac);
		Samples.loadAll();
		kick = new PlaySampleEvent(Samples.KICK_1, 0.4f);
		hihat1 = new PlaySampleEvent(Samples.HIHAT_1, 0.4f);
	}
	
	private EventGroup kicks() {
		return group(0, kick, BPM/2, kick);
	}
	
	private EventGroup hihats() {
		EventGroup g = new EventGroup();
		for (int i = 0; i < BPM; i += BPM/8) {
			g.addEvent(i, hihat1);
		}
		return g;
	}
	
	public void addKicks() {
		for (int i = 0; i < 10; i++) {
			kicks().schedule(i*BPM, seq);
		}
	}
	
	public void addHihats() {
		for (int i = 2; i < 10; i++) {
			hihats().schedule(i*BPM, seq);
		}
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
		beatBox.addKicks();
		beatBox.addHihats();
		
		beatBox.play();
	}
}
