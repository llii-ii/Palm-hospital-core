package com.kasite.core.common;

public class Version {

	public static String V = "1.0.0";
	
	public static void print()
	{	
		System.setProperty("vers.kasite", V);
	}
}
