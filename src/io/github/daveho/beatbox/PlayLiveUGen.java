package io.github.daveho.beatbox;

import net.beadsproject.beads.core.UGen;

public class PlayLiveUGen implements PlayLive {
	private UGen ugen;
	
	public PlayLiveUGen(UGen ugen) {
		this.ugen = ugen;
	}

	@Override
	public void start() {
		ugen.start();
	}

	@Override
	public void stop() {
		ugen.kill();
	}

}
