package io.github.daveho.beatbox;

import net.beadsproject.beads.core.Bead;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.DelayTrigger;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.WavePlayer;

/**
 * Play a square wave with given frequency and duration.
 */
public class PlaySquareWaveEvent extends SimpleSequencerEvent {
	private float durationMs;
	private float freq;
	private float gain;
	
	/**
	 * Constructor.
	 * 
	 * @param durationMs duration in milliseconds
	 * @param freq       frequency
	 * @param gain       gain
	 */
	public PlaySquareWaveEvent(float durationMs, float freq, float gain) {
		this.durationMs = durationMs;
		this.freq = freq;
		this.gain = gain;
	}

	@Override
	public void fire(Sequencer seq, int trackIndex) {
		
		final WavePlayer wp = new WavePlayer(seq.getDesk().getAc(), freq, Buffer.SQUARE);
		
		Gain g = new Gain(seq.getDesk().getAc(), 1, gain);
		g.addInput(wp);

		seq.getDesk().getTrack(trackIndex).addInput(g);
		
		wp.start();
		
		// Schedule a trigger to generate the end event
		DelayTrigger trigger = new DelayTrigger(seq.getDesk().getAc(), durationMs, new Bead() {
			@Override
			protected void messageReceived(Bead message) {
				wp.kill();
			}
		});
		seq.getDesk().getAc().out.addDependent(trigger);
	}

}
