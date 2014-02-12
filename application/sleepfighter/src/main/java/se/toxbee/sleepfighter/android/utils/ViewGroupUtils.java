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

package se.toxbee.sleepfighter.android.utils;

import android.view.View;
import android.view.ViewGroup;

/**
 * {@link ViewGroupUtils} provide utilities for {@link ViewGroup}s.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 23, 2013
 */
public class ViewGroupUtils {
	/**
	 * Returns the parent of a view as a {@link ViewGroup}.
	 *
	 * @param view the view to get parent from.
	 * @return the parent as ViewGroup.
	 */
	public static ViewGroup getParent( View view ) {
		return (ViewGroup) view.getParent();
	}

    /**
     * Removes a view from its parent (if any).
     *
     * @param view the view to remove.
     */
    public static void removeView(View view) {
		ViewGroup parent = getParent( view );
		if ( parent != null ) {
			parent.removeView( view );
		}
    }

    /**
     * Replaces a view with another in parent.
     *
     * @param currentView the view to replace.
     * @param newView the new view to replace old current with.
     */
	public static void replaceView( View currentView, View newView ) {
		ViewGroup parent = getParent( currentView );
		if ( parent == null ) {
			return;
		}

		final int index = parent.indexOfChild( currentView );
		removeView( currentView );
		removeView( newView ); // Avoid duplicate.
		parent.addView( newView, index );
	}
}