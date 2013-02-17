package org.petekinnecom.sequenced;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class WelcomeActivity extends Activity
{

	class WelcomeView extends View
	{
		private static final String TAG = "WelcomeView";
		private Context context;
		private Bitmap backgroundBMP;
		private Button[] buttons = new Button[5];
		private Button dummyTitleButton;
		
		private void loadAssets()
		{
			Log.d(TAG, "loadAssets()");
	        AssetManager assetManager = context.getAssets();
	        InputStream inputStream;
			try
			{
				inputStream = assetManager.open("bg-clean.png");
				backgroundBMP = BitmapFactory.decodeStream(inputStream);
				
			} catch (IOException e)
			{
				Log.d(TAG, "Error loading background.");
				e.printStackTrace();
				finish();
			}
		}
		
		private void initButtons()
		{
			buttons[0] = new Button("Sequenced", false);
			//buttons[0].pressed = true;
			buttons[1] = new Button("New Game");
			buttons[2] = new Button("High Scores");
			buttons[3] = new Button("Instructions");
			buttons[4] = new Button("About");
			
			dummyTitleButton = new Button("", false);
		}
		
		public WelcomeView(Context context)
		{
			super(context);
			this.context = context;
			loadAssets();
			initButtons();
		}
		
		private Rect[] buttonRects = new Rect[buttons.length];
		private Rect titleRect;
		private void setDimensions()
		{
			
			titleRect = new Rect(0, (int) ((float)this.getHeight()*0.9f), this.getWidth(), this.getHeight());
			
			int x1 = (int) ((float) this.getWidth() * 0.17f);
			int x2 = (int) ((float) this.getWidth()*0.83f);
			int y1, y2;
			int pad = (int) ((float) this.getHeight() * 0.05f);
			int height = (int) (((float) this.getHeight()*0.9f - 5f*pad)/((float) buttons.length));
			for(int i=0;i<buttons.length;i++)
			{
				
				y1 = (int) (i+1)*pad + (i)*height;
				y2 = y1 + height;
				
				buttonRects[i] = new Rect(x1, y1, x2, y2);
			}
			invalidate();
		}
		@Override
		protected void onDraw(Canvas canvas)
		{
			if(titleRect==null)
				setDimensions();
			Paint paint = new Paint();
			canvas.drawBitmap(backgroundBMP,
					new Rect(0,0,backgroundBMP.getWidth(), backgroundBMP.getHeight()-90), 
					new Rect(0,0,this.getWidth(),this.getHeight()), paint);
		
			paint.setColor(Color.WHITE);
			
			dummyTitleButton.draw(canvas, titleRect);
	
			for(int i=0;i<buttons.length;i++)
			{
				buttons[i].draw(canvas, buttonRects[i]);
			}
		}

		@Override
		public boolean onTouchEvent(MotionEvent e)
		{
			//Log.d(TAG, "touched : "+e.getX() + ", "+e.getY());
			int x = (int) e.getX();
			int y = (int) e.getY();
			if(e.getAction()==MotionEvent.ACTION_DOWN)
			{
				for(int i=0;i<buttons.length;i++)
				{
					if(buttonRects[i].contains(x , y))
					{
						buttons[i].pressed = true;
					}
				}
			}
			else if(e.getAction()==MotionEvent.ACTION_UP)
			{
				if(buttonRects[1].contains(x, y) && buttons[1].pressed)
				{
					C.GAME_MODEL = null;
					startActivity(new Intent(context, GameActivity.class));
				}
				else if(buttonRects[2].contains(x, y) && buttons[2].pressed)
				{
					//startActivity(new Intent(context, HighScoresActivity.class));
					startActivity(new Intent(context, HighScoresWeb.class));
				}
				else if(buttonRects[3].contains(x, y) && buttons[3].pressed)
				{
					startActivity(new Intent(context, Help.class));
				}
				else if(buttonRects[4].contains(x, y) && buttons[4].pressed)
				{
					startActivity(new Intent(context, About.class));
				}
				for(int i=1;i<buttons.length;i++)
				{
					buttons[i].pressed = false;
				}
			}
			invalidate();
			 
			return true;
		}
	}

	WelcomeView wView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		wView = new WelcomeView(this);
		setContentView(wView);

	}
}
