package com.ava.cskout;

import java.util.ArrayList;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Flirt_Buzz_wall extends BaseAdapter 
{	
	
	private LayoutInflater mInflater;
	public ArrayList<String[]> data;//陣列是存網址及文字說明
	GetWebImg ImgCache;
	public Flirt_Buzz_wall(Context c,ArrayList<String[]> d,GetWebImg cache) {			
		mInflater = LayoutInflater.from(c);
		data = d;		
		ImgCache=cache;
	}
	public int getCount() {
		return data.size();
	}
	public Object getItem(int position) {
		return data.get(position);
	}
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(final int position, View view, ViewGroup parent) 
	{
		
		final ViewHolder holder;
		if (view == null) 
		{
			view = mInflater.inflate(R.layout.flirt_buzz_single, null);
			holder = new ViewHolder();
			holder.title = (TextView) view.findViewById(R.id.buzz_text1);
			holder.content = (TextView) view.findViewById(R.id.buzz_Ctext);
			holder.time_dis = (TextView) view.findViewById(R.id.buzz_time_dis);
			holder.userPic = (ImageView) view.findViewById(R.id.buzz_userPic);
			holder.contentPic=(ImageView)view.findViewById(R.id.buzz_pic);
			holder.wait = (ProgressBar) view.findViewById(R.id.buzz_wait);
			view.setTag(holder);        		
		} else 
		{
			holder = (ViewHolder) view.getTag();
		} 
		holder.title.setText(data.get(position)[1]);//顯示文字說明
		
		if(data.get(position)[3].equals("text"))
		{
			holder.content.setText(data.get(position)[2]);
			holder.contentPic.setImageBitmap(null);
		}
		else if(data.get(position)[3].equals("photo"))
		{
			holder.content.setText("上傳了一張圖片");
			if (ImgCache.IsCache(data.get(position)[2]) == false)
			{
				ImgCache.LoadUrlPic(data.get(position)[2],h);
			}
			else if (ImgCache.IsDownLoadFine(data.get(position)[2]) == true)
			{//如果已經下載完成，就顯示圖片並把ProgressBar隱藏
				Bitmap Bmp=ImgCache.getImg(data.get(position)[2]);
				Bmp=Bitmap.createScaledBitmap(Bmp,128,(int)(Bmp.getHeight()*128/Bmp.getWidth()),true);
				holder.contentPic.setImageBitmap(Bmp);
			}else
			{
				//這裡是下載中，什麼事都不用做
			}
			
		}
		
		holder.time_dis.setText(data.get(position)[4]);
		holder.userPic.setVisibility(View.INVISIBLE);
		holder.wait.setVisibility(View.VISIBLE);
		if (ImgCache.IsCache(data.get(position)[0]) == false)
		{//如果圖片沒有暫存
			ImgCache.LoadUrlPic(data.get(position)[0],h);
		}else if (ImgCache.IsDownLoadFine(data.get(position)[0]) == true){//如果已經下載完成，就顯示圖片並把ProgressBar隱藏
			Bitmap Bmp=ImgCache.getImg(data.get(position)[0]);
			Bmp=Bitmap.createScaledBitmap(Bmp,64,64,true);
			holder.userPic.setImageBitmap(Bmp);
			holder.wait.setVisibility(View.GONE);
			holder.userPic.setVisibility(View.VISIBLE);	
		}else{
			//這裡是下載中，什麼事都不用做
		}		
		return view;
	}
	Handler h = new Handler()
	{//告訴BaseAdapter資料已經更新了
		@Override
		public void handleMessage(Message msg) 
		{
			notifyDataSetChanged();
			super.handleMessage(msg);
		}
	};	
	class ViewHolder
	{
		TextView title,content,time_dis;
		ImageView userPic,contentPic;
		ProgressBar wait;
	}
}
