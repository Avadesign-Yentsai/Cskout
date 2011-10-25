package com.ava.cskout;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class StartView extends Activity implements LocationListener
{
	/** Called when the activity is first created. */
	private ProgressDialog progressDialog;
	private String Lat,Lon;
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.startview);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);//customize titlebar
        
        String memberEmail="",memberPWD="";
        
        getLocation();
        
        InputStream XMLinputStream1=null;
        InputStream XMLinputStream2=null;
        try 
		{
			XMLinputStream1 = this.openFileInput("UserInfo.xml");
			XMLinputStream2 = this.openFileInput("UserInfo.xml");
			memberEmail=ProcessXML.ReadXML(XMLinputStream1,"memberEmail");
			memberPWD=ProcessXML.ReadXML(XMLinputStream2,"memberPWD");
		} catch (FileNotFoundException e) 
		{
			e.printStackTrace();
			memberEmail="nodata";
		}
		
        /*
        TextView test=(TextView)findViewById(R.id.startview_testView);
        test.setText(memberPWD);
        */
        
        
        if(memberEmail.equals("nodata"))//沒登入資料轉至登入畫面
        {
        	finish(); 
        	Intent intent=new Intent();
            intent.setClass(StartView.this, Login.class);
           	startActivity(intent);
        }
        else //有資料
        {
        	
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Please wait...");
            progressDialog.setIndeterminate(true);
     		progressDialog.setCancelable(false);
     		progressDialog.show();
			 
			doLogin(memberEmail, memberPWD,Lat,Lon);
        }
        
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
					
					//Toast.makeText(StartView.this,strResult, Toast.LENGTH_SHORT).show();

					if (strResult.equals("true"))
					{
						progressDialog.dismiss();
						finish(); 
						Intent intent=new Intent();
			            intent.setClass(StartView.this, Menu.class);
			           startActivity(intent);
			        }
					else 
					{
						progressDialog.dismiss();
						finish();
						Intent intent=new Intent();
			            intent.setClass(StartView.this, Login.class);
			            startActivity(intent);
					}
					
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
    
    
    private LocationManager locationManager;
    private void getLocation() 
	{
		
		locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
		
	}
	
	
	 
	 public void onLocationChanged(Location location) 
	 {
		Lat=Double.toString(location.getLatitude());
		Lon=Double.toString(location.getLongitude());
		//Toast.makeText(StartView.this,Lat+" "+Lon, Toast.LENGTH_SHORT).show();
		
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
