package applicationpack;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.border.Border;

class LoginFrame extends JFrame
{
    private Font            fnt = new Font("Verdana",1,12);
    private JComboBox       cmbRole;
    private JTextField      txtUid;
    private JPasswordField  txtPwd;
    private JButton         btnSignup,btnSignin,btnReset,btnExit;
    private String[]        role = {"Select Your Role","Admin","Professor","Student"};

    private JLabel makeLabel(String cap,int x,int y,int w,int h)
    {
        JLabel temp = new JLabel(cap);
        temp.setFont(new Font("Courier New", 1, 18));
        temp.setForeground(Color.BLUE);
        temp.setBounds(x,y,w,h);
        super.add(temp);
        return temp;
    }
    private JComponent makeTextField(int x,int y,int w,int h,int mode)
    {
        Border brdr = BorderFactory.createLineBorder(Color.BLACK, 1);
        JComponent temp = null;
        if(mode == 1)
            temp = new JTextField();
        else if(mode == 2)
            temp = new JPasswordField();
        temp.setFont(new Font("Courier New", 1, 18));
        temp.setBounds(x,y,w,h);
        temp.setBorder(brdr);
        super.add(temp);
        return temp;
    }
    private JComboBox makeComboBox(String sub[],int x,int y,int w,int h)
    {
        Border brdr = BorderFactory.createLineBorder(Color.BLACK, 1);
        JComboBox temp = new JComboBox(sub);
        temp.setBounds(x,y,w,h);
        temp.setFont(new Font("Courier New",1,18));
        temp.setBorder(brdr);
        ((JLabel)temp.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
        temp.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                int listIndex = temp.getSelectedIndex();
                if(listIndex == 0)
                {
                    btnSignup.setEnabled(false);
                    btnSignin.setEnabled(false);
                    btnReset.setEnabled(false);
                }
                if(listIndex == 1 || listIndex == 2 || listIndex == 3)
                {
                    btnSignup.setEnabled(false);
                    btnSignin.setEnabled(true);
                    btnReset.setEnabled(true);
                }
                if(listIndex == 1) btnSignup.setEnabled(true);
            }
        });
        add(temp);
        return temp;
    }
    private JButton makeButton(String cap,int x,int y,int w,int h)
    {
        JButton temp = new JButton(cap);
        temp.setFont(new Font("verdana", Font.BOLD, 12));
        temp.setBounds(x,y,w,h);
        temp.setMargin(new Insets(0,0,0,0));
        ActionListener act = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                try
                {
                    Object ob = e.getSource();
                    if(ob == btnSignup)
                    {
                        Toolkit tk = Toolkit.getDefaultToolkit();
                        Image img = tk.getImage("p0.jpg");
                        SignupFrame signFrame = new SignupFrame();
                        signFrame.setIconImage(img);
                        signFrame.setTitle("SIGN UP PANEL...");
                        signFrame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                        signFrame.setResizable(false);
                        signFrame.setSize(500,300);
                        signFrame.setLocationRelativeTo(null);
                        signFrame.getContentPane().setBackground(new Color(250,250,200));
                        signFrame.setLayout(new BorderLayout());
                        signFrame.setModal(true);
                        signFrame.setUndecorated(true);
                        signFrame.getRootPane().setWindowDecorationStyle(JRootPane.COLOR_CHOOSER_DIALOG);
                        signFrame.setVisible(true);
                    }
                    else if(ob == btnSignin)
                    {
                        if(cmbRole.getSelectedIndex() == 0 || txtUid.getText().equals("") || txtPwd.getText().equals(""))
                            JOptionPane.showMessageDialog(null, "INCOMPLETE CREDENTIAL SUBMITTED");
                        else
                        {
                            Class.forName("com.mysql.cj.jdbc.Driver");
                            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ankan?autoReconnect=true&useSSL=false","root","ankan");
                            PreparedStatement pst = con.prepareStatement("SELECT USERID,PASSWORD,ROLE FROM USER WHERE USERID = ? AND PASSWORD = ? AND ROLE = ?");
                            pst.setString(1, txtUid.getText());
                            pst.setString(2, txtPwd.getText());
                            pst.setString(3, (String)cmbRole.getSelectedItem());
                            ResultSet rst = pst.executeQuery();
                            if(!rst.next())
                            {
                                JOptionPane.showMessageDialog(null, "CREDENTIAL ERROR");
                                txtUid.setText("");
                                txtPwd.setText("");
                                txtUid.grabFocus();
                            }
                            else
                            {
                                dispose();
                                Toolkit tk = Toolkit.getDefaultToolkit();
                                Image img = tk.getImage("p0.jpg");
                                MainFrame mFrame = new MainFrame();
                                mFrame.setIconImage(img);
                                mFrame.setTitle("STUDENT MANAGEMENT SYSTEM");
                                mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                                mFrame.setResizable(false);
                                mFrame.setSize(800,600);
                                mFrame.setLocationRelativeTo(null);
                                mFrame.getContentPane().setBackground(new Color(190,230,160));
                                mFrame.setUndecorated(true);
                                mFrame.getRootPane().setWindowDecorationStyle(JRootPane.COLOR_CHOOSER_DIALOG);
                                mFrame.setVisible(true);
                            }
                        }
                    }
                    else if(ob == btnReset)
                    {
                        cmbRole.setSelectedIndex(0);
                        txtUid.setText("");
                        txtPwd.setText("");
                        txtUid.grabFocus();
                    }
                    else if(ob == btnExit)
                    {
                        System.exit(0);
                    }
                }
                catch(ClassNotFoundException | SQLException ex)
                {
                    JOptionPane.showMessageDialog(null,ex);
                }
            }
        };
        temp.addActionListener(act);
        super.add(temp);
        return temp;
    }
    public LoginFrame()
    {
        Border brdr1 = BorderFactory.createLineBorder(Color.RED, 2);
        Border brdr2 = BorderFactory.createLineBorder(Color.BLUE, 2);
        Border brdr3 = BorderFactory.createCompoundBorder(brdr1, brdr2);
        JLabel caption = new JLabel("ON BOARD LOGIN");
        caption.setFont(new Font("verdana",1,24));
        caption.setHorizontalAlignment(JLabel.CENTER);
        caption.setOpaque(true);
        caption.setBackground(Color.YELLOW);
        caption.setForeground(Color.red);
        caption.setBorder(brdr3);
        caption.setBounds(10,10,470,50);
        super.add(caption);
        
        makeLabel("SELECT ROLE/PRIVILEGE",       10, 70,250,30);
        cmbRole = makeComboBox(role,            250, 70,230,30);
        makeLabel("ENTER USER ID",               10,110,250,30);
        txtUid  = (JTextField)makeTextField(    250,110,230,30,1);
        txtUid.setHorizontalAlignment(JTextField.CENTER);
        makeLabel("ENTER PASSWORD",              10,150,250,30);
        txtPwd  = (JPasswordField)makeTextField(250,150,230,30,2);
        txtPwd.setHorizontalAlignment(JLabel.CENTER);
        txtPwd.setEchoChar('*');
        
        btnSignup   = makeButton("Sign Up",     20,190,100,30);
        btnSignup.setEnabled(false);
        btnSignin   = makeButton("Sign In",    140,190,100,30);
        btnSignin.setEnabled(false);
        btnReset    = makeButton("Reset",      260,190,100,30);
        btnReset.setEnabled(false);
        btnExit     = makeButton("Exit",       380,190,100,30);
    }
}

public class LoginMain
{
    public static void main(String[] args)
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ankan?autoReconnect=true&useSSL=false","root","ankan");
            DatabaseMetaData metadata = con.getMetaData();
            ResultSet result = metadata.getTables("ankan", "root", "USER", new String[]{"TABLE"});
            Statement sst = con.createStatement();
            if(!result.next())
            {
                String sql = "";
                sql = "CREATE TABLE USER(USERID VARCHAR(20) PRIMARY KEY,PASSWORD VARCHAR(20),ROLE VARCHAR(9))";
                sst.executeUpdate(sql);
                sql = "INSERT INTO USER VALUES('admin','admin','Admin')";
                sst.executeUpdate(sql);
                sql = "CREATE TABLE STUDENT_MASTER(STUDENT_ID VARCHAR(15) PRIMARY KEY,NAME VARCHAR(20),FATHER_NAME VARCHAR(20),GENDER VARCHAR(6),ADDRESS VARCHAR(50),DOB DATE,PHONE VARCHAR(12),EMAIL VARCHAR(30),COURSE VARCHAR(5),SEMESTERS CHAR(1))";
                sst.executeUpdate(sql);
                sql = "CREATE TABLE PROFESSOR_MASTER(PROFESSOR_ID VARCHAR(13) PRIMARY KEY,NAME VARCHAR(20),ADDRESS VARCHAR(50),GENDER VARCHAR(6),PHONE VARCHAR(12),EMAIL VARCHAR(30),DOB DATE,DOJ DATE)";
                sst.executeUpdate(sql);
                sql = "CREATE TABLE PROFESSOR_DEGREE(PROFESSOR_ID VARCHAR(13) REFERENCES PROFESSOR_MASTER(PROFESSOR_ID),DEGREE VARCHAR(10),PRIMARY KEY (PROFESSOR_ID,DEGREE))";
                sst.executeUpdate(sql);
                sql = "CREATE TABLE STUDENT_GRADE(STUDENT_ID VARCHAR(15) REFERENCES STUDENT_MASTER(STUDENT_ID),SEMESTER CHAR(1),GRADE CHAR(1),PRIMARY KEY(STUDENT_ID,SEMESTER))";
                sst.executeUpdate(sql);
            }
            con.close();
        
            Toolkit tk = Toolkit.getDefaultToolkit();
            Image img = tk.getImage("p0.jpg");
            LoginFrame logFrame = new LoginFrame();
            logFrame.setIconImage(img);
            logFrame.setTitle("SIGN IN PANEL...");
            logFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            logFrame.setResizable(false);
            logFrame.setSize(500,270);
            logFrame.setLocationRelativeTo(null);
            logFrame.getContentPane().setBackground(new Color(250,250,200));
            logFrame.setLayout(new BorderLayout());
            logFrame.setUndecorated(true);
            logFrame.getRootPane().setWindowDecorationStyle(JRootPane.COLOR_CHOOSER_DIALOG);
            logFrame.setVisible(true);
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null,ex);
        }
    }
}
