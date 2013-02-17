package org.petekinnecom.sequenced;

class MenuPair{
	String s;
	boolean enabled;
	int data;
	
	MenuPair(String s, boolean b, int d)
	{
		this.s = s;
		this.enabled = b;
		this.data = d;
	}
	
	@Override
	public String toString()
	{
		return s;
	}
}