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