package display;

import physics.Line;
import physics.Movement;
import physics.StellarBody;
import physics.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

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
    ArrayList<JComponent> components;
    ArrayList<JButton> bodyButtons;
    private final Icon settingsIcon = new ImageIcon("./src/display/settingsCog.png");
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
        for (StellarBody b : initialBodies) {
            Bodies.add(b.clone());
        }
    }
    private void mainMenu() {
        components = new ArrayList<>();
        bodyButtons = new ArrayList<>();

        setTitle("Main Menu");
        JButton addBBody = new JButton("Add Body");
        addBBody.setFont(new Font("Times New Roman", Font.BOLD, 24));
        components.add(addBBody);
        addBBody.addActionListener(e -> new AddBody().setVisible(true));
        ArrayList<JLabel> bodyLabels = new ArrayList<>();

        JButton settingsBtn = new JButton(settingsIcon);
        settingsBtn.setSize(new Dimension(16,16));
        settingsBtn.addActionListener(e -> new Settings().setVisible(true));

        for (StellarBody body : Bodies) {
            bodyButtons.add(new JButton(body.Title));
            bodyLabels.add(new JLabel(
                    String.format("Angle: %.2f -- Velocity: %.3f km/s-- Classification: %s -- Mass: %.3fe24 kg -- Pos (X,Y): (%.0f, %.0f) px -- Anchor: %s",
                            body.Movement.coefficient(), body.Movement.getMagnitude(), body.Classification, body.Mass/1e24, body.Position.get(0), body.Position.get(1), body.STATIC)));
        }
        for (JButton btn : (ArrayList<JButton>)bodyButtons.clone()) {
            btn.addActionListener(e -> {
                new AddBody(Start.this.Bodies.get(bodyButtons.indexOf(btn))).setVisible(true);
            });
        }


        GroupLayout.SequentialGroup bodyField = layout.createSequentialGroup() // HORIZONTAL
                .addGap(6, 6, 6);
        GroupLayout.ParallelGroup tmpBButtons = layout.createParallelGroup(LEADING);
        for (JButton c : bodyButtons) {
            tmpBButtons.addComponent(c);
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


        JLabel INFO = new JLabel("Click to Edit");
        bodyField.addGroup(tmpBButtons).addGroup(tmpBLabels); // HORIZONTAL
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(INFO)
                                        .addGroup(bodyField)
                                        .addComponent(settingsBtn))
                                .addGap(0,PREF_X-100,PREF_X-50)
                                .addGroup(layout.createParallelGroup(LEADING)
                                .addComponent(Simulate)
                                .addComponent(addBBody))
        ));

        GroupLayout.SequentialGroup pBodyField = layout.createSequentialGroup() // VERTICAL
                .addContainerGap()
                .addGroup(layout.createParallelGroup(TRAILING)
                    .addComponent(INFO)
                    .addComponent(addBBody))
                .addGap(30,30,30);
        ArrayList<GroupLayout.ParallelGroup> help = new ArrayList<>();
        for (JButton c: bodyButtons) {
            help.add(layout.createParallelGroup(BASELINE)
                    .addComponent(c)
                    .addComponent(bodyLabels.get(bodyButtons.indexOf(c)))
                    .addGap(5,10,25));
        }
        help.forEach(pBodyField::addGroup);
        pBodyField.addContainerGap((int)(PREF_Y*0.2), Short.MAX_VALUE);
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(LEADING)
                        .addGroup(pBodyField))
                .addGroup(layout.createParallelGroup(LEADING)
                        .addComponent(settingsBtn)
                        .addComponent(Simulate))
        );

        validate();
    }

    public void refresh() {
        getContentPane().removeAll();
        mainMenu();
        repaint();
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
        JComboBox<String> orbiterChoices;
        JTextField color = new JTextField(6);
        JCheckBox anchor = new JCheckBox("False");
        JButton preview = new JButton("Preview Movement");
        JButton finalize = new JButton("Finalize");
        JCheckBox track = new JCheckBox("False");


        public AddBody(StellarBody oldBody) {
            layout = new GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setAutoCreateGaps(true);
            layout.setAutoCreateContainerGaps(true);
            setTitle("Planet Action Menu");

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
            JLabel colText = new JLabel("Color (R,G,B): ");
            JLabel ancText = new JLabel("Anchor: ");
            JLabel errorText = new JLabel("");
            JLabel comboText = new JLabel("Parent Body: ");
            JLabel trackText = new JLabel("Display Velocity During Simulation: ");

            String[] Choices = new String[Start.this.Bodies.size()+1];
            Choices[0] = "None";
            for (int i=0;i < Start.this.Bodies.size(); i++) {
                Choices[i+1] = Start.this.Bodies.get(i).Title;
            }
            orbiterChoices = new JComboBox<>(Choices);

            angle.setText(String.valueOf(oldBody.Movement.coefficient()));
            magnitude.setText(String.format("%.2f", oldBody.Movement.getMagnitude()));
            posx.setText(String.valueOf(oldBody.Position.get(0)));
            posy.setText(String.valueOf(oldBody.Position.get(1)));
            mass.setText(String.valueOf(oldBody.Mass));
            radius.setText(String.valueOf(oldBody.Radius));
            if (oldBody.ORBITER) {
                int correctIndex = 0;
                for (int i = 0; i < orbiterChoices.getItemCount(); i++) {
                    if (orbiterChoices.getItemAt(i).equals(oldBody.orbitingPoint.Title)) {
                        correctIndex = i;
                    }
                }
                orbiterChoices.setSelectedIndex(correctIndex);
            }
            title.setText(oldBody.Title);
            color.setText(String.format("%d,%d,%d",
                    oldBody.COLOR.getRed(),
                    oldBody.COLOR.getGreen(),
                    oldBody.COLOR.getBlue())
            );
            classif.setText(oldBody.Classification);
            anchor.setSelected(oldBody.STATIC);
            track.setSelected(oldBody.TRACKVEL);

            finalize.addActionListener(e -> {
                try {
                    StellarBody nB = AddBody.this.finalize_body(oldBody);
                    Start.this.initialBodies.set(Start.this.Bodies.indexOf(oldBody), nB);
                    Start.this.Bodies.set(Start.this.Bodies.indexOf(oldBody), nB);
                    Start.this.refresh();
                    dispose();
                }
                catch (NumberFormatException exception) {
                    errorText.setText(String.format("Erorr: %s",exception.getMessage()));
                }
            });
            rtn.addActionListener(e -> dispose());

            preview.addActionListener(e -> {
                try {
                    ArrayList<StellarBody> displayBodies = new ArrayList<>();
                    for (StellarBody b : Start.this.Bodies) {
                        if (b.Title.equals(oldBody.Title)) {
                            displayBodies.add(AddBody.this.finalize_body(oldBody));
                        }
                        else {
                            displayBodies.add(b.clone());
                        }
                    }
                    Global GLOBAL = new Global(displayBodies);
                    system.display(GLOBAL);
                    GLOBAL.previewMovement(oldBody);
                } catch (NumberFormatException exception) {
                    errorText.setText(String.format("Erorr: %s",exception.getMessage()));
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
                JCheckBox source = AddBody.this.anchor;
                if (source.isSelected()) {
                    source.setText("True ");
                }
                else {
                    source.setText("False");
                }
            });
            track.addActionListener(e -> {
                JCheckBox source = AddBody.this.track;
                if (source.isSelected()) {
                    source.setText("True ");
                }
                else {
                    source.setText("False");
                }
            });
            if(anchor.isSelected()) anchor.setText("True ");
            else anchor.setText("False");
            if(track.isSelected()) track.setText("True ");
            else track.setText("False");

            JButton remove = new JButton("Remove Body");
            remove.addActionListener(e -> {
                Start.this.Bodies.remove(oldBody);
                Start.this.initialBodies.remove(oldBody);
                for (JButton btn : (ArrayList<JButton>)bodyButtons.clone()) {
                    if (String.valueOf(btn.getText()).equals(oldBody.Title)) {
                        bodyButtons.remove(btn);
                    }
                }
                Start.this.refresh();
                this.dispose();
            });

            layout.setHorizontalGroup(layout.createSequentialGroup() // HORIZONTAL
                    .addGroup(layout.createParallelGroup(LEADING)
                            .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(LEADING)
                                            .addComponent(rtn)
                                            .addComponent(angleText)
                                            .addComponent(magText)
                                            .addComponent(posText)
                                            .addComponent(titleText)
                                            .addComponent(classText))
                                    .addGap(7, 7, 7)
                                    .addGroup(layout.createParallelGroup(LEADING)
                                            .addComponent(angle)
                                            .addComponent(magnitude)
                                            .addGroup(layout.createSequentialGroup()
                                                    .addComponent(posx)
                                                    .addGap(5, 5, 5)
                                                    .addComponent(posy))
                                            .addComponent(title)
                                            .addComponent(classif))
                                    .addGap(3, 3, 3)
                                    .addGroup(layout.createParallelGroup(LEADING)
                                            .addComponent(physLabel)
                                            .addComponent(cosLabel))
                                    .addGroup(layout.createParallelGroup(LEADING)
                                            .addComponent(massText)
                                            .addComponent(radText)
                                            .addComponent(comboText)
                                            .addComponent(colText)
                                            .addComponent(ancText))
                                    .addGap(7, 7, 7)
                                    .addGroup(layout.createParallelGroup(LEADING)
                                            .addComponent(mass)
                                            .addComponent(radius)
                                            .addComponent(orbiterChoices)
                                            .addComponent(color)
                                            .addComponent(anchor)
                                            .addComponent(remove))
                                    .addGroup(layout.createParallelGroup(TRAILING)
                                            .addComponent(preview)
                                            .addComponent(finalize)))
                            .addGroup(layout.createParallelGroup(LEADING)
                                    .addComponent(errorText)
                                    .addGroup(layout.createSequentialGroup()
                                            .addComponent(trackText)
                                            .addComponent(track))))
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
                            .addComponent(posy)
                            .addComponent(comboText)
                            .addComponent(orbiterChoices))
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
                    .addComponent(errorText)
                    .addGroup(layout.createParallelGroup(TRAILING)
                            .addComponent(trackText)
                            .addComponent(track)
                            .addComponent(remove)
                            .addComponent(finalize))
            );

            pack();
            setLocationRelativeTo(null);
        }

        public AddBody() {
            layout = new GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setAutoCreateGaps(true);
            layout.setAutoCreateContainerGaps(true);
            rtn.addActionListener(e -> AddBody.this.dispose());
            setTitle("Planet Action Menu");
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
            JLabel comboText = new JLabel("Parent Body: ");
            JLabel trackText = new JLabel("Display Velocity During Simulation: ");

            angle.setMaximumSize(new Dimension(100,10));
            magnitude.setMaximumSize(new Dimension(100,10));
            posx.setMaximumSize(new Dimension(47,10));
            posy.setMaximumSize(new Dimension(47,10));
            title.setMaximumSize(new Dimension(100,10));
            classif.setMaximumSize(new Dimension(100,10));

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
            String[] Choices = new String[Start.this.Bodies.size()+1];
            Choices[0] = "None";
            for (int i=0;i < Start.this.Bodies.size(); i++) {
                Choices[i+1] = Start.this.Bodies.get(i).Title;
            }
            orbiterChoices = new JComboBox<>(Choices);



            String[] dfChoices = new String[system.DefaultBodies.size()+1];
            dfChoices[0] = "None";
            for (int i=0;i<system.DefaultBodies.size();i++) dfChoices[i+1] = system.DefaultBodies.keySet().toArray(new String[0])[i];
            JComboBox<String> defaultChoices = new JComboBox<>(dfChoices);
            AtomicReference<StellarBody> match = new AtomicReference<>();
            defaultChoices.addActionListener(e -> {
                for (StellarBody body : system.DefaultBodies.values()) {
                    if (body.Title.equals(String.valueOf(defaultChoices.getSelectedItem()))) {
                        match.set(body.clone());
                    }
                }

                if (!match.equals(new AtomicReference<>()) && !String.valueOf(defaultChoices.getSelectedItem()).equals("None")) {
                    angle.setText(String.valueOf(match.get().Movement.coefficient()));
                    magnitude.setText(String.format("%.2f", match.get().Movement.getMagnitude()));
                    posx.setText(String.valueOf(match.get().Position.get(0)));
                    posy.setText(String.valueOf(match.get().Position.get(1)));
                    mass.setText(String.valueOf(match.get().Mass));
                    radius.setText(String.valueOf(match.get().Radius));
                    if (match.get().ORBITER) {
                        int correctIndex = 0;
                        for (int i = 0; i < orbiterChoices.getItemCount(); i++) {
                            if (orbiterChoices.getItemAt(i).equals(match.get().orbitingPoint.Title)) {
                                correctIndex = i;
                            }
                        }
                        orbiterChoices.setSelectedIndex(correctIndex);
                    }
                    title.setText(match.get().Title);
                    color.setText(
                            String.format("%d,%d,%d",
                                    match.get().COLOR.getRed(),
                                    match.get().COLOR.getGreen(),
                                    match.get().COLOR.getBlue())
                    );
                    classif.setText(match.get().Classification);
                    anchor.setSelected(match.get().STATIC);
                    track.setSelected(match.get().TRACKVEL);

                    if(anchor.isSelected()) anchor.setText("True ");
                    else anchor.setText("False");
                    if(track.isSelected()) track.setText("True ");
                    else track.setText("False");
                }
                else {
                    angle.setText("");
                    magnitude.setText("");
                    posx.setText("POSX");
                    posy.setText("POSY");
                    mass.setText("");
                    radius.setText("");
                    orbiterChoices.setSelectedIndex(0);
                    title.setText("");
                    color.setText("0,0,0");
                    classif.setText("");
                    anchor.setSelected(false);
                    anchor.setText("False");
                    track.setSelected(false);
                    track.setText("False");
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
            track.addActionListener(e -> {
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
                            .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(LEADING)
                                            .addComponent(rtn)
                                            .addComponent(angleText)
                                            .addComponent(magText)
                                            .addComponent(posText)
                                            .addComponent(titleText)
                                            .addComponent(classText))
                                    .addGap(7, 7, 7)
                                    .addGroup(layout.createParallelGroup(LEADING)
                                            .addComponent(angle)
                                            .addComponent(magnitude)
                                            .addGroup(layout.createSequentialGroup()
                                                    .addComponent(posx)
                                                    .addGap(5, 5, 5)
                                                    .addComponent(posy))
                                            .addComponent(title)
                                            .addComponent(classif))
                                    .addGap(3, 3, 3)
                                    .addGroup(layout.createParallelGroup(LEADING)
                                            .addComponent(physLabel)
                                            .addComponent(cosLabel))
                                    .addGroup(layout.createParallelGroup(LEADING)
                                            .addComponent(massText)
                                            .addComponent(radText)
                                            .addComponent(comboText)
                                            .addComponent(colText)
                                            .addComponent(ancText))
                                    .addGap(7, 7, 7)
                                    .addGroup(layout.createParallelGroup(LEADING)
                                            .addComponent(mass)
                                            .addComponent(radius)
                                            .addComponent(orbiterChoices)
                                            .addComponent(color)
                                            .addComponent(anchor))
                                    .addGroup(layout.createParallelGroup(TRAILING)
                                            .addComponent(defaultChoices)
                                            .addComponent(preview)
                                            .addComponent(finalize)))
                    .addGroup(layout.createParallelGroup(LEADING)
                            .addComponent(errorText)
                            .addGroup(layout.createSequentialGroup()
                                    .addComponent(trackText)
                                    .addComponent(track))))
            );
            layout.setVerticalGroup(layout.createSequentialGroup() // VERTICAL
                    .addGroup(layout.createParallelGroup(BASELINE)
                            .addComponent(rtn)
                            .addComponent(preview))
                    .addGroup(layout.createParallelGroup(TRAILING)
                            .addGap(30,30,30)
                            .addComponent(defaultChoices))
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
                            .addComponent(posy)
                            .addComponent(comboText)
                            .addComponent(orbiterChoices))
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
                    .addComponent(errorText)
                    .addGroup(layout.createParallelGroup(TRAILING)
                            .addComponent(trackText)
                            .addComponent(track)
                            .addComponent(finalize))
            );

            pack();
            setLocationRelativeTo(null);
        }
        private StellarBody finalize_body() {
            if (String.valueOf(orbiterChoices.getSelectedItem()).equals("None")) {
                return new StellarBody(Float.parseFloat(posx.getText()), Float.parseFloat(posy.getText()), title.getText(), classif.getText(),
                        Double.parseDouble(mass.getText()), Integer.parseInt(radius.getText()), angle.getText(),
                        Double.parseDouble(magnitude.getText()), getColorByString(color.getText()), anchor.isSelected(), track.isSelected());
            }
            else {
                StellarBody orbitingBody = null;
                for (StellarBody body : Start.this.Bodies) {
                    if (body.Title.equals(String.valueOf(orbiterChoices.getSelectedItem()))) {
                        orbitingBody = body;
                    }
                }
                assert orbitingBody != null;

                StellarBody nB = new StellarBody(Float.parseFloat(posx.getText()), Float.parseFloat(posy.getText()), title.getText(), classif.getText(),
                        Double.parseDouble(mass.getText()), Integer.parseInt(radius.getText()), angle.getText(),
                        Double.parseDouble(magnitude.getText()), getColorByString(color.getText()), anchor.isSelected(), track.isSelected(), orbitingBody);

                Line Tether = new Line(nB, orbitingBody);
                double angle = Tether.Movement.coefficient()+90;
                nB.Movement = new Movement(String.format("%.2fd",angle),nB.Movement.getMagnitude());
                orbitingBody.find_orbit(nB,system.DistScale);
                nB.overrideInitialMovement(nB.Movement);

                return nB;
            }
        }
        private StellarBody finalize_body(StellarBody oldBody) {
            StellarBody nB = oldBody.clone();
            ArrayList<Float> pos = new ArrayList<>(2);
            pos.add(Float.parseFloat(posx.getText()));
            pos.add(Float.parseFloat(posy.getText()));
            nB.Position = pos;
            nB.overrideInitialPosition(pos);

            nB.Title = title.getText();
            nB.Classification = classif.getText();
            nB.Mass = Double.parseDouble(mass.getText());
            nB.Radius = Integer.parseInt(radius.getText());

            nB.Movement = new Movement(String.format("%sd",angle.getText()), Double.parseDouble(magnitude.getText()));
            nB.overrideInitialMovement(new Movement(String.format("%sd",angle.getText()), Double.parseDouble(magnitude.getText())));
            nB.STATIC = anchor.isSelected();
            nB.TRACKVEL = track.isSelected();

            if (!String.valueOf(orbiterChoices.getSelectedItem()).equals("None")) {
                nB.ORBITER = true;
                StellarBody orbiting = null;
                for (StellarBody body: Start.this.Bodies) {
                    if (body.Title.equals(String.valueOf(orbiterChoices.getSelectedItem()))) {
                        orbiting = body;
                    }
                }
                nB.orbitingPoint = orbiting;
            }

            return nB.clone();
        }
        public static Color getColorByString(String name) {
            int[] RGB = Arrays.stream(name.split(",")).mapToInt(Integer::parseInt).toArray();
            Color newColor = new Color(0,0,0);
            try {newColor = new Color(RGB[0],RGB[1],RGB[2]);}
            catch (IndexOutOfBoundsException ignored) {}
            return newColor;
        }
    }
    class Settings extends JFrame {
        protected static final int PREF_X = 880;
        protected static final int PREF_Y = 140;
        GroupLayout layout;
        JButton rtn = new JButton("Accept");
        JButton cncl = new JButton("Cancel");
        JTextField timeField = new JTextField(4);
        JTextField delayField = new JTextField(4);
        JTextField dilationField = new JTextField(4);

        public Settings() {
            layout = new GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setAutoCreateGaps(true);
            layout.setAutoCreateContainerGaps(true);
            rtn.addActionListener(e -> Settings.this.closeSettings());
            cncl.addActionListener(e -> Settings.this.dispose());
            setSize(PREF_X,PREF_Y);
            setLocationRelativeTo(null);

            JLabel timeLabel = new JLabel("Length of Simulation (s):");
            JLabel delayLabel = new JLabel("Delay Between Frames (ms):");
            JLabel dilationLabel = new JLabel("Time Dilation (x):");
            JLabel dilationNote = new JLabel("NOTE: between .01 and 2 is the");
            JLabel dilationNote2 = new JLabel("recommended range for Time Dilation.");

            timeField.setText(String.valueOf(system.CYCLES*system.CycleDelay/100.));
            delayField.setText(String.valueOf(system.CycleDelay));
            dilationField.setText(String.valueOf(system.TimeScale));

            { // Bracket is just to collapse in editor
                layout.setHorizontalGroup(layout.createSequentialGroup() // HORIZONTAL
                        .addGroup(layout.createParallelGroup(LEADING)
                                .addComponent(dilationLabel)
                                .addComponent(dilationNote)
                                .addComponent(dilationNote2)
                                .addComponent(cncl))
                        .addComponent(dilationField)
                        .addGap(15)
                        .addComponent(timeLabel)
                        .addComponent(timeField)
                        .addGap(15)
                        .addComponent(delayLabel)
                        .addGroup(layout.createParallelGroup(TRAILING)
                                .addComponent(delayField)
                                .addComponent(rtn))
                );
                layout.setVerticalGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(LEADING)
                                .addComponent(dilationLabel)
                                .addComponent(dilationField)
                                .addComponent(timeLabel)
                                .addComponent(timeField)
                                .addComponent(delayLabel)
                                .addComponent(delayField))
                        .addComponent(dilationNote)
                        .addComponent(dilationNote2)
                        .addGroup(layout.createParallelGroup(LEADING)
                                .addComponent(cncl)
                                .addComponent(rtn)));
            }
        }

        private void closeSettings() {
            double timeSeconds;
            long delayMs;
            double dilationMult;
            if (!(timeField.getText().isBlank())) {
                timeSeconds = Double.parseDouble(timeField.getText());
            }
            else {
                timeSeconds = system.CYCLES*system.CycleDelay / 100.;
            }
            if (!(delayField.getText().isBlank())) {
                delayMs = Long.parseLong(delayField.getText());
            }
            else {
                delayMs = system.CycleDelay;
            }
            if (!(dilationField.getText().isBlank())) {
                dilationMult = Double.parseDouble(dilationField.getText());
            }
            else {
                dilationMult = system.TimeScale;
            }

            delayMs = Math.max(1, delayMs);
            int numFrames = (int)(Math.round(timeSeconds*1000)/delayMs + 1)/2;

            system.CycleDelay = delayMs;
            system.CYCLES = numFrames;
            system.TimeScale = dilationMult;
            Settings.this.dispose();
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
