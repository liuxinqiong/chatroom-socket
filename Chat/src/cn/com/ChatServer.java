package cn.com;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
	List<Client> clients = new ArrayList<Client>();
	boolean started = false;
	ServerSocket ss = null;
	public static void main(String[] args) {
		new ChatServer().start();
	}

	public void start() {
		try {
			//�����������׽���
			ss = new ServerSocket(8888);
			System.out.println("���������ɹ������ڼ���8888�˿�...");
			started = true;
		} catch (BindException e) {
			System.out.println("�˿�ʹ����....");
			System.out.println("��ص���س����������з�������");
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			while (started) {
				//���� ѭ������
				Socket s = ss.accept();
				//Ϊ�ͻ��˴����߳�
				Client c = new Client(s);
				clients.add(c);
				System.out.println("һ���ͻ�����������");
				//�����߳�
				new Thread(c).start();
				// dis.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				ss.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	class Client implements Runnable {
		private Socket s;//�����׽���
		private DataInputStream dis = null;//������
		private boolean bConnected = false;
		private DataOutputStream dos;//�����
		public Client(Socket s) {
			this.s = s;
			try {
				//���ÿͻ����׽��ֱַ�õ��������������
				dis = new DataInputStream(s.getInputStream());
				dos = new DataOutputStream(s.getOutputStream());
				bConnected = true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//�������ݵķ���
		public void send(String msg) {
			try {
				dos.writeUTF(msg);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				clients.remove(this);
				System.out.println("һ���ͻ��˳���");
			}
		}

		public void run() {
			Client c = null;
			try {
				while (bConnected) {
					String str = dis.readUTF();
					System.out.println(str);
					for (int i = 0; i < clients.size(); i++) {
						//ת����ÿ���ͻ���
						c = clients.get(i);
						c.send(str);
					}
				}
			}catch (EOFException e) {
				System.out.println("Client closed!");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				//������Դ
				try {
					if (dos != null)
						dos.close();
					if (dis != null)
						dis.close();
					if (s != null)
						s.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
}
