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
        setTitle("��������");
        btnStart.setBounds(new Rectangle(19, 16, 103, 27));
        btnStart.setFont(new java.awt.Font("Dialog", Font.PLAIN, 12));
        btnStart.setText("����������");
        btnStart.addActionListener(new ServerControl_btnStart_actionAdapter(this));
        lblState.setFont(new java.awt.Font("Dialog", Font.PLAIN, 12));
        lblState.setForeground(Color.red);
        Border border = BorderFactory.createEtchedBorder(Color.white,
                new Color(170, 170, 170));
        lblState.setBorder(new TitledBorder(border, "������״̬",
                                            TitledBorder.ABOVE_TOP,
                                            TitledBorder.CENTER,
                                            new Font("Dialog", Font.PLAIN, 12)));
        lblState.setHorizontalAlignment(SwingConstants.CENTER);
        lblState.setText("�ر�");
        lblState.setBounds(new Rectangle(142, 8, 230, 34));
        srpList.setFont(new java.awt.Font("Dialog", Font.PLAIN, 12));
        srpList.setBorder(new TitledBorder(border, "�û��б�",
                                           TitledBorder.ABOVE_TOP,
                                           TitledBorder.CENTER,
                                           new Font("Dialog", Font.PLAIN, 12)));
        srpList.setBounds(new Rectangle(23, 62, 96, 287));
        srpChatMessage.setFont(new java.awt.Font("Dialog", Font.PLAIN, 12));
        srpChatMessage.setBorder(new TitledBorder(border, "�����¼",
                                                  TitledBorder.ABOVE_TOP,
                                                  TitledBorder.CENTER,
                                                  new Font("Dialog", Font.PLAIN,
                12)));
        srpChatMessage.setBounds(new Rectangle(140, 62, 235, 284));
        txtMsg.setFont(new java.awt.Font("Dialog", Font.PLAIN, 12));
        txtMsg.setText("");
        txtMsg.setBounds(new Rectangle(27, 390, 218, 27));
        cmbType.addItem("Ⱥ��");
        cmbType.addItem("˽��");
        cmbType.setFont(new java.awt.Font("Dialog", Font.PLAIN, 12));
        cmbType.setBounds(new Rectangle(264, 390, 106, 27));
        cmbType.setBorder(new LineBorder(Color.GRAY));
        btnSend.setBounds(new Rectangle(263, 426, 110, 21));
        btnSend.setFont(new java.awt.Font("Dialog", Font.PLAIN, 12));
        btnSend.setText("������Ϣ");
        btnSend.addActionListener(new ServerControl_btnSend_actionAdapter(this));
        btnKick.setBounds(new Rectangle(28, 357, 78, 21));
        btnKick.setFont(new java.awt.Font("Dialog", Font.PLAIN, 12));
        btnKick.setText("����");
        btnKick.addActionListener(new ServerControl_btnKick_actionAdapter(this));
        btnSave.setBounds(new Rectangle(118, 357, 119, 21));
        btnSave.setFont(new java.awt.Font("Dialog", Font.PLAIN, 12));
        btnSave.setText("���������¼");
        btnSave.addActionListener(new ServerControl_btnSave_actionAdapter(this));
        btnClear.setBounds(new Rectangle(251, 357, 119, 21));
        btnClear.setFont(new java.awt.Font("Dialog", Font.PLAIN, 12));
        btnClear.setText("���������Ϣ");
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

    //������ť
    public void btnStart_actionPerformed(ActionEvent e) {
        String text = btnStart.getText();
        if (text.equals("����������")) {
            startServer();
        } else {
            stopServer();
        }
    }

    //����������
    public void startServer() {
        try {
            flag=true;
            ss = new ServerSocket(PORT);
            lblState.setText("�������Ѿ�������������" + PORT + "�˿�");
            btnStart.setText("�رշ�����");
            new ListenerThread().start();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "����������ʧ�ܣ�");
            ex.printStackTrace();
        }

    }

    //�رշ�����
    public void stopServer() {
        flag = false;
        lblState.setText("�������Ѿ��ر�");
        btnStart.setText("����������");
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
            JOptionPane.showMessageDialog(null, "�رշ�������������");
        }
    }

    //���˹���
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
                String msg = "����Ա����" + cb.getClientName() + "���ϳ���������";
                sendAll(msg);
                //ˢ�¿ͻ����б�
                sendList();
            }
        } else {
            JOptionPane.showMessageDialog(null, "����û��б���ѡ����Ҫ�߳����ˣ�");
        }
    }
    
    //����������־
    public void btnSave_actionPerformed(ActionEvent e) {
    	//�����ļ�������
        JFileChooser jfc = new JFileChooser();
        jfc.setAcceptAllFileFilterUsed(false);
        jfc.setFileFilter(new MyFileFilter());
        int choice = jfc.showSaveDialog(this);
        //��ȡ�ļ���ѡ�����
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
                JOptionPane.showMessageDialog(null, "�����ļ���������");
            }
        }
    }

    //�����ļ�������
    class MyFileFilter extends javax.swing.filechooser.FileFilter {
    	//��дϵͳ����
        public boolean accept(File f) {
            if (f.isDirectory() || f.getName().endsWith(".log")) {
                return true;
            } else {
                return false;
            }
        }

        public String getDescription() {
            return "��־�ļ�(*.log)";
        }

    }


    //�����߳�
    class ListenerThread extends Thread {
        public void run() {
            while (flag) {
                try {
                    Socket s = ss.accept();
                    //������ͻ��˻Ự�߳�
                    new ChatThread(s).start();
                } catch (IOException ex) {
                    flag = false;
                }
            }
        }
    }

    //���Ϳͻ��б���Ϣ
    public void sendList()
    {
        DefaultListModel dlm=(DefaultListModel) lstUsers.getModel();
        Enumeration em =dlm.elements();
        while (em.hasMoreElements()) {
            ClientBean cb = (ClientBean) em.nextElement();
            cb.getOut().println("list:"+dlm.toString());
        }

    }

    //����Ⱥ����Ϣ
    public void sendAll(String msg) {
        ((DefaultListModel) lstChat.getModel()).addElement(msg);
        Enumeration em = ((DefaultListModel) lstUsers.getModel()).elements();
        while (em.hasMoreElements()) {
            ClientBean cb = (ClientBean) em.nextElement();
            cb.getOut().println(msg);
        }
    }

    //����˽����Ϣ
    public void sendPrivate(String text, ClientBean cb) {
        ((DefaultListModel) lstChat.getModel()).addElement(text);
        cb.getOut().println(text);
    }

    //���������Ϣ
    public void btnClear_actionPerformed(ActionEvent e) {
        int choice = JOptionPane.showConfirmDialog(null, "ȷ����������¼��", "��ʾ",
                JOptionPane.OK_CANCEL_OPTION);
        if (choice == JOptionPane.OK_OPTION) {
            ((DefaultListModel) lstChat.getModel()).clear();
        }
    }

    //������Ϣ
    public void btnSend_actionPerformed(ActionEvent e) {
        //����������Ϣ����
        String text = txtMsg.getText().trim();
        txtMsg.setText("");
        if (text.equals("")) {
            JOptionPane.showMessageDialog(null, "��Ϣ����Ϊ�գ�");
        } else {
            //��÷��͵�����
            int index = cmbType.getSelectedIndex();
            //����Ϊ0������Ⱥ��
            if (index == 0) {
                String msg = "����Ա��������˵:" + text;
                sendAll(msg);
            }
            //������˽��
            else {
                Object obj = lstUsers.getSelectedValue();
                if (obj == null) {
                    JOptionPane.showMessageDialog(null, "˽�ı���Ҫѡ��һ���ͻ�");
                } else {
                    ClientBean cb = (ClientBean) obj;
                    String msg = "����Ա��" + cb.getClientName() + "˵:" + text;
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
    //�ͻ��˻Ự�߳�
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
                //��ÿͻ����ǳ�
                String clientName = br.readLine();
                if(existsClientName(clientName))
                {
                    s.close();
                }else
                {
                        //�����ͻ���BEAN
                    ClientBean cb = new ClientBean(clientName, out, s);
                    //����û��б�
                    DefaultListModel dlm = (DefaultListModel) lstUsers.getModel();

                    //���ͻ�����ӵ�ServerControl����
                    dlm.addElement(cb);
                    //��ӭ��
                    String welcome = "�����ﻶӭ" + clientName + "����JAVA�����ҡ�����";
                    //����ͻ��˷��ͻ�ӭ��
                    sendAll(welcome);
                    sendList();
                    //������ȡ�ÿͻ�����Ϣ�߳�
                    new GetThread(br, cb).start();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


    //��ȡ�ͻ�����Ϣ�߳�
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
                        String msg = content[0] + "��������˵:" + content[2];
                        sendAll(msg);
                    } else {
                        int index = Integer.parseInt(content[1]);
                        Object obj=lstUsers.getModel().getElementAt(index);
                        if(obj!=null)
                        {
                            ClientBean cb=(ClientBean)obj;
                            String msg=content[0]+"��"+cb.getClientName()+"˵:"+content[2];
                            sendPrivate(msg,cb);
                        }
                    }

                }
            }
            //���ͻ���ֱ�ӹرմ���ʱ����JLISTֱ���Ƴ��ÿͻ�����Ϣ
            catch (SocketException se) {
                if(flag)
                {
                    DefaultListModel dlm = (DefaultListModel) lstUsers.getModel();
                    //�жϸÿͻ����Ƿ��Ǳ�����Ա�߳�ȥ�ģ�������Ǿͻ���
                    if (dlm.contains(cb)) {
                        dlm.removeElement(cb);
                        String goodbye = "<html>����" + cb.getClientName() +
                                         "�뿪������</html>";
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
