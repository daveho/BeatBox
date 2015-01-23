package io.github.daveho.beatbox;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;

/**
 * Generalization of AudioContext for multiple output tracks,
 * each of which feeds into the AudioContext's output.
 * This allows effects to be specified per-track, and
 * events are scheduled to be played on a specific track.
 */
public class Desk {
	private AudioContext ac;
	private UGen[] tracks;
	
	/**
	 * Constructor.
	 * Each track defaults to feeding directly into the
	 * AudioContext's output.
	 * 
	 * @param ac        the AudioContext
	 * @param numTracks number of tracks
	 */
	public Desk(AudioContext ac, int numTracks) {
		this.ac = ac;
		this.tracks = new UGen[numTracks];
		
		// By default, each track is just the AudioContext's
		// output UGen
		for (int i = 0; i < numTracks; i++) {
			tracks[i] = ac.out;
		}
	}
	
	/**
	 * Get the AudioContext.
	 * 
	 * @return the AudioContext
	 */
	public AudioContext getAc() {
		return ac;
	}
	
	/**
	 * Set a track.
	 * 
	 * @param index the track index (0 for first)
	 * @param track the UGen, which will be connected to the AudioContext's
	 *              output
	 */
	public void setTrack(int index, UGen track) {
		tracks[index] = track;
		ac.out.addInput(track);
	}
	
	/**
	 * Get a track.
	 * 
	 * @param index the track index (0 for first)
	 * @return the track UGen
	 */
	public UGen getTrack(int index) {
		return tracks[index];
	}
	
	
}
