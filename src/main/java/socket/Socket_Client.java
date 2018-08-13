package socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Socket_Client {
    Socket sock;
    BufferedReader reader;

    public static void main(String[] args){
        Socket_Client socketClient = new Socket_Client();
        socketClient.go();
    }
    //服务器端运行方法
    public void go(){
        try{
            ServerSocket server = new ServerSocket(4242);
            while (true){
                sock = server.accept();//等待链接
                reader = new BufferedReader(new InputStreamReader(
                        sock.getInputStream()));
                Thread t = new Thread(new Thread_Send());
                t.start();
                System.out.println("test");
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public class Thread_Send implements Runnable{
        PrintWriter writer;
        public Thread_Send(){
            try{
                writer = new PrintWriter(sock.getOutputStream());
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        @Override
        public void run() {
            try {
                String message_get;
                while ((message_get = reader.readLine())!=null){
                    if (message_get.matches(".*what.*your.*name.*")){
                        writer.println("My name is sunny");
                    }else {
                        writer.write(message_get+"\n");
                    }
                    writer.flush();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
