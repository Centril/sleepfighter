package se.chalmers.dat255.sleepfighter.model.audio;

import junit.framework.TestCase;

public class AudioSourceTest extends TestCase{

	public void testConstructor() {
		AudioSource source = new AudioSource(AudioSourceType.INTERNET_STREAM, "uri");
		assertEquals(AudioSourceType.INTERNET_STREAM, source.getType());
		assertEquals("uri", source.getUri());
	}
	
}
