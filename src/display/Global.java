package display;

import physics.Line;
import physics.StellarBody;
import physics.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.*;
import java.util.*;

class GlobalFrame extends JFrame {
    int PREF_X = 1440;
    int PREF_Y = 1080;
    @Override
    public Dimension getPreferredSize() {
        if (isPreferredSizeSet()) {return super.getPreferredSize();}
        return new Dimension(PREF_X, PREF_Y);
    }
    public GlobalFrame() {
        this.getContentPane().setLayout(null);
    }
}

public class Global extends JPanel {
    public int PREF_X;
    public int PREF_Y;
//    public ArrayList<MyShape> Shapes = new ArrayList<>();
    public final JFrame Frame;
    public boolean SimComplete = false;
    public boolean Paused = false;
    public boolean Preview;
    private final ArrayList<StellarBody> Bodies;
    public final ArrayList<StellarBody> CollidedBodies = new ArrayList<>();
    public Graphics2D g2;
    protected final LinkedList<Line> Lines = new LinkedList<>();
    public ArrayList<JLabel> velocityLabels = new ArrayList<>();
    public final JButton returnToMainMenu = new JButton("Exit To Menu");
    public final JButton pauseButton = new JButton("ll");
    public final JLabel pausedLabel = new JLabel("PAUSED");
    public void reset(Start start) {
        Bodies.clear();
        for (StellarBody b : start.initialBodies) {
            b.myShape = null;
            this.Bodies.add(b.clone());
        }

        Frame.dispose();
    }

    public Global(ArrayList<StellarBody> bodies, Start start) { // all-purpose
        Frame = new GlobalFrame();
        setLayout(null);
        Frame.getContentPane().add(this);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Insets scnMax = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
        int taskBarSize = scnMax.bottom;
        PREF_X = screenSize.width - getWidth();
        PREF_Y = screenSize.height - taskBarSize - getHeight();
        ((GlobalFrame) Frame).PREF_X = PREF_X;
        ((GlobalFrame) Frame).PREF_Y = PREF_Y;

        this.setBounds(0,0,PREF_X,PREF_Y);
        this.Bodies = bodies;

        returnToMainMenu.setFont(new Font("Times New Roman",Font.BOLD,16));
        returnToMainMenu.addActionListener(e -> {
            start.setVisible(true);
            start.reset();
            this.reset(start);
        });
        Frame.setResizable(false);

        pausedLabel.setBounds(20,20,250,40);
        pausedLabel.setFont(new Font("Times New Roman", Font.BOLD, 26));
        pausedLabel.setOpaque(true);
        Color transparentRed = new Color(255,0,0,100);
        pausedLabel.setForeground(transparentRed);

        pausedLabel.setMaximumSize(new Dimension(250,40));
        Frame.getContentPane().add(pausedLabel);
        pausedLabel.setVisible(false);

        this.Bodies.forEach(this::displayBody);
        setPreferredSize(new Dimension(PREF_X, PREF_Y));
        Frame.setTitle("Simulation");
        Frame.pack();
    }
    public Global(ArrayList<StellarBody> bodies, Start.AddBody prevMenu) { // meant for previewMovement only
        Frame = new GlobalFrame();
        setLayout(null);
        Frame.getContentPane().add(this);

        this.Frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Insets scnMax = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
        int taskBarSize = scnMax.bottom;
        PREF_X = screenSize.width - getWidth();
        PREF_Y = screenSize.height - taskBarSize - getHeight();
        ((GlobalFrame) Frame).PREF_X = PREF_X;
        ((GlobalFrame) Frame).PREF_Y = PREF_Y;

        this.setBounds(0,0,PREF_X,PREF_Y);
        this.Bodies = bodies;

        returnToMainMenu.setText("Close Preview");
        returnToMainMenu.setFont(new Font("Times New Roman",Font.BOLD,16));
        returnToMainMenu.addActionListener(e -> {
            prevMenu.toFront();
            prevMenu.requestFocus();
            this.Preview = false;
            this.Frame.dispose();
        });
        returnToMainMenu.setBounds(PREF_X/2-80,20,160,40);
        this.add(returnToMainMenu);

        this.Frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                prevMenu.toFront();
                prevMenu.requestFocus();
                Global.this.Preview = false;
                Global.this.Frame.dispose();
            }
        });

        this.Bodies.forEach(this::displayBody);
        setPreferredSize(new Dimension(PREF_X, PREF_Y));

        Frame.setTitle("Preview Movement");
        Frame.pack();
        Frame.setResizable(false);

    }
    @Override
    public Dimension getPreferredSize() {
        if (isPreferredSizeSet()) {return super.getPreferredSize();}
        return new Dimension(PREF_X, PREF_Y);
    }

    public void previewMovement(ArrayList<StellarBody> origBodies) {
        Preview = true;
        ArrayList<StellarBody> bodies = new ArrayList<>();
        origBodies.forEach(e -> bodies.add(e.clone()));

        this.g2 = (Graphics2D) Frame.getGraphics();
        for (int cycle = 0; cycle<100; cycle++) {
            this.collision(bodies);
            for (StellarBody obj : bodies) {
                if (!obj.myShape.isCollided) {
                    bodies.forEach(e -> {if (!e.Title.equals(obj.Title)) e.effect_movement(obj, 0.2, system.DistScale);});
                    this.move(obj, 0.2);
                }
            }
        }
        for (StellarBody body : bodies) {
            this.drawHistory(g2, body);
        }
    }

    public void displayBody(StellarBody obj) {
        MyShape newShape = new MyShape(obj.Title, new Ellipse2D.Float( // Create a new shape, missing history and flags
                PREF_X / 2f + obj.Position.get(0) - obj.Radius.floatValue(),
                PREF_Y / 2f + obj.Position.get(1) - obj.Radius.floatValue(),
                obj.Radius.floatValue() * 2, obj.Radius.floatValue() * 2), obj.COLOR);

        if (Objects.nonNull(obj.myShape)) {
            newShape.PositionHistory = obj.myShape.PositionHistory;
        }
        obj.myShape = newShape;

        JLabel velocity = new JLabel(String.format("%.2fkm/s",obj.Movement.getMagnitude()));
        velocity.setFont(new Font("Times New Roman",Font.PLAIN,12));
        velocity.setBounds(PREF_X/2+obj.Position.get(0).intValue()+obj.Radius+10, PREF_Y/2+obj.Position.get(1).intValue()-(obj.Radius*2)-10, 80,20);
        velocityLabels.add(velocity);
    }
    public void move(StellarBody obj, Double TimeScale) {
        obj.move(TimeScale);
        Double[] mv = obj.Movement.evaluate(TimeScale);
        obj.myShape.translate(mv[0], mv[1]);
    }
    public void collision(ArrayList<StellarBody> bodies) { // Goes through `bodies` and checks each for a collision
        ArrayList<StellarBody> toRemove = new ArrayList<>();
        ArrayList<String> Check = new ArrayList<>();
        for (StellarBody prim : bodies) {
            for (StellarBody sec : bodies) {
                if (!prim.Title.equals(sec.Title) &&
                        !(Check.contains(String.format("%s->%s", prim.Title, sec.Title)) || Check.contains(String.format("%s->%s", sec.Title, prim.Title))) &&
                        (!prim.myShape.isCollided && !sec.myShape.isCollided)) {

                    AffineTransform at = new AffineTransform();
                    GeneralPath pathObj = new GeneralPath();
                    pathObj.append(prim.myShape.Shape.getPathIterator(at), false);
                    double x = (prim.myShape.PosX - pathObj.getBounds().getMinX());
                    double y = (prim.myShape.PosY - pathObj.getBounds().getMinY());
                    at.translate(x, y);
                    pathObj.reset();
                    pathObj.append(prim.myShape.Shape.getPathIterator(at), false);

                    at = new AffineTransform();
                    GeneralPath pathShape = new GeneralPath();
                    pathShape.append(sec.myShape.Shape.getPathIterator(at), false);
                    x = pathShape.getBounds().getMinX()!=0.0?(sec.myShape.PosX - pathShape.getBounds().getMinX()):0.00;
                    y = pathShape.getBounds().getMinY()!=0.0?(sec.myShape.PosY - pathShape.getBounds().getMinY()):0.00;
                    at.translate(x,y);
                    pathShape.reset();
                    pathShape.append(sec.myShape.Shape.getPathIterator(at), false);

                    Area a1 = new Area(pathObj);
                    Area a2 = new Area(pathShape);
                    a2.intersect(a1);

                    if (!a2.isEmpty()) {
                        if (Objects.requireNonNull(prim).Mass > Objects.requireNonNull(sec).Mass) {
                            removeCollision(toRemove, prim, sec);
                        }
                        else if (prim.Mass < sec.Mass) {
                            removeCollision(toRemove, sec, prim);
                        }
                    }
                    Check.add(String.format("%s->%s", prim.Title, sec.Title));
                }
            }
        }
        toRemove.forEach(bodies::remove);
    }

    private void removeCollision(ArrayList<StellarBody> toRemove, StellarBody prim, StellarBody sec) {
        sec.myShape.isCollided = true;
        prim.Mass += sec.Mass;
        sec.Movement.setMagnitude(0.0);
        prim.myShape.PositionHistory.add(new Double[] {
                prim.myShape.PosX+prim.myShape.Shape.getBounds().width/2.0, prim.myShape.PosY+prim.myShape.Shape.getBounds().width/2.0, 1.0
        });
        CollidedBodies.add(sec);
        toRemove.add(sec);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2 = g!=null?(Graphics2D)g:g2;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (!Lines.isEmpty()) {
            for (Line line : Lines) {
                if (Objects.nonNull(line.Color)) g2.setColor(line.Color);
                else g2.setColor(Color.BLUE);
                g2.setStroke(new BasicStroke(1));
                g2.drawLine(line.Start.get(0).intValue(),line.Start.get(1).intValue(),line.End.get(0).intValue(),line.End.get(1).intValue());
                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(1));
            }
        }
        for (StellarBody body : Bodies) {
            body.myShape.draw(g2, false);
        }

    }

    public void refresh() { // refreshes the canvas in real-time. god this took me ages to find the solution to
        paintImmediately(0,0,getWidth(),getHeight());
        revalidate();
    }
    public void drawHistory(Graphics2D g2, StellarBody obj) {
        Iterator<Double[]> it = obj.myShape.PositionHistory.iterator();
        Double[] prevPosition = {0.0, 0.0, 0.0};
        Double[] curPosition;
        if (it.hasNext()) {
            prevPosition = it.next();
        }
        int i = 0;
        while (it.hasNext()) {
            curPosition = it.next();

            if (!this.Preview) g2.setColor(obj.COLOR);
            else g2.setColor(new Color((255/obj.myShape.PositionHistory.size())*i,(255/obj.myShape.PositionHistory.size())*i,(255/obj.myShape.PositionHistory.size())*i));
            this.line(prevPosition, curPosition, g2.getColor());
            if (i%10 == 0 && !Preview) { // Only draws an arrow every 10 steps
                Line change = new Line(curPosition, prevPosition, g2.getColor()); // Create direction arrows for history path
                double leftWingAngle = change.Movement.coefficient() + 30;
                double rightWingAngle = change.Movement.coefficient() - 30;
                double leftXLength = 10 * Math.cos(Math.toRadians(leftWingAngle));
                double leftYLength = 10 * Math.sin(Math.toRadians(leftWingAngle));
                double rightXLength = 10 * Math.cos(Math.toRadians(rightWingAngle));
                double rightYLength = 10 * Math.sin(Math.toRadians(rightWingAngle));
                this.line(curPosition, new Double[]{
                        curPosition[0] + leftXLength, curPosition[1] + leftYLength}, g2.getColor());
                this.line(curPosition, new Double[]{
                        curPosition[0] + rightXLength, curPosition[1] + rightYLength}, g2.getColor());
            }

            g2.setColor(Color.GREEN);
            if (curPosition[2] == 1) {
                g2.fillRoundRect(curPosition[0].intValue()-5, curPosition[1].intValue()-5, 10,10, 6, 6);
            }
            i++;
            prevPosition = curPosition;
        }
        obj.myShape.draw(g2, true);
    }

    public void line(Double[] Start, Double[] End, Color color) {
        Lines.add(new Line(Start, End, color));
        g2.drawLine(Start[0].intValue(),Start[1].intValue(), End[0].intValue(),End[1].intValue());
    }
    public void line(ArrayList<Float> Start, ArrayList<Float> End)  {
        g2.drawLine(Start.get(0).intValue(),Start.get(1).intValue(), End.get(0).intValue(),End.get(1).intValue());
    }
}