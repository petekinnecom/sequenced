package org.petekinnecom.sequenced;

import java.io.IOException;
import java.io.InputStream;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.text.InputFilter;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class GameView extends View
{

	private static final String TAG = "GameView";
	Rect qLane, boardLane, buttonLane;
	Rect qBox, boardBox, resetBox, nextBox;
	Rect newGameRect;
	int squareSize;
	private GameModel gameModel;
	private GameActivity gameActivity;
	private boolean endgame;
	private Button resetButton, nextButton;

	private DBHelper dbHelper;

	int mx, my;
	int adjX, adjY;
	int qx;
	int qCount;

	private boolean endgameTouch(MotionEvent event)
	{
		mx = (int) event.getX();
		my = (int) event.getY();
		
		if (event.getAction() != MotionEvent.ACTION_UP && event.getAction() !=MotionEvent.ACTION_DOWN)
		{
			return true;
		}
		if(event.getAction() == MotionEvent.ACTION_DOWN)
		{
			if(newGameRect.contains(mx,my))
			{
				nextButton.pressed = true;
				invalidate();
				return true;
			}
		}


		if (newGameRect.contains(mx, my))
		{
			if (nextButton.text == "New Game")
			{
				// gameActivity.finish();
				gameModel = new GameModel();
				nextButton = new Button("next");
				resetButton = new Button("undo");
				nextButton.enabled = false;
				resetButton.enabled = false;
				endgame = false;
			}

		}

		invalidate();
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		nextButton.pressed = false;
		resetButton.pressed = false;
		if (resetBox.contains((int) event.getX(), (int) event.getY())
				&& resetButton.enabled)
		{
			resetButton.pressed = true;
		} else if (nextBox.contains((int) event.getX(), (int) event.getY())
				&& nextButton.enabled)
		{
			nextButton.pressed = true;
		}
		if (endgame)
		{
			return endgameTouch(event);
		} else
		{
			return gameTouch(event);

		}
	}

	private boolean gameTouch(MotionEvent event)
	{
		mx = (int) event.getX();
		my = (int) event.getY();

		if (qBox.contains(mx, my))
		{
			gameModel.qSelect((mx - qBox.left) / squareSize);
			invalidate();
			return true;
		} else if (boardBox.contains(mx, my)
				&& event.getAction() == MotionEvent.ACTION_DOWN)
		{
			adjX = (mx - boardBox.left) / squareSize;
			adjY = (my - boardBox.top) / squareSize;
			qCount = gameModel.boardSelect(adjX, adjY);

			nextButton.text = "next";
			if (gameModel.boardCount() == 0)
			{
				nextButton.text = "finish";
				nextButton.enabled = true;
			}

			invalidate();
			return true;

		} else if (resetBox.contains(mx, my))
		{
			if (event.getAction() == MotionEvent.ACTION_UP)
			{
				if (resetButton.enabled)
					if (!gameModel.resetMove())
						resetButton.enabled = false;
				resetButton.pressed = false;
				nextButton.enabled = false;
				nextButton.text = "next";

			}
		} else if (nextBox.contains(mx, my))
		{

			if (event.getAction() == MotionEvent.ACTION_UP)
			{
				if (nextButton.enabled && gameModel.boardCount() > 0)
				{
					gameModel.nextMove();
					resetButton.enabled = false;
					nextButton.enabled = false;
					nextButton.pressed = false;
				} else if (gameModel.boardCount() == 0)
				{
					if (C.DEBUG)
						Log.d(TAG, "Game ended. Switching to endgame.");
					endgame = true;
					gameModel.endGame();
					nextButton.enabled = true;
					nextButton.pressed = false;
					nextButton.text = "New Game";
					resetButton.enabled = false;
					resetButton.text = "";

					Log.d(TAG, "checking if high score");
					if (dbHelper.isTopScore(C.WIDTH, gameModel.findSequences()))
					{
						Log.d(TAG, "new High score!");
						topScoreDialog();
					} else
					{
						Log.d(TAG, "not high score");
					}
					invalidate();
					return true;
				}
			}
		}
		qCount = gameModel.qCount();
		/**
		 * If two squares have been used from the queue, enable the NEXT button.
		 */
		if (qCount > 1)
			nextButton.enabled = true;
		else if (nextButton.text == "next")
			nextButton.enabled = false;

		/**
		 * If any squares have been used from the queue, enable the RESET
		 * button.
		 */

		if (qCount > 0)
			resetButton.enabled = true;
		else
			resetButton.enabled = false;

		/**
		 * If all squares are filled, change text from 'next' to 'finish'
		 */
		invalidate();
		return true;

	}

	Rect clipRect;
	Bitmap backgroundBMP, grayBMP, whiteBMP, blueBMP, scoreBMP;

	private void loadAssets()
	{
		Log.d(TAG, "loadAssets()");
		AssetManager assetManager = gameActivity.getAssets();
		InputStream inputStream;
		try
		{
			inputStream = assetManager.open("bg-clean.png");
			backgroundBMP = BitmapFactory.decodeStream(inputStream);

			inputStream = assetManager.open("box-gray.png");
			Square.grayBMP = BitmapFactory.decodeStream(inputStream);

			inputStream = assetManager.open("box-white.png");
			Square.whiteBMP = BitmapFactory.decodeStream(inputStream);

			inputStream = assetManager.open("box-blue.png");
			Square.blueBMP = BitmapFactory.decodeStream(inputStream);
			
			inputStream = assetManager.open("box-score.png");
			scoreBMP = BitmapFactory.decodeStream(inputStream);

		} catch (IOException e)
		{
			Log.d(TAG, "ERRROR!!!!!");
			e.printStackTrace();
		}
	}

	private void setDimensions(int width, int height)
	{
		float fl = (height * 1f) / 4f;
		int f = (int) fl;

		qLane = new Rect(0, 0, width, f);
		boardLane = new Rect(0, f, width, 3 * f);
		buttonLane = new Rect(0, 3 * f, width, height);

		int padding = 6;

		/**
		 * Height is almost definitely the limiting factor.
		 */
		squareSize = (boardLane.height() - 2 * padding) / gameModel.getWidth();

		int qBoxHorPad = (width - gameModel.getQSize() * squareSize) / 2;
		int qBoxVerPad = (f - squareSize) / 2;

		qBox = new Rect(qBoxHorPad, qBoxVerPad, qBoxHorPad + squareSize
				* gameModel.getQSize(), qBoxVerPad + squareSize);

		int boardBoxHorPad = (width - gameModel.getWidth() * squareSize) / 2;
		int boardBoxVerPad = (boardLane.height() - gameModel.getWidth()
				* squareSize) / 2;

		boardBox = new Rect(boardBoxHorPad, f + boardBoxVerPad, boardBoxHorPad
				+ gameModel.getWidth() * squareSize, f + boardBoxVerPad
				+ gameModel.getWidth() * squareSize);

		int buttonBoxVerticalPadding = (f - squareSize) / 2;
		// resetBox = new Rect(padding, (int)(3.14 * f +
		// buttonBoxVerticalPadding), width
		// / 2 - padding, height - buttonBoxVerticalPadding);
		// nextBox = new Rect(width / 2 + padding, (int) (3.14 * f
		// + buttonBoxVerticalPadding), width - padding, height
		// - buttonBoxVerticalPadding);
		resetBox = new Rect(padding,
				(int) (3.14 * f + buttonBoxVerticalPadding), width / 2
						- padding, height);
		nextBox = new Rect(width / 2 + padding,
				(int) (3.14 * f + buttonBoxVerticalPadding), width - padding,
				height);
	}

	public GameView(GameActivity gameActivity, GameModel gameModel)
	{
		super((Context) gameActivity);
		this.gameActivity = gameActivity;
		this.gameModel = gameModel;
		paint = new Paint();
		resetButton = new Button("undo");
		nextButton = new Button("next");
		endgame = false;

		dbHelper = new DBHelper((Context) gameActivity);
		loadAssets();
	}

	/**
	 * onDraw vars
	 */
	Paint paint;
	float x1, y1, x2, y2;
	Square square;

	@Override
	protected void onDraw(Canvas canvas)
	{

		
		/**
		 * Draw background
		 */

		Rect sourceRect = new Rect(0, 0, backgroundBMP.getWidth(),
				backgroundBMP.getHeight());
		Rect destRect = new Rect(0, 0, this.getWidth(), this.getHeight());
		canvas.drawBitmap(backgroundBMP, sourceRect, destRect, paint);

		/*
		 * If the game is over, we draw things differently
		 */
		if (endgame)
		{
			drawEndgame(canvas);
			return;
		}

		if (qLane == null)
		{
			setDimensions(this.getWidth(), this.getHeight());
			clipRect = new Rect();
		}

		drawBoard(canvas);

		drawQ(canvas);

		/**
		 * Draw buttons.
		 */

		resetButton.draw(canvas, resetBox);
		nextButton.draw(canvas, nextBox);

	}

	void drawBoard(Canvas canvas)
	{
		/**
		 * Draw grid on boardbox
		 */

		x1 = boardBox.left;
		x2 = boardBox.right;
		y1 = boardBox.top;
		y2 = boardBox.bottom;

		/**
		 * Draw squares.
		 * 
		 */
		for (int i = 0; i < gameModel.getWidth(); i++)
		{
			for (int j = 0; j < gameModel.getWidth(); j++)
			{
				clipRect.set((int) x1 + i * squareSize, (int) y1 + j
						* squareSize, (int) x1 + (i + 1) * squareSize, (int) y1
						+ (j + 1) * squareSize);

				gameModel.getSquare(i, j).draw(canvas, clipRect);
			}
		}

	}

	void drawBoardOldSchool(Canvas canvas)
	{
		/**
		 * Draw grid on boardbox
		 */

		x1 = boardBox.left;
		x2 = boardBox.right;
		y1 = boardBox.top;
		y2 = boardBox.bottom;

		/**
		 * Draw squares.
		 */
		for (int i = 0; i < gameModel.getWidth(); i++)
		{
			for (int j = 0; j < gameModel.getWidth(); j++)
			{
				clipRect.set((int) x1 + i * squareSize, (int) y1 + j
						* squareSize, (int) x1 + (i + 1) * squareSize, (int) y1
						+ (j + 1) * squareSize);

				gameModel.getSquare(i, j).draw(canvas, clipRect);
			}
		}

		/**
		 * draw board;
		 */
		for (int i = 0; i < gameModel.getWidth() + 1; i++)
		{
			for (int j = 0; j < gameModel.getWidth() + 1; j++)
			{
				paint.setColor(Color.DKGRAY);
				canvas.drawLine(x1 + i * squareSize, y1, x1 + i * squareSize,
						y2, paint);
				canvas.drawLine(x1, y1 + i * squareSize, x2, y1 + i
						* squareSize, paint);

				// canvas.drawText(""+square.val, x1+i*squareSize,
				// y1+j*squareSize, paint);

			}
		}
	}

	void drawQ(Canvas canvas)
	{
		/*
		 * Draw grid on qBox
		 */
		x1 = qBox.left;
		x2 = qBox.right;
		y1 = qBox.top;
		y2 = qBox.bottom;

		for (int i = 0; i < gameModel.getQSize(); i++)
		{
			clipRect.set((int) x1 + i * squareSize, (int) y1, (int) x1
					+ (i + 1) * squareSize, (int) y1 + squareSize);

			gameModel.getQSquare(i).draw(canvas, clipRect);
		}

	}

	void drawQOldSchool(Canvas canvas)
	{
		/*
		 * Draw grid on qBox
		 */
		x1 = qBox.left;
		x2 = qBox.right;
		y1 = qBox.top;
		y2 = qBox.bottom;

		for (int i = 0; i < gameModel.getQSize(); i++)
		{
			clipRect.set((int) x1 + i * squareSize, (int) y1, (int) x1
					+ (i + 1) * squareSize, (int) y1 + squareSize);

			gameModel.getQSquare(i).draw(canvas, clipRect);
		}

		for (int i = 0; i < gameModel.getQSize() + 1; i++)
		{
			canvas.drawLine(x1 + i * squareSize, y1, x1 + i * squareSize, y2,
					paint);
		}
		canvas.drawLine(x1, y1, x2, y1, paint);
		canvas.drawLine(x1, y2, x2, y2, paint);

	}

	void drawEndgame(Canvas canvas)
	{

		drawBoard(canvas);

		drawScoreOldSchool(canvas);

		/**
		 * Draw buttons.
		 */

		//resetButton.draw(canvas, resetBox);
		newGameRect = new Rect(this.getWidth()/4, nextBox.top, this.getWidth()*3/4, nextBox.bottom);
		nextButton.draw(canvas, newGameRect);
	}

	void drawScoreOldSchool(Canvas canvas)
	{
		int width, height;
		width = qBox.width();
		height = qBox.height();
		paint.reset();
		paint.setColor(Color.parseColor("#a1643d"));

		paint.setStyle(Style.FILL);
		paint.setAntiAlias(true);
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		paint.setTextSize(height * 0.7f);
		paint.setTextAlign(Paint.Align.CENTER);

		FontMetrics fm = paint.getFontMetrics();
		// Centering in X: use alignment (and X at midpoint)
		float x = width / 2;
		// Centering in Y: measure ascent/descent first
		float y = height / 2 - (fm.ascent + fm.descent) / 2;

		canvas.drawBitmap(scoreBMP, new Rect(0,0,scoreBMP.getWidth(), scoreBMP.getHeight()), qBox, paint);
		canvas.drawText("score : " + gameModel.findSequences(), x + qBox.left, y
				+ qBox.top, paint);
		
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(2);
		paint.setColor(Color.rgb(100,100,100));
		paint.setAntiAlias(true);
		canvas.drawText("score : " + gameModel.findSequences(), x + qBox.left, y
				+ qBox.top, paint);
		
	}

	private void topScoreDialog()
	{
		AlertDialog.Builder alert = new AlertDialog.Builder(gameActivity);

		alert.setTitle("New high score!");
		alert.setMessage("Name: ");

		// Set an EditText view to get user input
		final EditText input = new EditText(gameActivity);
		InputFilter[] FilterArray = new InputFilter[1];
		FilterArray[0] = new InputFilter.LengthFilter(12);
		input.setFilters(FilterArray);
		input.setText("");
		alert.setView(input);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton)
			{
				String name = input.getText().toString();
				dbHelper.addTopScore(C.WIDTH, gameModel.findSequences(), name);
			}
		});
		alert.setCancelable(false);
		alert.show();
	}
	
	public void onPause()
	{
		C.GAME_MODEL = gameModel;
	}
	
	public void onResume()
	{
		this.gameModel = C.GAME_MODEL;
		if(gameModel.boardCount() == 0)
		{
			endgame = true;
		}
		
		setDimensions(this.getWidth(), this.getHeight());
		loadAssets();
		
		invalidate();
	}
}
