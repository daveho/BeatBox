package io.github.daveho.beatbox;


/**
 * Abstract base class for polysynths that receive input events and
 * play notes in response.  Each in-progress note is represented
 * by a {@live PlayLive} object.
 */
public abstract class AbstractPolySynth implements InputEventListener {
	protected final Sequencer seq;
	protected final float maxGain;
	protected final int trackIndex;
//	private final Map<Integer, Instrument> noteMap;
	protected final int maxPoly;
	protected final Instrument[] instruments;
	
	/**
	 * 
	 * @param seq
	 * @param maxGain
	 * @param trackIndex
	 * @param maxPoly
	 */
	public AbstractPolySynth(Sequencer seq, float maxGain, int trackIndex, int maxPoly) {
		this.seq = seq;
		this.maxGain = maxGain;
		this.trackIndex = trackIndex;
		this.maxPoly = maxPoly;
		//this.noteMap = new HashMap<>();
		this.instruments = new Instrument[maxPoly];
	}

	@Override
	public void onInputEvent(InputEvent inputEvent) {
		Instrument player;

		/*
		switch (inputEvent.getType()) {
		case KEY_DOWN:
			player = startNote(inputEvent.getNote(), inputEvent.getVelocity());
			noteMap.put(inputEvent.getNote(), player);
			break;
			
		case KEY_UP:
			player = noteMap.get(inputEvent.getNote());
			if (player != null) {
				player.stop();
				noteMap.remove(inputEvent.getNote());
			}
			break;
			
		default:
			System.out.println("Unknown input event type? " + inputEvent.getType());
		}
		*/
		
		switch (inputEvent.getType()) {
		case KEY_DOWN:
			player = findAvailable();
			if (player != null) {
				player.setParam(ParamType.NOTE, inputEvent.getNote());
				player.on();
			}
			break;
		case KEY_UP:
			player = find(inputEvent.getNote());
			if (player != null) {
				player.off();
			}
			break;
		}
	}

	private Instrument findAvailable() {
		for (Instrument instrument : instruments) {
			if (!instrument.isOn()) {
				return instrument;
			}
		}
		return null;
	}

	private Instrument find(int note) {
		for (Instrument instrument : instruments) {
			if (instrument.isOn() && instrument.hasParam(ParamType.NOTE)) {
				if (instrument.getParam(ParamType.NOTE) == (float)note) {
					return instrument;
				}
			}
		}
		return null;
	}
	
//	protected abstract Instrument startNote(int note, int velocity);
//	
//	protected abstract void endNote(int note, Instrument player);
//	
//	public void close() {
//		for (Instrument player : noteMap.values()) {
//			player.stop();
//		}
//	}
}
