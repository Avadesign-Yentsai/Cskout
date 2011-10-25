package com.ava.cskout;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class GetURLBitmap 
{
	public static Bitmap getURLBitmap(String uriPic)
    {
		URL imgURL=null;
		Bitmap bitmap =null;
		try
		{
			imgURL=new URL(uriPic);
		}
		catch(MalformedURLException e)
		{
			e.printStackTrace();
		}
		try
		{
			HttpURLConnection conn=(HttpURLConnection)imgURL.openConnection();
			conn.connect();
			InputStream is=conn.getInputStream();
			bitmap=BitmapFactory.decodeStream(is);
			is.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
    	return bitmap;
    }
}
