package com.ava.cskout;

import java.io.FileNotFoundException;
import java.io.InputStream;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Find_Flirts extends Activity
{
/** Called when the activity is first created. */
	
	//String[] vData = null;
	private int phoneWidth,phoneHeight;
	private int numColumns=4;
	private TextView mTextView1;
	private GridView gridview;
	private String[] mail_PicURL;
	private String[] mail;
	private String[] PicURL;
	private ProgressDialog d;
	private String memberEmail;
	private ImageButton litchat,litmenu,find_search;
		    
	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.find_flirts);
        
        Bundle bundle=this.getIntent().getExtras();
        String minage=bundle.getString("KEY_minage");
        String maxage=bundle.getString("KEY_maxage");
        String gender=bundle.getString("KEY_gender");
        String interested=bundle.getString("KEY_interested");
        
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar);//customize titlebar
        
        //偵測手機寬高
        DisplayMetrics DisplayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(DisplayMetrics);
        phoneWidth = DisplayMetrics.widthPixels;
        phoneHeight = DisplayMetrics.heightPixels;
        
        gridview = (GridView)findViewById(R.id.gridView1 );
	    mTextView1=(TextView)findViewById(R.id.find_flirts_textView);
	    
	    litchat=(ImageButton)findViewById(R.id.litchat);
	    litmenu=(ImageButton)findViewById(R.id.litmenu);
	    
	    litmenu.setOnClickListener(new Button.OnClickListener()
        {
        	public void onClick(View V)
        	{
        		finish();
        		Intent intent=new Intent();
                intent.setClass(Find_Flirts.this, Menu.class);
               	startActivity(intent);
        	}
        });
	    
	   
	    String tmpPic="",tmpmail="";
	    
	    InputStream XMLinputStream=null;
		try 
		{
			XMLinputStream = this.openFileInput("UserInfo.xml");
		} catch (FileNotFoundException e) 
		{
			
			e.printStackTrace();
		}
		memberEmail=ProcessXML.ReadXML(XMLinputStream,"memberEmail");
		
		mail_PicURL = (EregiReplace.eregi_replace("(\r\n|\r|\n|\n\r| |)", "", HttpGetResponse.getHttpResponse(GetServerInfo.getWebPath()+
				"search.jsp?memberEmail="+memberEmail+"&gender="+gender+"&interested="+interested+"&minage="+minage+"&maxage="+maxage))).split(":SPLIT:");

		for(int i=0;i<mail_PicURL.length;i++)
	    {
	    	if(i%2==0)
	    	{
	    		tmpmail=tmpmail+mail_PicURL[i]+":SPLIT:";
	    	}
	    	else
	    	{
	    		tmpPic=tmpPic+mail_PicURL[i]+":SPLIT:";
	    	}
	    }
	    PicURL=tmpPic.split(":SPLIT:");
	    mail=tmpmail.split(":SPLIT:");
	    
	    //mTextView1.setText(tmpmail);
		
	    gridview.setNumColumns(numColumns);
		gridview.setAdapter(new ImageAdapter(this));
		gridview.setOnItemClickListener(new OnItemClickListener() 
        {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
             {
                //Toast.makeText(PicWallActivity.this, "" + PicURL[position], Toast.LENGTH_SHORT).show();
                //mTextView1.setText(mail[position]);
            	
                Intent intent=new Intent();
                intent.setClass(Find_Flirts.this, Member.class);
                Bundle bundle =new Bundle();
                bundle.putString("KEY_mail", mail[position].toString());
                intent.putExtras(bundle);
                startActivity(intent);
              
             }
        });
		
		find_search=(ImageButton)findViewById(R.id.find_search);
		find_search.setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View V)
             {
              
                Intent intent=new Intent();
                intent.setClass(Find_Flirts.this, Search.class);
                startActivity(intent);
              
             }
        });
		
    }

	public class ImageAdapter extends BaseAdapter
	{
		private Context mContext;
		public ImageAdapter(Context c) {
	        mContext = c;
	    }
 
	    public int getCount() {
	        return PicURL.length;//PicURL最後一個值為"",扣掉不計
	    }
	 
	    public Object getItem(int position) {
	    	return position;
	    }
	 
	    public long getItemId(int position) {
	    	return position;
	    }
 
        public View getView(int position, View convertView, ViewGroup parent) 
        {  
        	final ViewHolder holder;
        	View v;
        	int imageWidth=phoneWidth/numColumns-3;//寬度減3,解決圖邊緣會重疊的問題
        	if(convertView==null)
        	{
        		LayoutInflater li = getLayoutInflater();
            	v = li.inflate(R.layout.gridview_single, null);
            	TextView tv = (TextView)v.findViewById(R.id.grid_name);
            	ImageView iv = (ImageView)v.findViewById(R.id.grid_pic);
            	Bitmap Bmp=GetURLBitmap.getURLBitmap(PicURL[position]);
            	Bmp=Bitmap.createScaledBitmap(Bmp,imageWidth,imageWidth,true);//設定imageView寬高,正方形
            	iv.setImageBitmap(Bmp);
            }
        	else
        	{
        		v = convertView;
        	}
           
        	return v;
        }  
        
        
    }
	class ViewHolder
	{
		TextView memberName;
		ImageView memberPic,onlinePic;
		ProgressBar wait;
	}
	@Override
	public void onBackPressed() 
	{
		finish();
		Intent intent=new Intent();
        intent.setClass(Find_Flirts.this, Menu.class);
       	startActivity(intent);
	}
	
}

