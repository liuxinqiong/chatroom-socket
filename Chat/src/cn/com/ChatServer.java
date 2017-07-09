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
			//创建服务器套接字
			ss = new ServerSocket(8888);
			System.out.println("服务启动成功，正在监听8888端口...");
			started = true;
		} catch (BindException e) {
			System.out.println("端口使用中....");
			System.out.println("请关掉相关程序并重新运行服务器！");
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			while (started) {
				//阻塞 循环监听
				Socket s = ss.accept();
				//为客户端创建线程
				Client c = new Client(s);
				clients.add(c);
				System.out.println("一个客户端连接上来");
				//启动线程
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
		private Socket s;//创建套接字
		private DataInputStream dis = null;//输入流
		private boolean bConnected = false;
		private DataOutputStream dos;//输出流
		public Client(Socket s) {
			this.s = s;
			try {
				//利用客户端套接字分别得到输入流和输出流
				dis = new DataInputStream(s.getInputStream());
				dos = new DataOutputStream(s.getOutputStream());
				bConnected = true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//发送数据的方法
		public void send(String msg) {
			try {
				dos.writeUTF(msg);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				clients.remove(this);
				System.out.println("一个客户退出了");
			}
		}

		public void run() {
			Client c = null;
			try {
				while (bConnected) {
					String str = dis.readUTF();
					System.out.println(str);
					for (int i = 0; i < clients.size(); i++) {
						//转发给每个客户端
						c = clients.get(i);
						c.send(str);
					}
				}
			}catch (EOFException e) {
				System.out.println("Client closed!");
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				//回收资源
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
