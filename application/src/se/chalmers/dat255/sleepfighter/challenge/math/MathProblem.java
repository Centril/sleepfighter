/*******************************************************************************
 * Copyright (c) 2013 See AUTHORS file.
 * 
 * This file is part of SleepFighter.
 * 
 * SleepFighter is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * SleepFighter is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with SleepFighter. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package se.chalmers.dat255.sleepfighter.challenge.math;

/**
 * Define a math problem.
 * See SimpleProblem.java for a relatively simple example implementation of this class.
 */
public interface MathProblem {

	/** The string used for showing the problem in the WebView. Since a WebView is used, you can use 
	 html tags and such in this stirng. Also, the webwiew uses jqmath to render math. 
	 By surronding text with dollar signs, $, you can render pretty math formulas. 
	 See: http://mathscribe.com/author/jqmath.html
	 For more details on how write jqmath. 
	 */
	public String render();
	
	
	/** Returns the solution to the problem. 
	 * We will only allow problems with integer solutions, to prevent too hard problems.
	 * Also, because it's a pain in the ass to input numbers with decimals using the android keyboard.
	 * 
	 */
	public int solution();
	
	/** generate a new problem of this type. The return values of render() of solution() SHOULD change after
	 * this call
	*/
	public void newProblem();

}
