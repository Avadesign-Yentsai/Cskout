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
    // 用串列來儲存每個client
    //private static ArrayList<Socket> players=new ArrayList<Socket>();
 
    // 程式進入點
    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(serverport);
            System.out.println("Server is start.");
 
            // 當Server運作中時
            while (!serverSocket.isClosed()) {
                // 顯示等待客戶端連接
                System.out.println("Wait new clinet connect");
 
                // 呼叫等待接受客戶端連接
                waitNewPlayer();
            }
 
        } catch (IOException e) {
            System.out.println("Server Socket ERROR");
        }
 
    }
 
    // 等待接受客戶端連接
    public static void waitNewPlayer() {
        try {
            Socket socket = serverSocket.accept();
			
            // 呼叫創造新的使用者
            createNewPlayer(socket);
        } catch (IOException e) {
 
        }
 
    }
 
    // 創造新的使用者
    public static void createNewPlayer(final Socket socket)
	{
 
        // 以新的執行緒來執行
        Thread t = new Thread(new Runnable() 
		{
            @Override
            public void run() 
			{
                try 
				{
                    // 增加新的使用者
                    //players.add(socket);
					
					String k=socket.getInetAddress().toString()+socket.getPort();
					map.put(k,socket);
					System.out.println(k);
					System.out.println(map);

                    // 取得網路串流 
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(socket.getInputStream(),"UTF8"));
 
                    // 當Socket已連接時連續執行
                    while (socket.isConnected()) 
					{
                        // 取得網路串流的訊息
                        String msg= br.readLine();
						
						// 廣播訊息給其它的客戶端
                        //castMsg(msg);
						
						//左k將訊息給右k
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
					//map.remove(socket.getInetAddress().toString()+socket.getPort());//從map移除資料
				}
 
                // 移除客戶端
                //players.remove(socket);            
            }
        });
 
        // 啟動執行緒
        t.start();
    }
	/*
	// 廣播訊息給其它的客戶端
    public static void castMsg(String Msg){
        // 創造socket陣列
        Socket[] ps=new Socket[players.size()]; 
 
        // 將players轉換成陣列存入ps
        players.toArray(ps);
 
        // 走訪ps中的每一個元素
        for (Socket socket :ps ) {
            try {
				System.out.println(socket);
                // 創造網路輸出串流
                BufferedWriter bw;
                bw = new BufferedWriter( new OutputStreamWriter(socket.getOutputStream(),"UTF8"));
 
                // 寫入訊息到串流
                bw.write(Msg+"\n");
 
                // 立即發送
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
        // 走訪ps中的每一個元素
        
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