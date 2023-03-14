package applicationpack;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
public class ProfessorAddFrame extends JDialog
{
    private JTextField  txtID,txtName,txtAddress,txtPhone,txtEmail,txtDOB,txtDOJ,txtDegree;
    private JComboBox   cmbGender;
    private JButton     btnAddNew,btnUpdate,btnCancel,btnReturn;
    private String[]    gender = {"Male","Female","Trans","Other"};
    private int         pIDSequence = 0;
    private Connection  con = null;
    private PreparedStatement pst1 = null,pst2 = null;
    private JLabel makeLabel(String cap,int x,int y,int w,int h)
    {
        JLabel temp = new JLabel(cap);
        temp.setFont(new Font("Courier New", 1, 18));
        temp.setForeground(Color.BLACK);
        temp.setBounds(x,y,w,h);
        super.add(temp);
        return temp;
    }
    private JTextField makeTextField(int x,int y,int w,int h)
    {
        Border brdr = BorderFactory.createLineBorder(Color.BLACK, 1);
        JTextField temp = new JTextField();
        temp.setFont(new Font("Courier New", 1, 18));
        temp.setBounds(x,y,w,h);
        temp.setHorizontalAlignment(JLabel.CENTER);
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
        add(temp);
        return temp;
    }
    private JButton makeButton(String cap,int x,int y,int w,int h)
    {
        JButton temp = new JButton(cap);
        temp.setFont(new Font("Georgia", Font.BOLD, 14));
        temp.setBounds(x,y,w,h);
        temp.setMargin(new Insets(0,0,0,0));
        ActionListener act;
        act = new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                Object ob = e.getSource();
                if(ob == btnAddNew)
                {
                    setReset();
                    Date dt = new Date();
                    int y = dt.getYear()+1900;
                    int m = dt.getMonth()+1;
                    pIDSequence++;
                    String pid = String.format("P-%4d-%02d-%03d", y,m,pIDSequence);
                    txtID.setText(pid);
                    txtName.grabFocus();
                }
                else if(ob == btnUpdate)
                {
                    try
                    {
                        String sql1 = "INSERT INTO PROFESSOR_MASTER VALUES(?,?,?,?,?,?,?,?)";
                        String sql2 = "INSERT INTO PROFESSOR_DEGREE VALUES(?,?)";
                        pst1 = con.prepareStatement(sql1);
                        pst2 = con.prepareStatement(sql2);
                        pst1.setString(1, txtID.getText());
                        pst1.setString(2, txtName.getText());
                        pst1.setString(3, txtAddress.getText());
                        pst1.setString(4, (String)cmbGender.getSelectedItem());
                        pst1.setString(5, txtPhone.getText());
                        pst1.setString(6, txtEmail.getText());
                        pst1.setString(7, txtDOB.getText());
                        pst1.setString(8, txtDOJ.getText());
                        pst1.executeUpdate();
                        String degree[] = txtDegree.getText().split(",");
                        for(String temp:degree)
                        {
                            pst2.setString(1, txtID.getText());
                            pst2.setString(2, temp);
                            pst2.executeUpdate();
                        }
                        setReset();
                        btnAddNew.grabFocus();
                    }
                    catch(Exception ex)
                    {
                        JOptionPane.showMessageDialog(null, ex);
                    }
                }
                else if(ob == btnCancel)
                {
                    setReset();
                    btnAddNew.grabFocus();
                    pIDSequence--;
                }
                else if(ob == btnReturn)
                {
                    dispose();
                }
            }
        };
        temp.addActionListener(act);
        super.add(temp);
        return temp;
    }
    private void setReset()
    {
        txtID.setText("");
        txtName.setEnabled(!txtName.isEnabled());
        txtName.setText("");
        txtAddress.setEnabled(!txtAddress.isEnabled());
        txtAddress.setText("");
        cmbGender.setEnabled(!cmbGender.isEnabled());
        cmbGender.setSelectedIndex(0);
        txtPhone.setEnabled(!txtPhone.isEnabled());
        txtPhone.setText("");
        txtEmail.setEnabled(!txtEmail.isEnabled());
        txtEmail.setText("");
        txtDOB.setEnabled(!txtDOB.isEnabled());
        txtDOB.setText("");
        txtDOJ.setEnabled(!txtDOJ.isEnabled());
        txtDOJ.setText("");
        txtDegree.setText("");
        btnAddNew.setEnabled(!btnAddNew.isEnabled());
        btnUpdate.setEnabled(!btnUpdate.isEnabled());
        btnCancel.setEnabled(!btnCancel.isEnabled());
        btnReturn.setEnabled(!btnReturn.isEnabled());
    }
    public ProfessorAddFrame()
    {
        try
        {
            Border brdr1 = BorderFactory.createLineBorder(Color.RED, 2);
            Border brdr2 = BorderFactory.createLineBorder(Color.BLUE, 2);
            Border brdr3 = BorderFactory.createCompoundBorder(brdr1, brdr2);
            JLabel caption = new JLabel("NEW PROFESSOR REGISTRATION");
            caption.setFont(new Font("verdana",1,22));
            caption.setHorizontalAlignment(JLabel.CENTER);
            caption.setOpaque(true);
            caption.setBackground(Color.YELLOW);
            caption.setForeground(Color.red);
            caption.setBorder(brdr3);
            caption.setBounds(10,10,500,50);
            super.add(caption);

            makeLabel("PROFESSOR ID GENERATED",  10, 70,250,30);
            txtID       = makeTextField(        260, 70,250,30);
            txtID.setEditable(false);
            makeLabel("ENTER PROFESSOR NAME",    10,110,250,30);
            txtName     = makeTextField(        260,110,250,30);
            makeLabel("ENTER LOCAL ADDRESS",     10,150,250,30);
            txtAddress  = makeTextField(        260,150,250,30);
            makeLabel("SELECT GENDER STATUS",    10,190,250,30);
            cmbGender   = makeComboBox(gender,  260,190,250,30);
            makeLabel("ENTER PHONE NUMBER",      10,230,250,30);
            txtPhone    = makeTextField(        260,230,250,30);
            makeLabel("ENTER EMAIL ADDRESS",     10,270,250,30);
            txtEmail    = makeTextField(        260,270,250,30);
            makeLabel("ENTER DATE OF BIRTH",     10,310,250,30);
            txtDOB      = makeTextField(        260,310,250,30);
            makeLabel("ENTER DATE OF JOIN",      10,350,250,30);
            txtDOJ      = makeTextField(        260,350,250,30);
            makeLabel("DEGREE COMMA SEPARATED",   10,390,250,30);
            txtDegree   = makeTextField(        260,390,250,30);
            txtDegree.setToolTipText("Comma Separated Value for Multiple Degree (Eg.BSc,BTech,MTech)");
            btnAddNew   = makeButton("Add New",  26,430,100,30);
            btnAddNew.setEnabled(false);
            btnUpdate   = makeButton("Update",  152,430,100,30);
            btnCancel   = makeButton("Cancel",  278,430,100,30);
            btnReturn   = makeButton("Return",  404,430,100,30);
            btnReturn.setEnabled(false);
            setReset();

            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ankan?autoReconnect=true&useSSL=false","root","ankan");
            Statement sst = con.createStatement();
            ResultSet rst = sst.executeQuery("SELECT PROFESSOR_ID FROM PROFESSOR_MASTER ORDER BY PROFESSOR_ID DESC LIMIT 1");
            if(rst.next())
            {
                String sid = rst.getString(1);
                pIDSequence = Integer.parseInt(sid.substring(sid.lastIndexOf("-")+1));
            }
            else
            {
                pIDSequence = 0;
            }
            sst.close();
        }
        catch(ClassNotFoundException | SQLException | NumberFormatException ex)
        {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
}
