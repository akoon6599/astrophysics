package display;

import physics.StellarBody;
import physics.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import static javax.swing.GroupLayout.Alignment.*;

public class Start extends JFrame {
    protected static final int PREF_X = 1080;
    protected static final int PREF_Y = 720;
    GroupLayout layout;
    @Override
    public Dimension getPreferredSize() {
        if (isPreferredSizeSet()) {return super.getPreferredSize();}
        return new Dimension(PREF_X, PREF_Y);
    }
    ArrayList<StellarBody> Bodies = new ArrayList<>();
    ArrayList<JComponent> components = new ArrayList<>();
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
        components.add(addBBody);
        addBBody.addActionListener(e -> new AddBody().setVisible(true));
        ArrayList<JButton> bodyButtons = new ArrayList<>();
        ArrayList<Circle> bodyDisplays = new ArrayList<>();
        ArrayList<JLabel> bodyLabels = new ArrayList<>();

        for (StellarBody body : Bodies) {
            bodyButtons.add(new JButton(body.Title));
            bodyDisplays.add(new Circle(0, 0,
                                        body.Radius,body.Radius));
            bodyLabels.add(new JLabel(
                    String.format("Angle: %.2f -- Magnitude: %.3f -- Classification: %s",
                            body.Movement.coefficient(), body.Movement.getMagnitude(), body.Classification)));
        }
        for (JButton btn : (ArrayList<JButton>)bodyButtons.clone()) { // maybe change the buttons to an edit/remove dropdown action?
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Start.this.Bodies.remove(bodyButtons.indexOf(btn));
                    bodyButtons.remove(btn);
                    Start.this.refresh();
                }
            });
        }


        GroupLayout.SequentialGroup bodyField = layout.createSequentialGroup() // HORIZONTAL
                .addGap(6, 6, 6);
        GroupLayout.ParallelGroup tmpBButtons = layout.createParallelGroup(LEADING);
        for (JButton c : bodyButtons) {
            tmpBButtons.addComponent(c);
            components.add(c);}
        GroupLayout.ParallelGroup tmpBBodies = layout.createParallelGroup(LEADING);
        for (Circle c : bodyDisplays) {
            tmpBBodies.addComponent(c);
            components.add(c);}
        GroupLayout.ParallelGroup tmpBLabels = layout.createParallelGroup(LEADING);
        for (JLabel c : bodyLabels) {
            tmpBLabels.addComponent(c);
            components.add(c);}
        JButton Simulate = new JButton("Start Simulation");
        Simulate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
//                try {
//                    system.start_simulation(Start.this.Bodies, 10, Start.this);
//                } catch (InterruptedException ex) {
//                    throw new RuntimeException(ex);
//                }
            }
        });
        bodyField.addGroup(tmpBButtons).addGroup(tmpBBodies).addGroup(tmpBLabels);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(addBBody)
                                        .addGroup(bodyField))
                                .addGap(0,PREF_X-100,PREF_X-50)
                                .addComponent(Simulate)
        ));

        GroupLayout.SequentialGroup pBodyField = layout.createSequentialGroup() // VERTICAL
                .addContainerGap()
                .addComponent(addBBody)
                .addGap(30,30,30);
        ArrayList<GroupLayout.ParallelGroup> help = new ArrayList<>();
        for (JButton c: bodyButtons) {
            help.add(layout.createParallelGroup(BASELINE)
                    .addComponent(c)
                    .addComponent(bodyDisplays.get(bodyButtons.indexOf(c)))
                    .addComponent(bodyLabels.get(bodyButtons.indexOf(c)))
                    .addGap(5,10,25));
        }
        help.forEach(pBodyField::addGroup);
        pBodyField.addContainerGap((int)(PREF_Y*0.2), Short.MAX_VALUE);
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pBodyField))
                .addComponent(Simulate));
    }

    public void refresh() {
        components.forEach(this::remove);
        this.mainMenu();
        EventQueue.invokeLater(this::repaint);
        System.out.println("a");
    }
    class AddBody extends JFrame {
        protected static final int PREF_X = 720;
        protected static final int PREF_Y = 400;
        GroupLayout layout;
        @Override
        public Dimension getPreferredSize() {
            if (isPreferredSizeSet()) {return super.getPreferredSize();}
            return new Dimension(PREF_X, PREF_Y);
        }

        // determine interactables - here for use in finalize_body
        JButton rtn = new JButton("Return to Menu"); // TODO: done
        JTextField angle = new JTextField(6);
        JTextField magnitude = new JTextField(6);
        JButton pos = new JButton("CLICK"); // TODO: do
        JTextField title = new JTextField(20);
        JTextField classif = new JTextField(20);
        JTextField mass = new JTextField(8);
        JTextField radius = new JTextField(6);
        JTextField color = new JTextField(6);
        JToggleButton anchor = new JToggleButton("False");
        JButton preview = new JButton("Preview Movement"); // TODO: do
        JButton finalize = new JButton("Finalize"); // TODO: do


        public AddBody() {
            layout = new GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setAutoCreateGaps(true);
            layout.setAutoCreateContainerGaps(true);
            rtn.addActionListener(e -> AddBody.this.dispose());
            // determine labels
            JLabel angleText = new JLabel("Angle: ");
            JLabel magText = new JLabel("Magnitude: ");
            JLabel posText = new JLabel("Position: ");
            JLabel titleText = new JLabel("Title: ");
            JLabel classText = new JLabel("Classification: ");
            JLabel physLabel = new JLabel("- PHYSICS -");
            JLabel cosLabel = new JLabel("- COSMETIC - ");
            JLabel massText = new JLabel("Mass: ");
            JLabel radText = new JLabel("Radius: ");
            JLabel colText = new JLabel("Icon Color: ");
            JLabel ancText = new JLabel("Anchor: ");

            // TODO: add button interaction logic
            finalize.addActionListener(new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) {
                    AddBody.this.finalize_body();
                    dispose();
                }
            });



            layout.setHorizontalGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(LEADING)
                            .addComponent(rtn)
                            .addComponent(angleText)
                            .addComponent(magText)
                            .addComponent(posText)
                            .addComponent(titleText)
                            .addComponent(classText))
                    .addGap(15,15,15)
                    .addGroup(layout.createParallelGroup(LEADING)
                            .addComponent(angle)
                            .addComponent(magnitude)
                            .addComponent(pos)
                            .addComponent(title)
                            .addComponent(classif))
                    .addGap(10,10,10)
                    .addGroup(layout.createParallelGroup(LEADING)
                            .addComponent(physLabel)
                            .addComponent(cosLabel))
                    .addGroup(layout.createParallelGroup(LEADING)
                            .addComponent(massText)
                            .addComponent(radText)
                            .addComponent(colText)
                            .addComponent(ancText))
                    .addGap(15,15,15)
                    .addGroup(layout.createParallelGroup(LEADING)
                            .addComponent(mass)
                            .addComponent(radius)
                            .addComponent(color)
                            .addComponent(anchor))
                    .addGroup(layout.createParallelGroup(TRAILING)
                            .addComponent(preview)
                            .addComponent(finalize))
            );
            layout.setVerticalGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(BASELINE)
                            .addComponent(rtn)
                            .addComponent(preview))
                    .addGap(30,30,30)
                    .addComponent(physLabel)
                    .addGap(10,10,10)
                    .addGroup(layout.createParallelGroup(BASELINE)
                            .addComponent(angleText)
                            .addComponent(angle)
                            .addComponent(massText)
                            .addComponent(mass))
                    .addGap(10,10,10)
                    .addGroup(layout.createParallelGroup(BASELINE)
                            .addComponent(magText)
                            .addComponent(magnitude)
                            .addComponent(radText)
                            .addComponent(radius))
                    .addGap(10,10,10)
                    .addGroup(layout.createParallelGroup(BASELINE)
                            .addComponent(posText)
                            .addComponent(pos))
                    .addGap(25,25,25)
                    .addComponent(cosLabel)
                    .addGap(10,10,10)
                    .addGroup(layout.createParallelGroup(BASELINE)
                            .addComponent(titleText)
                            .addComponent(title)
                            .addComponent(colText)
                            .addComponent(color))
                    .addGap(10,10,10)
                    .addGroup(layout.createParallelGroup(BASELINE)
                            .addComponent(classText)
                            .addComponent(classif)
                            .addComponent(ancText)
                            .addComponent(anchor))
                    .addGap(25,25,25)
                    .addComponent(finalize));

            pack();
            setLocationRelativeTo(null);
        }
        private void finalize_body() { // TODO: do the position sutff here too
            Start.this.Bodies.add(new StellarBody(-400f,-400f, title.getText(),classif.getText(),
                    Double.parseDouble(mass.getText()),Integer.parseInt(radius.getText()),angle.getText(),
                    Double.parseDouble(magnitude.getText()),getColorByName(color.getText())));
            Start.this.refresh();
        }
        public static Color getColorByName(String name) { // Credit: shmosel @ stackoverflow
            try {
                return (Color)Color.class.getField(name.toUpperCase()).get(null);
            } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
                e.printStackTrace();
                return null;
            }
        }
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
