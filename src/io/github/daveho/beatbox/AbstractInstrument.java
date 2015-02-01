package io.github.daveho.beatbox;

import java.util.HashMap;
import java.util.Map;

import net.beadsproject.beads.core.UGen;

/**
 * Abstract base class for {@link Instrument}s which wrap a UGen.
 */
public abstract class AbstractInstrument implements Instrument {
	protected Sequencer seq;
	protected int trackIndex;
	private boolean started;
	private final Map<ParamType, Float> paramMap;
	protected UGen ugen;
	private boolean on;
	
	public AbstractInstrument(Sequencer seq, int trackIndex, UGen ugen) {
		this.seq = seq;
		this.trackIndex = trackIndex;
		this.ugen = ugen;
		this.paramMap = new HashMap<>();
		this.started = false;
		this.on = false;
	}
	
	@Override
	public void setParam(ParamType type, float value) {
		paramMap.put(type, value);
	}

	@Override
	public boolean hasParam(ParamType param) {
		return paramMap.containsKey(param);
	}

	@Override
	public float getParam(ParamType type) {
		Float value = paramMap.get(type);
		if (value == null) {
			throw new IllegalStateException("Parameter " + type + " is not set");
		}
		return value;
	}

	@Override
	public void on() {
		System.out.println("Yurm! I am " + getClass().getSimpleName());
		if (!started) {
			seq.getDesk().getTrack(trackIndex).addInput(ugen);
			ugen.start();
			started = true;
		}
		on = true;
	}
	
	@Override
	public void off() {
		on = false;
	}
	
	@Override
	public boolean isOn() {
		return on;
	}

	@Override
	public void close() {
		if (started) {
			ugen.kill();
		}
	}

}
