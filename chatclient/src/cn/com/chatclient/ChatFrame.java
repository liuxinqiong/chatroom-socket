package cn.com.chatclient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.util.*;
import java.net.*;
import java.io.*;

public class ChatFrame extends JFrame {
    private Socket s;
    private String clientName;
    private PrintWriter out;
    JScrollPane srpList = new JScrollPane();
    JList lstUsers = new JList();
    JScrollPane srpChat = new JScrollPane();
    JList lstChat = new JList();
    JTextField txtMessage = new JTextField();
    JComboBox cmbType = new JComboBox();
    JButton btnSend = new JButton();


    public ChatFrame(Socket s, String clientName) {
        try {
            this.s = s;
            this.clientName = clientName;
            this.setTitle("��ӭ" + clientName + "������������");
            jbInit();
        } catch (Exception exception) {

        }
    }

    private void jbInit() throws Exception {
        lstChat.setFont(new java.awt.Font("Dialog", Font.PLAIN, 12));
        lstChat.setModel(new DefaultListModel());
        lstUsers.setFont(new java.awt.Font("Dialog", Font.PLAIN, 12));
        lstUsers.setModel(new DefaultListModel());
        getContentPane().setLayout(null);
        Border border = BorderFactory.createEtchedBorder(Color.white,
                new Color(170, 170, 170));
        srpList.setBorder(new TitledBorder(border, "�û��б�",
                                           TitledBorder.ABOVE_TOP,
                                           TitledBorder.CENTER,
                                           new Font("Dialog", Font.PLAIN, 12)));
        srpList.setBounds(new Rectangle(18, 14, 104, 314));
        srpChat.setBounds(new Rectangle(137, 16, 327, 309));
        srpChat.setBorder(new TitledBorder(border, "�����¼",
                                           TitledBorder.ABOVE_TOP,
                                           TitledBorder.CENTER,
                                           new Font("Dialog", Font.PLAIN, 12)));
        cmbType.setFont(new java.awt.Font("Dialog", Font.PLAIN, 12));
        cmbType.setBounds(new Rectangle(344, 333, 115, 28));
        Vector vc = new Vector();
        btnSend.setBounds(new Rectangle(344, 372, 114, 28));
        btnSend.setFont(new java.awt.Font("Dialog", Font.PLAIN, 12));
        btnSend.setText("����");
        btnSend.addActionListener(new ChatFrame_btnSend_actionAdapter(this));
        txtMessage.setFont(new java.awt.Font("Dialog", Font.PLAIN, 12));
        vc.add("Ⱥ��");
        vc.add("˽��");
        cmbType.setBorder(new LineBorder(Color.GRAY));
        cmbType.setModel(new DefaultComboBoxModel(vc));
        this.getContentPane().add(srpList);
        txtMessage.setText("");
        txtMessage.setBounds(new Rectangle(22, 333, 305, 28));
        this.getContentPane().add(txtMessage);
        this.getContentPane().add(srpChat);
        this.getContentPane().add(cmbType);
        this.getContentPane().add(btnSend);
        srpChat.getViewport().add(lstChat);
        srpList.getViewport().add(lstUsers);
        lstUsers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        new ChatThread(s).start();

    }

//������Ϣ
    public void btnSend_actionPerformed(ActionEvent e) {
        String text = txtMessage.getText().trim();
        txtMessage.setText("");
        if (text.equals("")) {
            JOptionPane.showMessageDialog(null, "��Ϣ����Ϊ�գ�");
        } else {
            //��÷�����Ϣ������
            int index = cmbType.getSelectedIndex();
            if (index == 0) {
                out.println(clientName + ":all:" + text);
            } else {
                int item = lstUsers.getSelectedIndex();
                if (item == -1) {
                    JOptionPane.showMessageDialog(null, "˽�ı���ѡ��һ���û�");
                } else {
                    String name=lstUsers.getSelectedValue().toString();
                    if(name.equals(clientName))
                    {
                        JOptionPane.showMessageDialog(null,"�����ܺ����Լ�˽��");
                    }else
                    {
                        ((DefaultListModel)lstChat.getModel()).addElement(clientName+"��"+name+"˵:"+text);
                        out.println(clientName + ":" + item + ":" + text);
                    }
                }
            }
        }
    }


//�Ự�߳�
    class ChatThread extends Thread {
        private Socket s;
        public ChatThread(Socket s) {
            this.s = s;
        }

        public void run() {
            try {
                out = new PrintWriter(s.getOutputStream(), true);
                BufferedReader br = new BufferedReader(new InputStreamReader(s.
                        getInputStream()));
                new GetThread(br).start();
                out.println(clientName);
            } catch (Exception ex) {
            }
        }
    }


//��ȡ��Ϣ�߳�
    class GetThread extends Thread {
        private BufferedReader br;
        public GetThread(BufferedReader br) {
            this.br = br;
        }

        public void run() {
            try {
                while (true) {
                    String msg = br.readLine();
                    //�ж��Ƿ�������Ѿ��رգ������Ѿ���������T����
                    if (msg == null) {
                        break;
                    }
                    //�жϷ��������͹�����������ʲô���ͣ� //���Ϳͻ��б���Ϣ
                    if(msg.startsWith("list:"))
                    {
                        DefaultListModel  dlm=new DefaultListModel();;
                        String list=msg.substring(5);
                        StringTokenizer st=new StringTokenizer(list,"[,]");
                        while(st.hasMoreTokens())
                        {
                           dlm.addElement(st.nextToken().trim());
                        }
                        lstUsers.setModel(dlm);
                    }else
                    {
                        ((DefaultListModel) lstChat.getModel()).addElement(msg);
                    }
                }
            } catch (Exception ex) {
            } finally {
                JOptionPane.showMessageDialog(null, "<html>������ʧȥ��ϵ�����������ԭ�������:<br>�������ǳ��Ѿ�����,�����<br>�ڷ������Ѿ��ر�,��ȴ���������������<br>����������Ա�߳����ˣ���ע�����������ҹ���</html>");
                new Start();
                ChatFrame.this.dispose();
            }
        }
    }

}


class ChatFrame_btnSend_actionAdapter implements ActionListener {
    private ChatFrame adaptee;
    ChatFrame_btnSend_actionAdapter(ChatFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.btnSend_actionPerformed(e);
    }
}
