package com.ava.cskout;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Member extends Activity implements Runnable 
{
	private ProgressDialog d;
	private TextView memberName,memberGender,memberAge,memberLoc,memberInterested,memberABme,membertestview,memberstatus;
	private ImageView mimageview1,mimageview2,mimageview3,mimageview4,mimageview5;
	private String PicPath=GetServerInfo.getPicPath();
	private String[] memberInfo;
	private String PicName="",Email="";
	private String[] memberBD;
	private double phoneWidth,phoneHeight;
	private long Age;
	private Bitmap mainBmp,Bmp1,Bmp2,Bmp3,Bmp4;
	private int mainBmpWidth,mainBmpHeight;
	private Matrix matrix;
	private String memberEmail,memberPWD;
	private ListView memberlistView;
	private ImageButton edit,addpic,status,wink,chat,addhotlist,gift,more,litchat,litmenu;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
		Bundle bundle=this.getIntent().getExtras();
        Email=bundle.getString("KEY_mail");
        Email=Email.replace("\n", "").replace("\r", "");
        
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
		
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		
        if(memberEmail.equals(Email))
        {
        	setContentView(R.layout.myprofile);
        }
        else
        {
            setContentView(R.layout.member);
        }
        
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);//customize titlebar
        
        if(memberEmail.equals(Email))
        {
        	setContentView(R.layout.myprofile);
        	edit=(ImageButton)findViewById(R.id.edit);
        	addpic=(ImageButton)findViewById(R.id.addpic);
        	status=(ImageButton)findViewById(R.id.status);
        	
        }
        else
        {
            setContentView(R.layout.member);
            wink=(ImageButton)findViewById(R.id.wink);
            chat=(ImageButton)findViewById(R.id.chat);
            addhotlist=(ImageButton)findViewById(R.id.addhotlist);
            gift=(ImageButton)findViewById(R.id.gift);
            more=(ImageButton)findViewById(R.id.more);
        }
        
        memberName=(TextView)findViewById(R.id.memberNickName);
        memberGender=(TextView)findViewById(R.id.memberGender);
        memberAge=(TextView)findViewById(R.id.memberAge);
        memberLoc=(TextView)findViewById(R.id.memberLoc);
        memberInterested=(TextView)findViewById(R.id.memberInterested);
        memberABme=(TextView)findViewById(R.id.memberAboutMeCenter);
        membertestview=(TextView)findViewById(R.id.membertestview);
		memberlistView=(ListView)findViewById(R.id.memberlistView);
		memberstatus=(TextView)findViewById(R.id.memberstatus);
        mimageview1=(ImageView)findViewById(R.id.mimageView1);
        mimageview2=(ImageView)findViewById(R.id.mimageView2);
        mimageview3=(ImageView)findViewById(R.id.mimageView3);
        mimageview4=(ImageView)findViewById(R.id.mimageView4);
        mimageview5=(ImageView)findViewById(R.id.mimageView5);
        litchat=(ImageButton)findViewById(R.id.litchat);
	    litmenu=(ImageButton)findViewById(R.id.litmenu);
	    
	    litmenu.setOnClickListener(new Button.OnClickListener()
        {
        	public void onClick(View V)
        	{
        		Intent intent=new Intent();
                intent.setClass(Member.this, Menu.class);
               	startActivity(intent);
        	}
        });
        
        d=new ProgressDialog(Member.this);
        d.setMessage("載入中");
        d.show();
        Thread thread =new Thread(Member.this);
        thread.start();
        
        //偵測手機寬高
        DisplayMetrics DisplayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(DisplayMetrics);
        phoneWidth = DisplayMetrics.widthPixels;
        phoneHeight = DisplayMetrics.heightPixels;
        
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
        memberInfo[12] 最後動態(text)
       	*/
       
	}
	
	private Handler handler =new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{

			d.dismiss();
			
				/*
				StringBuffer tmp=new StringBuffer("");
		        for (int i=0;i<memberInfo.length;i++)
		        {
		        	tmp=tmp.append(memberInfo[i]);
		        }
		        membertestview.setText(tmp.toString()+",");
		        */
			
			memberName.setText(""+memberInfo[0]+"     ");
	        memberGender.setText(""+memberInfo[1]+"     ");
	        memberAge.setText(""+Age+"     ");
	        memberLoc.setText("("+memberInfo[3]+","+memberInfo[4]+")"+"     ");//經緯度,以後要換成距離/KM
	        memberInterested.setText(""+memberInfo[5]+"     ");
	        memberABme.setText(""+memberInfo[6]);
	        memberstatus.setText(memberInfo[12]+"     ");
	        
	        mimageview1.setImageBitmap(Bitmap.createBitmap(mainBmp,0,0,mainBmpWidth,mainBmpHeight,matrix,true));
	        
			mimageview2.setImageBitmap(Bmp1);
	        mimageview3.setImageBitmap(Bmp2);
            mimageview4.setImageBitmap(Bmp3);
            mimageview5.setImageBitmap(Bmp4);
            
            mimageview2.setOnClickListener(new Button.OnClickListener()
            {
            	public void onClick(View V) 
                {
            		Intent intent=new Intent();
                    intent.setClass(Member.this, PhotoAlbum.class);
                    Bundle bundle =new Bundle();
                    bundle.putString("KEY_mail", Email);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            mimageview3.setOnClickListener(new Button.OnClickListener()
            {
            	public void onClick(View V) 
                {
            		Intent intent=new Intent();
                    intent.setClass(Member.this, PhotoAlbum.class);
                    Bundle bundle =new Bundle();
                    bundle.putString("KEY_mail", Email);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            mimageview4.setOnClickListener(new Button.OnClickListener()
            {
            	public void onClick(View V) 
                {
            		Intent intent=new Intent();
                    intent.setClass(Member.this, PhotoAlbum.class);
                    Bundle bundle =new Bundle();
                    bundle.putString("KEY_mail", Email);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            
            if(memberEmail.equals(Email))
	   	     {
	   	   		edit.setOnClickListener(new Button.OnClickListener()
	   	         {
	   	         	public void onClick(View V)
	   	         	{
	   	         		 finish();
	   	         		 Intent intent=new Intent();
	   	                 intent.setClass(Member.this, EditMemInf.class);
	   	                 startActivity(intent);
	   	         	}
	   	         });
	   	   		 addpic.setOnClickListener(new Button.OnClickListener()
	   	         {
	   	         	public void onClick(View V)
	   	         	{
	   	         		AlertDialog.Builder builder = new AlertDialog.Builder(Member.this);
		   	         	builder.setTitle("Add Picture");
		    	        builder.setItems(new String[] { "From SD Card", "Use Camera" }, new DialogInterface.OnClickListener()
		    	        {
		    	        	public void onClick(DialogInterface dialog, int which) 
		    	        	{
		    	        		if(which==0)
		    	        		{
		    	        			//Toast.makeText(Member.this, "From SD Card", Toast.LENGTH_SHORT).show();
		    	        			finish();
		    	        			Intent intent=new Intent();
			   	   	                intent.setClass(Member.this, ChoosePic.class);
			   	   	                Bundle bundle =new Bundle();
					                bundle.putString("KEY_from", "Member");
					                intent.putExtras(bundle);
			   	   	                startActivity(intent);
		    	        		}
		    	        		else if(which==1)
		    	        		{
		    	        			finish();
		    	        			Intent intent=new Intent();
			   	   	                intent.setClass(Member.this, TakePic.class);
			   	   	                Bundle bundle =new Bundle();
					                bundle.putString("KEY_from", "Member");
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
	   	   		status.setOnClickListener(new Button.OnClickListener()
	   	         {
	   	         	public void onClick(View V)
	   	         	{
	   	         	
	   	         		AlertDialog.Builder builder = new AlertDialog.Builder(Member.this);
	   	         		LayoutInflater factory = LayoutInflater.from(Member.this); 
	   	         		final View textEntryView = factory.inflate(R.layout.post_status, null);
	   	         		builder.setIcon(R.drawable.icon);  
	   	         		builder.setTitle("輸入狀態");  
	   	         		builder.setView(textEntryView); 
	   	         		
	   	         		builder.setPositiveButton("發送", new DialogInterface.OnClickListener() 
	   	         		{  
	   	         			public void onClick(DialogInterface dialog, int whichButton) 
	   	         			{  
	   		   	         		EditText status = (EditText) textEntryView.findViewById(R.id.statustext);
	   		   	         		if(status.getText().toString().replace(" ", "").length()>0)
			   	         		{
			   	 	        		String[] input;
			   	 	        		input=new String[9];
			   	 	        		input[0]=GetServerInfo.getWebPath()+"flirtsbuzz_toDB.jsp";
			   	 	        		input[1]="memberEmail";
			   	 	        		input[2]=memberEmail;
			   	 	        		input[3]="memberPWD";
			   	 	        		input[4]=memberPWD;
			   	 	        		input[5]="buzz_input";
			   	 	        		input[6]=status.getText().toString();
			   	 	        		input[7]="type";
			   	 	        		input[8]="text";
			   	 	        		String re=EregiReplace.eregi_replace("(\r\n|\r|\n|\n\r| |)", "", HttpPostResponse.getHttpResponse(input));
			   	 	        		
			   	 	        		if(re.equals("true"))
			   	 	        		{
			   	 		        		finish();  
			   	 		        		Intent intent=new Intent();
			   	 		        		intent.setClass(Member.this, Member.class);
			   	 		        		Bundle bundle =new Bundle();
			   	 		        		bundle.putString("KEY_mail", memberEmail);
			   	 		        		intent.putExtras(bundle);
			   	 		        		startActivity(intent);
			   	 	        		}
			   	 	        		else
			   	 	        		{
			   	 	        			Toast.makeText(Member.this, "Writing Error", Toast.LENGTH_SHORT).show();
			   	 	        		}
			   	         		}
			   	         		else
			   	         		{
			   	         			Toast.makeText(Member.this, "Invalid String", Toast.LENGTH_SHORT).show();
			   	         		}
	   	         			}  
	   	         		});  

	   	         		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() 
	   	         		{  
	   	         			public void onClick(DialogInterface dialog, int whichButton) 
	   	         			{  

	   	         			}  
	   	         		});  

	   	         		builder.create().show();  
	   	         	}
	   	         });
	   	   		
	   	     }
	   	     else
	   	     {
	   	    	 
	   	    	 wink.setOnClickListener(new Button.OnClickListener()
	   	         {
	   	         	public void onClick(View V)
	   	         	{
	   	         		 
	   	         	}
	   	         });
	   	    	 
	   	    	 chat.setOnClickListener(new Button.OnClickListener()
	   	         {
	   	         	public void onClick(View V)
	   	         	{
	   	         		 
	   	         	}
	   	         });
	   	         
	   	    	 addhotlist.setOnClickListener(new Button.OnClickListener()
	   	         {
	   	         	public void onClick(View V)
	   	         	{
	   	         		 
	   	         	}
	   	         });
	   	         
	   	    	 gift.setOnClickListener(new Button.OnClickListener()
	   	         {
	   	         	public void onClick(View V)
	   	         	{
	   	         		
	   	         	}
	   	         });
	   	         
	   	    	 more.setOnClickListener(new Button.OnClickListener()
	   	         {
	   	         	public void onClick(View V)
	   	         	{
	   	         		 
	   	         	}
	   	         });
	   	         
	   	     }
	        
		}
	};
	@Override
	public void run() 
	{
		
        memberInfo= (EregiReplace.eregi_replace("(\r\n|\r|\n|\n\r)", "", HttpGetResponse.getHttpResponse(GetServerInfo.getWebPath()+"member.jsp?memberEmail="+Email))).split(":SPLIT:");
        
        memberBD=memberInfo[2].split("-");//Age Calc
        Calendar cal = Calendar.getInstance();
        Date d2 = cal.getTime();
        cal.set(Integer.parseInt(memberBD[0]), Integer.parseInt(memberBD[1]), Integer.parseInt(memberBD[2]));
        Date d1 =cal.getTime();
        long daterange = d2.getTime() - d1.getTime();
        Age=daterange/1000/60/60/24/365;
        
        mainBmp =GetURLBitmap.getURLBitmap(memberInfo[7]);
        mainBmpWidth=mainBmp.getWidth();
        mainBmpHeight=mainBmp.getHeight();
        double scale=phoneWidth/mainBmpWidth;
        matrix=new Matrix();
        matrix.postScale((float)scale,(float)scale);
 
        Bmp1=GetURLBitmap.getURLBitmap(memberInfo[8]);
        Bmp2=GetURLBitmap.getURLBitmap(memberInfo[9]);
        Bmp3=GetURLBitmap.getURLBitmap(memberInfo[10]);
        Bmp4=GetURLBitmap.getURLBitmap(memberInfo[11]);
       
        Bmp1=Bitmap.createScaledBitmap(Bmp1,(int)(phoneWidth/4),(int)(Bmp1.getHeight()*phoneWidth/4/Bmp1.getWidth()),true);
        Bmp2=Bitmap.createScaledBitmap(Bmp2,(int)(phoneWidth/4),(int)(Bmp2.getHeight()*phoneWidth/4/Bmp2.getWidth()),true);
        Bmp3=Bitmap.createScaledBitmap(Bmp3,(int)(phoneWidth/4),(int)(Bmp3.getHeight()*phoneWidth/4/Bmp3.getWidth()),true);
        Bmp4=Bitmap.createScaledBitmap(Bmp4,(int)(phoneWidth/4),(int)(Bmp4.getHeight()*phoneWidth/4/Bmp4.getWidth()),true);
        
   	 	
        handler.sendEmptyMessage(0);
	}
	
	
}