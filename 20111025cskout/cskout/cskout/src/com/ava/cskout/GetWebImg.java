package com.ava.cskout;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

import org.apache.http.util.ByteArrayBuffer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;

public class GetWebImg 
{
	private final String TAG_NAME="m"; 
	public final int DOWNLOAD_ERROR=0,DOWNLOAD_FINISH=1;
	private HashMap<String, Bitmap> picmap = new HashMap<String, Bitmap>();//�ŧi�@��HashMap�ΨӦs���}�ιϤ��Ϊ�

	public Bitmap getImg(String u)
	{
		return picmap.get(u);
	}
	
	public boolean IsCache(String u)
	{//�P�_�O�_���Ȧs
		return picmap.containsKey(u);
	}
	
	public boolean IsDownLoadFine(String u)
	{//�P�_�Ϥ��O�_�U�����\
		return (picmap.get(u)!= null)?true:false;
	}
	
	public boolean IsLoading(String u)
	{//�P�_�Ϥ��O�_�U����
		return (IsCache(u)==true && IsDownLoadFine(u)==false)?true:false;
	}
	
	public void LoadUrlPic(final String u,final Handler h) 
	{
		picmap.put(u,null);//���Ȧs���Ŷ�
		new Thread(new Runnable() {
			@Override
			public void run()
			{				
				Bitmap temp = GetURLBitmap.getURLBitmap(u);//�U���Ϥ����ۭq���
				if (temp == null)
				{//�p�G�U������
					picmap.remove(u);//���X�Ȧs�Ŷ�					
					h.sendMessage(h.obtainMessage(DOWNLOAD_ERROR,null));
				}
				else
				{
					picmap.put(u, temp);//�s�_��
					h.sendMessage(h.obtainMessage(DOWNLOAD_FINISH,temp));
				}
			}
		}).start();
	}
	
	
}
