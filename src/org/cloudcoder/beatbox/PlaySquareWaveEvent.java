package org.cloudcoder.beatbox;

import net.beadsproject.beads.core.Bead;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.DelayTrigger;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.WavePlayer;

public class PlaySquareWaveEvent implements SequencerEvent {
	private float durationMs;
	private float freq;
	private float gain;
	
	public PlaySquareWaveEvent(float durationMs, float freq, float gain) {
		this.durationMs = durationMs;
		this.freq = freq;
		this.gain = gain;
	}

	@Override
	public void onAdd(int beat, Sequencer seq) {
		// Nothing to do
	}

	@Override
	public void fire(Sequencer seq) {
		
		final WavePlayer wp = new WavePlayer(seq.getAc(), freq, Buffer.SQUARE);
		
		Gain g = new Gain(seq.getAc(), 1, gain);
		g.addInput(wp);

		seq.getAc().out.addInput(g);
		
		wp.start();
		
		// Schedule a trigger to generate the end event
		DelayTrigger trigger = new DelayTrigger(seq.getAc(), durationMs, new Bead() {
			@Override
			protected void messageReceived(Bead message) {
				wp.kill();
			}
		});
		seq.getAc().out.addDependent(trigger);
	}

}
