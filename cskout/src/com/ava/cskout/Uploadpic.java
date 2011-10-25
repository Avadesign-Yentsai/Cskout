package com.ava.cskout;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Uploadpic 
{
	public static String upload (String strDate, String PicPath,String memberEmail)
	{
		  String end = "\r\n";
		  String twoHyphens = "--";
		  String boundary = "*****";
		  HttpURLConnection con=null;
		  URL url;
		  
		  try 
		  {
				url = new URL(GetServerInfo.getWebPath()+"uploadpic.jsp");
				con = (HttpURLConnection) url.openConnection();
				
				con.setDoInput(true);
				con.setDoOutput(true);
				con.setUseCaches(false);
				con.setRequestMethod("POST");
				con.setRequestProperty("Connection", "Keep-Alive");
				con.setRequestProperty("Charset", "UTF-8");
				con.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
				
				DataOutputStream ds = new DataOutputStream(con.getOutputStream());
				String inputName = "uploadFile";
				String fileName = memberEmail+"-"+strDate+".jpg";//這裡要改成程式設定的名字
				String filePath = PicPath;
				
				// 寫入檔案
				 ds.writeBytes(twoHyphens + boundary + end);
				 ds.writeBytes("Content-Disposition: form-data;");
				 ds.writeBytes("name=\"" + inputName + "\";filename=\"" + fileName+ "\"" + end + end);
				
				 int bufferSize = 1024;
				 int length = -1;
				 byte[] buffer = new byte[bufferSize];
				 FileInputStream fStream = new FileInputStream(filePath);
				 
				 while ((length = fStream.read(buffer)) != -1) 
				 {
				 	ds.write(buffer, 0, length);
				 }
				 ds.writeBytes(end);
				 
				 // 結束
				 ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
				 // close stream
				 ds.flush();
				 ds.close(); 
				 fStream.close();
				 
				 InputStream is = con.getInputStream(); 
				 int ch; 
				 StringBuffer b =new StringBuffer(); 
				 while( ( ch = is.read() ) != -1 ) 
				 { 
				  b.append( (char)ch ); 
				 } 
				 //將Response回傳  
				 if((b.toString().trim()).equals("true"))
				 {
					 return(fileName);
				 }
				 else
				 {
					 return b.toString().trim();
				 }
				 
			 
		  } catch (IOException e) 
		  {
			  e.printStackTrace();
			  return(e.toString());
		  }
		
		
		
	}
}
