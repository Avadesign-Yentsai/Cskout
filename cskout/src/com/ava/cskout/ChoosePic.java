package com.ava.cskout;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ChoosePic extends Activity 
{
	private String memberEmail,memberPWD;
    private Button picupload,reload;
    private String img_path,from;
	@Override
	 public void onCreate(Bundle savedInstanceState) 
	 {
			Bundle bundle=this.getIntent().getExtras();
			from=bundle.getString("KEY_from");
        
		 	super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
	        setContentView(R.layout.choosepic);
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
	        
	        picupload =(Button)findViewById(R.id.choosepic_upload);
	        picupload.setEnabled(false);
	        
	        Intent intent = new Intent();
			intent.setType("image/*");
			Intent.createChooser(intent,"Select Picture");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(intent, 1);
			
			 picupload.setOnClickListener(new Button.OnClickListener()
	        {
	        	public void onClick(View V)
	        	{
	        		SimpleDateFormat sdFormat = new SimpleDateFormat("yyMMddhhmmss");
					Date date = new Date();
					String strDate = sdFormat.format(date);
					
	        		String re=Uploadpic.upload(strDate,img_path, memberEmail);
	        		
	  				TextView test = (TextView)findViewById(R.id.choosepictestview);
	  				
	  				
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
	  							intent.setClass(ChoosePic.this, Member.class);
	  						}
	  						else if(from.equals("Flirt_Buzz"))
	  						{
	  							intent.setClass(ChoosePic.this, Flirt_Buzz.class);
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
			
			
	}
	@Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{ 
	     super.onActivityResult(requestCode, resultCode, data); 
	     if(resultCode == RESULT_OK)
	     {
	    	 
	    	 Uri uri = data.getData();
	    	 
	    	 String[] proj = { MediaStore.Images.Media.DATA };
	    	 Cursor actualimagecursor = managedQuery(uri,proj,null,null,null);
	    	 int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);    
             actualimagecursor.moveToFirst();    
             img_path = actualimagecursor.getString(actual_image_column_index);
             
	    	 ContentResolver cr=this.getContentResolver();
	    	 try
	    	 {
	    		 Bitmap bmp = BitmapFactory.decodeFile(img_path); //利用BitmapFactory去取得剛剛拍照的圖像
	    		 ImageView ivTest = (ImageView)findViewById(R.id.choosepic_view);
		    	 ivTest.setImageBitmap(bmp);
	    	 }catch(Exception e)
	    	 {
	    		 e.printStackTrace();
	    	 }
	    	 picupload.setEnabled(true);
	     }

	 }
}
