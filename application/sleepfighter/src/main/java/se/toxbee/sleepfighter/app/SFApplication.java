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

package se.toxbee.sleepfighter.app;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import se.toxbee.sleepfighter.R;
import se.toxbee.sleepfighter.android.preference.SharedPreferenceManager;
import se.toxbee.sleepfighter.android.utils.ActivityUtils;
import se.toxbee.sleepfighter.android.utils.ApplicationUtils;
import se.toxbee.sleepfighter.audio.AudioDriver;
import se.toxbee.sleepfighter.audio.factory.AudioDriverFactory;
import se.toxbee.sleepfighter.factory.FromPresetAlarmFactory;
import se.toxbee.sleepfighter.factory.PresetAlarmFactory;
import se.toxbee.sleepfighter.model.Alarm;
import se.toxbee.sleepfighter.model.AlarmList;
import se.toxbee.sleepfighter.model.gps.GPSFilterArea;
import se.toxbee.sleepfighter.model.gps.GPSFilterAreaSet;
import se.toxbee.sleepfighter.persist.PersistenceManager;
import se.toxbee.sleepfighter.preference.AppPreferenceManager;
import se.toxbee.sleepfighter.preference.migration.UpgradeExecutor;
import se.toxbee.sleepfighter.speech.TextToSpeechUtil;
import se.toxbee.sleepfighter.utils.debug.Debug;
import se.toxbee.sleepfighter.utils.message.Message;
import se.toxbee.sleepfighter.utils.message.MessageBus;
import se.toxbee.sleepfighter.utils.model.LocalizationProvider;

/**
 * A custom implementation of Application for SleepFighter.
 */
public class SFApplication extends Application implements LocalizationProvider, TextToSpeech.OnInitListener {
	private static final boolean CLEAN_START = false;
	public static final boolean DEBUG = true;

	private static SFApplication app;

	// State that is is initialized in onCreate:
	private MessageBus<Message> bus;
	private PersistenceManager persistenceManager;
	private AppPreferenceManager prefs;
	private TextToSpeech tts;

	private AlarmList alarmList;
	private Alarm ringingAlarm;

	private AudioDriver audioDriver;
	private AudioDriverFactory audioDriverFactory;

	private FromPresetAlarmFactory fromPresetFactory;

	private GPSFilterAreaSet gpsAreaManaged;

	private SFLocalizationProvider localizationProvider;

	// called when the text to speech engine is initialized. 
	@Override
	public void onInit(int status) {
		Debug.d("on init tts");
		tts.setLanguage(TextToSpeechUtil.getBestLanguage(tts, this));
		TextToSpeechUtil.config(tts);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		app = this;

		this.initLocalizationProvider();

		this.initPreferences();

		this.tryUpgrade();

		this.initPersister();

		this.tts = new TextToSpeech(this, this);

		ActivityUtils.forceActionBarOverflow( this );
	}


	/**
	 * Returns the one and only SFApplication in town.
	 *
	 * @return the application.
	 */
	public static final SFApplication get() {
		return app;
	}

	public long now() {
		return this.localizationProvider.now();
	}

	public Locale locale() {
		return this.localizationProvider.locale();
	}

	@Override
	public String format( Object key ) {
		return this.localizationProvider.format( key );
	}

	/**
	 * Returns the application global GlobalPreferencesReader object.
	 *
	 * @return the GlobalPreferencesReader.
	 */
	public synchronized AppPreferenceManager getPrefs() {
		return this.prefs;
	}

	/**
	 * Returns the factory that creates alarms from preset.<br/>
	 * If factory does not exist, neither does preset,<br/>
	 * so one is made via {@link PresetAlarmFactory}.
	 *
	 * @return the factory that creates alarms from preset.
	 */
	public synchronized FromPresetAlarmFactory getFromPresetFactory() {
		// Make sure we have pulled a list from database or this won't work.
		if ( this.alarmList == null ) {
			this.getAlarms();
		}

		if ( this.fromPresetFactory == null ) {
			// Make preset alarm, add to database and finally store in factory.
			Alarm preset = new PresetAlarmFactory().createAlarm();
			preset.setMessageBus( this.getBus() );

			this.getPersister().addAlarm( preset );

			this.fromPresetFactory = new FromPresetAlarmFactory( preset );
		}

		return this.fromPresetFactory;
	}

	/**
	 * Filters out preset alarm and stores it in SFApplication for later use.<br/>
	 * If no preset was found, preset is not stored.
	 *
	 * @param alarms the list of alarms to filter.
	 */
	private void filterPresetAlarm( List<Alarm> alarms ) {
		Iterator<Alarm> iter = alarms.iterator();
		while ( iter.hasNext() ) {
			Alarm alarm = iter.next();
			if ( alarm.isPresetAlarm() ) {
				// Take the opportunity to store preset in a factory.
				this.fromPresetFactory = new FromPresetAlarmFactory( alarm );
				iter.remove();
				alarm.setMessageBus(this.getBus());
				break;
			}
		}
	}

	/**
	 * Returns the AlarmList for the application.<br/>
	 * It is lazy loaded.
	 * 
	 * @return the AlarmList for the application
	 */
	public synchronized AlarmList getAlarms() {
		if ( this.alarmList == null ) {
			List<Alarm> alarms = this.getPersister().fetchAlarms();
			this.filterPresetAlarm( alarms );

			this.alarmList = new AlarmList( alarms );
			this.alarmList.setMessageBus( this.getBus() );
		}

		return alarmList;
	}

	/**
	 * Returns the default MessageBus for the application.
	 * 
	 * @return the default MessageBus for the application
	 */
	public synchronized MessageBus<Message> getBus() {
		if ( this.bus == null ) {
			this.bus = new MessageBus<Message>();
		}
		return bus;
	}

	/**
	 * Returns the PersistenceManager for the application.
	 *
	 * @return the persistence manager.
	 */
	public synchronized PersistenceManager getPersister() {
		return this.persistenceManager;
	}

	/**
	 * Tries to upgrade app, and kills app if failure.
	 */
	private void tryUpgrade() {
		boolean success = new UpgradeExecutor().execute( this, this.prefs );
		if ( !success ) {
			ApplicationUtils.kill( false );
		}
	}

	/**
	 * Initializes the persister.
	 */
	private void initPersister() {
		this.persistenceManager = new PersistenceManager( this );

		this.getBus().subscribe( this.persistenceManager );

		if ( CLEAN_START ) {
			this.persistenceManager.cleanStart();
		}
	}

	/**
	 * Initializes the preferences manager.
	 */
	private void initPreferences() {
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences( this );
		SharedPreferenceManager backend = new SharedPreferenceManager( sharedPrefs );
		this.prefs = new AppPreferenceManager( backend, this.localizationProvider );
	}

	/**
	 * Returns the application global AudioDriver if any.
	 *
	 * @return the audio driver.
	 */
	public synchronized AudioDriver getAudioDriver() {
		return this.audioDriver;
	}

	/**
	 * Sets an application global AudioDriver, null is allowed.<br/>
	 * If the previous audio driver was playing, it is stopped.
	 *
	 * @param driver the audio driver to set.
	 */
	public synchronized void setAudioDriver( AudioDriver driver ) {
		if ( this.audioDriver != null && this.audioDriver.isPlaying() ) {
			this.audioDriver.stop();
		}

		this.audioDriver = driver;
	}

	/**
	 * Returns the application global AudioDriverFactory object.<br/>
	 * This object is lazy loaded.
	 *
	 * @return the factory.
	 */
	public synchronized AudioDriverFactory getAudioDriverFactory() {
		if ( this.audioDriverFactory == null ) {
			this.audioDriverFactory = new AudioDriverFactory();
		}

		return this.audioDriverFactory;
	}
	
	/**
	 * Fetches the set of areas and manages it in application.<br/>
	 * Call {@link #releaseGPSSet()} to release reference to it in application.
	 */
	public synchronized GPSFilterAreaSet getGPSSet() {
		if ( this.gpsAreaManaged == null ) {
			this.gpsAreaManaged = this.getPersister().fetchGPSFilterAreas();
			this.gpsAreaManaged.setMessageBus( this.getBus() );
		}

		return this.gpsAreaManaged;
	}

	/**
	 * Clears the reference the application holds to any {@link GPSFilterAreaSet}
	 * 
	 * @return the set, or null if no set is managed.
	 */
	public synchronized void releaseGPSSet() {
		this.gpsAreaManaged = null;
	}

	/**
	 * Returns the TextToSpeech engine.
	 *
	 * @return the TTS engine.
	 */
	public TextToSpeech getTts() {
		return this.tts;
	}

	/**
	 * Gets the currently ringing alarm.
	 * 
	 * @return the ringing alarm, null if no alarm is ringing
	 */
	public Alarm getRingingAlarm() {
		return ringingAlarm;
	}

	/**
	 * Sets the currently ringing alarm.
	 * 
	 * <p>
	 * Added for being able to check from activities if an alarm is currently
	 * ringing, to then redirect the user to AlarmActivity.
	 * </p>
	 * 
	 * @param alarm
	 *            the alarm to set as the ringing alarm
	 */
	public void setRingingAlarm(Alarm alarm) {
		this.ringingAlarm = alarm;
	}

	private void initLocalizationProvider() {
		this.localizationProvider = new SFLocalizationProvider( this );

		// Alarm related formats:
		Alarm.setLocalizationProvider( this.localizationProvider );
		this.localizationProvider.setFormat( Alarm.Field.NAME, R.string.alarm_unnamed_format );

		// Location filter related formats:
		GPSFilterArea.setLocalizationProvider( this.localizationProvider );
		this.localizationProvider.setFormat( GPSFilterArea.Field.NAME, R.string.edit_gpsfilter_area_unnamed );
	}
}