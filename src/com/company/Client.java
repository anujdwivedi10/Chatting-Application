package com.company;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ScrollBarUI;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Client implements ActionListener
{
    JPanel p1;
    JTextField textField;
    JButton button;
    static JPanel textArea;
    static JFrame f = new JFrame();

    static Box vertical  = Box.createVerticalBox();       // to display messages in vertical order

    static Socket s;
    static DataOutputStream dout;
    static DataInputStream din;

    Boolean typing;

    Client()
    {
        f.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        p1 = new JPanel();
        p1.setLayout(null);
        p1.setBackground(new Color(7,94,84));
        p1.setBounds(0,0,450,70);
        f.add(p1);


        ImageIcon backArrow = new ImageIcon(ClassLoader.getSystemResource("com/company/images/arrow.png"));
        Image i1 = backArrow.getImage().getScaledInstance(30,30,Image.SCALE_DEFAULT);
        ImageIcon i2 = new ImageIcon(i1);
        JLabel l1 = new JLabel(i2);
        l1.setBounds(5,20,30,30);
        p1.add(l1);

        l1.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                System.exit(0);
            }
        });


        ImageIcon userImage = new ImageIcon(ClassLoader.getSystemResource("com/company/images/ronaldo.jpg"));
        Image i3 = userImage.getImage().getScaledInstance(50,50,Image.SCALE_DEFAULT);
        ImageIcon i4 = new ImageIcon(i3);
        JLabel l2 = new JLabel(i4);
        l2.setBounds(45,8,50,50);
        p1.add(l2);


        JLabel name = new JLabel("Ronaldo");
        name.setBounds(110,16,100,20);
        name.setFont(new Font("SAN_SERIF",Font.ITALIC,20));
        name.setForeground(Color.white);
        p1.add(name);


        JLabel status = new JLabel("Active Now");
        status.setBounds(110,32,100,20);
        status.setFont(new Font("serif",Font.ITALIC,15));
        status.setForeground(Color.white);
        p1.add(status);


        Timer t = new Timer(1, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                if(!typing)
                {
                    status.setText("Active Now");
                }
            }
        });
        t.setInitialDelay(2000);


        ImageIcon video = new ImageIcon(ClassLoader.getSystemResource("com/company/images/video.png"));
        Image i5 = video.getImage().getScaledInstance(35,35,Image.SCALE_DEFAULT);
        ImageIcon i6 = new ImageIcon(i5);
        JLabel l3 = new JLabel(i6);
        l3.setBounds(290,18,35,30);
        p1.add(l3);


        ImageIcon phone = new ImageIcon(ClassLoader.getSystemResource("com/company/images/phone.png"));
        Image i7 = phone.getImage().getScaledInstance(35,35,Image.SCALE_DEFAULT);
        ImageIcon i8 = new ImageIcon(i7);
        JLabel l4 = new JLabel(i8);
        l4.setBounds(350,18,35,30);
        p1.add(l4);


        ImageIcon threeIcon = new ImageIcon(ClassLoader.getSystemResource("com/company/images/3icon.png"));
        Image i9 = threeIcon.getImage().getScaledInstance(12,25,Image.SCALE_DEFAULT);
        ImageIcon i10 = new ImageIcon(i9);
        JLabel l5 = new JLabel(i10);
        l5.setBounds(410,20,12,25);
        p1.add(l5);


        textField = new JTextField();
        textField.setBounds(5,650,300,40);
        textField.setFont(new Font("SAN_SERIF",Font.PLAIN,15));
        f.add(textField);
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                status.setText("typing...");
                t.stop();
                typing=true;
            }

            @Override
            public void keyReleased(KeyEvent e) {
                typing = false;

                if(!t.isRunning())
                {
                    t.start();
                }
            }
        });


        button = new JButton("Send");
        button.setBounds(320,650,120,40);
        button.setBackground(new Color(7,94,84));
        button.setFont(new Font("SEN_SERIF",Font.PLAIN,15));
        button.setForeground(Color.white);
        button.addActionListener(this);
        f.add(button);


        textArea = new JPanel();
        textArea.setBounds(5,75,440,570);
//        textArea.setBackground(Color.lightGray);
        textArea.setFont(new Font("SEN_SERIF",Font.PLAIN,15));
        f.add(textArea);

        JScrollPane sp = new JScrollPane(textArea); // adding scrollbar to chat messages
        sp.setBounds(5,75,440,570);
        sp.setBorder(BorderFactory.createEmptyBorder());     // empty border because is scrollpane gives default border



        ScrollBarUI ui = new BasicScrollBarUI()
        {
            protected JButton createDecreaseButton(int orientation)
            {
                JButton dbutton = super.createDecreaseButton(orientation);
                dbutton.setBackground(new Color(7,94,84));
                dbutton.setForeground(Color.white);
                this.thumbColor = new Color(7,94,84);
                return dbutton;
            }

            protected JButton createIncreaseButton(int orientation)
            {
                JButton ibutton = super.createIncreaseButton(orientation);
                ibutton.setBackground(new Color(7,94,84));
                ibutton.setForeground(Color.white);
                return ibutton;
            }

        };

        sp.getVerticalScrollBar().setUI(ui);
        f.add(sp);      // adding scrollpane to frame



        f.setLayout(null);  // beacause setbounds is used to create our own layout
        f.setSize(450,700);
        f.setLocation(900,200);
        f.setUndecorated(true);
        f.setVisible(true);

        f.getContentPane().setBackground(Color.white);

    }
//    @Override

    public void actionPerformed(ActionEvent ae)
    {
        try
        {
            String out = textField.getText();

            sendTextToFile(out);

            textField.setText("");

            JPanel show = formatLabel(out);          // calling formatLalbel method of JPanel type

            textArea.setLayout(new BorderLayout());     // making layout of dislayed text as border       and default layout is flowlayout

            JPanel right = new JPanel(new BorderLayout());

            right.add(show,BorderLayout.LINE_END);             // to display sent message to the right side of screen(LINE_END is used)
            vertical.add(right);
            vertical.add(Box.createVerticalStrut(15));       // height of box

            textArea.add(vertical,BorderLayout.PAGE_START);       // to start showing message from botton


            dout.writeUTF(out);

        }

        catch (Exception e)
        {

        }
    }

    public static void sendTextToFile(String message) throws FileNotFoundException
    {
        // saving data in a external file
        // so exception handling is used
        try(  FileWriter fw = new FileWriter("chat.txt",true);       // name of text file in which message will be saved. This file will be automatically created.
              PrintWriter pw = new PrintWriter(new BufferedWriter(fw));)     //  to write inside a file
        {

            pw.println("Cristiano Ronaldo: " + message);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static  JPanel formatLabel(String out)
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));

        JLabel label = new JLabel("<html><p style= \"width : 150px\">"+ out +"</p></html>");      // using this so that text is displayd right below the starting point of previous line
        label.setBackground(new Color(37,211,102));
        label.setOpaque(true);                //     it will fill the label with color
        label.setFont(new Font("Tahoma",Font.PLAIN,16));
        label.setBorder(new EmptyBorder(15,15,15,50));

        Calendar cal = Calendar.getInstance();                    // this will give calender
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");     // format of time to be dispalyed

        JLabel time = new JLabel();       // creating label to show time  it will be displayed below the mssage
        time.setText(sdf.format(cal.getTime()));   // taking current time from calender and adding to the label

        panel.add(label);
        panel.add(time);

        return panel;          // returning the formatted panel
    }

    public static void main(String[] args)
    {
        new Client().f.setVisible(true);

        try
        {
            s = new Socket("127.0.0.1",5000);

            din = new DataInputStream(s.getInputStream());

            dout = new DataOutputStream(s.getOutputStream());

            String msginput = "";

            while (true)
            {
                msginput = din.readUTF();      // taking received dat
                JPanel jp = formatLabel(msginput);    // formatting it

                JPanel left = new JPanel(new BorderLayout());      // displaying it to the left side
                left.add(jp, BorderLayout.LINE_START);
                vertical.add(left);

                textArea.setLayout(new BorderLayout());
                vertical.add(Box.createVerticalStrut(15));
                textArea.add(vertical, BorderLayout.PAGE_START);


                f.validate();             // it will check all field by itself now
            }
        }

        catch(Exception e)
        {

        }
    }

}
