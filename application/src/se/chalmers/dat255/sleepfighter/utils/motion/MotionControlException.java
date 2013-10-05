package se.chalmers.dat255.sleepfighter.utils.motion;

public class MotionControlException extends Exception {

	private static final long serialVersionUID = 858451799792946532L;

	public MotionControlException(){
		super("Required Sensor is not available on the current device.");
	}
	
}
