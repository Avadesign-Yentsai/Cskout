import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.*;
 
public class ChatServer {
 
    private static int serverport = 5050;
    private static ServerSocket serverSocket;
	private static Map<String, Socket> map = new HashMap<String, Socket>();
    // �Φ�C���x�s�C��client
    //private static ArrayList<Socket> players=new ArrayList<Socket>();
 
    // �{���i�J�I
    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(serverport);
            System.out.println("Server is start.");
 
            // ��Server�B�@����
            while (!serverSocket.isClosed()) {
                // ��ܵ��ݫȤ�ݳs��
                System.out.println("Wait new clinet connect");
 
                // �I�s���ݱ����Ȥ�ݳs��
                waitNewPlayer();
            }
 
        } catch (IOException e) {
            System.out.println("Server Socket ERROR");
        }
 
    }
 
    // ���ݱ����Ȥ�ݳs��
    public static void waitNewPlayer() {
        try {
            Socket socket = serverSocket.accept();
			
            // �I�s�гy�s���ϥΪ�
            createNewPlayer(socket);
        } catch (IOException e) {
 
        }
 
    }
 
    // �гy�s���ϥΪ�
    public static void createNewPlayer(final Socket socket)
	{
 
        // �H�s��������Ӱ���
        Thread t = new Thread(new Runnable() 
		{
            @Override
            public void run() 
			{
                try 
				{
                    // �W�[�s���ϥΪ�
                    //players.add(socket);
					
					String k=socket.getInetAddress().toString()+socket.getPort();
					map.put(k,socket);
					System.out.println(k);
					System.out.println(map);

                    // ���o������y 
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(socket.getInputStream(),"UTF8"));
 
                    // ��Socket�w�s���ɳs�����
                    while (socket.isConnected()) 
					{
                        // ���o������y���T��
                        String msg= br.readLine();
						
						// �s���T�����䥦���Ȥ��
                        //castMsg(msg);
						
						//��k�N�T�����kk
						String t[]=msg.split(":SPLIT:");
						poMsg(k,t[0],t[1]);
                    }
					while (socket.isClosed()) 
					{
                        System.out.println((socket.getInetAddress().toString()+socket.getPort())+"disconnect");
                    }
 
                } catch (IOException e) 
				{
					System.out.println(e);
                } catch (NullPointerException e)
				{
					System.out.println(e);
					//map.remove(socket.getInetAddress().toString()+socket.getPort());//�qmap�������
				}
 
                // �����Ȥ��
                //players.remove(socket);            
            }
        });
 
        // �Ұʰ����
        t.start();
    }
	/*
	// �s���T�����䥦���Ȥ��
    public static void castMsg(String Msg){
        // �гysocket�}�C
        Socket[] ps=new Socket[players.size()]; 
 
        // �Nplayers�ഫ���}�C�s�Jps
        players.toArray(ps);
 
        // ���Xps�����C�@�Ӥ���
        for (Socket socket :ps ) {
            try {
				System.out.println(socket);
                // �гy������X��y
                BufferedWriter bw;
                bw = new BufferedWriter( new OutputStreamWriter(socket.getOutputStream(),"UTF8"));
 
                // �g�J�T�����y
                bw.write(Msg+"\n");
 
                // �ߧY�o�e
                bw.flush();
            } catch (IOException e) {
 
            }
        }
    }
	*/
	public static void poMsg(String Deliver, String Reciver, String Msg)
	{
        Socket delSocket=map.get(Deliver);
		Socket recSocket=map.get(Reciver);
        // ���Xps�����C�@�Ӥ���
        
		try {
			System.out.println("From:"+delSocket);
			System.out.println("TO:"+recSocket);
			System.out.println("Msg:"+Msg);
			BufferedWriter recbw;
			recbw = new BufferedWriter( new OutputStreamWriter(recSocket.getOutputStream(),"UTF8"));
			recbw.write(Msg+"\n");
			recbw.flush();
			
			BufferedWriter delbw;
			delbw = new BufferedWriter( new OutputStreamWriter(delSocket.getOutputStream(),"UTF8"));
			delbw.write(Msg+"\n");
			delbw.flush();
			
		} catch (IOException e) {

		}
        
    }
	
}