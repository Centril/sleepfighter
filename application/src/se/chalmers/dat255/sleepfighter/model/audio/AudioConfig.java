package se.chalmers.dat255.sleepfighter.model.audio;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * AudioConfig models data per Alarm such as volume, etc.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep 27, 2013
 */
@DatabaseTable(tableName = "audio_config")
public class AudioConfig {
	public static final String ID_COLUMN = "id";

	@DatabaseField(generatedId = true)
	private int id;

	// TODO: REMOVE when real fields are added, NEEDED 'cause SQLite crashes otherwise.
	@DatabaseField
	private String temp;

	/**
	 * Constructs an AudioConfig, for DB purposes only.
	 */
	public AudioConfig() {
	}

	/**
	 * Returns the id of the AudioConfig (in DB).
	 *
	 * @return the id.
	 */
	public int getId() {
		return this.id;
	}
}