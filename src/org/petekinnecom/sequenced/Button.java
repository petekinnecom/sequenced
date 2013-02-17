package org.petekinnecom.sequenced;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Typeface;

public class Button
{
	private static final int[] colors = {Color.LTGRAY, Color.DKGRAY};
	
	public boolean pressed;
	public boolean enabled;
	public int color;
	public String text;
	
	private Paint paint;
	
	Button(String t)
	{
		text = t;
		pressed = false;
		enabled = true;
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	}
	Button(String t, boolean b)
	{
		this(t);
		enabled = b;
	}
	
	private int width, height;
	
	public void draw(Canvas canvas, Rect rect)
	{
		//paint.setColor(Color.WHITE);
		//canvas.drawRect(rect, paint);
		
		paint.reset();
		/** FONT ALIGN MEASUREMENTS */
		width = rect.width();
		height = rect.height();
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
		
		/** VARIABLE PAINT SETTINGS */
		if(pressed)
		{
			paint.setColor(Color.GRAY);
		}
		else if(enabled)
		{
			paint.setStyle(Style.FILL);
		}
		else
		{
			paint.setStyle(Style.STROKE);
			paint.setStrokeWidth(1);
		}
		
		canvas.drawText(text, x+rect.left, y+rect.top, paint);
		
	}
	
}
