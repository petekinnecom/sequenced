package org.petekinnecom.sequenced;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Paint.FontMetrics;

public class HighScore
{
	public String name;
	public int score;
	public String date;

	HighScore(String n, int s, String d)
	{
		name = n;
		score = s;
		date = d;
	}
	
	public void draw(Canvas canvas, Rect rect)
	{
		Paint paint = new Paint();
		/** FONT ALIGN MEASUREMENTS */
		int width = rect.width();
		int height = rect.height();
		FontMetrics fm = paint.getFontMetrics();
		// Centering in X: use alignment (and X at midpoint)
		float x = width / 2;
		// Centering in Y: measure ascent/descent first
		float y = height / 2 - (fm.ascent + fm.descent) / 2;
		
		/** GENERAL PAINT SETTINGS */
		paint.setColor(Color.WHITE);
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		paint.setAntiAlias(true);
		paint.setShadowLayer(2f, 2f, 2f, Color.BLACK);
		paint.setTextSize(height * 0.45f);
		paint.setTextAlign(Paint.Align.CENTER);
		
		canvas.drawText(score + " :: "+name,x+rect.left, y+rect.top, paint);
	}
}
