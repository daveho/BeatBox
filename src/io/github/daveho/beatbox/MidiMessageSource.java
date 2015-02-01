package io.github.daveho.beatbox;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.Bead;
import net.beadsproject.beads.core.BeadArray;

/**
 * Bead for receiving MidiMessages and notifying other beads when
 * a MidiMessage is received.  Listeners can assume that the received
 * message will be an instance of {@link MidiMessageSource}, and
 * they should call {@link #getMessage()} on that object to get
 * the actual MidiMessage.
 */
public class MidiMessageSource extends Bead implements Receiver {
	private AudioContext ac;
	private BeadArray listeners;
	private AtomicReference<List<MidiMessage>> pending;
	private MidiMessage message;
	
	public MidiMessageSource(AudioContext ac) {
		this.ac = ac;
		this.listeners = new BeadArray();
		this.pending = new AtomicReference<>();
	}
	
	/**
	 * Add a listener Bead, which will receive a message (with this Bead as
	 * the message) when a MidiMessage is received.  The recipient can
	 * invoke the {@link #getMessage()} and {@link #getTimeStamp()}
	 * methods to get the midi message data.
	 * 
	 * @param bead a listener Bead to add
	 */
	public void addMessageListener(Bead bead) {
		listeners.add(bead);
	}

	@Override
	public void send(MidiMessage message, long timeStamp) {
		System.out.println("Received midi message!");

		boolean needSched = false;
		
		boolean updated;

		do {
			// Use compare and set to update the list of pending messages
			List<MidiMessage> messages = pending.get();
			
			List<MidiMessage> update = new ArrayList<>();
			if (messages == null) {
				// It looks like we are adding the first pending message
				// for the next audio frame, so schedule listeners to
				// be notified.
				needSched = true;
			} else {
				// At least one message was already pending, so listeners
				// should already be scheduled to be notified.
				update.addAll(messages);
				needSched = false;
			}
			update.add(message);
			
			updated = pending.compareAndSet(messages, update);
		} while (!updated);

		if (needSched) {
			// Schedule to have listeners notified
			ac.invokeBeforeFrame(this);
		}
	}
	
	@Override
	protected void messageReceived(Bead message) {
		// Atomically get and clear list of pending messages
		List<MidiMessage> messages = pending.getAndSet(null);
		
		System.out.println("Dispatching " + messages.size() + " midi messages");
		
		// Notify listeners for each received message.
		// There will typically just be one, but it's definitely possible that
		// we could receive multiple midi messages to be processed
		// in a single audio frame.
		for (MidiMessage m : messages) {
			this.message = m;
			listeners.messageReceived(this);
		}
	}
	
	/**
	 * Get the received MidiMessage.
	 * 
	 * @return the received MidiMessage
	 */
	public MidiMessage getMessage() {
		return message;
	}

	@Override
	public void close() {
		// Nothing to do at the moment
	}

}
