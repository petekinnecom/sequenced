package org.petekinnecom.sequenced;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements OnClickListener
{

	public void onCreate(Bundle b)
	{
		super.onCreate(b);
		setContentView(R.layout.main);

		// Set up click listeners for all the buttons
		View continueButton = findViewById(R.id.start_button);
		continueButton.setOnClickListener(this);
	}
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.start_button:
			Intent i = new Intent(this, GameActivity.class);
			startActivity(i);
			break;
		}
		
	}

}
