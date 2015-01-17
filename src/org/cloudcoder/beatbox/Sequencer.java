package org.cloudcoder.beatbox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.beadsproject.beads.core.AudioContext;

/**
 * Sequencer: triggers {@link SequencerEvent}s (e.g., play a sound) when beats occur.
 */
public class Sequencer {
	private static final int DEFAULT_IDLE_SHUTDOWN_COUNT = 32;
	private AudioContext ac;
	private int idleShutdownCount;

	/** Global event map: events that fire on a specific beat. */
	private Map<Integer, List<SequencerEvent>> eventMap;
	private int maxBeat;
	
	/**
	 * Constructor.
	 * 
	 * @param ac    the AudioContext
	 */
	public Sequencer(AudioContext ac) {
		this.ac = ac;
		this.idleShutdownCount = DEFAULT_IDLE_SHUTDOWN_COUNT;
		this.eventMap = new HashMap<>();
		this.maxBeat = 0;
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
	 * Set the sequencer to shut down after being idle for this many beats.
	 * 
	 * @param idleShutdownCount number of idle beats before shutting down
	 */
	public void setIdleShutdownCount(int idleShutdownCount) {
		this.idleShutdownCount = idleShutdownCount;
	}
	
	/**
	 * Add an event to occur on a specific beat.
	 * 
	 * @param beat the beat
	 * @param evt the event
	 */
	public void atBeat(int beat, SequencerEvent evt) {
		System.out.println("Schedule " + evt + " at " + beat);
		List<SequencerEvent> events = eventMap.get(beat);
		if (events == null) {
			events = new ArrayList<>();
			eventMap.put(beat, events);
		}
		events.add(evt);
		evt.onAdd(beat, this);
		if (beat > maxBeat) {
			maxBeat = beat;
		}
	}
	
	/**
	 * Add an event to fire starting at specified beat,
	 * repeating a specified number of times, and skipping a
	 * specified number of beats between occurrences.
	 * 
	 * @param start    the start beat
	 * @param howmany  how many times the event should fire
	 * @param skip     number of beats to skip between occurrences
	 * @param evt      the event
	 */
	public void atBeats(int start, int howmany, int skip, SequencerEvent evt) {
		for (int i = 0; i < howmany; i++) {
			atBeat(start + i*skip, evt);
		}
	}

	/**
	 * This method should be called when beats occur:
	 * appropriate {@link SequencerEvent}s will be fired.
	 * 
	 * @param beat the beat (0 for first, 1 for second, etc.)
	 */
	public void tick(int beat) {
		List<SequencerEvent> events = eventMap.get(beat);
		if (events != null) {
			//events.forEach(e -> e.fire(this));
			for (SequencerEvent evt : events) {
				//System.out.println("Play " + evt + " at " + beat);
				evt.fire(this);
			}
		}
		if (beat > maxBeat + idleShutdownCount) {
			System.out.println("Idle, shutting down");
			ac.stop();
		}
	}
}
