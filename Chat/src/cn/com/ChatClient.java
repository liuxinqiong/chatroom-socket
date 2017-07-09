package cn.com;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ChatClient extends Frame {
	TextField tfTxt = new TextField();
	TextArea taContent = new TextArea();
	DataOutputStream dos;
	Socket s;
	DataInputStream dis;
	boolean connected = false;
	Thread t=new Thread(new ReceiveThread());

	public static void main(String[] args) {
		new ChatClient().launchFrame();
	}

	public void launchFrame() {

		this.setLocation(400, 300);
		this.setSize(300, 300);
		add(tfTxt, BorderLayout.SOUTH);
		add(taContent, BorderLayout.NORTH);
		pack();

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				disConnect();
				System.exit(0);
			}
		});
		tfTxt.addActionListener(new TFListener());
		this.setVisible(true);
		connect();	
		//守护进程
		t.setDaemon(true);
		t.start();
	}

	public void connect() {
		try {
			//创建客户端套接字
			s = new Socket("127.0.0.1", 8888);
			//分别得到输入输出流
			dos = new DataOutputStream(s.getOutputStream());
			dis = new DataInputStream(s.getInputStream());
			//System.out.println("已经连上服务器哦");
			taContent.setText("已经连上服务器哦\n");
			connected = true;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void disConnect() {
		try {
			connected=false;
	//		t.join();
			dos.close();
			dis.close();
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private class TFListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			//得到写入内容
			String str = tfTxt.getText().trim();
			//taContent.append(str + "\n");
			tfTxt.setText("");
			try {
				//写入管道
				dos.writeUTF(str);
				dos.flush();
				// dos.close();不能关闭
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	private class ReceiveThread implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				while (connected) {
					//循环读取数据
					String data = dis.readUTF();
					taContent.append(data+"\n");
				}
			}catch(SocketException e){
				System.out.println("退出了，bye！");
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
