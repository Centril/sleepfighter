package se.chalmers.dat255.sleepfighter.audio;

import se.chalmers.dat255.sleepfighter.model.audio.AudioConfig;
import se.chalmers.dat255.sleepfighter.model.audio.AudioSource;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.MediaColumns;

public class LocalContentDriver extends BaseAudioDriver {

	private Uri ringtoneUri;
	private String name;

	@Override
	public void setSource(Context context, AudioSource source) {
		super.setSource(context, source);
		this.ringtoneUri = Uri.parse(source.getUri());
	}

	@Override
	public String printSourceName() {
		if (this.name == null) {
			// Query for title
			String[] projection = { MediaColumns._ID, MediaColumns.TITLE };
			Cursor cursor = getContext().getContentResolver().query(
					ringtoneUri, projection, null, null, null);

			if (cursor == null || !cursor.moveToFirst()) {
				return ringtoneUri.toString();
			}
			this.name = cursor.getString(1);
		}
		return this.name;
	}

	@Override
	public void play(AudioConfig config) {
		super.play(config);

		Intent i = new Intent(AudioService.ACTION_PLAY);
		i.putExtra(AudioService.BUNDLE_URI, ringtoneUri);
		getContext().startService(i);
	}

	@Override
	public void stop() {
		Intent i = new Intent(AudioService.ACTION_STOP);
		getContext().startService(i);
		super.stop();
	}
}
