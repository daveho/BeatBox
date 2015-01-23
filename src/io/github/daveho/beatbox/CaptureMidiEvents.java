package io.github.daveho.beatbox;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

public class CaptureMidiEvents {
	
	public static void getMidiInput(Receiver receiver) throws MidiUnavailableException {
		
		MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
		for (MidiDevice.Info info : infos) {
			MidiDevice device;
			device = MidiSystem.getMidiDevice(info);
			System.out.println("Found: " + device);
			
			int maxTransmitters = device.getMaxTransmitters();
			System.out.println("  Max transmitters: " + maxTransmitters);
			
			if (maxTransmitters == -1 || maxTransmitters > 0) {
				Transmitter transmitter = device.getTransmitter();
				transmitter.setReceiver(receiver);
				device.open();
				return;
			}
		}
		
		throw new MidiUnavailableException("Could not find any midi input sources");
	}
	
	static class MyReceiver implements Receiver {

		@Override
		public void send(MidiMessage message, long timeStamp) {
			System.out.println("Recieved MidiMessage@" + timeStamp + ", status=" + message.getStatus());
		}

		@Override
		public void close() {
			System.out.println("Closing...");
		}
	}
	
	public static void main(String[] args) throws MidiUnavailableException {
		getMidiInput(new MyReceiver());
		
		while (true) {
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
				System.out.println("Interrupted?");
			}
		}
	}
}
