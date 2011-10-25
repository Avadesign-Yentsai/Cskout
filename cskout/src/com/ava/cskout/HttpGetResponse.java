package com.ava.cskout;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


public class HttpGetResponse 
{
	public static String getHttpResponse (String URL)//HTTP-GET
	{
		String strResult="";
		HttpGet httpRequest=new HttpGet(URL);
		try
		{
			HttpResponse httpResponse=new DefaultHttpClient().execute(httpRequest);
			if(httpResponse.getStatusLine().getStatusCode()==200)
			{
				strResult=EntityUtils.toString(httpResponse.getEntity());
			}
			else
			{
				strResult="Error Response"+httpResponse.getStatusLine().toString();
			}
		}
		catch(ClientProtocolException e)
		{
			strResult=e.getMessage().toString();
			e.printStackTrace();
		}
		catch(IOException e)
		{
			strResult=e.getMessage().toString();
			e.printStackTrace();
		}
		catch(Exception e)
		{
			strResult=e.getMessage().toString();
			e.printStackTrace();
		}
		return strResult;
	}
}
