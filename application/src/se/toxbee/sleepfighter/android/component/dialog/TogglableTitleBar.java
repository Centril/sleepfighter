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
package se.toxbee.sleepfighter.android.component.dialog;

import se.toxbee.sleepfighter.R;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * <p>{@link TogglableTitleBar} is a title-bar intended for dialogs.</p>
 *
 * <p>It contains the dialog title and a checkable with its label.</p>
 *
 * <p>It requires a layout named dialog_titlebar_toggle and the ids: title, toggle, toggle_label, toggle_bg.</p>
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Dec 24, 2013
 */
public class TogglableTitleBar extends LinearLayout implements Checkable {
	private static final int LAYOUT = R.layout.dialog_titlebar_toggle;

	protected TextView titleView;
	protected TextView toggleLabel;
	protected CompoundButton toggleBox;

	public TogglableTitleBar( Context context ) {
		this( context, null );
	}

	public TogglableTitleBar( Context context, AttributeSet attrs ) {
		super( context, attrs );
		commonCtor( context );
	}
	@TargetApi( Build.VERSION_CODES.HONEYCOMB )
	public TogglableTitleBar( Context context, AttributeSet attrs, int defStyle ) {
		super( context, attrs, defStyle );
		commonCtor( context );
	}

	/**
	 * Common "constructor" for view.
	 *
	 * @param context
	 */
	protected void commonCtor( Context context ) {
		this.inflate( context );
		this.bindViews();
		this.configTogglerBg();
	}

	/**
	 * Provides the Layout resource id to use for non-overridden {@link #inflate()}.
	 *
	 * @return the id.
	 */
	protected int inflateLayoutId() {
		return LAYOUT;
	}

	/**
	 * Inflates the layout, hook provided for subclasses.
	 * If you want to override id, provide a different in {@link #inflateLayoutId()}.
	 *
	 * @param ctx the context to get {@link LayoutInflater} from. 
	 */
	protected void inflate( Context ctx ) {
		LayoutInflater inflater = (LayoutInflater) ctx
				.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		inflater.inflate( this.inflateLayoutId(), this, true );
	}

	/**
	 * Binds all the views to fields, provided for subclasses.
	 */
	protected void bindViews() {
		// Find views.
		this.titleView = ((TextView) this.findViewById( R.id.title ));
		this.toggleLabel = ((TextView) this.findViewById( R.id.toggle_label ));
		this.toggleBox = (CompoundButton) this.findViewById( R.id.toggle );
	}

	/**
	 * Configures the background of the toggler if any, provided for subclasses to override.
	 */
	protected void configTogglerBg() {
		this.findViewById( R.id.toggle_bg ).setOnClickListener( new View.OnClickListener() {
			@Override
			public void onClick( View v ) {
				toggle();
			}
		} );
	}

	public void setChecked( boolean checked ) {
		this.toggleBox.setChecked( checked );
	}

	public boolean isChecked() {
		return this.toggleBox.isChecked();
	}

	@Override
	public void toggle() {
		this.toggleBox.toggle();
	}

	/**
	 * Sets the title, label and checked state.
	 *
	 * @param title the title.
	 * @param label the label of toggle.
	 * @param checked the checked state of toggle.
	 * @return this.
	 */
	public TogglableTitleBar setAll( CharSequence title, CharSequence label, boolean checked ) {
		this.setTitle( title ).setToggleLabel( label ).setChecked( checked );
		return this;
	}

	/**
	 * Sets the title and label via resource ids, and checked state.
	 *
	 * @param title the title.
	 * @param label the label of toggle.
	 * @param checked the checked state of toggle.
	 * @return this.
	 */
	public TogglableTitleBar setAll( int title, int label, boolean checked ) {
		this.setTitle( title ).setToggleLabel( label ).setChecked( checked );
		return this;
	}

	/**
	 * Sets the title of the bar.
	 *
	 * @param title the title text.
	 * @return this.
	 */
	public TogglableTitleBar setTitle( CharSequence title ) {
		this.titleView.setText( title );
		return this;
	}

	/**
	 * Sets the title of the bar.
	 *
	 * @param title the title resource id.
	 * @return this.
	 */
	public TogglableTitleBar setTitle( int resid ) {
		this.titleView.setText( resid );
		return this;
	}

	/**
	 * Sets the label of the toggle switch.
	 *
	 * @param label the label text to set.
	 * @return this.
	 */
	public TogglableTitleBar setToggleLabel( CharSequence label ) {
		this.toggleLabel.setText( label );
		return this;
	}

	/**
	 * Sets the label of the toggle switch.
	 *
	 * @param resid  the text resource id.
	 * @return this.
	 */
	public TogglableTitleBar setToggleLabel( int resid ) {
		this.toggleLabel.setText( resid );
		return this;
	}

	/**
	 * Returns the title-view for advanced manipulation.
	 *
	 * @return the view.
	 */
	public TextView titleView() {
		return this.titleView;
	}

	/**
	 * Returns the label-view of the toggle for advanced manipulation.
	 *
	 * @return the view.
	 */
	public TextView toggleLabel() {
		return this.toggleLabel;
	}

	/**
	 * Returns the toggle-view for advanced manipulation.
	 *
	 * @return the view.
	 */
	public CompoundButton togger() {
		return this.toggleBox;
	}
}
