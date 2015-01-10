package org.cloudcoder.beatbox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.beadsproject.beads.core.AudioContext;

/**
 * Sequencer: triggers {@link SequencerEvent}s (e.g., play a sound) when beats occur.
 * 
 * @author David Hovemeyer
 */
public class Sequencer {
	private AudioContext ac;
	private int bpm;
	/** Global event map: events that fire on a specific beat. */
	private Map<Integer, List<SequencerEvent>> eventMap;
	/** 
	 * Repeating event map: events that fire on a specific beat of each measure
	 * (0 for first, 1 for second, etc.)
	 */
	private Map<Integer, List<SequencerEvent>> repeatingEventMap; 
	
	/**
	 * Constructor.
	 * 
	 * @param ac    the AudioContext
	 * @param bpm   beats per measure
	 */
	public Sequencer(AudioContext ac, int bpm) {
		this.ac = ac;
		this.bpm = bpm;
		this.eventMap = new HashMap<>();
		this.repeatingEventMap = new HashMap<>();
	}
	
	/**
	 * Get beats per minute.
	 * 
	 * @return beats per minute
	 */
	public int getBpm() {
		return bpm;
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
	 * Add an event to occur at the beginning of a measure.
	 * 
	 * @param measure the measure (0 for first, 1 for second, etc.)
	 * @param evt     the event
	 */
	public void at(int measure, SequencerEvent evt) {
		addEvent(eventMap, measure*bpm, evt);
	}
	
	/**
	 * Add an event to occur on a specific beat.
	 * 
	 * @param beat the beat
	 * @param evt the event
	 */
	public void atBeat(int beat, SequencerEvent evt) {
		addEvent(eventMap, beat, evt);
	}
	
	/**
	 * Add a repeating event, to occur at a specific beat in each
	 * measure.
	 * 
	 * @param beat beat with measure (0 for first, 1 for second, etc.)
	 * @param evt the event
	 */
	public void repeat(int beat, SequencerEvent evt) {
		if (beat >= bpm) {
			throw new IllegalArgumentException("Illegal beat " + beat + " in measure (bpm=" + bpm + ")");
		}
		addEvent(repeatingEventMap, beat, evt);
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
		// repeating events
		fireEvents(repeatingEventMap, beat % bpm);
		
		// one shot events
		fireEvents(eventMap, beat);
	}

	private void fireEvents(Map<Integer, List<SequencerEvent>> map, int beat) {
		List<SequencerEvent> events = map.get(beat);
		if (events != null) {
			events.forEach(e -> e.fire(this));
		}
	}
}
