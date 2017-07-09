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
		//�ػ�����
		t.setDaemon(true);
		t.start();
	}

	public void connect() {
		try {
			//�����ͻ����׽���
			s = new Socket("127.0.0.1", 8888);
			//�ֱ�õ����������
			dos = new DataOutputStream(s.getOutputStream());
			dis = new DataInputStream(s.getInputStream());
			//System.out.println("�Ѿ����Ϸ�����Ŷ");
			taContent.setText("�Ѿ����Ϸ�����Ŷ\n");
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
			//�õ�д������
			String str = tfTxt.getText().trim();
			//taContent.append(str + "\n");
			tfTxt.setText("");
			try {
				//д��ܵ�
				dos.writeUTF(str);
				dos.flush();
				// dos.close();���ܹر�
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
					//ѭ����ȡ����
					String data = dis.readUTF();
					taContent.append(data+"\n");
				}
			}catch(SocketException e){
				System.out.println("�˳��ˣ�bye��");
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
