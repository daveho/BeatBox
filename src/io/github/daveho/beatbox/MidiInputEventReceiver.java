package io.github.daveho.beatbox;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;

public class MidiInputEventReceiver implements Receiver {
	private MidiDevice device;
	private List<InputEventListener> listeners;
	
	private MidiInputEventReceiver() {
		this.listeners = new ArrayList<>();
	}

	public void addListener(InputEventListener listener) {
		listeners.add(listener);
	}

	@Override
	public void send(MidiMessage message, long timeStamp) {
		System.out.println("Midi event!");
		
		InputEvent inputEvent = null;
		
		byte[] data = message.getMessage();
		if (message.getStatus() == 144) {
			// Key down
			int note = data[1];
			int velocity = data[2];
			
			inputEvent = new InputEvent(InputEventType.KEY_DOWN, note, velocity);
		} else if (message.getStatus() == 128) {
			int note = data[1];
			
			inputEvent = new InputEvent(InputEventType.KEY_UP, note, 0);
		}
		
		if (inputEvent != null) {
			for (InputEventListener listener : listeners) {
				listener.onInputEvent(inputEvent);
			}
		}
	}

	@Override
	public void close() {
		device.close();
	}

	public static MidiInputEventReceiver create() throws MidiUnavailableException {
		MidiInputEventReceiver r = new MidiInputEventReceiver();
		MidiDevice device = CaptureMidiEvents.getMidiInput(r);
		r.device = device;
		return r;
	}
}
