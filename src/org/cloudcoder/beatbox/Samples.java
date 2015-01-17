package org.cloudcoder.beatbox;

import java.lang.reflect.Field;

import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.data.SampleManager;

public interface Samples {
	public static String KICK_1 = "175961__fawkes027__analogish-kick-001.wav";
	public static String HIHAT_1 = "75037__cbeeching__hat-04_16bit.wav";
	public static String HIHAT_2 = "802__bdu__closehatac.wav";
	public static String SNARE_1 = "209887__veiler__hi-snare.wav";
	
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
