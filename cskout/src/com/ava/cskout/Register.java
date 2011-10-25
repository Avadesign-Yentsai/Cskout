package com.ava.cskout;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.Calendar;

import org.xmlpull.v1.XmlSerializer;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends Activity 
{
	private TextView reg_Email,reg_PWD,reg_rePWD,reg_name,testView;;
	private Button reg_birthday,reg_sub;
	private Spinner reg_gender,reg_interested;
	static final int DATE_DIALOG_ID = 0; 
	private static final String[] gender = {"Male", "Female"};
	private static final String[] interested = {"Male", "Female","Both"};
	private ArrayAdapter<String> genderadapter;
	private ArrayAdapter<String> interestedadapter;
	private int mYear;
	private int mMonth;
	private int mDay;
	private StringBuilder birthday;
	private String Sgender,Sinterested;
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.register);
        
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);//customize titlebar
        
        reg_Email=(TextView)findViewById(R.id.reg_Email);
        reg_PWD=(TextView)findViewById(R.id.reg_PWD);
        reg_rePWD=(TextView)findViewById(R.id.reg_rePWD);
        reg_name=(TextView)findViewById(R.id.reg_name);
        reg_gender=(Spinner)findViewById(R.id.reg_gender);
        reg_birthday=(Button)findViewById(R.id.reg_birthday);
        reg_interested=(Spinner)findViewById(R.id.reg_interested);
        testView=(TextView)findViewById(R.id.testView);
        reg_sub=(Button)findViewById(R.id.reg_sub);

        
        genderadapter =new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,gender);
        genderadapter .setDropDownViewResource(R.layout.spinner_dropdown);
        reg_gender.setAdapter(genderadapter);
        reg_gender.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
		{
        	@Override
        	public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2,long arg3)
        	{
        		Sgender=gender[arg2];
        		arg0.setVisibility(View.VISIBLE);
        	}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
        
        interestedadapter =new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,interested);
        interestedadapter .setDropDownViewResource(R.layout.spinner_dropdown);
        reg_interested.setAdapter(interestedadapter);
        reg_interested.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
		{
        	@Override
        	public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2,long arg3)
        	{
        		Sinterested=interested[arg2];
        		arg0.setVisibility(View.VISIBLE);
        	}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
        
        
        reg_birthday.setOnClickListener(new Button.OnClickListener()
        {
        	public void onClick(View V)
        	{
        		showDialog(DATE_DIALOG_ID);
        	}
        });
        
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        
        updateDisplay();
        
        reg_sub.setOnClickListener(new Button.OnClickListener()
        {
        	public void onClick(View V)
        	{
        		if((reg_PWD.getText().toString()).equals(reg_rePWD.getText().toString()))
        		{
        			String result=HttpGetResponse.getHttpResponse(GetServerInfo.getWebPath()+
	                		"register.jsp?memberEmail="+reg_Email.getText().toString()+
	                		"&memberGender="+Sgender+"&memberName="+reg_name.getText()+
	                		"&memberBD="+birthday.toString()+
	                		"&memberInterested="+Sinterested+
	                		"&memberPWD="+reg_PWD.getText().toString());
	                result=EregiReplace.eregi_replace("(\r\n|\r|\n|\n\r| |)", "", result);
	                //testView.setText(result);
	                if(result.equals("invaidEmail"))
	                {
	                	Toast.makeText(Register.this, "Email已經被使用", Toast.LENGTH_SHORT).show();
	                	reg_Email.requestFocus();
	                }
	                else if(result.equals("true"))
	                {
	                	writeXML("UserInfo.xml",outXml(reg_Email.getText().toString(),reg_PWD.getText().toString()));
	                	{
	                		Intent intent=new Intent();
				            intent.setClass(Register.this, StartView.class);
				            startActivity(intent);
	                	}
	                }
        			
        		}
        		else
        		{
        			Toast.makeText(Register.this, "Recomfirm Password", Toast.LENGTH_SHORT).show();
        			reg_PWD.requestFocus();
        		}
        	}
        });

        
    }
	
	private void updateDisplay() 
	{	// Month is 0 based so add 1
		birthday=(new StringBuilder().append(mYear).append("-").append(mMonth + 1).append("-").append(mDay));
		reg_birthday.setText(birthday.toString());
    }
 
    @Override
    protected Dialog onCreateDialog(int id)
    {
        switch (id) 
        {
        	case DATE_DIALOG_ID:
            return new DatePickerDialog(this,mDateSetListener, mYear-25, mMonth, mDay);
        }
        return null;
    }
 
    // the callback received when the user "sets" the date in the dialog
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() 
    {
    	public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth)
    	{
	        mYear = year;
		    mMonth = monthOfYear;
		    mDay = dayOfMonth;
		    updateDisplay();
        }
    };     
    
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

    	//</userinfo>
    	serializer.endTag("","userinfo");
    	
    	serializer.endDocument();
    	return writer.toString();
    	}
    	catch(Exception e)
    	{
    	throw new RuntimeException(e);
    	}
    }
}
