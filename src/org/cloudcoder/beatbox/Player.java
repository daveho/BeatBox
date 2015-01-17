package org.cloudcoder.beatbox;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.Bead;
import net.beadsproject.beads.ugens.Clock;

public abstract class Player {
	private class OnTick extends Bead {
		// We wait a bit before actually playing
		boolean playing = false;

		// Beat counter
		int beat = 0;

		@Override
		protected void messageReceived(Bead message) {
			// Avoid playing on the first few beats to avoid
			// noisy audio artifact.
			if (!playing) {
				playing = (beat >= bpm-1);
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

	protected final AudioContext ac;
	protected final Sequencer seq;
	protected final int bpm;

	public Player(int bpm) {
		this.bpm = bpm;
		this.ac = new AudioContext();
		this.seq = new Sequencer(ac);
	}

	public void play() {
		Clock clock = new Clock(ac, 1100);
		Bead onTick = new OnTick();
		clock.addMessageListener(onTick);
		
		// Note that the Clock's class notion of "beat" is actually
		// what sequencer considers to mean "measure".
		clock.setTicksPerBeat(bpm);
		
		ac.out.addDependent(clock);
		
		ac.start();
	}

}