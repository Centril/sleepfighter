package se.chalmers.dat255.sleepfighter.challenge.math;

public interface MathProblem {

	// The string used for showing it in the webview, rendered with jqmath. so this stirng should use
	// the format that jqmath uses.
	public String render();
	
	// we will only allow problems with integer solutions, to prevent too hard problems.
	public int solution();
	
	
	// generate a new problem of this type. The return values of render() of solution() will change after
	// you call this method.
	public void newProblem();

}
