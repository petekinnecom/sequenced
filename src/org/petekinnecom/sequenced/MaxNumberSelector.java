package org.petekinnecom.sequenced;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


public class MaxNumberSelector extends Activity implements OnItemClickListener
{

	private static final String TAG = "MaxNumberSelector";
	
	private ListView listView;

	
	
	class DisableAdapter extends ArrayAdapter
	{
		private int[] fonts = { Color.WHITE, Color.GRAY };

		public DisableAdapter(Context context, int textViewResourceId,
				MenuPair[] mp)
		{
			super(context, textViewResourceId, mp);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			TextView view = (TextView) super.getView(position, convertView,
					parent);
			view.setTextColor(Color.WHITE);

			return view;
		}
	}

	
	private void initAdapter()
	{

		listView = (ListView) findViewById(R.id.ListView);
		DisableAdapter adapter = new DisableAdapter(this,
				android.R.layout.simple_list_item_1, C.MAX_NUMBERS[C.WIDTH - 3]);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		
	}
	
	@Override
	public void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);


		/**
		 * Used for the String adapter.
		 */

		setContentView(R.layout.listview);

		initAdapter();

		setTitle("Playable numbers:");

	}
	
	@Override
	public void onItemClick(AdapterView<?> eh, View who_cares, int index, long whats_this_for_I_dont_care)
	{
		if(C.MAX_NUMBERS[C.WIDTH - 3][index].enabled)
		{
			C.MAX_NUMBER = C.MAX_NUMBERS[C.WIDTH - 3][index].data;
			startActivity(new Intent(this, GameActivity.class));
			this.finish();
		}
	}

	
}