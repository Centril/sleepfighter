/*
 * Copyright 2014 toxbee.se
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package se.toxbee.sleepfighter.challenge.math;

/**
 * Defines a math problem.
 * See SimpleProblem.java for a relatively simple example implementation of this class.
 */
public interface MathProblem {

	/** The string used for showing the problem in the WebView. Since a WebView is used, you can use 
	 html tags and such in this string. Also, the webwiew uses jqmath to render math.
	 By surrounding text with dollar signs, $, you can render pretty math formulas. 
	 See: http://mathscribe.com/author/jqmath.html
	 For more details on how use jqmath. 
	 */
	public String render();
	
	
	/** Returns the solution to the problem. 
	 * We will only allow problems with integer solutions, to prevent too hard problems.
	 * Also, because it's a pain in the ass to input numbers with decimals using the android keyboard.
	 * 
	 */
	public int solution();
	
	/** generate a new problem of this type. The return values of render() and solution() SHOULD change after
	 * this call
	*/
	public void newProblem();

}
