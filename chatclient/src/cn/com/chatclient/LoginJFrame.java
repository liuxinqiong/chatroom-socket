package cn.com.chatclient;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.net.*;

public class LoginJFrame extends JFrame {
    JPanel contentPane;
    JLabel lblTitle = new JLabel();
    JLabel lblHost = new JLabel();
    JTextField txtName = new JTextField();
    JLabel lblNick = new JLabel();
    JTextField txtNick = new JTextField();
    JButton btnLogin = new JButton();
    JButton btnClose = new JButton();
    public LoginJFrame() {
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
        contentPane = (JPanel) getContentPane();
        contentPane.setLayout(null);
        setSize(new Dimension(333, 224));
        setTitle("用户登录");
        lblTitle.setFont(new java.awt.Font("Dialog", Font.PLAIN, 12));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setText("聊天登录界面");
        lblTitle.setBounds(new Rectangle(50, 13, 233, 30));
        lblHost.setFont(new java.awt.Font("Dialog", Font.PLAIN, 12));
        lblHost.setText("请输入主机名：");
        lblHost.setBounds(new Rectangle(29, 60, 93, 21));
        txtName.setText("localhost");
        txtName.setBounds(new Rectangle(133, 57, 173, 27));
        lblNick.setFont(new java.awt.Font("Dialog", Font.PLAIN, 12));
        lblNick.setText("昵                称：");
        lblNick.setBounds(new Rectangle(29, 103, 90, 24));
        txtNick.setText("");
        txtNick.setBounds(new Rectangle(133, 103, 173, 27));
        btnLogin.setBounds(new Rectangle(134, 145, 67, 30));
        btnLogin.setFont(new java.awt.Font("Dialog", Font.PLAIN, 12));
        btnLogin.setText("登录");
        btnLogin.addActionListener(new LoginJFrame_btnLogin_actionAdapter(this));
        btnClose.setBounds(new Rectangle(237, 144, 67, 30));
        btnClose.setFont(new java.awt.Font("Dialog", Font.PLAIN, 12));
        btnClose.setText("关闭");
        btnClose.addActionListener(new LoginJFrame_btnClose_actionAdapter(this));
        contentPane.add(lblTitle);
        contentPane.add(lblHost);
        contentPane.add(txtName);
        contentPane.add(lblNick);
        contentPane.add(txtNick);
        contentPane.add(btnLogin);
        contentPane.add(btnClose);

    }

    public void btnClose_actionPerformed(ActionEvent e) {
        System.exit(0);
    }

    public void btnLogin_actionPerformed(ActionEvent e) {
        String hostName=txtName.getText().trim();
        String nickName=txtNick.getText().trim();
        if(hostName.equals("")||nickName.equals(""))
        {
            JOptionPane.showMessageDialog(null,"主机名和昵称必须输入！","错误",JOptionPane.ERROR_MESSAGE);
        }else
        {
            try {
                InetAddress addr = InetAddress.getByName(hostName);
                Socket s=new Socket(addr,9001);
                ChatFrame cf=new ChatFrame(s,nickName);
                cf.setSize(490,450);
                cf.setResizable(false);
                cf.setLocationRelativeTo(cf);
                cf.setVisible(true);
                this.dispose();
            } catch (Exception ex)
            {
                JOptionPane.showMessageDialog(null,"主机不存在！请检查您的输入！");
            }
        }
    }
}


class LoginJFrame_btnLogin_actionAdapter implements ActionListener {
    private LoginJFrame adaptee;
    LoginJFrame_btnLogin_actionAdapter(LoginJFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.btnLogin_actionPerformed(e);
    }
}


class LoginJFrame_btnClose_actionAdapter implements ActionListener {
    private LoginJFrame adaptee;
    LoginJFrame_btnClose_actionAdapter(LoginJFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.btnClose_actionPerformed(e);
    }
}
