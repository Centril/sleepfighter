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

/**
 * Utilities for application lifecycle.<br/>
 * Handle these utilities with caution.
 *
 * @version 1.0
 * @since Nov 13, 2013
 */
public class ApplicationUtils {
    /**
     * Kill the app either safely or quickly. The app is killed safely by
     * killing the virtual machine that the app runs in after finalizing all
     * {@link Object}s created by the app. The app is killed quickly by abruptly
     * killing the process that the virtual machine that runs the app runs in
     * without finalizing all {@link Object}s created by the app. Whether the
     * app is killed safely or quickly the app will be completely created as a
     * new app in a new virtual machine running in a new process if the user
     * starts the app again.
     *
     * <P>
     * <B>NOTE:</B> The app will not be killed until all of its threads have
     * closed if it is killed safely.
     * </P>
     *
     * <P>
     * <B>NOTE:</B> All threads running under the process will be abruptly
     * killed when the app is killed quickly. This can lead to various issues
     * related to threading. For example, if one of those threads was making
     * multiple related changes to the database, then it may have committed some
     * of those changes but not all of those changes when it was abruptly
     * killed.
     * </P>
     *
     * Code is from: http://stackoverflow.com/questions/2092951/how-to-close-android-application
     *
     * @param killSafely
     *            Whether the app should be killed safely or quickly.
     *            If true then the app will be killed safely.
     *            Otherwise it will be killed quickly.
     */
	@SuppressWarnings( "deprecation" )
	public static void kill( boolean killSafely ) {
		if ( killSafely ) {
			/*
			 * Notify the system to finalize and collect all objects of the app
			 * on exit so that the virtual machine running the app can be killed
			 * by the system without causing issues. NOTE: If this is set to
			 * true then the virtual machine will not be killed until all of its
			 * threads have closed.
			 */
			System.runFinalizersOnExit( true );

			/*
			 * Force the system to close the app down completely instead of
			 * retaining it in the background. The virtual machine that runs the
			 * app will be killed. The app will be completely created as a new
			 * app in a new virtual machine running in a new process if the user
			 * starts the app again.
			 */
			System.exit( 0 );
		} else {
			/*
			 * Alternatively the process that runs the virtual machine could be
			 * abruptly killed. This is the quickest way to remove the app from
			 * the device but it could cause problems since resources will not
			 * be finalized first. For example, all threads running under the
			 * process will be abruptly killed when the process is abruptly
			 * killed. If one of those threads was making multiple related
			 * changes to the database, then it may have committed some of those
			 * changes but not all of those changes when it was abruptly killed.
			 */
			android.os.Process.killProcess( android.os.Process.myPid() );
		}
	}
}