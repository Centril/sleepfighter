package se.toxbee.sleepfighter.challenge.gdx;

import se.toxbee.sleepfighter.challenge.BaseChallenge;
import se.toxbee.sleepfighter.challenge.ChallengeResolvedParams;
import android.app.Activity;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.LifecycleListener;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationBackend;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.utils.Clipboard;

/**
 * <p>GdxChallenge is the equivalent of {@link AndroidApplication} but for challenges.</p>
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @author mzechner
 * @version 1.0
 * @since Nov 9, 2013
 */
public abstract class GdxChallenge extends BaseChallenge implements Application, ApplicationListener {
	private AndroidApplicationBackend backend;

	/**
	 * Initializes libgdx. All subclasses must call this in
	 * {@link #start(Activity, ChallengeResolvedParams)}
	 *
	 * @param cfg the config to use.
	 */
	protected void initGdx( AndroidApplicationConfiguration cfg ) {
		this.backend = new AndroidApplicationBackend( this.activity() );

		RelativeLayout layout = new RelativeLayout( this.activity() );
		layout.addView( this.backend.initializeForView( this, cfg ) );

		this.activity().setContentView( layout );
	}

	@Override
	public void onPause() {
		backend.onPause();
	}

	@Override
	public void onResume() {
		backend.onResume();
	}

	@Override
	public void resize( int width, int height ) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}

	protected void createWakeLock (AndroidApplicationConfiguration config) {
		backend.createWakeLock(config);
	}

	protected void hideStatusBar (AndroidApplicationConfiguration config) {
		backend.hideStatusBar(config);
	}

	protected FrameLayout.LayoutParams createLayoutParams () {
		return backend.createLayoutParams();
	}

	@Override
	public ApplicationListener getApplicationListener () {
		return backend.getApplicationListener();
	}

	@Override
	public Graphics getGraphics () {
		return backend.getGraphics();
	}

	@Override
	public Audio getAudio () {
		return backend.getAudio();
	}

	@Override
	public Input getInput () {
		return backend.getInput();
	}

	@Override
	public Files getFiles () {
		return backend.getFiles();
	}

	@Override
	public Net getNet () {
		return backend.getNet();
	}

	@Override
	public void log (String tag, String message) {
		backend.log(tag, message);
	}

	@Override
	public void log (String tag, String message, Throwable exception) {
		backend.log(tag, message, exception);
	}

	@Override
	public void error (String tag, String message) {
		backend.error(tag, message);
	}

	@Override
	public void error (String tag, String message, Throwable exception) {
		backend.error(tag, message, exception);
	}

	@Override
	public void debug (String tag, String message) {
		backend.debug(tag, message);
	}

	@Override
	public void debug (String tag, String message, Throwable exception) {
		backend.debug(tag, message, exception);
	}

	@Override
	public void setLogLevel (int logLevel) {
		backend.setLogLevel(logLevel);
	}

	@Override
	public int getLogLevel () {
		return backend.getLogLevel();
	}

	@Override
	public ApplicationType getType () {
		return backend.getType();
	}

	@Override
	public int getVersion () {
		return backend.getVersion();
	}

	@Override
	public long getJavaHeap () {
		return backend.getJavaHeap();
	}

	@Override
	public long getNativeHeap () {
		return backend.getNativeHeap();
	}

	@Override
	public Preferences getPreferences (String name) {
		return backend.getPreferences(name);
	}

	@Override
	public Clipboard getClipboard () {
		return backend.getClipboard();
	}

	@Override
	public void postRunnable (Runnable runnable) {
		backend.postRunnable(runnable);
	}

	@Override
	public void exit () {
		backend.exit();
	}

	@Override
	public void addLifecycleListener (LifecycleListener listener) {
		backend.addLifecycleListener(listener);
	}

	@Override
	public void removeLifecycleListener (LifecycleListener listener) {
		backend.removeLifecycleListener(listener);
	}
}