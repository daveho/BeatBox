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
	private AudioContext ac;

	/** Global event map: events that fire on a specific beat. */
	private Map<Integer, List<SequencerEvent>> eventMap;
	
	/**
	 * Constructor.
	 * 
	 * @param ac    the AudioContext
	 */
	public Sequencer(AudioContext ac) {
		this.ac = ac;
		this.eventMap = new HashMap<>();
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
	 * Add an event to occur on a specific beat.
	 * 
	 * @param beat the beat
	 * @param evt the event
	 */
	public void atBeat(int beat, SequencerEvent evt) {
		System.out.println("Schedule " + evt + " at " + beat);
		addEvent(eventMap, beat, evt);
	}

	private void addEvent(Map<Integer, List<SequencerEvent>> map, int beat, SequencerEvent evt) {
		List<SequencerEvent> events = map.get(beat);
		if (events == null) {
			events = new ArrayList<>();
			map.put(beat, events);
		}
		events.add(evt);
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
	}
}
