package org.petekinnecom.sequenced;

import android.graphics.Bitmap;

public class C
{
	public static final boolean DEBUG = false;
	public static final MenuPair[] SIZES = {new MenuPair("3 x 3", true, 3), new MenuPair("4 x 4", true, 4), new MenuPair("5 x 5", true, 5), new MenuPair("", false, 0)};
	
	private static final MenuPair twelve = new MenuPair("0 - 12", true, 13);
	private static final MenuPair twentyfour = new MenuPair("0 - 24", true, 25);
	private static final MenuPair thirtysix = new MenuPair("0 - 36", true, 37);
	
	private static final MenuPair[] x3 = {twelve, new MenuPair("", false, 0)};
	private static final MenuPair[] x4 = {twentyfour, new MenuPair("", false, 0)};
	private static final MenuPair[] x5 = {thirtysix, new MenuPair("", false, 0)};
	
	public static final MenuPair[][] MAX_NUMBERS = {x3, x4, x5};
	
	public static boolean NEW_GAME_FLAG = true;
	
	public static final MenuPair[] GAME_TYPES = {new MenuPair("New game", true, 0), new MenuPair("High scores", true, 0), new MenuPair("Instructions", true, 0), new MenuPair("About", true, 0)};
	
	public static final int WIDTH = 4;
	public static int MAX_NUMBER = 13;
	
	public static Bitmap BG_CLEAN = null;
	
	public static GameModel GAME_MODEL = null;
	
}
