package org.cloudcoder.beatbox;

import java.lang.reflect.Field;

import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.data.SampleManager;

public interface Samples {
	public static String KICK_1 = "175961__fawkes027__analogish-kick-001.wav";
	public static String KICK_2 = "175962__fawkes027__analogish-kick-002.wav";
	public static String HIHAT_1 = "75037__cbeeching__hat-04_16bit.wav";
	public static String HIHAT_2 = "802__bdu__closehatac.wav";
	public static String SNARE_1 = "209887__veiler__hi-snare.wav";
	public static String TOM_1 = "183109__dwsd__prc-phat909roomtom_16bit.wav";
	public static String CLAP_1 = "239906__jamesabdulrahman__fat-clap.wav";
	public static String BOING_1 = "146264__setuniman__boing-0h-16m.wav";
	public static String COWBELL_1 = "99766__menegass__cow.wav";
	public static String COWBELL_2 = "159766__timgormly__cowbell-os-4.wav";
	
	/**
	 * Retrieve a sample.
	 * 
	 * @param fileName filename of the sample
	 * @return the sample
	 */
	public static Sample get(String fileName) {
		return SampleManager.sample("samples/" + fileName);
	}

	/**
	 * Load all samples.
	 * Assumes that all fields are static strings indicating
	 * filenames of samples.
	 */
	public static void loadAll() {
		try {
			Class<Samples> cls = Samples.class;
			for (Field field : cls.getDeclaredFields()) {
				String fileName = (String) field.get(null);
				get(fileName);
			}
		} catch (Throwable e) {
			throw new RuntimeException("Couldn't load samples", e);
		}
	}
}
