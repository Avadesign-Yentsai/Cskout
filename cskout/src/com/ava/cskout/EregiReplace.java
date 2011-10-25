package com.ava.cskout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EregiReplace 
{
	public static String eregi_replace(String strFrom,String strTo,String strTarget)
	{
		String strPattern="(?i)"+strFrom;
		Pattern p =Pattern.compile(strPattern);
		Matcher m =p.matcher(strTarget);
		if(m.find())
		{
			return strTarget.replaceAll(strFrom, strTo);
		}
		else
		{
			return strTarget;
		}
	}
}
