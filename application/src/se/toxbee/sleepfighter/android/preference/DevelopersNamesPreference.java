package se.toxbee.sleepfighter.android.preference;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/*
 * If the summary of a preference is too long, it may get cut off.
 * This class prevents this.
 */
public class DevelopersNamesPreference extends Preference {

    public DevelopersNamesPreference(Context context) {
        super(context);
    }
    
    public DevelopersNamesPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public DevelopersNamesPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        
        TextView summaryView = (TextView) view.findViewById(android.R.id.summary);
        
        // max 10 lines in the summary. 
        summaryView.setMaxLines(10);
    }
	
}
