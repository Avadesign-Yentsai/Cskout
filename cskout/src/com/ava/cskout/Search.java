package com.ava.cskout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Search extends Activity 
{
	private static final String[] agender = {"Male", "Female","Both"};
	private static final String[] ainterested = {"Male", "Female","Both"};
	private ArrayAdapter<String> genderadapter;
	private ArrayAdapter<String> interestedadapter;
	private String Sgender,Sinterested;
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.search);
        
        final EditText minage = (EditText)findViewById(R.id.search_minage);
        final EditText maxage = (EditText)findViewById(R.id.search_maxage);
        Spinner gender=(Spinner)findViewById(R.id.search_gender);
        Spinner interested=(Spinner)findViewById(R.id.search_interested);
        Button submit =(Button)findViewById(R.id.search_submit);
        
        genderadapter =new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,agender);
        genderadapter .setDropDownViewResource(R.layout.spinner_dropdown);
        gender.setAdapter(genderadapter);
        gender.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
		{
        	@Override
        	public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2,long arg3)
        	{
        		Sgender=agender[arg2];
        		arg0.setVisibility(View.VISIBLE);
        	}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
        
        interestedadapter =new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,ainterested);
        interestedadapter .setDropDownViewResource(R.layout.spinner_dropdown);
        interested.setAdapter(interestedadapter);
        interested.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
		{
        	@Override
        	public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2,long arg3)
        	{
        		Sinterested=ainterested[arg2];
        		arg0.setVisibility(View.VISIBLE);
        	}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
        submit.setOnClickListener(new Button.OnClickListener()
        {
        	public void onClick(View V)
        	{
        		if(Integer.parseInt(maxage.getText().toString())<Integer.parseInt(minage.getText().toString()))
        		{
        			Toast.makeText(Search.this, "¦~ÄÖ½d³ò¿ù»~", Toast.LENGTH_SHORT).show();
        			minage.requestFocus();
        		}
        		 TextView test=(TextView)findViewById(R.id.search_testview);
        	     test.setText("gender:"+Sgender+"int"+Sinterested+"ming"+minage.getText().toString()+"maxg"+maxage.getText().toString());
        	}
        });
       
        
	}
}
