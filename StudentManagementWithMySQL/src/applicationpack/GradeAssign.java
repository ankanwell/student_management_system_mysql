package applicationpack;
import java.awt.Color;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
public class GradeAssign extends JDialog
{
    private JLabel      lblName,lblCourse;
    private JTextField  txtID;
    private JComboBox   cmbSemester,cmbGrade;
    private JButton     btnSearch,btnSubmit,btnReset,btnReturn;
    private String[]    sem = {"Select Sem","1","2","3","4","5","6","7","8"};
    private String[]    grade = {"Select Grade","O","E","A","B","C","D","F"};
    private Connection  con = null;
    private PreparedStatement pst1 = null, pst2 = null, pst3 = null, pst4 = null;
    
    
    private JLabel makeLabel(String cap,int x,int y,int w,int h,int mode)
    {
        JLabel temp = new JLabel(cap);
        temp.setFont(new Font("Courier New", 1, 18));
        temp.setForeground(Color.BLACK);
        temp.setBounds(x,y,w,h);
        if(mode == 2)
        {
            temp.setOpaque(true);
            temp.setBackground(Color.WHITE);
            Border brdr = BorderFactory.createLineBorder(Color.BLACK, 1);
            temp.setBorder(brdr);
            temp.setHorizontalAlignment(JLabel.CENTER);
        }
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
                try
                {
                    Object ob = e.getSource();
                    if(ob == btnSearch)
                    {
                        ResultSet rst = null;
                        int mode = Integer.parseInt(System.getProperty("gradeassign"));
                        if(mode == 1)
                        {
                            pst1.setString(1, txtID.getText());
                            rst = pst1.executeQuery();
                        }
                        else if(mode == 2)
                        {
                            pst2.setString(1, txtID.getText());
                            pst2.setString(2, (String)cmbSemester.getSelectedItem());
                            rst = pst2.executeQuery();
                        }
                        if(rst.next())
                        {
                            lblName.setText(rst.getString(1));
                            lblCourse.setText(rst.getString(2));
                            if(mode == 2)
                            {
                                cmbSemester.setSelectedItem(rst.getString(3));
                                cmbGrade.setSelectedItem(rst.getString(4));
                            }
                            cmbSemester.setEnabled(true);
                            cmbGrade.setEnabled(true);
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(null, "NO SUCH STUDENT EXISTS");
                            reset();
                            txtID.grabFocus();
                        }
                    }
                    else if(ob == btnSubmit)
                    {
                        if(cmbSemester.getSelectedIndex() != 0 && cmbGrade.getSelectedIndex() != 0)
                        {
                            int mode = Integer.parseInt(System.getProperty("gradeassign"));
                            if(mode == 1)
                            {
                                pst3.setString(1,txtID.getText());
                                pst3.setString(2,(String)cmbSemester.getSelectedItem());
                                pst3.setString(3,(String)cmbGrade.getSelectedItem());
                                pst3.executeUpdate();
                            }
                            else if(mode == 2)
                            {
                                pst4.setString(1,(String)cmbGrade.getSelectedItem());
                                pst4.setString(2,txtID.getText());
                                pst4.setString(3,(String)cmbSemester.getSelectedItem());
                                pst4.executeUpdate();
                            }
                            reset();
                        }
                    }
                    else if(ob == btnReset)
                    {
                        reset();
                    }
                    else if(ob == btnReturn)
                    {
                        dispose();
                    }
                }
                catch(SQLException | HeadlessException ex)
                {
                    JOptionPane.showMessageDialog(null, ex);
                }
            }
        };
        temp.addActionListener(act);
        super.add(temp);
        return temp;
    }
    private void reset()
    {
        txtID.setText("");
        lblName.setText("");
        lblCourse.setText("");
        cmbSemester.setSelectedIndex(0);
        cmbGrade.setSelectedIndex(0);
        cmbSemester.setEnabled(false);
        cmbGrade.setEnabled(false);
    }
    public GradeAssign()
    {
        try
        {
            Border brdr1 = BorderFactory.createLineBorder(Color.RED, 2);
            Border brdr2 = BorderFactory.createLineBorder(Color.BLUE, 2);
            Border brdr3 = BorderFactory.createCompoundBorder(brdr1, brdr2);
            JLabel caption = new JLabel("ASSIGNING OF GRADE");
            caption.setFont(new Font("verdana",1,22));
            caption.setHorizontalAlignment(JLabel.CENTER);
            caption.setOpaque(true);
            caption.setBackground(Color.YELLOW);
            caption.setForeground(Color.red);
            caption.setBorder(brdr3);
            caption.setBounds(10,10,500,50);
            super.add(caption);

            makeLabel("ENTER STUDENT ID",      10, 70,200,30,1);
            txtID       = makeTextField(      210, 70,200,30);
            btnSearch   = makeButton("Search",420, 70, 90,30);
            makeLabel("NAME",                  10,110, 70,30,1);
            lblName     = makeLabel("",        80,110,200,30,2);
            makeLabel("COURSE",               290,110, 70,30,1);
            lblCourse   = makeLabel("",       370,110,140,30,2);
            makeLabel("SEMESTER",              10,150,100,30,1);
            cmbSemester = makeComboBox(sem,   110,150,150,30);
            int mode = Integer.parseInt(System.getProperty("gradeassign"));
            if(mode == 1)
                cmbSemester.setEnabled(false);
            else if(mode == 2)
                cmbSemester.setEnabled(true);
            makeLabel("GRADE",                270,150, 70,30,1);
            cmbGrade    = makeComboBox(grade, 340,150,170,30);
            cmbGrade.setEnabled(false);
            btnSubmit   = makeButton("Submit", 55,190,100,30);
            btnReset    = makeButton("Reset", 210,190,100,30);
            btnReturn   = makeButton("Return",365,190,100,30);
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ankan?autoReconnect=true&useSSL=false","root","ankan");
            pst1 = con.prepareStatement("SELECT NAME,COURSE FROM STUDENT_MASTER WHERE STUDENT_ID = ?");
            pst2 = con.prepareStatement("SELECT NAME,COURSE,SEMESTER,GRADE FROM STUDENT_MASTER AS SM,STUDENT_GRADE AS SG WHERE SM.STUDENT_ID = ? AND SG.SEMESTER = ? AND SM.STUDENT_ID = SG.STUDENT_ID");
            pst3 = con.prepareStatement("INSERT INTO STUDENT_GRADE VALUES(?,?,?)");
            pst4 = con.prepareStatement("UPDATE STUDENT_GRADE SET GRADE = ? WHERE STUDENT_ID = ? AND SEMESTER = ?");
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
}
