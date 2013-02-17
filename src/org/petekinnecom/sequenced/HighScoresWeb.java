package org.petekinnecom.sequenced;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

public class HighScoresWeb extends Activity
{

	WebView wv;

	private void initView()
	{
		wv = (WebView) findViewById(R.id.webviewblack);
		wv.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		int width = wv.getWidth();
		Log.d("web", "width: " + width);
		final String mimeType = "text/html";
		final String encoding = "utf-8";

		ArrayList<HighScore> scores;
		
		wv.setBackgroundResource(R.drawable.bgclean);
		String html = "<BODY><center><div style='position:fixed; top:0; left:0; width:100%; height:100%;'>" +
				"<img src='file:///android_asset/bg-clean.png' width='100%' height='100%'></div>" +
				"<div style='position:relative; z-index:1;'>";
		
		for(int i=4;i<5;i++)
		{
			scores = dbHelper.getHighScoresOLD(i);
			html += "<table border='1' cellpadding='10' bordercolor='#FFFFFF' rules='rows'>";
			int j=0;
			for(HighScore s : scores)
			{
				if(j<4)
				{
				html+= "<tr>" +
				"<td><font color='#FFFFFF' size='5'>"+s.name+"</font></td>"+
				"<td><font color='#FFFFFF' size='5'>"+s.score+"</font></td>"+
				"<td><font color='#FFFFFF' size='5'>"+s.date+"</font></td>"+
				"</tr>";
				}
				j++;
			}
			html += "</table><br/></div>";
		}
		html+="</body>";

		wv.loadDataWithBaseURL("fake://not/needed", html, mimeType, encoding,
				"");

		Log.d("web", "width: " + wv.getWidth());
	}

	DBHelper dbHelper;

	@Override
	public void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);
		this.setContentView(R.layout.webviewblack);
		dbHelper = new DBHelper(this);
		setTitle("High Scores");
		initView();

	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus)
	{
		initView();
	}
}
