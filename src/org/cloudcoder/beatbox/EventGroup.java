package org.cloudcoder.beatbox;

import java.util.ArrayList;
import java.util.List;

/**
 * A composite {@link SequencerEvent} which represents a group
 * of {@link SequencerEvent}s which should be scheduled to fire
 * at an offset from the "base" beat.
 */
public class EventGroup implements SequencerEvent {
	private static class Item {
		int offset;
		SequencerEvent evt;
	}
	private List<Item> itemList;
	
	/**
	 * Constructor.  The EventGroup is initially empty.
	 */
	public EventGroup() {
		itemList = new ArrayList<>();
	}
	
	/**
	 * Add a {@link SequencerEvent} to the group.
	 * 
	 * @param offset the offset relative to the base beat when the event should be scheduled
	 * @param evt    the event to add
	 */
	public void addEvent(int offset, SequencerEvent evt) {
		Item item = new Item();
		item.offset = offset;
		item.evt = evt;
		itemList.add(item);
	}

	@Override
	public void onAdd(int beat, Sequencer seq) {
		// Add all of the items at their offset from the base beat
		for (Item item : itemList) {
			seq.atBeat(beat + item.offset, item.evt);
		}
	}

	@Override
	public void fire(Sequencer seq) {
		// Do nothing
	}

	/**
	 * Convenience method for creating an {@link EventGroup}.
	 * The arguments should be an alternating list of integer beat offsets
	 * and {@link SequencerEvent}s.
	 * 
	 * @param args alternating list of beat offsets and events
	 * @return the {@link EventGroup}
	 */
	public static EventGroup group(Object... args) {
		if (args.length % 2 != 0) {
			throw new IllegalArgumentException("group method should have an even number of arguments");
		}
		EventGroup result = new EventGroup();
		for (int i = 0; i < args.length; i += 2) {
			Integer offset = (Integer) args[i];
			SequencerEvent evt = (SequencerEvent) args[i+1];
			result.addEvent(offset, evt);
		}
		return result;
	}
}
