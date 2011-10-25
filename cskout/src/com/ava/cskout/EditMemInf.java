package com.ava.cskout;

import java.io.FileNotFoundException;
import java.io.InputStream;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class EditMemInf extends Activity
{
	 private EditText edit_name,edit_ABme;
	 private Button edit_birthday,edit_save;
	 private Spinner edit_gender,edit_interested;
	 private String[] memberInfo;
	 private String memberEmail,memberPWD;
	 private static final String[] gender = {"Male", "Female"};
	 private static final String[] interested = {"Male", "Female","Both"};
	 private ArrayAdapter<String> genderadapter;
	 private ArrayAdapter<String> interestedadapter;
	 static final int DATE_DIALOG_ID = 0; 
	 private int mYear;
	 private int mMonth;
	 private int mDay;
	 private StringBuilder birthday;
	 private String Sgender,Sinterested;

	 public void onCreate(Bundle savedInstanceState) 
	 {
		super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
	    setContentView(R.layout.edit_meminf);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);//customize titlebar
        
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
		}
        
        edit_name=(EditText)findViewById(R.id.edit_name);
        edit_ABme=(EditText)findViewById(R.id.edit_ABme);
        edit_birthday=(Button)findViewById(R.id.edit_birthday);
        edit_save=(Button)findViewById(R.id.edit_save);
        edit_gender=(Spinner)findViewById(R.id.edit_gender);
        edit_interested=(Spinner)findViewById(R.id.edit_interested);
        
        genderadapter =new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,gender);
        genderadapter .setDropDownViewResource(R.layout.spinner_dropdown);
        interestedadapter =new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,interested);
        interestedadapter .setDropDownViewResource(R.layout.spinner_dropdown);

        memberInfo= (EregiReplace.eregi_replace("(\r\n|\r|\n|\n\r)", "", HttpGetResponse.getHttpResponse(GetServerInfo.getWebPath()+"member.jsp?memberEmail="+memberEmail))).split(":SPLIT:");
        /*
        memberInfo[0] Name
        memberInfo[1] Gender
        memberInfo[2] Birthday(yyyy-MM-dd)
        memberInfo[3] Location-Loc
        memberInfo[4] Location-Lan
        memberInfo[5] Interested in
        memberInfo[6] About me
        memberInfo[7] 主要照
        memberInfo[8]-[11] 其他照(包含主要照)
       	*/
        edit_name.setText(memberInfo[0]);
        edit_ABme.setText(memberInfo[6]);
        edit_birthday.setText(memberInfo[2]);
        birthday=(new StringBuilder().append(memberInfo[2]));
        edit_gender.setAdapter(genderadapter);
        if(memberInfo[1].equals("Male"))
        {
            edit_gender.setSelection(0);
            Sgender=gender[0];
        }
        else
        {
        	edit_gender.setSelection(1);
        	Sgender=gender[1];
        }
        edit_interested.setAdapter(interestedadapter);
        if(memberInfo[5].equals("Male"))
        {
        	edit_interested.setSelection(0);
        	Sinterested=interested[0];
        }
        else if(memberInfo[5].equals("Female"))
        {
        	edit_interested.setSelection(1);
        	Sinterested=interested[1];
        }
        else
        {
        	edit_interested.setSelection(2);
        	Sinterested=interested[2];
        }
        String[] memberBD=memberInfo[2].split("-");
        mYear= Integer.parseInt(memberBD[0]);
        mMonth= Integer.parseInt(memberBD[1]);
        mDay= Integer.parseInt(memberBD[2]);
        edit_birthday.setOnClickListener(new Button.OnClickListener()
        {
        	public void onClick(View V)
        	{
        		showDialog(DATE_DIALOG_ID);
        	}
        });
        
        edit_gender.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
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
        
        edit_interested.setOnItemSelectedListener(new Spinner.OnItemSelectedListener()
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
        
        edit_save.setOnClickListener(new Button.OnClickListener()
        {
        	public void onClick(View V)
        	{
        		String[] input;
        		input=new String[13];
        		input[0]=GetServerInfo.getWebPath()+"editmeminfo.jsp";
        		input[1]="memberEmail";
        		input[2]=memberEmail;
        		input[3]="memberName";
        		input[4]=edit_name.getText().toString();
        		input[5]="memberGender";
        		input[6]=Sgender;
        		input[7]="memberBD";
        		input[8]=birthday.toString();
        		input[9]="memberInterested";
        		input[10]=Sinterested;
        		input[11]="memberABme";
        		input[12]=edit_ABme.getText().toString();
        		String re=EregiReplace.eregi_replace("(\r\n|\r|\n|\n\r| |)", "", HttpPostResponse.getHttpResponse(input));
        		
        		if(re.equals("true"))
        		{
	        		finish();  
	                Intent intent = new Intent(EditMemInf.this, Member.class);  
	                Bundle bundle =new Bundle();
	                bundle.putString("KEY_mail", memberEmail);
	                intent.putExtras(bundle);
	                startActivity(intent);  
        		}
        		else
        		{
        			Toast.makeText(EditMemInf.this, "Writing Error", Toast.LENGTH_SHORT).show();
        		}
        		
        	}
        });
        
	}
	 
	 
		 private void updateDisplay() 
		{	// Month is 0 based so add 1
			birthday=(new StringBuilder().append(mYear).append("-").append(mMonth+1).append("-").append(mDay));
			edit_birthday.setText(birthday.toString());
	    }
	    
		 @Override
	    protected Dialog onCreateDialog(int id)
	    {
	        switch (id) 
	        {
	        	case DATE_DIALOG_ID:
	            return new DatePickerDialog(this,mDateSetListener, mYear, mMonth-1, mDay);
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
}
