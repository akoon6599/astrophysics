package display;

import physics.StellarBody;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.Alignment.LEADING;

public class Start extends JFrame {
    protected static final int PREF_X = 1440;
    protected static final int PREF_Y = 1080;
    GroupLayout layout;
    @Override
    public Dimension getPreferredSize() {
        if (isPreferredSizeSet()) {return super.getPreferredSize();}
        return new Dimension(PREF_X, PREF_Y);
    }
    ArrayList<StellarBody> Bodies = new ArrayList<>();
    public Start(ArrayList<StellarBody> Bodies) {
        this.Bodies = Bodies;
        layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        mainMenu();
    }
    private void mainMenu() {
        JButton addBBody = new JButton("Add Body");
        addBBody.setFont(new Font("Times New Roman", Font.BOLD, 24));
        addBBody.addActionListener(e -> new AddBody().setVisible(true));
        ArrayList<JButton> bodyButtons = new ArrayList<>();
        ArrayList<Circle> bodyDisplays = new ArrayList<>();
        ArrayList<JLabel> bodyLabels = new ArrayList<>();

        for (StellarBody body : Bodies) {
            bodyButtons.add(new JButton(body.Title));
            bodyDisplays.add(new Circle(0, 0,
                                        body.Radius/2,body.Radius/2));
            bodyLabels.add(new JLabel(
                    String.format("Angle: %.2f -- Magnitude: %.3f -- Classification: %s",
                            body.Movement.coefficient(), body.Movement.getMagnitude(), body.Classification)));
        }


        GroupLayout.SequentialGroup bodyField = layout.createSequentialGroup()
                .addGap(6, 6, 6);
        GroupLayout.ParallelGroup tmpBButtons = layout.createParallelGroup(LEADING);
        for (JButton c : bodyButtons) {tmpBButtons.addComponent(c);}
        GroupLayout.ParallelGroup tmpBBodies = layout.createParallelGroup(LEADING);
        for (Circle c : bodyDisplays) {tmpBBodies.addComponent(c);}
        GroupLayout.ParallelGroup tmpBLabels = layout.createParallelGroup(LEADING);
        for (JLabel c : bodyLabels) {tmpBLabels.addComponent(c);}
        bodyField.addGroup(tmpBButtons).addGroup(tmpBBodies).addGroup(tmpBLabels);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(addBBody)
                                        .addGroup(bodyField))
                                .addContainerGap(700, Short.MAX_VALUE))
        );

        GroupLayout.SequentialGroup pBodyField = layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(addBBody)
                .addGap(9,9,9);
        ArrayList<GroupLayout.ParallelGroup> help = new ArrayList<>();
        for (JButton c: bodyButtons) {
            help.add(layout.createParallelGroup(BASELINE)
                    .addComponent(c)
                    .addComponent(bodyDisplays.get(bodyButtons.indexOf(c)))
                    .addComponent(bodyLabels.get(bodyButtons.indexOf(c)))
                    .addGap(5,10,25));
        }
        help.forEach(pBodyField::addGroup);
        pBodyField.addContainerGap(550, Short.MAX_VALUE);
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pBodyField));
    }

    public void refresh() {
        EventQueue.invokeLater(this::repaint);
        System.out.println("a");
    }
}
class AddBody extends JFrame {
    protected static final int PREF_X = 720;
    protected static final int PREF_Y = 540;
    GroupLayout layout;
    @Override
    public Dimension getPreferredSize() {
        if (isPreferredSizeSet()) {return super.getPreferredSize();}
        return new Dimension(PREF_X, PREF_Y);
    }
    public AddBody() {
        layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        JButton rtn = new JButton("Return to Menu");
        rtn.addActionListener(e -> AddBody.this.dispose());
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addComponent(rtn));
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(rtn));


        pack();
        setLocationRelativeTo(null);
    }
}

class Circle extends JComponent{

    private int height;
    private int width;
    private int xPos;
    private int yPos;

    public Circle(int x,int y,int height,int width) {
        this.height = height;
        this.width = width;
        xPos = x;
        yPos = y;
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        Ellipse2D.Double ellipse = new Ellipse2D.Double(xPos,yPos,width,height);
        g2.draw(ellipse);
        g2.setColor(Color.black);
        g2.fill(ellipse);
    }
}
