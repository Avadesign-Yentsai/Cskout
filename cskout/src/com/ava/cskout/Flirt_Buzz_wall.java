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
	public ArrayList<String[]> data;//�}�C�O�s���}�Τ�r����
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
		holder.title.setText(data.get(position)[1]);//��ܤ�r����
		
		if(data.get(position)[3].equals("text"))
		{
			holder.content.setText(data.get(position)[2]);
			holder.contentPic.setImageBitmap(null);
		}
		else if(data.get(position)[3].equals("photo"))
		{
			holder.content.setText("�W�ǤF�@�i�Ϥ�");
			if (ImgCache.IsCache(data.get(position)[2]) == false)
			{
				ImgCache.LoadUrlPic(data.get(position)[2],h);
			}
			else if (ImgCache.IsDownLoadFine(data.get(position)[2]) == true)
			{//�p�G�w�g�U�������A�N��ܹϤ��ç�ProgressBar����
				Bitmap Bmp=ImgCache.getImg(data.get(position)[2]);
				Bmp=Bitmap.createScaledBitmap(Bmp,128,(int)(Bmp.getHeight()*128/Bmp.getWidth()),true);
				holder.contentPic.setImageBitmap(Bmp);
			}else
			{
				//�o�̬O�U�����A����Ƴ����ΰ�
			}
			
		}
		
		holder.time_dis.setText(data.get(position)[4]);
		holder.userPic.setVisibility(View.INVISIBLE);
		holder.wait.setVisibility(View.VISIBLE);
		if (ImgCache.IsCache(data.get(position)[0]) == false)
		{//�p�G�Ϥ��S���Ȧs
			ImgCache.LoadUrlPic(data.get(position)[0],h);
		}else if (ImgCache.IsDownLoadFine(data.get(position)[0]) == true){//�p�G�w�g�U�������A�N��ܹϤ��ç�ProgressBar����
			Bitmap Bmp=ImgCache.getImg(data.get(position)[0]);
			Bmp=Bitmap.createScaledBitmap(Bmp,64,64,true);
			holder.userPic.setImageBitmap(Bmp);
			holder.wait.setVisibility(View.GONE);
			holder.userPic.setVisibility(View.VISIBLE);	
		}else{
			//�o�̬O�U�����A����Ƴ����ΰ�
		}		
		return view;
	}
	Handler h = new Handler()
	{//�i�DBaseAdapter��Ƥw�g��s�F
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
