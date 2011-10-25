package com.ava.cskout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class TakePic extends Activity 
{

	File tmpFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"image.jpg");
    Uri outputFileUri= Uri.fromFile(tmpFile);
    private String memberEmail,memberPWD,from;
    private Button picupload,reload;
	@Override
	 public void onCreate(Bundle savedInstanceState) 
	 {
			Bundle bundle=this.getIntent().getExtras();
			from=bundle.getString("KEY_from");
		
		 	super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
	        setContentView(R.layout.takepic);
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
			
			picupload =(Button)findViewById(R.id.takepic_upload);
	        picupload.setEnabled(false);
	        
	        Intent intent =  new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);//利用intent去開啟android本身的照相介面
	        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
	        startActivityForResult(intent, 0);
	        
	        picupload.setOnClickListener(new Button.OnClickListener()
	        {
	        	public void onClick(View V)
	        	{
	        		SimpleDateFormat sdFormat = new SimpleDateFormat("yyMMddhhmmss");
					Date date = new Date();
					String strDate = sdFormat.format(date);
					
	        		String re=Uploadpic.upload(strDate,outputFileUri.getPath(), memberEmail);
	  				
	        		TextView test = (TextView)findViewById(R.id.takepictestview);
	  				
	        		if(re.equals((memberEmail+"-"+strDate+".jpg")))
	  				{
	  					re=HttpGetResponse.getHttpResponse(GetServerInfo.getWebPath()+"uploadpicInf.jsp?memberEmail="+memberEmail+"&memberPWD="+memberPWD+"&picName="+re);
	  					re=EregiReplace.eregi_replace("(\r\n|\r|\n|\n\r)", "", re);
	  					if(re.equals("true"))
		  				{
	  						finish();
	  						Intent intent=new Intent();
	  		                if(from.equals("Member"))
	  						{
	  							intent.setClass(TakePic.this, Member.class);
	  						}
	  						else if(from.equals("Flirt_Buzz"))
	  						{
	  							intent.setClass(TakePic.this, Flirt_Buzz.class);
	  						}
	  		                Bundle bundle =new Bundle();
	  		                bundle.putString("KEY_mail", memberEmail);
	  		                intent.putExtras(bundle);
	  		                startActivity(intent);
		  				}
	  					else
	  					{
	  						test.setText(re);
	  					}
	  				}
	  				else
	  				{
	  					test.setText(re);
	  				}
	  					
	  			}
	        });
	        
	        reload =(Button)findViewById(R.id.takepic_reload);
	        reload.setEnabled(false);
	        reload.setOnClickListener(new Button.OnClickListener()
	        {
	        	public void onClick(View V)
	        	{
        			finish();
					Intent intent=new Intent();
	                intent.setClass(TakePic.this, TakePic.class);
	                startActivity(intent);
	  			}
	        });
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{ 
		 super.onActivityResult(requestCode, resultCode, data);
		  
		 if (resultCode == RESULT_OK) 
		 {
			  Bitmap bmp = BitmapFactory.decodeFile(outputFileUri.getPath()); //利用BitmapFactory去取得剛剛拍照的圖像
			  ImageView ivTest = (ImageView)findViewById(R.id.takepic_view);
			  ivTest.setImageBitmap(bmp);
			  
			  picupload.setEnabled(true);
		 } 
	}
}
