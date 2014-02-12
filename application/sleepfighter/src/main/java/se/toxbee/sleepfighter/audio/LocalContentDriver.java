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

package se.toxbee.sleepfighter.audio;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.MediaColumns;

import se.toxbee.sleepfighter.audio.utils.VolumeUtils;
import se.toxbee.sleepfighter.model.audio.AudioConfig;
import se.toxbee.sleepfighter.model.audio.AudioSource;
import se.toxbee.sleepfighter.service.AudioService;

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
