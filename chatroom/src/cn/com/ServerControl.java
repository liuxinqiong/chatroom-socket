package cn.com;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;
import javax.swing.border.*;
import java.util.*;

public class ServerControl extends JFrame {
    JPanel contentPane;
    JButton btnStart = new JButton();
    JLabel lblState = new JLabel();
    TitledBorder titledBorder1 = new TitledBorder("");
    JScrollPane srpList = new JScrollPane();

    DefaultListModel dlmUsers = new DefaultListModel();
    JList lstUsers = new JList(dlmUsers);
    JScrollPane srpChatMessage = new JScrollPane();
    DefaultListModel dlmChat = new DefaultListModel();
    JList lstChat = new JList(dlmChat);
    ServerSocket ss;
    public static int PORT = 9001;
    public boolean flag = true;
    JTextField txtMsg = new JTextField();
    JComboBox cmbType = new JComboBox();
    JButton btnSend = new JButton();
    JButton btnKick = new JButton();
    JButton btnSave = new JButton();
    JButton btnClear = new JButton();

    public ServerControl() {
        try {
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            jbInit();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Component initialization.
     *
     * @throws java.lang.Exception
     */
    private void jbInit() throws Exception {
        lstChat.setFont(new java.awt.Font("Dialog", Font.PLAIN, 12));
        lstChat.setAutoscrolls(true);
        this.srpChatMessage.setAutoscrolls(true);
        contentPane = (JPanel) getContentPane();
        contentPane.setLayout(null);
        setSize(new Dimension(395, 500));
        setResizable(false);
        setTitle("服务器端");
        btnStart.setBounds(new Rectangle(19, 16, 103, 27));
        btnStart.setFont(new java.awt.Font("Dialog", Font.PLAIN, 12));
        btnStart.setText("启动服务器");
        btnStart.addActionListener(new ServerControl_btnStart_actionAdapter(this));
        lblState.setFont(new java.awt.Font("Dialog", Font.PLAIN, 12));
        lblState.setForeground(Color.red);
        Border border = BorderFactory.createEtchedBorder(Color.white,
                new Color(170, 170, 170));
        lblState.setBorder(new TitledBorder(border, "服务器状态",
                                            TitledBorder.ABOVE_TOP,
                                            TitledBorder.CENTER,
                                            new Font("Dialog", Font.PLAIN, 12)));
        lblState.setHorizontalAlignment(SwingConstants.CENTER);
        lblState.setText("关闭");
        lblState.setBounds(new Rectangle(142, 8, 230, 34));
        srpList.setFont(new java.awt.Font("Dialog", Font.PLAIN, 12));
        srpList.setBorder(new TitledBorder(border, "用户列表",
                                           TitledBorder.ABOVE_TOP,
                                           TitledBorder.CENTER,
                                           new Font("Dialog", Font.PLAIN, 12)));
        srpList.setBounds(new Rectangle(23, 62, 96, 287));
        srpChatMessage.setFont(new java.awt.Font("Dialog", Font.PLAIN, 12));
        srpChatMessage.setBorder(new TitledBorder(border, "聊天记录",
                                                  TitledBorder.ABOVE_TOP,
                                                  TitledBorder.CENTER,
                                                  new Font("Dialog", Font.PLAIN,
                12)));
        srpChatMessage.setBounds(new Rectangle(140, 62, 235, 284));
        txtMsg.setFont(new java.awt.Font("Dialog", Font.PLAIN, 12));
        txtMsg.setText("");
        txtMsg.setBounds(new Rectangle(27, 390, 218, 27));
        cmbType.addItem("群聊");
        cmbType.addItem("私聊");
        cmbType.setFont(new java.awt.Font("Dialog", Font.PLAIN, 12));
        cmbType.setBounds(new Rectangle(264, 390, 106, 27));
        cmbType.setBorder(new LineBorder(Color.GRAY));
        btnSend.setBounds(new Rectangle(263, 426, 110, 21));
        btnSend.setFont(new java.awt.Font("Dialog", Font.PLAIN, 12));
        btnSend.setText("发送消息");
        btnSend.addActionListener(new ServerControl_btnSend_actionAdapter(this));
        btnKick.setBounds(new Rectangle(28, 357, 78, 21));
        btnKick.setFont(new java.awt.Font("Dialog", Font.PLAIN, 12));
        btnKick.setText("踢人");
        btnKick.addActionListener(new ServerControl_btnKick_actionAdapter(this));
        btnSave.setBounds(new Rectangle(118, 357, 119, 21));
        btnSave.setFont(new java.awt.Font("Dialog", Font.PLAIN, 12));
        btnSave.setText("保存聊天记录");
        btnSave.addActionListener(new ServerControl_btnSave_actionAdapter(this));
        btnClear.setBounds(new Rectangle(251, 357, 119, 21));
        btnClear.setFont(new java.awt.Font("Dialog", Font.PLAIN, 12));
        btnClear.setText("清除聊天信息");
        btnClear.addActionListener(new ServerControl_btnClear_actionAdapter(this));
        lstUsers.setFont(new java.awt.Font("Dialog", Font.PLAIN, 12));
        contentPane.add(btnStart);
        contentPane.add(srpList);
        contentPane.add(srpChatMessage);
        contentPane.add(lblState);
        contentPane.add(btnKick);
        contentPane.add(btnSave);
        contentPane.add(btnClear);
        contentPane.add(cmbType);
        contentPane.add(txtMsg);
        contentPane.add(btnSend);
        srpChatMessage.getViewport().add(lstChat);
        srpList.getViewport().add(lstUsers);
        lstUsers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    //启动按钮
    public void btnStart_actionPerformed(ActionEvent e) {
        String text = btnStart.getText();
        if (text.equals("启动服务器")) {
            startServer();
        } else {
            stopServer();
        }
    }

    //启动服务器
    public void startServer() {
        try {
            flag=true;
            ss = new ServerSocket(PORT);
            lblState.setText("服务器已经启动，在侦听" + PORT + "端口");
            btnStart.setText("关闭服务器");
            new ListenerThread().start();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "服务器启动失败！");
            ex.printStackTrace();
        }

    }

    //关闭服务器
    public void stopServer() {
        flag = false;
        lblState.setText("服务器已经关闭");
        btnStart.setText("启动服务器");
        try {
            DefaultListModel dlm=(DefaultListModel)lstUsers.getModel();
            Enumeration emu=dlm.elements();
            while(emu.hasMoreElements())
            {
                ((ClientBean)emu.nextElement()).getS().close();
            }
            dlm.clear();
            ss.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "关闭服务器产生错误！");
        }
    }

    //踢人功能
    public void btnKick_actionPerformed(ActionEvent e) {
        Object obj = lstUsers.getSelectedValue();
        if (obj != null) {
            ClientBean cb = (ClientBean) obj;
            ((DefaultListModel) lstUsers.getModel()).removeElement(cb);
            try {
                cb.getS().close();
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                String msg = "管理员将【" + cb.getClientName() + "】赶出了聊天室";
                sendAll(msg);
                //刷新客户端列表
                sendList();
            }
        } else {
            JOptionPane.showMessageDialog(null, "请从用户列表中选择您要踢出的人！");
        }
    }
    
    //保存聊天日志
    public void btnSave_actionPerformed(ActionEvent e) {
    	//设置文件过滤器
        JFileChooser jfc = new JFileChooser();
        jfc.setAcceptAllFileFilterUsed(false);
        jfc.setFileFilter(new MyFileFilter());
        int choice = jfc.showSaveDialog(this);
        //获取文件的选择对象
        if (choice == jfc.APPROVE_OPTION) {
            File destnation = jfc.getSelectedFile();
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter(
                        destnation.getAbsolutePath() + ".log", true));
                DefaultListModel dlm = (DefaultListModel) lstChat.getModel();
                Enumeration em = dlm.elements();
                while (em.hasMoreElements()) {
                    bw.write(em.nextElement().toString());
                    bw.newLine();
                }
                bw.close();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "保存文件发生错误！");
            }
        }
    }

    //设置文件过滤器
    class MyFileFilter extends javax.swing.filechooser.FileFilter {
    	//重写系统方法
        public boolean accept(File f) {
            if (f.isDirectory() || f.getName().endsWith(".log")) {
                return true;
            } else {
                return false;
            }
        }

        public String getDescription() {
            return "日志文件(*.log)";
        }

    }


    //监听线程
    class ListenerThread extends Thread {
        public void run() {
            while (flag) {
                try {
                    Socket s = ss.accept();
                    //启动与客户端会话线程
                    new ChatThread(s).start();
                } catch (IOException ex) {
                    flag = false;
                }
            }
        }
    }

    //发送客户列表信息
    public void sendList()
    {
        DefaultListModel dlm=(DefaultListModel) lstUsers.getModel();
        Enumeration em =dlm.elements();
        while (em.hasMoreElements()) {
            ClientBean cb = (ClientBean) em.nextElement();
            cb.getOut().println("list:"+dlm.toString());
        }

    }

    //发送群聊信息
    public void sendAll(String msg) {
        ((DefaultListModel) lstChat.getModel()).addElement(msg);
        Enumeration em = ((DefaultListModel) lstUsers.getModel()).elements();
        while (em.hasMoreElements()) {
            ClientBean cb = (ClientBean) em.nextElement();
            cb.getOut().println(msg);
        }
    }

    //发送私聊信息
    public void sendPrivate(String text, ClientBean cb) {
        ((DefaultListModel) lstChat.getModel()).addElement(text);
        cb.getOut().println(text);
    }

    //清空聊天信息
    public void btnClear_actionPerformed(ActionEvent e) {
        int choice = JOptionPane.showConfirmDialog(null, "确认清空聊天记录吗？", "提示",
                JOptionPane.OK_CANCEL_OPTION);
        if (choice == JOptionPane.OK_OPTION) {
            ((DefaultListModel) lstChat.getModel()).clear();
        }
    }

    //发送消息
    public void btnSend_actionPerformed(ActionEvent e) {
        //获得输入的信息内容
        String text = txtMsg.getText().trim();
        txtMsg.setText("");
        if (text.equals("")) {
            JOptionPane.showMessageDialog(null, "消息不能为空！");
        } else {
            //获得发送的类型
            int index = cmbType.getSelectedIndex();
            //索引为0代表是群聊
            if (index == 0) {
                String msg = "管理员对所有人说:" + text;
                sendAll(msg);
            }
            //否则是私聊
            else {
                Object obj = lstUsers.getSelectedValue();
                if (obj == null) {
                    JOptionPane.showMessageDialog(null, "私聊必须要选择一个客户");
                } else {
                    ClientBean cb = (ClientBean) obj;
                    String msg = "管理员对" + cb.getClientName() + "说:" + text;
                    sendPrivate(msg, cb);
                }
            }
        }
    }

    public boolean existsClientName(String clientName)
    {
        boolean flag=false;
        java.util.Enumeration emu=((DefaultListModel) lstUsers.getModel()).elements();
        while(emu.hasMoreElements())
        {
            ClientBean cb=(ClientBean)emu.nextElement();
            if(cb.getClientName().equals(clientName))
            {
                flag=true;
                break;
            }
        }
        return flag;
    }
    //客户端会话线程
    class ChatThread extends Thread {
        private Socket s;
        public ChatThread(Socket s) {
            this.s = s;
        }

        public void run() {
            try {
                PrintWriter out = new PrintWriter(s.getOutputStream(), true);
                BufferedReader br = new BufferedReader(new InputStreamReader(s.
                        getInputStream()));
                //获得客户端昵称
                String clientName = br.readLine();
                if(existsClientName(clientName))
                {
                    s.close();
                }else
                {
                        //创建客户端BEAN
                    ClientBean cb = new ClientBean(clientName, out, s);
                    //获得用户列表
                    DefaultListModel dlm = (DefaultListModel) lstUsers.getModel();

                    //将客户端添加到ServerControl界面
                    dlm.addElement(cb);
                    //欢迎词
                    String welcome = "☆☆★★★欢迎" + clientName + "来到JAVA聊天室★★★☆☆";
                    //向各客户端发送欢迎词
                    sendAll(welcome);
                    sendList();
                    //启动获取该客户端信息线程
                    new GetThread(br, cb).start();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


    //获取客户端信息线程
    class GetThread extends Thread {
        private BufferedReader br;
        private ClientBean cb;
        private DefaultListModel dlm = (DefaultListModel) lstChat.getModel();

        public GetThread(BufferedReader br, ClientBean cb) {
            this.br = br;
            this.cb = cb;
        }

        public void run() {
            try {
                while (true) {
                    String text = br.readLine();
                    String[] content = text.split(":");
                    if (content[1].equals("all")) {
                        String msg = content[0] + "对所有人说:" + content[2];
                        sendAll(msg);
                    } else {
                        int index = Integer.parseInt(content[1]);
                        Object obj=lstUsers.getModel().getElementAt(index);
                        if(obj!=null)
                        {
                            ClientBean cb=(ClientBean)obj;
                            String msg=content[0]+"对"+cb.getClientName()+"说:"+content[2];
                            sendPrivate(msg,cb);
                        }
                    }

                }
            }
            //当客户端直接关闭窗体时，从JLIST直接移除该客户端信息
            catch (SocketException se) {
                if(flag)
                {
                    DefaultListModel dlm = (DefaultListModel) lstUsers.getModel();
                    //判断该客户端是否是被管理员踢出去的，如果不是就欢送
                    if (dlm.contains(cb)) {
                        dlm.removeElement(cb);
                        String goodbye = "<html>欢送" + cb.getClientName() +
                                         "离开聊天室</html>";
                        sendAll(goodbye);
                        sendList();
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}


class ServerControl_btnSend_actionAdapter implements ActionListener {
    private ServerControl adaptee;
    ServerControl_btnSend_actionAdapter(ServerControl adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.btnSend_actionPerformed(e);
    }
}


class ServerControl_btnClear_actionAdapter implements ActionListener {
    private ServerControl adaptee;
    ServerControl_btnClear_actionAdapter(ServerControl adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.btnClear_actionPerformed(e);
    }
}


class ServerControl_btnSave_actionAdapter implements ActionListener {
    private ServerControl adaptee;
    ServerControl_btnSave_actionAdapter(ServerControl adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.btnSave_actionPerformed(e);
    }
}


class ServerControl_btnKick_actionAdapter implements ActionListener {
    private ServerControl adaptee;
    ServerControl_btnKick_actionAdapter(ServerControl adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.btnKick_actionPerformed(e);
    }
}


class ServerControl_btnStart_actionAdapter implements ActionListener {
    private ServerControl adaptee;
    ServerControl_btnStart_actionAdapter(ServerControl adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.btnStart_actionPerformed(e);
    }
}
