package org.petekinnecom.sequenced;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;

public class GameActivity extends Activity
{

	GameView gameView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		
		GameModel gameModel = new GameModel();
		if(C.GAME_MODEL != null)
			gameModel = C.GAME_MODEL;
		gameView = new GameView(this, gameModel);
		setContentView(gameView);
		
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		gameView.onPause();
	}
	

}