package org.petekinnecom.sequenced;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class HighScoresActivity extends Activity
{
	class ScoresView extends View
	{
		private static final String TAG = "WelcomeView";
		private Context context;
		private Bitmap backgroundBMP;

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

		public ScoresView(Context context)
		{
			super(context);
			this.context = context;
			loadAssets();
		}

		private Rect[] scoresRects = new Rect[4];
		private Rect titleRect;

		private void setDimensions()
		{

			titleRect = new Rect(0, (int) ((float) this.getHeight() * 0.9f),
					this.getWidth(), this.getHeight());

			int x1 = (int) ((float) this.getWidth() * 0.17f);
			int x2 = (int) ((float) this.getWidth() * 0.83f);
			int y1, y2;
			int pad = (int) ((float) this.getHeight() * 0.05f);
			int height = (int) (((float) this.getHeight() * 0.9f - 5f * pad) / 4f);
			for (int i = 0; i < 4; i++)
			{

				y1 = (int) (i + 1) * pad + (i) * height;
				y2 = y1 + height;

				scoresRects[i] = new Rect(x1, y1, x2, y2);
			}
			invalidate();
		}

		@Override
		protected void onDraw(Canvas canvas)
		{
			setDimensions();

			Paint paint = new Paint();
			canvas.drawBitmap(
					backgroundBMP,
					new Rect(0, 0, backgroundBMP.getWidth(), backgroundBMP
							.getHeight()),
					new Rect(0, 0, this.getWidth(), this.getHeight()), paint);

			int i = 0;
			for (HighScore s : scores)
			{

				s.draw(canvas, scoresRects[i]);
				i++;
			}

		}
	}

	ScoresView sView;
	DBHelper dbHelper;
	ArrayList<HighScore> scores;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		dbHelper = new DBHelper(this);
		scores = dbHelper.getHighScores();
		sView = new ScoresView(this);
		setContentView(sView);
	}

}
