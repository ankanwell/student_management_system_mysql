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
public class StudentAddFrame extends JDialog
{
    private JTextField  txtID,txtName,txtFather,txtAddress,txtDOB,txtPhone,txtEmail;
    private JComboBox   cmbGender,cmbCourse,cmbSems;
    private JButton     btnAddNew,btnUpdate,btnCancel,btnReturn;
    private String[]    gender = {"Male","Female","Trans","Other"};
    private String[]    course = {"BTech","BCA","BSc","BBA","MTech","MCA","MSc","MBA"};
    private String[]    sems   = {"3","4","5","6","7","8"};
    private int         sIDSequence = 0;
    private Connection  con = null;
    private PreparedStatement pst = null;
    
    private JLabel makeLabel(String cap,int x,int y,int w,int h)
    {
        JLabel temp = new JLabel(cap);
        temp.setFont(new Font("Courier New", 1, 18));
        temp.setForeground(Color.BLUE);
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
        temp.setFont(new Font("verdana", Font.BOLD, 12));
        temp.setBounds(x,y,w,h);
        temp.setMargin(new Insets(0,0,0,0));
        ActionListener act = new ActionListener()
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
                    sIDSequence++;
                    String sid = String.format("S-%4d-%02d-%05d", y,m,sIDSequence);
                    txtID.setText(sid);
                    txtName.grabFocus();
                }
                else if(ob == btnUpdate)
                {
                    try
                    {
                        String sql = "INSERT INTO STUDENT_MASTER VALUES(?,?,?,?,?,?,?,?,?,?)";
                        pst = con.prepareStatement(sql);
                        pst.setString(1, txtID.getText());
                        pst.setString(2, txtName.getText());
                        pst.setString(3, txtFather.getText());
                        pst.setString(4, (String)cmbGender.getSelectedItem());
                        pst.setString(5, txtAddress.getText());
                        pst.setString(6, txtDOB.getText());
                        pst.setString(7, txtPhone.getText());
                        pst.setString(8, txtEmail.getText());
                        pst.setString(9, (String)cmbCourse.getSelectedItem());
                        pst.setString(10, (String)cmbSems.getSelectedItem());
                        pst.executeUpdate();
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
                    sIDSequence--;
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
        txtFather.setEnabled(!txtFather.isEnabled());
        txtFather.setText("");
        cmbGender.setEnabled(!cmbGender.isEnabled());
        cmbGender.setSelectedIndex(0);
        txtAddress.setEnabled(!txtAddress.isEnabled());
        txtAddress.setText("");
        txtDOB.setEnabled(!txtDOB.isEnabled());
        txtDOB.setText("");
        txtPhone.setEnabled(!txtPhone.isEnabled());
        txtPhone.setText("");
        txtEmail.setEnabled(!txtEmail.isEnabled());
        txtEmail.setText("");
        cmbCourse.setEnabled(!cmbCourse.isEnabled());
        cmbCourse.setSelectedIndex(0);
        cmbSems.setEnabled(!cmbSems.isEnabled());
        cmbSems.setSelectedIndex(5);
        btnAddNew.setEnabled(!btnAddNew.isEnabled());
        btnUpdate.setEnabled(!btnUpdate.isEnabled());
        btnCancel.setEnabled(!btnCancel.isEnabled());
        btnReturn.setEnabled(!btnReturn.isEnabled());
    }
    public StudentAddFrame()
    {
        try
        {
            Border brdr1 = BorderFactory.createLineBorder(Color.RED, 2);
            Border brdr2 = BorderFactory.createLineBorder(Color.BLUE, 2);
            Border brdr3 = BorderFactory.createCompoundBorder(brdr1, brdr2);
            JLabel caption = new JLabel("NEW STUDENT REGISTRATION");
            caption.setFont(new Font("verdana",1,22));
            caption.setHorizontalAlignment(JLabel.CENTER);
            caption.setOpaque(true);
            caption.setBackground(Color.YELLOW);
            caption.setForeground(Color.red);
            caption.setBorder(brdr3);
            caption.setBounds(10,10,500,50);
            super.add(caption);

            makeLabel("STUDENT ID GENERATED",    10, 70,250,30);
            txtID       = makeTextField(        260, 70,250,30);
            txtID.setEditable(false);
            makeLabel("ENTER STUDENT NAME",      10,110,250,30);
            txtName     = makeTextField(        260,110,250,30);
            makeLabel("ENTER FATHER'S NAME",     10,150,250,30);
            txtFather   = makeTextField(        260,150,250,30);
            makeLabel("SELECT GENDER STATUS",    10,190,250,30);
            cmbGender   = makeComboBox(gender,  260,190,250,30);
            makeLabel("ENTER LOCAL ADDRESS",     10,230,250,30);
            txtAddress  = makeTextField(        260,230,250,30);
            makeLabel("ENTER DATE OF BIRTH",     10,270,250,30);
            txtDOB      = makeTextField(        260,270,250,30);
            makeLabel("ENTER PHONE NUMBER",      10,310,250,30);
            txtPhone    = makeTextField(        260,310,250,30);
            makeLabel("ENTER EMAIL ADDRESS",     10,350,250,30);
            txtEmail    = makeTextField(        260,350,250,30);
            makeLabel("SELECT COURSE ENROLED",   10,390,250,30);
            cmbCourse   = makeComboBox(course,  260,390,250,30);
            makeLabel("NUMBER OF SEMESTERS",     10,430,250,30);
            cmbSems     = makeComboBox(sems,    260,430,250,30);
            btnAddNew   = makeButton("Add New",  26,470,100,30);
            btnAddNew.setEnabled(false);
            btnUpdate   = makeButton("Update",  152,470,100,30);
            btnCancel   = makeButton("Cancel",  278,470,100,30);
            btnReturn   = makeButton("Return",  404,470,100,30);
            btnReturn.setEnabled(false);
            setReset();
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ankan?autoReconnect=true&useSSL=false","root","ankan");
            Statement sst = con.createStatement();
            ResultSet rst = sst.executeQuery("SELECT STUDENT_ID FROM STUDENT_MASTER ORDER BY STUDENT_ID DESC LIMIT 1");
            if(rst.next())
            {
                String sid = rst.getString(1);
                sIDSequence = Integer.parseInt(sid.substring(sid.lastIndexOf("-")+1));
            }
            else
            {
                sIDSequence = 0;
            }
            sst.close();
        }
        catch(ClassNotFoundException | SQLException | NumberFormatException ex)
        {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
}
