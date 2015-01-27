package io.github.daveho.beatbox;

import java.lang.reflect.Field;

import net.beadsproject.beads.data.Sample;
import net.beadsproject.beads.data.SampleManager;

public class SampleBank {
	/**
	 * Preload all of the samples named by static fields
	 * of the given class/interface.
	 * 
	 * @param cls a class or interface with static fields naming samples
	 */
	public static void preload(Class<?> cls) {
		try {
			for (Field field : cls.getDeclaredFields()) {
				String fileName = (String) field.get(null);
				get(fileName);
			}
		} catch (Throwable e) {
			throw new RuntimeException("Couldn't load samples", e);
		}
	}
	
	/**
	 * Retrieve a sample.
	 * 
	 * @param fileName filename of the sample
	 * @return the sample
	 */
	public static Sample get(String fileName) {
		return SampleManager.sample("samples/" + fileName);
	}
}
