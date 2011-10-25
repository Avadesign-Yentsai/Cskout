package ccc.ClientSocketDemo3;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;



// 
public class ClientSocketDemo3 extends Activity {
	public static Handler mHandler = new Handler();
	TextView TextView01;	// �Ψ���ܤ�r�T��
	EditText EditText01;	// ��r���
	EditText EditText02;	// ��r���
	String tmp;				// �Ȧs��r�T��
	Socket clientSocket;	// �Ȥ��socket
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		// �q�귽�ɸ̨��o��}��j���૬����r���
		TextView01 = (TextView) findViewById(R.id.TextView01);
		EditText01=(EditText) findViewById(R.id.EditText01);
		EditText02=(EditText) findViewById(R.id.EditText02);
		
		// �H�s���������Ū�����
		Thread t = new Thread(readData);
		
		// �Ұʰ����
		t.start();
		
		// �q�귽�ɸ̨��o��}��j���૬�����s
		Button button1=(Button) findViewById(R.id.Button01);
		
		// �]�w���s���ƥ�
		button1.setOnClickListener(new Button.OnClickListener() {		
			// ����U���s���ɭ�Ĳ�o�H�U����k
			public void onClick(View v) {
				// �p�G�w�s���h
				if(clientSocket.isConnected()){
					
					BufferedWriter bw;
					
					try {
						// ���o������X��y
						bw = new BufferedWriter( new OutputStreamWriter(clientSocket.getOutputStream(),"UTF8"));
						
						// �g�J�T��
						bw.write(EditText01.getText()+":SPLIT:"+EditText02.getText()+"\n");
						
						// �ߧY�o�e
						bw.flush();
					} catch (IOException e) {
						
					}
					// �N��r����M��
					EditText02.setText("");
				}	
			}
		});
		
	}

	// ��ܧ�s�T��
	private Runnable updateText = new Runnable() {
		public void run() 
		{
			// �[�J�s�T���ô���
			TextView01.append(tmp + "\n");
		}
	};

	// ���o�������
	private Runnable readData = new Runnable() {
		public void run() {
			// server�ݪ�IP
			InetAddress serverIp;

			try {
				// �H���w(�����q����)IP��Server��
				serverIp = InetAddress.getByName("192.168.214.130");
				int serverPort = 5050;
				clientSocket = new Socket(serverIp, serverPort);
				
				// ���o������J��y
				BufferedReader br = new BufferedReader(new InputStreamReader(
						clientSocket.getInputStream(),"UTF8"));
				
				// ��s�u��
				while (clientSocket.isConnected()) {
					// ���o�����T��
					tmp = br.readLine();
					
					// �p�G���O�ŰT���h
					if(tmp!=null)
						// ��ܷs���T��
						mHandler.post(updateText);
				}

			} catch (IOException e) {
				
			}
		}
	};
	@Override
	   protected void onPause()
	   {
		   super.onPause();
		   try {
			clientSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   }
}