package org.cloudcoder.beatbox;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.Bead;
import net.beadsproject.beads.ugens.Clock;

public class BeatBox {
	private static final int BPM = 16;

	private final class OnTick extends Bead {
		// We wait a bit before actually playing
		boolean playing = false;

		// Beat counter
		int beat = 0;

		@Override
		protected void messageReceived(Bead message) {
			if (!playing) {
				playing = (beat >= BPM-1);
			} else {
				seq.tick(beat);
			}
			beat++;
		}
	}

	private AudioContext ac;
	private Sequencer seq;
	
	public BeatBox() {
		ac = new AudioContext();
		seq = new Sequencer(ac, BPM);
		Samples.loadAll();
	}
	
	public void kicksAndClaps() {
		SequencerEvent kick = new PlaySampleEvent(Samples.KICK_1, 0.4f);
		seq.repeat(0, kick);
		seq.repeat(BPM/2, kick);
		SequencerEvent clap1 = new PlaySampleEvent(Samples.CLAP_1, 0.4f);
		SequencerEvent clap3 = new PlaySampleEvent(Samples.CLAP_1, 0.4f);
		
		seq.repeat(3, clap1);
		seq.repeat(6, clap1);
		seq.repeat(BPM/2+2, clap3);
		seq.repeat(BPM/2+4, clap3);
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
		
		beatBox.kicksAndClaps();
		
		beatBox.play();
	}
}
