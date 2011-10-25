package com.ava.cskout;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

public class PhotoAlbum extends Activity 
{
	private String[] imgurl;
	private Gallery photogallery;
	private ImageView photo_view;
	private TextView test;
	private double phoneWidth,phoneHeight;

	@Override
    public void onCreate(Bundle savedInstanceState) 
	{
		 super.onCreate(savedInstanceState);
         requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
         setContentView(R.layout.photoalbum);
         
         //偵測手機寬高
         DisplayMetrics DisplayMetrics = new DisplayMetrics();
         this.getWindowManager().getDefaultDisplay().getMetrics(DisplayMetrics);
         phoneWidth = DisplayMetrics.widthPixels;
         phoneHeight = DisplayMetrics.heightPixels;
         
         Bundle bundle=this.getIntent().getExtras();
         String Email=bundle.getString("KEY_mail");
         
         photogallery=(Gallery)findViewById(R.id.photo_gallery);
         photo_view=(ImageView)findViewById(R.id.photo_view);
         test=(TextView)findViewById(R.id.photoalbumtest);
         
         String re=HttpGetResponse.getHttpResponse(GetServerInfo.getWebPath()+"photoalbum.jsp?memberEmail="+Email);
         //test.setText(re);
         imgurl=re.split(":SPLIT:");
         /*按下圖片的設定
         photogallery.setOnItemClickListener(new OnItemClickListener()
         {

             @Override
             public void onItemClick(AdapterView<?> parent, View view,
                     int position, long id) 
             {
            	 photo_view.setImageBitmap(GetURLBitmap.getURLBitmap(imgurl[position]));
                 // 一樣使用 Gallery 子元件的背景
            	 photo_view.setBackgroundResource(R.styleable.Gallery_android_galleryItemBackground);
             }
         });
         */
         photogallery.setAdapter(new myInternetGalleryAdapter(this));
	}
	
	public class myInternetGalleryAdapter extends BaseAdapter
	{
		private Context myContext;
		private int GalleryItemBackground;
		
		public myInternetGalleryAdapter(Context c)
		{
			this.myContext=c;
			TypedArray a =myContext.obtainStyledAttributes(R.styleable.Gallery);
			
			GalleryItemBackground=a.getResourceId(R.styleable.Gallery_android_galleryItemBackground, 0);
			
			a.recycle();
		}
		@Override
		public int getCount() 
		{
			return imgurl.length-1;//imgurl最後一個為"";
		}

		@Override
		public Object getItem(int position) 
		{
			return position;
		}

		@Override
		public long getItemId(int position) 
		{
			
			return position;
		}
		public float getScale(boolean focused ,int offset)
		{
			return Math.max(0, 1.0f/(float)Math.pow(2,Math.abs(offset)));
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) 
		{
			final ViewHolder holder;
			LayoutInflater mInflater = LayoutInflater.from(this.myContext);
			View myView = mInflater.inflate(R.layout.photoalbum_single, null);
			holder = new ViewHolder();
			holder.but = (Button) myView.findViewById(R.id.photo_no_sum);
			holder.pic =(ImageView) myView.findViewById(R.id.photo_view_single);
			myView.setTag(holder);   
			try
			{
				URL aryURI=new URL(imgurl[position]);
				URLConnection conn=aryURI.openConnection();
				conn.connect();
				InputStream is =conn.getInputStream();
				Bitmap bm=BitmapFactory.decodeStream(is);
				is.close();
				
				int mainBmpWidth=bm.getWidth();
		        int mainBmpHeight=bm.getHeight();
		        double scale=phoneWidth/mainBmpWidth;
		        
		        holder.pic.setImageBitmap(Bitmap.createScaledBitmap(bm,(int)(mainBmpWidth*scale),(int)(mainBmpHeight*scale),true));
		        holder.but.setText(""+(position+1)+"/"+(imgurl.length-1));
			}catch(IOException e)
			{
				e.printStackTrace();
			}
			//holder.pic.setLayoutParams(new Gallery.LayoutParams(320, 480));//調圖片大小
			
			holder.pic.setScaleType(ImageView.ScaleType.FIT_XY);
			myView.setBackgroundResource(GalleryItemBackground);
			return myView;
			
		}
		
		class ViewHolder{
			TextView text;
			ImageView pic;
			Button but;
			
		}
		
	}
}
