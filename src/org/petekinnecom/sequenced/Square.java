package org.petekinnecom.sequenced;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.Log;

public class Square
{
	//private static final int[] colors = {Color.BLUE, Color.RED, Color.GREEN, Color.CYAN, Color.YELLOW};
	
	/** light icon color scheme */
	//private static final int[] colors = {Color.parseColor("#FFFFFF"),Color.parseColor("#BFBFBF"),Color.parseColor("#6699FF"), Color.parseColor("#99CC33"), Color.parseColor("#FFCC00")};
	
	private static final int selectedColor = Color.LTGRAY;
	
	public static Bitmap whiteBMP, grayBMP, blueBMP;
	
	
	private static final String TAG = "Square";
	public int val;
	public String valString;
	public int color;
	public boolean reset = false;
	public boolean selected;
	public boolean filled;
	public boolean active;
	
	private Random gen;
	private Paint paint;

	public Square()
	{
		this.filled = false;
		this.reset = false;
		this.selected = false;
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		
		this.color = Color.WHITE;
		valString = "";
	}
	
	public Square(int maxNumber)
	{
		this.filled = true;
		this.reset = false;
		this.selected = false;
		this.active = true;
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		
		if(!filled)
		{
			this.color = Color.WHITE;
			valString = "";
		}
		else
		{
			gen = new Random();
			val = gen.nextInt(maxNumber);
			color = Color.parseColor("#6699FF");
			valString = ""+val;
			
		}
	}

	int height, width;
	Bitmap bmp;
	private Paint bmpPaint = new Paint();
	public void draw(Canvas canvas, Rect rect)
	{
		bmpPaint.reset();
		if(selected)
		{
			paint.reset();
			paint.setStyle(Style.FILL);
			paint.setStrokeWidth(2);
			paint.setColor(Color.argb(230, 255, 255, 102));
			//paint.setColor(Color.WHITE);
			//paint.setShadowLayer(2f, 2f, 2f, Color.YELLOW);
			//canvas.drawRect(rect, paint);
			canvas.drawRoundRect(new RectF(rect.left-1, rect.top-1, rect.right+1, rect.bottom+1), 5, 5, paint);
		}
			/** draw rect */
		if(active && filled)
		{
			bmp = Square.blueBMP;
			paint.setColor(Color.parseColor("#214764"));
			paint.setShadowLayer(1f, 1f, 1f, Color.WHITE);

		}
		else if(active)
		{
			bmp = Square.whiteBMP;
		}
		else
		{
			bmp = Square.grayBMP;
			paint.setColor(Color.WHITE);
			paint.setShadowLayer(2f, 2f, 2f, Color.BLACK);
		}
		
		canvas.drawBitmap(bmp, new Rect(0,0,bmp.getWidth(), bmp.getHeight()), rect, bmpPaint);
		
		/** GENERAL FONT SETTINGS 
		 * 		 * Note that we give them an extra two pixels on the edges.
		 * This leaves room for drop shadows.
		 * **/
		rect.right -= 2;
		width = rect.width();
		height = rect.height();
		paint.setStyle(Style.FILL);
		paint.setAntiAlias(true);
		paint.setTextSize(height * 0.75f);
		paint.setTextScaleX(width / height);
		paint.setTextAlign(Paint.Align.CENTER);
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		
		
		/** FONT ALIGN SETTINGS **/

		FontMetrics fm = paint.getFontMetrics();
		// Centering in X: use alignment (and X at midpoint)
		float x = width / 2;
		// Centering in Y: measure ascent/descent first
		float y = height / 2 - (fm.ascent + fm.descent) / 2;
		



		canvas.drawText(valString, x+rect.left, y+rect.top, paint);
		
		/** Outline and shadow for BLUE squares */
		if(active && filled)
		{
			paint.reset();
			paint.setColor(Color.BLACK);
			paint.setStyle(Paint.Style.STROKE);
			paint.setAntiAlias(true);
			paint.setTextSize(height * 0.75f);
			paint.setTextScaleX(width / height);
			paint.setTextAlign(Paint.Align.CENTER);
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			paint.setStrokeWidth(1);
			
			canvas.drawText(valString, x+rect.left, y+rect.top, paint);
		}
		

	}
	public void drawOLD(Canvas canvas, Rect clipRect)
	{
		
	
		/** draw rect */
		paint.setColor(color);
		if(selected)
			paint.setColor(selectedColor);
		canvas.drawRect(clipRect, paint);
		
		/** magical draw font code */
		width = clipRect.width();
		height = clipRect.height();
		paint.setColor(Color.BLACK);
		paint.setStyle(Style.FILL);
		paint.setTextSize(height * 0.75f);
		paint.setTextScaleX(width / height);
		paint.setTextAlign(Paint.Align.CENTER);

		FontMetrics fm = paint.getFontMetrics();
		// Centering in X: use alignment (and X at midpoint)
		float x = width / 2;
		// Centering in Y: measure ascent/descent first
		float y = height / 2 - (fm.ascent + fm.descent) / 2;

		canvas.drawText(valString, x+clipRect.left, y+clipRect.top, paint);

	}


}
