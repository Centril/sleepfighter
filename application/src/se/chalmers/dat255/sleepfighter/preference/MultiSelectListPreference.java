package se.chalmers.dat255.sleepfighter.preference;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.preference.ListPreference;
import android.util.AttributeSet;

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
    	this.entryChecked = entryChecked;
    }

    public boolean[] getEntryChecked() {
    	return this.entryChecked;
    }
}