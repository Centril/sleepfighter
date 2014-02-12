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

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.preference.ListPreference;
import android.util.AttributeSet;

import java.util.Arrays;

/**
 * This class is a modified version of https://gist.github.com/cardil/4754571
 * Because we wish to support API levels below 11, so we need this class. 
 * 
 */
public class MultiSelectListPreference extends ListPreference {

    private  boolean[] entryChecked;

    public MultiSelectListPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        entryChecked = new boolean[getEntries().length];
    }

    public MultiSelectListPreference(Context context) {
        this(context, null);
    }

    @Override
    protected void onPrepareDialogBuilder(Builder builder) {
        CharSequence[] entries = getEntries();
        CharSequence[] entryValues = getEntryValues();
        if (entries == null || entryValues == null
                || entries.length != entryValues.length) {
            throw new IllegalStateException(
                    "MultiSelectListPreference requires an entries array and an entryValues "
                            + "array which are both the same length");
        }

        OnMultiChoiceClickListener listener = new DialogInterface.OnMultiChoiceClickListener() {
            public void onClick(DialogInterface dialog, int which, boolean val) {
                entryChecked[which] = val;
            }
        };
        
        builder.setMultiChoiceItems(entries, entryChecked, listener);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
    	if (positiveResult) {
    		callChangeListener(entryChecked);
    	}
    }

    public void setEntryChecked(boolean[] entryChecked) {
    	this.entryChecked = Arrays.copyOf(entryChecked, entryChecked.length);
    }

    public boolean[] getEntryChecked() {
    	return Arrays.copyOf(this.entryChecked, this.entryChecked.length);
    }
}
