package com.ava.cskout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class Menu extends Activity 
{
	private ImageButton but_findflirts,but_flirtsbuzz,but_lookatme,but_myhotlist,but_mychats,but_myprofiles,but_points,but_setting;
	private TextView maintestview;
	private String memberEmail;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.menu);
        
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);//customize titlebar
        
        ImageButton litchat=(ImageButton)findViewById(R.id.litchat);
        ImageButton litmenu=(ImageButton)findViewById(R.id.litmenu);
        litmenu.setVisibility(View.GONE);//invisible=1 
        
        
        but_findflirts=(ImageButton)findViewById(R.id.but_findflirts);
        but_flirtsbuzz=(ImageButton)findViewById(R.id.but_flirtsbuzz);
        
        but_lookatme=(ImageButton)findViewById(R.id.but_lookatme);
        but_myhotlist=(ImageButton)findViewById(R.id.but_myhotlist);
        
        but_mychats=(ImageButton)findViewById(R.id.but_mychats);
        but_myprofiles=(ImageButton)findViewById(R.id.but_myprofiles);
        but_points=(ImageButton)findViewById(R.id.but_points);
        but_setting=(ImageButton)findViewById(R.id.but_setting);
        maintestview=(TextView)findViewById(R.id.maintestview);
        
        InputStream XMLinputStream1=null;
        InputStream XMLinputStream2=null;
        try 
		{
			XMLinputStream1 = this.openFileInput("UserInfo.xml");
			XMLinputStream2 = this.openFileInput("UserInfo.xml");
			memberEmail=ProcessXML.ReadXML(XMLinputStream1,"memberEmail");
	        maintestview.setText(memberEmail+","+ProcessXML.ReadXML(XMLinputStream2,"memberPWD"));

		} catch (FileNotFoundException e) 
		{
			e.printStackTrace();
			
		}
       
        
        but_findflirts.setOnClickListener(new Button.OnClickListener()
        {
        	public void onClick(View V)
        	{
        		Intent intent=new Intent();
                intent.setClass(Menu.this, Find_Flirts.class);
                Bundle bundle =new Bundle();
                bundle.putString("KEY_minage", "");
                bundle.putString("KEY_maxage", "");
                bundle.putString("KEY_gender", "");
                bundle.putString("KEY_interested", "");
                intent.putExtras(bundle);
               	startActivity(intent);
        	}
        });
        but_flirtsbuzz.setOnClickListener(new Button.OnClickListener()
        {
        	public void onClick(View V)
        	{
        		Intent intent=new Intent();
                intent.setClass(Menu.this, Flirt_Buzz.class);
               	startActivity(intent);
        	}
        });
        
        but_myprofiles.setOnClickListener(new Button.OnClickListener()
        {
        	public void onClick(View V)
        	{
        		Intent intent=new Intent();
                intent.setClass(Menu.this, Member.class);
                Bundle bundle =new Bundle();
                bundle.putString("KEY_mail", memberEmail);
                intent.putExtras(bundle);
                startActivity(intent);
        	}
        });
        but_mychats.setOnClickListener(new Button.OnClickListener()
        {
        	public void onClick(View V)
        	{
        		
        	}
        });
        
        
       
     }
    @Override
	public void onBackPressed() 
	{
		finish();
	}
}