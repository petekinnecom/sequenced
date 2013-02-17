package org.petekinnecom.sequenced;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

public class About extends Activity
{
	WebView wv;
	private void initView()
	{
		wv = (WebView) findViewById(R.id.webview);
		int width = wv.getWidth();
		Log.d("web", "width: "+width);
	    final String mimeType = "text/html";
	    final String encoding = "utf-8";
	    final String html = "" +
	    		"<h1>The Game</h1>" +
	    		"A silly little idea." +
	    		"<h1>Other games</h1>" +
	    		"<a href='https://market.android.com/search?q=kinnecom'>Found here</a>" +
	    		"<h1>Favorite animal</h1>" +
	    		"Jellyfish, cause, what are they?" +
	    		"<h1>Use for flying monkeys</h1>" +
	    		"Only ethical purposes." +
	    		"<h1>Places to see</h1>" +
	    		"Jupiter.  And Chicago." +
	    		"<h1>Birthday cake</h1>" +
	    		"Delicious.";
	    
	    wv.loadDataWithBaseURL("fake://not/needed", html, mimeType, encoding, "");
		
	    
		Log.d("web", "width: "+wv.getWidth());
	}
	
	@Override
	public void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);
		this.setContentView(R.layout.webview);
		initView();

	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus)
	{
		initView();
	}

}