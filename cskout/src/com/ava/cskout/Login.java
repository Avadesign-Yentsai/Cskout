package com.ava.cskout;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Xml;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends Activity implements LocationListener
{
	/** Called when the activity is first created. */
	private ProgressDialog progressDialog;
	private EditText memberEmail;
	private EditText memberPWD; 
	private Button btn_login,btn_register;
	private TextView testview;
	
	private String Lat="0",Lon="0";
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);//customize titlebar
        
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setIndeterminate(true);
 		progressDialog.setCancelable(false);
 
 		memberEmail = (EditText) findViewById(R.id.memberEmail);
 		memberPWD = (EditText) findViewById(R.id.memberPWD);
 		btn_login = (Button) findViewById(R.id.okbutton); 
 		btn_register= (Button) findViewById(R.id.register); 
 		testview =(TextView) findViewById(R.id.login_textView);
 		
 		getLocation();
 		
 		btn_login.setOnClickListener(new Button.OnClickListener()
 		{
 			 public void onClick(View v) 
 			 {
				 int usersize = memberEmail.getText().length();
				 int passsize = memberPWD.getText().length();
				 if(usersize > 0 && passsize > 0) 
				 {
					 progressDialog.show();
					 
					 doLogin(memberEmail.getText().toString(), memberPWD.getText().toString(),Lat,Lon);
				 } 
				 else 
				{
					 AlertDialog ad = new AlertDialog.Builder(Login.this)
					 .setPositiveButton("Ok", null)
					 .setTitle("Error")
					 .setMessage("Please enter Username and Password")
					 .create();
					 ad.show();
				}
 			 }
 		});
 		
 		btn_register.setOnClickListener(new Button.OnClickListener()
 		{
 			 public void onClick(View v) 
 			 {
 				Intent intent=new Intent();
                intent.setClass(Login.this, Register.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               	startActivity(intent);
 			 }
 		});
 		
    }
	
	private void doLogin(final String memberEmail, final String memberPWD, final String Lat, final String Lon) 
	{
		
        //final String pw = md5(pass);
		Thread t = new Thread() 
		{
			public void run() 
			{
				String strResult="";
				Looper.prepare();
				HttpEntity entity;         
				try 
				{
					
					HttpPost httpRequest = new HttpPost(GetServerInfo.getWebPath()+"login.jsp");
					List <NameValuePair> params = new ArrayList <NameValuePair>();
					params.add(new BasicNameValuePair("memberEmail", memberEmail));
					params.add(new BasicNameValuePair("memberPWD", memberPWD));
					params.add(new BasicNameValuePair("Lat", Lat));
					params.add(new BasicNameValuePair("Lon", Lon));
					httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
					HttpResponse response = new DefaultHttpClient().execute(httpRequest);
					
					entity = response.getEntity();
					strResult = EntityUtils.toString(entity);
					strResult =EregiReplace.eregi_replace("(\r\n|\r|\n|\n\r| |)", "", strResult);
					
					if (strResult.equals("true"))
					{
						progressDialog.dismiss();
						 /*
	                    AlertDialog ad = new AlertDialog.Builder(Login.this)
						 .setPositiveButton("Ok", null)
						 .setTitle("OK")
						 .setMessage("Login Success")
						 .create();
						 ad.show();
						 */
						 writeXML("UserInfo.xml",outXml(memberEmail,memberPWD));
						 //testVresult(ReadXML("UserInfo.xml","memberEmail"));
						 finish(); 
						 Intent intent=new Intent();
			             intent.setClass(Login.this, Menu.class);
			             startActivity(intent);
			               	
						 //testVresult(strResult);
						 
						 
					}
					else if(strResult.equals("false"))
					{
						progressDialog.dismiss();
						 
	                    AlertDialog ad = new AlertDialog.Builder(Login.this)
						 .setPositiveButton("Ok", null)
						 .setTitle("Error")
						 .setMessage("Invald Account or Password")
						 .create();
						 ad.show();
						 testVresult(strResult);
					}
					else
					{
						progressDialog.dismiss();
						 
	                    AlertDialog ad = new AlertDialog.Builder(Login.this)
						 .setPositiveButton("Ok", null)
						 .setTitle("Error")
						 .setMessage(strResult)
						 .create();
						 ad.show();
						 testVresult(strResult);
					}
					
					InputStream is = entity.getContent();
					//read(is);
					is.close();
					if (entity != null) entity.consumeContent();
					
                } catch(ClientProtocolException e)
        		{
                	progressDialog.dismiss();
        			strResult=e.getMessage().toString();
        			e.printStackTrace();
        			
        		}
        		catch(IOException e)
        		{
        			progressDialog.dismiss();
        			strResult=e.getMessage().toString();
        			e.printStackTrace();
        			
        		}
        		catch(Exception e)
        		{
        			progressDialog.dismiss();
        			strResult=e.getMessage().toString();
        			e.printStackTrace();
        			
        		}
 
                Looper.loop();               
            }
		};
		t.start();
    } 
	
	private void testVresult(String result)
	{
		 testview.setText(result);
	}
	
	
	public boolean writeXML(String path,String txt)
    {
	    try
	    {
		    OutputStream os = openFileOutput(path,MODE_PRIVATE);
		    OutputStreamWriter osw=new OutputStreamWriter(os);
		    osw.write(txt);
		    osw.close();
		    os.close();
	    }
	    catch(FileNotFoundException e)
	    {
	    	return false;
	    }catch(IOException e)
	    {
	    	return false;
	    }
	return true;
    }
	 	 
	private String outXml(String memberEmail,String memberPWD)
    {
    	XmlSerializer serializer = Xml.newSerializer();
    	StringWriter writer = new StringWriter();
    	try{
    	serializer.setOutput(writer);

    	// <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    	serializer.startDocument("UTF-8",true);

    	//<userinfo data="2009-09-23">
    	serializer.startTag("","userinfo");
    	//serializer.attribute("","date","2009-09-23");
    	
    	// <memberEmail>Android XML</memberEmail>
    	serializer.startTag("","memberEmail");
    	serializer.text(memberEmail);
    	serializer.endTag("","memberEmail");
    	
    	serializer.startTag("","memberPWD");
    	serializer.text(memberPWD);
    	serializer.endTag("","memberPWD");
    	
    	//<userinfo>
    	serializer.endTag("","userinfo");
    	
    	
    	serializer.endDocument();
    	return writer.toString();
    	}
    	catch(Exception e)
    	{
    	throw new RuntimeException(e);
    	}
    }
	

	private LocationManager locationManager;
	private void getLocation() 
	{
		
		locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
	}
	
	
	 
	 public void onLocationChanged(Location location) 
	 {
		Lat=Double.toString(location.getLatitude());
		Lon=Double.toString(location.getLongitude());
	 }

	 
	 public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	 }
	 
	 public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	 }

	 
	 public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	 }
	 

	 @Override
	 protected void onResume()
	 {
		 super.onResume();
		 locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
		 locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
	 }
   
	 @Override
	 protected void onPause()
	 {
		 super.onPause();
		 locationManager.removeUpdates(this);
	 }
	 
}
