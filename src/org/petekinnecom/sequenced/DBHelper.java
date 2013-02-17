package org.petekinnecom.sequenced;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DBHelper extends SQLiteOpenHelper
{

	private static final String DATABASE_NAME = "high_scores.db";
	private static final int DATABASE_VERSION = 1;
	private static final String TAG = "DBHelper";

	private Context context;

	public DBHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		Log.d(TAG, "onCreate");
		db.execSQL("CREATE TABLE scores ("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "size INT NOT NULL, " + "name TEXT NOT NULL, "
				+ "score INT NOT NULL, " + "date STRING NOT NULL " + ");");
	}

	public void resetDB()
	{
		SQLiteDatabase db = this.getWritableDatabase();
		try
		{
			db.execSQL("DROP TABLE scores");
			db.execSQL("CREATE TABLE scores ("
					+ "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ "size INT NOT NULL, " + "name TEXT NOT NULL, "
					+ "score INT NOT NULL, " + "date STRING NOT NULL " + ");");
		} catch (Exception e)
		{
			Log.d(TAG, e.getMessage());
		} finally
		{
			db.close();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2)
	{
		// TODO Auto-generated method stub
		Log.d(TAG, "onUpgrade");
	}

	private int getBottomScore(int size)
	{
		size = 4;
		SQLiteDatabase db = this.getReadableDatabase();
		int lowScore = -1;
		try
		{

			Log.d(TAG, "Reading scores for size: " + size);

			Cursor c = db.rawQuery("SELECT * FROM scores WHERE size=" + size
					+ " ORDER BY score DESC", new String[] {});

			/**
			 * If there are fewer than four records, any score gets in.
			 */
			if (c.getCount() <= 4)
			{
				Log.d(TAG, "We have fewer than four records.  Free high score!");
				return -1;
			}
			for (int i = 0; i < 4; i++)
			{
				c.moveToNext();
				Log.d(TAG,
						"hiscore " + i + " : "
								+ c.getInt(c.getColumnIndex("score")));
			}
			lowScore = c.getInt(c.getColumnIndex("score"));

			Log.d(TAG, "Finished reading scores");
		} catch (Exception e)
		{
			Log.d(TAG, e.getMessage());
			// resetDB();

		} finally
		{
			if (db != null)
				db.close();
		}
		return lowScore;
	}

	public MenuPair[] getTopScores(int size)
	{

		ArrayList<MenuPair> topScores = new ArrayList<MenuPair>();
		int i = 0;
		SQLiteDatabase db = this.getReadableDatabase();
		try
		{

			Log.d(TAG, "Reading scores for size: " + size);

			Cursor c = db.rawQuery("SELECT * FROM scores WHERE size=" + size
					+ " ORDER BY score DESC", new String[] {});

			String s;
			while (c.moveToNext() && i < 10)
			{
				s = c.getString(c.getColumnIndex("date")) + "\n"
						+ c.getString(c.getColumnIndex("score")) + " :: "
						+ c.getString(c.getColumnIndex("name"));
				topScores.add(new MenuPair(s, true, 0));
				i++;
			}

			Log.d(TAG, "Finished reading scores");
		} catch (Exception e)
		{
			Log.d(TAG, e.getMessage());
			// resetDB();

		} finally
		{
			if (db != null)
				db.close();
		}

		MenuPair[] scores = new MenuPair[topScores.size()];
		i = 0;
		for (MenuPair p : topScores)
		{
			scores[i] = p;
			i++;
		}

		return scores;
	}

	public ArrayList<HighScore> getHighScores()
	{

		/** hard code size to 4 just cause */
		int size = 4;

		ArrayList<HighScore> tmp = new ArrayList<HighScore>();
		SQLiteDatabase db = this.getReadableDatabase();
		int i = 0;
		try
		{

			Log.d(TAG, "Reading scores for size: " + size);

			Cursor c = db.rawQuery("SELECT * FROM scores WHERE size=" + size
					+ " ORDER BY score DESC", new String[] {});

			while (c.moveToNext() && i < 4)
			{
				tmp.add(new HighScore(c.getString(c.getColumnIndex("name")), c
						.getInt(c.getColumnIndex("score")), c.getString(c
						.getColumnIndex("date"))));
				i++;
			}

			Log.d(TAG, "Finished reading scores");
		} catch (Exception e)
		{
			Log.d(TAG, e.getMessage());
			// resetDB();

		} finally
		{
			if (db != null)
				db.close();
		}

		return tmp;
	}

	public ArrayList<HighScore> getHighScoresOLD(int size)
	{
		ArrayList<HighScore> tmp = new ArrayList<HighScore>();
		SQLiteDatabase db = this.getReadableDatabase();
		int i = 0;
		try
		{

			Log.d(TAG, "Reading scores for size: " + size);

			Cursor c = db.rawQuery("SELECT * FROM scores WHERE size=" + size
					+ " ORDER BY score DESC", new String[] {});

			String s;
			while (c.moveToNext() && i < 10)
			{
				tmp.add(new HighScore(c.getString(c.getColumnIndex("name")), c
						.getInt(c.getColumnIndex("score")), c.getString(c
						.getColumnIndex("date"))));
				i++;
			}

			Log.d(TAG, "Finished reading scores");
		} catch (Exception e)
		{
			Log.d(TAG, e.getMessage());
			// resetDB();

		} finally
		{
			if (db != null)
				db.close();
		}

		return tmp;
	}

	public boolean isTopScore(int size, int score)
	{
		size = 4;
		if (score > getBottomScore(size))
			return true;
		return false;
	}

	public String getDate()
	{
		Date date = new Date();
		// String DATE_FORMAT = ""+SimpleDateFormat.SHORT;
		SimpleDateFormat sdf = (SimpleDateFormat) DateFormat
				.getDateInstance(DateFormat.SHORT);
		// System.out.println("Today is " + sdf.format(date));
		return sdf.format(date);

	}

	private void deleteBottomScore(int size)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		try
		{

			Cursor c = db.rawQuery("SELECT * FROM scores ORDER BY score ASC",
					new String[] {});

			c.moveToNext();

			int id = c.getInt(c.getColumnIndex("_id"));
			db.rawQuery("DELETE FROM scores WHERE _id=" + id, new String[] {});

		} catch (Exception e)
		{
			Log.d(TAG, e.getMessage());
			// resetDB();

		} finally
		{
			if (db != null)
				db.close();
		}
	}

	public void addTopScore(int size, int score, String name)
	{

		/**
		 * First we delete the bottom score if needed
		 */

		if (getBottomScore(size) != -1)
		{
			deleteBottomScore(size);
		}

		SQLiteDatabase db = this.getWritableDatabase();

		if (name.equals(""))
			name = "Somebody";

		ContentValues cv = new ContentValues();

		try
		{
			cv.put("name", name);
			cv.put("size", size);
			cv.put("score", score);
			cv.put("date", getDate());

			db.insert("scores", null, cv);

		} catch (Exception e)
		{
			if (C.DEBUG)
				Log.d(TAG, "addTopScore: " + e.getMessage());
		} finally
		{
			db.close();
		}
		if (C.DEBUG)
			Log.d(TAG, "Wrote topScore");

	}

}
