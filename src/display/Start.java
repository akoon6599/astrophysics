package display;

import physics.StellarBody;
import physics.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
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
    ArrayList<StellarBody> Bodies;
    ArrayList<StellarBody> initialBodies = new ArrayList<>();
    ArrayList<JComponent> components = new ArrayList<>();
    public Start(ArrayList<StellarBody> Bodies) {
        this.Bodies = Bodies;
        for (StellarBody b : Bodies) {
            initialBodies.add(b.clone());
        }
        layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        mainMenu();
    }
    public void reset() {
        Bodies.clear();
//        Bodies = initialBodies;
        for (StellarBody b : initialBodies) {
            Bodies.add(b.clone());
        }
    }
    private void mainMenu() {
        this.setTitle("Main Menu");
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
                    String.format("Angle: %.2f -- Magnitude: %.3f -- Classification: %s -- Mass: %.0fkg -- Pos (X,Y): (%.0f, %.0f) -- Anchor: %s",
                            body.Movement.coefficient(), body.Movement.getMagnitude(), body.Classification, body.Mass, body.Position.get(0), body.Position.get(1), body.STATIC)));
        }
        for (JButton btn : (ArrayList<JButton>)bodyButtons.clone()) { // TODO: maybe change the buttons to an edit/remove dropdown action?
            btn.addActionListener(e -> {
                Start.this.Bodies.remove(bodyButtons.indexOf(btn));
                Start.this.initialBodies.remove(bodyButtons.indexOf(btn));
                bodyButtons.remove(btn);
                Start.this.refresh();
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
        Simulate.addActionListener(e -> {
            Start.this.setVisible(false);
            try {
                system.start_simulation(Start.this.Bodies, system.CYCLES);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });
        bodyField.addGroup(tmpBButtons).addGroup(tmpBBodies).addGroup(tmpBLabels); // HORIZONTAL
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
        JButton rtn = new JButton("Return to Menu");
        JTextField angle = new JTextField(6);
        JTextField magnitude = new JTextField(6);
        JTextField posx = new JTextField("X POS", 5);
        JTextField posy = new JTextField("Y POS",5);
        JTextField title = new JTextField(20);
        JTextField classif = new JTextField(20);
        JTextField mass = new JTextField(8);
        JTextField radius = new JTextField(6);
        JTextField color = new JTextField(6);
        JCheckBox anchor = new JCheckBox("False");
        JButton preview = new JButton("Preview Movement"); // TODO: do
        JButton finalize = new JButton("Finalize");


        public AddBody() {
            layout = new GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setAutoCreateGaps(true);
            layout.setAutoCreateContainerGaps(true);
            rtn.addActionListener(e -> AddBody.this.dispose());
            this.setTitle("Planet Action Menu");
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
            JLabel errorText = new JLabel("");

            // TODO: add button interaction logic -- what does this mean???
            finalize.addActionListener(e -> {
                try {
                    StellarBody nB = AddBody.this.finalize_body();
                    Start.this.Bodies.add(nB);
                    Start.this.initialBodies.add(nB);
                    Start.this.refresh();
                    dispose();
                }
                catch (NumberFormatException exception) {
                    errorText.setText(String.format("Erorr: %s",exception.getMessage()));
                }
            });
            preview.addActionListener(e -> {
                try {
                    ArrayList<StellarBody> displayBodies = new ArrayList<>();
                    for (StellarBody b : Start.this.Bodies) {
                        displayBodies.add(b.clone());
                    }
                    StellarBody nB = AddBody.this.finalize_body();
                    displayBodies.add(nB);
                    Global GLOBAL = new Global(displayBodies);
                    system.display(GLOBAL);
                    GLOBAL.previewMovement(nB);
                } catch (NumberFormatException exception) {
                    errorText.setText(String.format("Erorr: %s",exception.getMessage()));
//                } catch (InterruptedException ex) {
//                    throw new RuntimeException(ex);
                }
            });
            posx.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    JTextField source = (JTextField)e.getComponent();
                    source.setText("");
                    source.removeFocusListener(this);
                }
            });
            posy.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    JTextField source = (JTextField)e.getComponent();
                    source.setText("");
                    source.removeFocusListener(this);
                }
            });
            anchor.addActionListener(e -> {
                JCheckBox source = (JCheckBox) e.getSource();
                if (source.isSelected()) {
                    source.setText("True ");
                }
                else {
                    source.setText("False");
                }
            });

            layout.setHorizontalGroup(layout.createSequentialGroup() // HORIZONTAL
                    .addGroup(layout.createParallelGroup(LEADING)
                            .addComponent(rtn)
                            .addComponent(angleText)
                            .addComponent(magText)
                            .addComponent(posText)
                            .addComponent(titleText)
                            .addComponent(classText)
                            .addComponent(errorText))
                    .addGap(15, 15, 15)
                    .addGroup(layout.createParallelGroup(LEADING)
                            .addComponent(angle)
                            .addComponent(magnitude)
                            .addGroup(layout.createSequentialGroup()
                                    .addComponent(posx)
                                    .addGap(5, 5, 5)
                                    .addComponent(posy))
                            .addComponent(title)
                            .addComponent(classif))
                    .addGap(10, 10, 10)
                    .addGroup(layout.createParallelGroup(LEADING)
                            .addComponent(physLabel)
                            .addComponent(cosLabel))
                    .addGroup(layout.createParallelGroup(LEADING)
                            .addComponent(massText)
                            .addComponent(radText)
                            .addComponent(colText)
                            .addComponent(ancText))
                    .addGap(15, 15, 15)
                    .addGroup(layout.createParallelGroup(LEADING)
                            .addComponent(mass)
                            .addComponent(radius)
                            .addComponent(color)
                            .addComponent(anchor))
                    .addGroup(layout.createParallelGroup(TRAILING)
                            .addComponent(preview)
                            .addComponent(finalize))
            );
            layout.setVerticalGroup(layout.createSequentialGroup() // VERTICAL
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
                            .addComponent(posx)
                            .addComponent(posy))
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
                            .addGap(0,0,5)
                            .addComponent(anchor))
                    .addGap(25,25,25)
                    .addComponent(errorText)
                    .addComponent(finalize));

            pack();
            setLocationRelativeTo(null);
        }
        private StellarBody finalize_body() {
            StellarBody nB = new StellarBody(Float.parseFloat(posx.getText()),Float.parseFloat(posy.getText()), title.getText(),classif.getText(),
                    Double.parseDouble(mass.getText()),Integer.parseInt(radius.getText()),angle.getText(),
                    Double.parseDouble(magnitude.getText()),getColorByName(color.getText()), anchor.isSelected());
            return nB;
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

    private final int height;
    private final int width;
    private final int xPos;
    private final int yPos;

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
