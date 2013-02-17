package org.petekinnecom.sequenced;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;


public class Help extends Activity
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
	    		"<h1>Instructions</h1>" +
	    		"<p>The goal of the game is to gain points by placing numbers into a grid." +
	    		" The board will be filled with numbers in an attempt to make sequences of numbers that form " +
	    		"<i>arithmetic sequences</i> (explained below)." +
	    		"</p>" +
	    		"<h2>Understanding the board</h2>" +
	    		"The board has four parts to it. Up top, you will see a slot for four numbers.  This is called the <b>queue</b>. " +
	    		"Near the bottom, you will see two buttons.  An <b>undo button</b> and a <b>next button</b>.</p>" +
	    		"Once you have placed two numbers from the queue onto the board, the next button will " +
	    		"become active, allowing you to generate four new numbers for the queue.  <b>Note:</b> you " +
	    		"must play <i>at least</i> two numbers from the queue, but you are allowed to all of them, if you wish. " +
	    		"</p>" +
	    		"<img src='file:///android_asset/board.png' width='"+(width/2)+"'/><br/>" +
	    		"The queue is highlighted." +
	    		"<h2>Sequences</h2>" +
	    		"<p>" +
	    		"Points will be scored by creating <b>sequences</b>.  A sequence is three or more numbers in a row " +
	    		"(horizontal, vertical, or diagonal) that following a simple addition pattern.  For example, in the picture below " +
	    		"the sequence 12, 14, 16 follows the pattern 'add two'.</p>" +
	    		"<img src='file:///android_asset/diag.png' width='"+(width/2)+"'/><br/>" +
	    		"Sequences need at least 3 squares." +
	    		"<h2>Scoring</h2>" +
	    		"<p>After the board has been filled, you will be scored.  Every square in each sequence will count as a point. " +
	    		"The sequence '7, 14, 21' (rule: add 7) would be worth three points.  Notice, however, if we are able to place a " +
	    		"zero in front, we will now have the sequence '0, 7, 14, 21'.  This will be worth <b>ten</b> points.  Why ten?  " +
	    		"Notice that the four-square sequence also contains three-square sequences within it. So, you would score:</p>" +
	    		"3 points : 0, 7, 14<br/>" +
	    		"3 points : 7, 14, 21<br/>" +
	    		"4 points : 0, 7, 14, 21</br>" +
	    		"10 points : total<br/>" +
	    		"<img src='file:///android_asset/sideways2.png' width='"+(width/2)+"'/><br/>" +
	    		"This sequence is 10 points." +
	    		"<h2>More on scoring</h2>" +
	    		"<p> Check out the board below.  How many points is it currently worth?</p>" +
	    		"<img src='file:///android_asset/lots1.png' width='"+(width/2)+"'/><br/>" +
	    		"How many points?" +
	    		"<h2>Scoring breakdown:</h2>" +
	    		"<img src='file:///android_asset/lots2.png' width='"+(width/2)+"'/><br/>" +
	    		"Rule add 1.  Points: 10 (see above)</br><br/>" +
	    		"<img src='file:///android_asset/lots3.png' width='"+(width/2)+"'/><br/>" +
	    		"Rule add 1.  Points: 3<br/><br/>"  +
	    		"<img src='file:///android_asset/lots4.png' width='"+(width/2)+"'/><br/>" +
	    		"Rule add 3.  Points: 3<br/><br/>"  +
	    		"<img src='file:///android_asset/lots5.png' width='"+(width/2)+"'/><br/>" +
	    		"Rule add 3.  Points: 3<br/><br/>"  +
	    		"<p>In total, we currently have 8 points.</p>" +
	    		"<h2>You read this far?</h2>" +
	    		"<p>Sweet! You have just validated the hour I put into this tutorial.  Enjoy!</p>";
	    
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
