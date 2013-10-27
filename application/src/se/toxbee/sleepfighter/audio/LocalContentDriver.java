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
package se.toxbee.sleepfighter.audio;

import se.toxbee.sleepfighter.audio.utils.VolumeUtils;
import se.toxbee.sleepfighter.model.audio.AudioConfig;
import se.toxbee.sleepfighter.model.audio.AudioSource;
import se.toxbee.sleepfighter.service.AudioService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.MediaColumns;

public class LocalContentDriver extends BaseAudioDriver {

	private Uri uri;
	private String name;
	private int volume = 0;

	@Override
	public void setSource(Context context, AudioSource source) {
		super.setSource(context, source);
		this.uri = Uri.parse(source.getUri());
	}

	@Override
	public String printSourceName() {
		if (this.name == null) {
			// Query for title
			String[] projection = { MediaColumns._ID, MediaColumns.TITLE };
			Cursor cursor = getContext().getContentResolver().query(
					uri, projection, null, null, null);

			if (cursor == null || !cursor.moveToFirst()) {
				return uri.toString();
			}
			this.name = cursor.getString(1);
		}
		return this.name;
	}

	@Override
	public void play(AudioConfig config) {
		super.play(config);
		this.volume = config.getVolume();

		float bundleVol = VolumeUtils.convertUIToFloatVolume(config.getVolume());

		Intent i = new Intent(AudioService.ACTION_PLAY_TRACK);
		i.putExtra(AudioService.BUNDLE_URI, uri);
		i.putExtra(AudioService.BUNDLE_FLOAT_VOLUME, bundleVol);
		getContext().startService(i);
	}

	@Override
	public void stop() {
		Intent i = new Intent(AudioService.ACTION_STOP);
		getContext().startService(i);
		super.stop();
	}

	@Override
	public void setVolume(int volume) {
		this.volume = volume;
		float bundleVol = VolumeUtils.convertUIToFloatVolume(volume);

		Intent i = new Intent(AudioService.ACTION_VOLUME);
		i.putExtra(AudioService.BUNDLE_FLOAT_VOLUME, bundleVol);
		getContext().startService(i);
	}
	
	public int getVolume() {
		return this.volume;
	}
}
