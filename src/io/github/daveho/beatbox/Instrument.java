package io.github.daveho.beatbox;

/**
 * Interface to be implemented by audio generators which
 * (potentially) play in response to real-time events,
 * e.g., {@link InputEvent}s.  The idea is that Instruments
 * are persistent, and can be turned on and off dynamically.
 * One reason this interface exists (beyond Bead/UGen)
 * is that UGens have no clear way to continue
 * generating audio after being asked to stop: e.g.,
 * to do a fade, echo, reverb, etc. following the end
 * of a note or sample.
 */
public interface Instrument {
	/**
	 * Set a parameter.
	 * 
	 * @param type the {@link ParamType} to set
	 * @param value the value to set
	 */
	public void setParam(ParamType type, float value);
	
	/**
	 * Turn on (start generating audio).
	 */
	public void on();
	
	/**
	 * Turn off (stop generating audio).
	 */
	public void off();
	
	public boolean isOn();
	
	public boolean hasParam(ParamType param);
	
	public float getParam(ParamType type);

	public void close();

//	/**
//	 * Stop playing.
//	 */
//	public void stop();

}