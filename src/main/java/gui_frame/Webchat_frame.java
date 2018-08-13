package gui_frame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

public class Webchat_frame extends JFrame {
    private JTextArea textArea;
    private JTextField inputArea;
    private JButton send,endchat;
    private JPanel panel;
    private PrintWriter writer;
    private BufferedReader reader;
    Socket sock;

    public Webchat_frame(){
        setFrame();//对象启动时设置框架
    }
    //提供一个方法获得textarea
    public JTextArea getTextArea() {
        return textArea;
    }
    //对话框设置方法
    private void setFrame(){
        textArea = new JTextArea(6,10);
        inputArea = new JTextField("",16);
        send = new JButton("Send");
        endchat = new JButton("endChat");
        panel = new JPanel();

        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    System.out.println("pressed");
                    String temp = inputArea.getText();
                    if (temp!=null&&!temp.equals("")){
                        writer.write(temp+"\n");
                        writer.flush();
                        textArea.append("apple:"+temp+"\n");
                        }
                        inputArea.setText("");
            }
        });

        endchat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    writer.close();
            }
        });

        this.getContentPane().add(textArea,BorderLayout.CENTER);
        panel.add(inputArea);
        panel.add(send);
        panel.add(endchat);
        this.add(panel,BorderLayout.SOUTH);

        setSocket();//建立链接
        Thread readerThread = new Thread(new SeverReader());
        readerThread.start();

        this.setSize(300,300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    public void setSocket(){
        try {
            sock = new Socket("127.0.0.1",4242);
            writer = new PrintWriter(sock.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(
                    sock.getInputStream()));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public class SeverReader implements Runnable{
        @Override
        public void run() {
            try{
                String temp_get = "";
                while ((temp_get=reader.readLine())!=null){
                    System.out.println("got message");
                    textArea.append("server:"+temp_get+"\n");
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
