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

package se.toxbee.sleepfighter.android.component.web;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/*
 * A special version of WebView, that doesn't hide the keyboard when loadUrl is called.
 */
public class CustomWebView extends WebView {

	public CustomWebView(Context context) {
		super(context);
	}

	public CustomWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	public void loadUrl(String url) {
	    HitTestResult testResult = this.getHitTestResult();
	    if (url.startsWith("javascript:") && testResult != null && testResult.getType() == HitTestResult.EDIT_TEXT_TYPE)
	    {
	            //Don't do anything right now, we have an active cursor on the EDIT TEXT
	            //This should be Input Method Independent
	    }
	    else
	    {
	        super.loadUrl(url);
	    }
	}
}
