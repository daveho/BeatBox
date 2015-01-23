package io.github.daveho.beatbox;

import static io.github.daveho.beatbox.EventGroup.group;

import java.util.HashMap;
import java.util.Map;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;

import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.Pitch;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Reverb;

public class BeatBox extends Player {
	static final int BPM = 16;
	static final float MEASURE_LEN_MS = 1100.0f;
	static final float BEAT_LEN_MS = MEASURE_LEN_MS / BPM;
	static final int NUM_TRACKS = 1;
	
	PlaySampleEvent kick;
	PlaySampleEvent kick2;
	PlaySampleEvent hihat1;
	PlaySampleEvent hihat2;
	PlaySampleEvent hihat3;
	PlaySampleEvent snare1;
	PlaySampleEvent snare2;
	PlaySampleEvent snare2_loud;
	PlaySampleEvent tom1;
	PlaySampleEvent tom2;
	PlaySampleEvent clap1;
	PlaySampleEvent boing1;
	PlaySampleEvent cowbell1;
	PlaySampleEvent cowbell2;
	PlaySampleEvent cowbell2_loud;
	PlaySampleEvent cymbal1;
	
	EventGroup g_kicks;
	EventGroup g_kicks2;
	EventGroup g_hihats;
	EventGroup g_hihats2;
	EventGroup g_hihats3;
	EventGroup g_snare1;
	EventGroup g_snare2;
	EventGroup g_claps1;
	EventGroup g_boings1;
	
	public BeatBox() {
		super(BPM, MEASURE_LEN_MS, NUM_TRACKS);
		Samples.loadAll();
		
		// Track 0 feeds into the reverb splitter, which
		// feeds into both the AudioContext's output (implicitly,
		// via the call to setTrack), and the reverb's output
		// (which in turn is fed into the AudioContext's
		// output).  So, the eventual output is a mix of both
		// the wet and dry signals.
		Gain reverbSplit = new Gain(seq.getDesk().getAc(), 1);
		Reverb reverb = new Reverb(seq.getDesk().getAc());
		reverb.addInput(reverbSplit);
		seq.getDesk().setTrack(0, reverbSplit);
		seq.getDesk().getAc().out.addInput(reverb);
		
		// Sample events
		kick = new PlaySampleEvent(Samples.KICK_1, 0.4f);
		kick2 = new PlaySampleEvent(Samples.KICK_2, 0.4f);
		hihat1 = new PlaySampleEvent(Samples.HIHAT_1, 0.3f);
		hihat2 = new PlaySampleEvent(Samples.HIHAT_2, 0.3f);
		hihat3 = new PlaySampleEvent(Samples.HIHAT_3, 0.4f);
		snare1 = new PlaySampleEvent(Samples.SNARE_1, 0.5f);
		snare2 = new PlaySampleEvent(Samples.SNARE_2, 0.2f);
		snare2_loud = new PlaySampleEvent(Samples.SNARE_2, 0.9f);
		tom1 = new PlaySampleEvent(Samples.TOM_1, 0.3f);
		tom2 = new PlaySampleEvent(Samples.TOM_2, 0.9f);
		clap1 = new PlaySampleEvent(Samples.CLAP_1, 0.4f);
		boing1 = new PlaySampleEvent(Samples.BOING_1, 0.4f);
		cowbell1 = new PlaySampleEvent(Samples.COWBELL_1, 0.15f);
		cowbell2 = new PlaySampleEvent(Samples.COWBELL_2, 0.25f);
		cowbell2_loud = new PlaySampleEvent(Samples.COWBELL_2, 0.9f);
		cymbal1 = new PlaySampleEvent(Samples.CYMBAL_1, 0.3f);
		
		// Event groups
		g_kicks = group(0, kick2, 2, kick2, 8, kick); // paired kicks
		g_kicks2 = group(0, kick, 8, kick); // basic kicks
		g_hihats = group(
				0, hihat1,
				2, hihat1,
				4, hihat1,
				6, hihat1,
				8, hihat1,
				10, hihat1,
				12, hihat1,
				14, hihat1
				);
		g_hihats2 = group(
				0, hihat1,
				2, hihat1,
				4, hihat2,
				6, hihat2,
				8, hihat1,
				10, hihat2,
				12, hihat1,
				14, hihat2
				);
		g_hihats3 = group(
				0, hihat2,
				2, hihat3,
//				3, hihat2,
				4, hihat3,
//				6, hihat2,
				8, hihat1,
				9, hihat3,
				10, cymbal1,
//				12, hihat3,
				14, hihat3
				);
		g_snare1 = group(8, snare1, 12, snare1, 14, snare1);
		g_snare2 = group(8, snare1, 14, snare1);
		g_claps1 = group(12, clap1, 14, clap1);
		g_boings1 = group(0, boing1);
	}
	
	
	SequencerEvent oneBeatSquareWave(float freq) {
		return new PlaySquareWaveEvent(BEAT_LEN_MS, freq, 0.1f);
	}
	
	int addRhythm(int m) {
		int start = m*BPM;
		seq.atBeats(0, start, 8, BPM, g_kicks);
		seq.atBeats(0, start+0*BPM, 8, BPM, g_hihats);
		seq.atBeats(0, start+0*BPM, 2, BPM, g_snare1);
		seq.atBeats(0, start+2*BPM, 2, BPM, g_snare2);
		seq.atBeats(0, start+4*BPM, 2, BPM, g_snare1);
		seq.atBeats(0, start+6*BPM, 2, BPM, g_snare2);
		return m+8;
	}
	
	int addBasicKicks(int m) {
		int start = m*BPM;
		seq.atBeats(0, start+0, 4, BPM, g_kicks2);
		return m+4;
	}
	
	int addPairedKicks(int m) {
		int start = m*BPM;
		seq.atBeats(0, start+0, 4, BPM, g_kicks);
		return m+4;
	}
	
	int addPairedKicksAndBoings(int m) {
		int start = m*BPM;
		seq.atBeats(0, start+0, 4, BPM, g_kicks);
		seq.atBeat(0, start+0, g_boings1);
		return m+4;
	}
	
	int addRhythm3(int m) {
		int start = m*BPM;
		seq.atBeats(0, start+0, 8, BPM, g_kicks);
		seq.atBeats(0, start+0*BPM, 8, BPM, g_hihats2);
		seq.atBeats(0, start+0*BPM, 2, BPM, g_snare1);
		seq.atBeats(0, start+2*BPM, 2, BPM, g_snare2);
		seq.atBeats(0, start+4*BPM, 2, BPM, g_snare1);
		seq.atBeats(0, start+6*BPM, 2, BPM, g_snare2);
		return m+8;
	}

	int addBasicKicksAndClaps(int m) {
		int start = m*BPM;
		seq.atBeats(0, start+0, 4, BPM, g_kicks2);
		seq.atBeats(0, start+0, 4, BPM, g_claps1);
		return m+4;
	}
	
	int addSquareWavePattern(int m) {
		int start = m*BPM;
		seq.atBeats(0, start+0, 4, BPM, g_kicks2);
		seq.atBeats(0, start+4, 4, BPM, oneBeatSquareWave(440.0f));
		seq.atBeats(0, start+6, 4, BPM, oneBeatSquareWave(440.0f));
		seq.atBeats(0, start+12, 4, BPM, oneBeatSquareWave(440.0f));
		seq.atBeats(0, start+14, 4, BPM, oneBeatSquareWave(440.0f));
		return m+4;
	}
	
	int addBasicKicksAndFastHihats(int m) {
		int start = m*BPM;
		seq.atBeats(0, start+0, 4, BPM, g_kicks2);
		seq.atBeats(0, start+0*BPM, 1, BPM, g_hihats3);
		seq.atBeats(0, start+2*BPM, 1, BPM, g_hihats3);
		return m+4;
	}

	int addRhythm4(int m) {
		int start = m*BPM;
		seq.atBeats(0, start+0, 4, BPM, g_kicks);
		seq.atBeats(0, start+0*BPM, 4, BPM, g_hihats3);
		seq.atBeats(0, start+0*BPM, 2, BPM, g_snare1);
		seq.atBeats(0, start+2*BPM, 2, BPM, g_snare2);
//		seq.atBeats(0, start+4*BPM, 2, BPM, g_snare1);
//		seq.atBeats(0, start+6*BPM, 2, BPM, g_snare2);
		return m+4;
	}
	
	int addRhythm5(int m) {
		int start = m*BPM;
		addRhythm4(m);
		seq.atBeats(0, start+0*BPM, 2, BPM*2, group(4, snare2, 10, snare2_loud, 12, snare2_loud));
		return m+4;
	}

	int addRhythm6(int m) {
		int start = m*BPM;
		addRhythm4(m);
		seq.atBeats(0, start+0*BPM, 2, BPM, group(4, snare2, 10, snare2_loud, 12, snare2_loud));
		seq.atBeats(0, start+2*BPM, 1, 0, group(0, snare1, 4, snare2_loud, 8, snare1, 12, snare2_loud));
		seq.atBeats(0, start+3*BPM, 1, 0, group(8, snare2_loud, 10, snare2_loud, 12, tom2));
		return m+2;
	}

	public void addEvents() {
		int m = 0;
		
		m = addBasicKicks(m);
		m = addPairedKicks(m);
		m = addRhythm(m);
		m = addBasicKicksAndClaps(m);
		m = addRhythm3(m);
		m = addPairedKicksAndBoings(m);
//		m = addBasicKicksAndClaps(m);
		m = addRhythm3(m);
		m = addBasicKicksAndFastHihats(m);
		m = addRhythm4(m);
		m = addRhythm5(m);
		m = addRhythm6(m);
	}
	
	public void liveSynth() throws MidiUnavailableException {
		final Map<Integer, PlayLiveSquareWave> pitchMap = new HashMap<>();
		
		Receiver receiver = new Receiver() {
			@Override
			public void send(MidiMessage message, long timeStamp) {
				byte[] data = message.getMessage();
				if (message.getStatus() == 144) {
					// Key down
					int note = data[1];
					int velocity = data[2]; // unused for now
					
					float freq = Pitch.mtof(note);
					
					float gain = (velocity/128.0f) * 0.2f;

					PlayLiveSquareWave player = new PlayLiveSquareWave(seq.getDesk().getAc(), freq, gain, desk.getTrack(0));
					pitchMap.put(note, player);
					
					player.start();
				} else if (message.getStatus() == 128) {
					// Key up
					int note = data[1];
					PlayLiveSquareWave player = pitchMap.get(note);
					if (player != null) {
						player.stop();
						pitchMap.remove(note);
					}
				}
			}
			
			@Override
			public void close() {
				for (PlayLiveSquareWave player : pitchMap.values()) {
					player.stop();
				}
			}
		};
		
		final MidiDevice device = CaptureMidiEvents.getMidiInput(receiver);
		
		seq.setShutdownHook(new Runnable() {
			@Override
			public void run() {
				System.out.println("Sequencer shutting down, close midi device");
				receiver.close();
				device.close();
			}
		});
	}
	
	public static void main(String[] args) throws MidiUnavailableException {
		BeatBox beatBox = new BeatBox();

		beatBox.addEvents();
		beatBox.liveSynth();

//		beatBox.recordToFile("beats.wav");
		
		beatBox.play();
	}
}
