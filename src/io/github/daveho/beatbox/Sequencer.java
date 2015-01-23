package io.github.daveho.beatbox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Sequencer: triggers {@link SequencerEvent}s (e.g., play a sound) when beats occur.
 */
public class Sequencer {
	private static class ScheduledEvent {
		int trackIndex;
		SequencerEvent evt;
		
		ScheduledEvent(int trackIndex, SequencerEvent evt) {
			this.trackIndex = trackIndex;
			this.evt = evt;
		}
	}
	
	private static final int DEFAULT_IDLE_SHUTDOWN_COUNT = 32;
	private Desk desk;
	private int idleShutdownCount;

	/** Global event map: events that fire on a specific beat. */
	private Map<Integer, List<ScheduledEvent>> eventMap;
	private int maxBeat;
	
	// Shutdown hook
	private Runnable shutdownHook;
	
	/**
	 * Constructor.
	 * 
	 * @param desk the {@link Desk}
	 */
	public Sequencer(Desk desk) {
		this.desk = desk;
		this.idleShutdownCount = DEFAULT_IDLE_SHUTDOWN_COUNT;
		this.eventMap = new HashMap<>();
		this.maxBeat = 0;
	}
	
	/**
	 * Get the {@link Desk}.
	 * 
	 * @return the {@link Desk}
	 */
	public Desk getDesk() {
		return desk;
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
	 * @param trackIndex  the track in which the event should be played
	 * @param beat the beat
	 * @param evt the event
	 */
	public void atBeat(int trackIndex, int beat, SequencerEvent evt) {
		System.out.println("Schedule " + evt + " at " + beat);
		List<ScheduledEvent> events = eventMap.get(beat);
		if (events == null) {
			events = new ArrayList<>();
			eventMap.put(beat, events);
		}
		events.add(new ScheduledEvent(trackIndex, evt));
		evt.onAdd(trackIndex, beat, this);
		if (beat > maxBeat) {
			maxBeat = beat;
		}
	}
	
	/**
	 * Add an event to fire starting at specified beat,
	 * repeating a specified number of times, and skipping a
	 * specified number of beats between occurrences.
	 * 
	 * @param trackIndex  the track in which the event should be played
	 * @param start    the start beat
	 * @param howmany  how many times the event should fire
	 * @param skip     number of beats to skip between occurrences
	 * @param evt      the event
	 */
	public void atBeats(int trackIndex, int start, int howmany, int skip, SequencerEvent evt) {
		for (int i = 0; i < howmany; i++) {
			atBeat(trackIndex, start + i*skip, evt);
		}
	}

	/**
	 * This method should be called when beats occur:
	 * appropriate {@link SequencerEvent}s will be fired.
	 * 
	 * @param beat the beat (0 for first, 1 for second, etc.)
	 */
	public void tick(int beat) {
		List<ScheduledEvent> events = eventMap.get(beat);
		if (events != null) {
			for (ScheduledEvent e : events) {
				//System.out.println("Play " + evt + " at " + beat);
				e.evt.fire(this, e.trackIndex);
			}
		}
		if (beat > maxBeat + idleShutdownCount) {
			System.out.println("Idle, shutting down");
			desk.getAc().stop();
			if (shutdownHook != null) {
				shutdownHook.run();
			}
		}
	}
	
	/**
	 * Set a hook to be run when the sequencer shuts down
	 * (i.e., because there are no more events to be fired.)
	 * 
	 * @param shutdownHook the shutdown hook
	 */
	public void setShutdownHook(Runnable shutdownHook) {
		this.shutdownHook = shutdownHook;
	}
}
