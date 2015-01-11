package org.cloudcoder.beatbox;

import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.SamplePlayer;

/**
 * Implementation of {@link SequencerEvent} which plays a sample
 * at a specified gain.
 */
public class PlaySampleEvent extends SimpleSequencerEvent {
	private Sample sample;
	private float gain;

	public PlaySampleEvent(String fileName, float gain) {
		this.sample = Samples.get(fileName);
		this.gain = gain;
	}

	@Override
	public void fire(Sequencer seq) {
		Gain g = new Gain(seq.getAc(), 1, gain);
		SamplePlayer sp = new SamplePlayer(seq.getAc(), sample);
		sp.setKillOnEnd(true);
		g.addInput(sp);
		sp.setToLoopStart();
		seq.getAc().out.addInput(g);
		sp.start();
	}
	
	@Override
	public String toString() {
		return "[" + sample.getFileName() + "]";
	}
}
