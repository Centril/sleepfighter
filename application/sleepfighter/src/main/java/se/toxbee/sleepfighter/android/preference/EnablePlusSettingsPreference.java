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

package se.toxbee.sleepfighter.android.preference;

import android.content.Context;
import android.preference.CheckBoxPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import se.toxbee.sleepfighter.R;

/**
 * <p>EnablePlusSettingsPreference is a preference used for<br/>
 * a one in two scenario where you can enable disable something<br/>
 * and where the setting has more options.</p>
 *
 * <p>It has a checkbox + a settings button that you may bind to a custom action</p>
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Oct 5, 2013
 */
public class EnablePlusSettingsPreference extends CheckBoxPreference {
	private boolean useButton = true;
	private ImageButton button;
	private OnClickListener listener;

	private Integer titleColor = null;

	private View view;

	public EnablePlusSettingsPreference( Context context, AttributeSet attrs, int defStyle ) {
		super( context, attrs );

		this.setLayoutResource( R.layout.preference_enable_plus_settings );
	}

	public EnablePlusSettingsPreference( Context context, AttributeSet attrs ) {
		this( context, attrs, 0 );
	}

	public EnablePlusSettingsPreference( Context context ) {
		this( context, null );
	}

	@Override
	protected void onBindView( final View view ) {
		super.onBindView( view );

		this.view = view;

		this.updateTitleColor();

		this.button = (ImageButton) view.findViewById( R.id.preference_enable_plus_settings_button );
		if ( this.useButton ) {
			if ( this.listener != null ) {
				this.button.setOnClickListener( this.listener );
			}
		} else {
			this.hideButton();
		}
	}

	/**
	 * Sets the title color of the preference.
	 *
	 * @param color the color.
	 */
	public void setTitleColor( int color ) {
		this.titleColor = color;

		if ( this.view != null ) {
			this.updateTitleColor();
		}
	}

	/**
	 * Updates the title color if set.
	 */
	private void updateTitleColor() {
		if ( this.titleColor == null ) {
			return;
		}

		TextView tv = (TextView) this.view.findViewById( android.R.id.title );
		tv.setTextColor( this.titleColor );
	}

	/**
	 * Sets whether or not to use button.<br/>
	 * Default is true.
	 *
	 * @param useButton true = use, false = hide.
	 */
	public void setUseButton( boolean useButton ) {
		this.useButton = useButton;

		if ( this.button != null ) {
			this.hideButton();
		}
	}

	/**
	 * Hides the button.
	 */
	private void hideButton() {
		this.button.setVisibility( View.INVISIBLE );
	}

	/**
	 * Sets the listener that receives button clicks.
	 *
	 * @param listener the listener to bind to button.
	 */
	public void setOnButtonClickListener( OnClickListener listener ) {
		this.listener = listener;
		if ( this.button != null ) {
			this.button.setOnClickListener( listener  );
		}
	}
}