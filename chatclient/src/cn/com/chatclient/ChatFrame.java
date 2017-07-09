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
            this.setTitle("欢迎" + clientName + "来到本聊天室");
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
        srpList.setBorder(new TitledBorder(border, "用户列表",
                                           TitledBorder.ABOVE_TOP,
                                           TitledBorder.CENTER,
                                           new Font("Dialog", Font.PLAIN, 12)));
        srpList.setBounds(new Rectangle(18, 14, 104, 314));
        srpChat.setBounds(new Rectangle(137, 16, 327, 309));
        srpChat.setBorder(new TitledBorder(border, "聊天记录",
                                           TitledBorder.ABOVE_TOP,
                                           TitledBorder.CENTER,
                                           new Font("Dialog", Font.PLAIN, 12)));
        cmbType.setFont(new java.awt.Font("Dialog", Font.PLAIN, 12));
        cmbType.setBounds(new Rectangle(344, 333, 115, 28));
        Vector vc = new Vector();
        btnSend.setBounds(new Rectangle(344, 372, 114, 28));
        btnSend.setFont(new java.awt.Font("Dialog", Font.PLAIN, 12));
        btnSend.setText("发送");
        btnSend.addActionListener(new ChatFrame_btnSend_actionAdapter(this));
        txtMessage.setFont(new java.awt.Font("Dialog", Font.PLAIN, 12));
        vc.add("群聊");
        vc.add("私聊");
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

//发送消息
    public void btnSend_actionPerformed(ActionEvent e) {
        String text = txtMessage.getText().trim();
        txtMessage.setText("");
        if (text.equals("")) {
            JOptionPane.showMessageDialog(null, "信息不能为空！");
        } else {
            //获得发送消息的类型
            int index = cmbType.getSelectedIndex();
            if (index == 0) {
                out.println(clientName + ":all:" + text);
            } else {
                int item = lstUsers.getSelectedIndex();
                if (item == -1) {
                    JOptionPane.showMessageDialog(null, "私聊必须选择一个用户");
                } else {
                    String name=lstUsers.getSelectedValue().toString();
                    if(name.equals(clientName))
                    {
                        JOptionPane.showMessageDialog(null,"您不能和您自己私聊");
                    }else
                    {
                        ((DefaultListModel)lstChat.getModel()).addElement(clientName+"对"+name+"说:"+text);
                        out.println(clientName + ":" + item + ":" + text);
                    }
                }
            }
        }
    }


//会话线程
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


//获取信息线程
    class GetThread extends Thread {
        private BufferedReader br;
        public GetThread(BufferedReader br) {
            this.br = br;
        }

        public void run() {
            try {
                while (true) {
                    String msg = br.readLine();
                    //判断是否服务器已经关闭，或者已经被服务器T出来
                    if (msg == null) {
                        break;
                    }
                    //判断服务器发送过来的内容是什么类型？ //发送客户列表信息
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
                JOptionPane.showMessageDialog(null, "<html>与主机失去联系！发生错误的原因可能有:<br>①您的昵称已经存在,请更改<br>②服务器已经关闭,请等待服务器重新启动<br>③您被管理员踢出来了！请注意遵守聊天室规则</html>");
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
