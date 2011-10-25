package com.ava.cskout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class HttpPostResponse 
{
	public static String getHttpResponse (String[] input)//HTTP-POST
	{
		/*
		 [0]URL
		 [1]Attribute
		 [2]Value
		 [3]Attribute
		 [4]Value
		 [5]...
		 [6]...
		*/
		String strResult="";
		HttpPost httpRequest=new HttpPost(input[0]);
		List <NameValuePair> params=new ArrayList <NameValuePair>();
		for(int i=1;i<input.length;i++)
		{
			if(i %2 !=0)
			{
				params.add(new BasicNameValuePair(input[i],input[i+1]));
			}
		}
		try
		{
			httpRequest.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
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
