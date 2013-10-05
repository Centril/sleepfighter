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
