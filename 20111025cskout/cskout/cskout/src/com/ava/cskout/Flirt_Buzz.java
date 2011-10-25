package com.ava.cskout;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Flirt_Buzz extends Activity 
{
	private Button flirt_buzz_send;
	private EditText Buzz_con;
	private String memberEmail,memberPWD;
	private TextView testview;
	ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
	private SimpleAdapter adapter;
	private String httpre;
	GetWebImg ImgCache = new GetWebImg();
	private ImageButton Pic_upload;
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.flirt_buzz);
        
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);//customize titlebar
        
        testview=(TextView)findViewById(R.id.flirtbuzztestview);
        flirt_buzz_send=(Button)findViewById(R.id.flirt_buzz_send);
        Buzz_con=(EditText)findViewById(R.id.Buzz_con);
        Pic_upload=(ImageButton)findViewById(R.id.flirt_buzz_pic_upload);
        
        ImageButton litchat=(ImageButton)findViewById(R.id.litchat);
        ImageButton litmenu=(ImageButton)findViewById(R.id.litmenu);
        litmenu.setOnClickListener(new Button.OnClickListener()
        {
        	public void onClick(View V)
        	{
        		Intent intent=new Intent();
                intent.setClass(Flirt_Buzz.this, Menu.class);
               	startActivity(intent);
        	}
        });
        
        //從手機內的XML讀取mail和pwd
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
   
        
        //資料POST到 DB,返回 Flirts buzz的列表資料(JSON字串)
        String[] httpinput=new String[5];
        httpinput[0]=GetServerInfo.getWebPath()+"flirtsbuzz_fromDB.jsp";
        httpinput[1]="memberEmail";
        httpinput[2]=memberEmail;
        httpinput[3]="wh";
        httpinput[4]="1";//0看自己的flirtbuzz, 1看其他人的
        httpre=HttpPostResponse.getHttpResponse(httpinput);
        //Toast.makeText(Flirt_Buzz.this, httpre, Toast.LENGTH_LONG).show();
		//testview.setText(httpre);
        
        
        //JSON解析
        StringBuffer sb_memberEmail =new StringBuffer("");
        StringBuffer sb_memberName =new StringBuffer("");
        StringBuffer sb_picName =new StringBuffer("");
        StringBuffer sb_flirtsTitle =new StringBuffer("");
        StringBuffer sb_flirtsType =new StringBuffer("");
        StringBuffer sb_flirtsCon =new StringBuffer("");
        StringBuffer sb_flirtsUpDate =new StringBuffer("");
        try 
        {
        	JSONObject jsonObject = new JSONObject(httpre).getJSONObject("flirts"); 
        	JSONArray jsonArray = jsonObject.getJSONArray("flirtslist"); 
        	for(int i=0;i<jsonArray.length();i++)
        	{ 
		        JSONObject jsonObject2 = (JSONObject)jsonArray.opt(i); 
		        
		        sb_memberEmail.append(jsonObject2.getString("memberEmail")+":SPLIT:");
		        sb_memberName.append(jsonObject2.getString("memberName")+":SPLIT:");
		        sb_picName.append(jsonObject2.getString("picName")+":SPLIT:");
		        sb_flirtsTitle.append(jsonObject2.getString("flirtsTitle")+":SPLIT:");
		        sb_flirtsCon.append(jsonObject2.getString("flirtsCon")+":SPLIT:");
		        sb_flirtsType.append(jsonObject2.getString("flirtsType")+":SPLIT:");
		        sb_flirtsUpDate.append(jsonObject2.getString("flirtsUpDate")+":SPLIT:");
			}
		} catch (JSONException e1) {
			e1.printStackTrace();
		} 
		
		final String[] ar_memberEmail=sb_memberEmail.toString().split(":SPLIT:");
		String[] ar_memberName=sb_memberName.toString().split(":SPLIT:");
		String[] ar_picName=sb_picName.toString().split(":SPLIT:");
		String[] ar_flirtsTitle=sb_flirtsTitle.toString().split(":SPLIT:");
		String[] ar_flirtsCon=sb_flirtsCon.toString().split(":SPLIT:");
		String[] ar_flirtsType=sb_flirtsType.toString().split(":SPLIT:");
		String[] ar_flirtsUpDate=sb_flirtsUpDate.toString().split(":SPLIT:");
		
		
		//傳到listview
		ArrayList<String[]> alldata = new ArrayList<String[]>();
		for(int i=0; i<ar_flirtsTitle.length; i++)
		{
			alldata.add(createData(ar_picName[i],ar_flirtsTitle[i],ar_flirtsCon[i],ar_flirtsType[i],ar_flirtsUpDate[i]));
		}
		ListView buzzlist=(ListView)findViewById(R.id.buzzlist);
		buzzlist.setAdapter(new Flirt_Buzz_wall(Flirt_Buzz.this,alldata,ImgCache));  
		
		//ListView按鍵
		buzzlist.setOnItemClickListener(new OnItemClickListener() 
        {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
             {
                Intent intent=new Intent();
                intent.setClass(Flirt_Buzz.this, Member.class);
                Bundle bundle =new Bundle();
                bundle.putString("KEY_mail", ar_memberEmail[position].toString());
                intent.putExtras(bundle);
                startActivity(intent);
              
             }
        });
		
		        
        //發送flirt_buzz設定
		flirt_buzz_send.setOnClickListener(new Button.OnClickListener()
        {
        	public void onClick(View V)
        	{
        		if(Buzz_con.getText().toString().replace(" ", "").length()>0)
        		{
	        		String[] input;
	        		input=new String[9];
	        		input[0]=GetServerInfo.getWebPath()+"flirtsbuzz_toDB.jsp";
	        		input[1]="memberEmail";
	        		input[2]=memberEmail;
	        		input[3]="memberPWD";
	        		input[4]=memberPWD;
	        		input[5]="buzz_input";
	        		input[6]=Buzz_con.getText().toString();
	        		input[7]="type";
	        		input[8]="text";
	        		String re=EregiReplace.eregi_replace("(\r\n|\r|\n|\n\r| |)", "", HttpPostResponse.getHttpResponse(input));
	        		
	        		if(re.equals("true"))
	        		{
		        		finish();  
		                Intent intent = new Intent(Flirt_Buzz.this, Flirt_Buzz.class);  
		                startActivity(intent);  
	        		}
	        		else
	        		{
	        			Toast.makeText(Flirt_Buzz.this, "Writing Error", Toast.LENGTH_SHORT).show();
	        		}
        		}
        		else
        		{
        			Toast.makeText(Flirt_Buzz.this, "Invalid String", Toast.LENGTH_SHORT).show();
        		}
        	}
        });
		
		Pic_upload.setOnClickListener(new Button.OnClickListener()
	    {
	    	public void onClick(View V)
	    	{
	    		AlertDialog.Builder builder = new AlertDialog.Builder(Flirt_Buzz.this);
   	         	builder.setTitle("Upload Picture");
    	        builder.setItems(new String[] { "From SD Card", "Use Camera" }, new DialogInterface.OnClickListener()
    	        {
    	        	public void onClick(DialogInterface dialog, int which) 
    	        	{
    	        		if(which==0)
    	        		{
    	        			//Toast.makeText(Member.this, "From SD Card", Toast.LENGTH_SHORT).show();
    	        			finish();
    	        			Intent intent=new Intent();
	   	   	                intent.setClass(Flirt_Buzz.this, ChoosePic.class);
	   	   	                Bundle bundle =new Bundle();
			                bundle.putString("KEY_from", "Flirt_Buzz");
			                intent.putExtras(bundle);
	   	   	                startActivity(intent);
    	        		}
    	        		else if(which==1)
    	        		{
    	        			finish();
    	        			Intent intent=new Intent();
	   	   	                intent.setClass(Flirt_Buzz.this, TakePic.class);
	   	   	                Bundle bundle =new Bundle();
			                bundle.putString("KEY_from", "Flirt_Buzz");
			                intent.putExtras(bundle);
	   	   	                startActivity(intent);
    	        		}
    	        	}
    	        });
    	        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() 
    	        {
    	        	public void onClick(DialogInterface dialog, int id) 
    	        	{
    	        	}
    	        });   
    	        
    	        AlertDialog alert = builder.create();
    	        alert.show();
	    	}
	    });
        
	}
	
	
	private String[] createData(String imgurl,String title,String content,String type,String dis_time){
    	String temp[]={imgurl,title,content,type,dis_time};
    	return temp;
    }
	@Override
	public void onBackPressed() 
	{
		finish();
		Intent intent=new Intent();
        intent.setClass(Flirt_Buzz.this, Menu.class);
       	startActivity(intent);
	}
   
}
