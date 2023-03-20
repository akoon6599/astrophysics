package display;

import physics.Line;
import physics.StellarBody;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;

class GlobalFrame extends JFrame {
    int PREF_X = 1440;
    int PREF_Y = 1080;
    @Override
    public Dimension getPreferredSize() {
        if (isPreferredSizeSet()) {return super.getPreferredSize();}
        return new Dimension(PREF_X, PREF_Y);
    }
    public GlobalFrame() {
    }
}

public class Global extends JPanel {
    protected int PREF_X = 1440;
    protected int PREF_Y = 1080;
    public ArrayList<MyShape> Shapes = new ArrayList<>();
    public final JFrame Frame;
    public boolean SimComplete = false;
    private final ArrayList<StellarBody> Bodies;
    private ArrayList<StellarBody> CollidedBodies = new ArrayList<>();
    protected Graphics2D g2;
    protected final LinkedList<Line> Lines = new LinkedList<>();
    public ArrayList<JLabel> velocityLabels = new ArrayList<>();
    private final JButton returnToMainMenu = new JButton("Exit To Menu");
    public void reset(Start start) {
        Shapes.clear();
        Bodies.clear();
        for (StellarBody b : start.initialBodies) {
            this.Bodies.add(b.clone());
        }

        Frame.dispose();
    }

    public Global(ArrayList<StellarBody> bodies, Start start) { // all-purpose

        Frame = new GlobalFrame();
        Frame.getContentPane().add(this);
        this.Bodies = bodies;

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Insets scnMax = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
        int taskBarSize = scnMax.bottom;
        PREF_X = screenSize.width - getWidth();
        PREF_Y = screenSize.height - taskBarSize - getHeight();
        ((GlobalFrame) Frame).PREF_X = PREF_X;
        ((GlobalFrame) Frame).PREF_Y = PREF_Y;

        setLayout(null);

        this.Bodies.forEach(this::displayBody);
        setPreferredSize(new Dimension(PREF_X, PREF_Y));
        Frame.setTitle("Simulation");
        Frame.pack();

        returnToMainMenu.setFont(new Font("Times New Roman",Font.BOLD,16));
        returnToMainMenu.addActionListener(e -> {
            start.setVisible(true);
            start.reset();
            this.reset(start);
        });
        Frame.setResizable(false);

    }
    public Global(ArrayList<StellarBody> bodies) { // meant for previewMovement only
        Frame = new GlobalFrame();
        Frame.getContentPane().add(this);
        this.Bodies = bodies;

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Insets scnMax = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
        int taskBarSize = scnMax.bottom;
        PREF_X = screenSize.width - getWidth();
        PREF_Y = screenSize.height - taskBarSize - getHeight();
        ((GlobalFrame) Frame).PREF_X = PREF_X;
        ((GlobalFrame) Frame).PREF_Y = PREF_Y;

        this.Bodies.forEach(this::displayBody);
        setPreferredSize(new Dimension(PREF_X, PREF_Y));
        returnToMainMenu.setText("Close Preview");
        returnToMainMenu.setFont(new Font("Times New Roman",Font.BOLD,16));
        returnToMainMenu.addActionListener(e -> this.Frame.dispose());
        returnToMainMenu.setBounds(PREF_X/2-40,10,80,20);
        this.add(returnToMainMenu);
        Frame.setTitle("Preview Movement");
        Frame.pack();
        Frame.setResizable(false);
    }
    @Override
    public Dimension getPreferredSize() {
        if (isPreferredSizeSet()) {return super.getPreferredSize();}
        return new Dimension(PREF_X, PREF_Y);
    }

    public void previewMovement(StellarBody obj) {
        StellarBody tempObj = obj.clone();
        tempObj.move(10.0);
        this.g2 = (Graphics2D) Frame.getGraphics();
        assert this.g2 != null;

        float[] CENTER = new float[2];
        CENTER[0] = PREF_X/2f;
        CENTER[1] = PREF_Y/2f;
        Lines.add(new Line((ArrayList<Float>) obj.Position.clone(), (ArrayList<Float>) tempObj.Position.clone(), CENTER, obj.Radius));
    }




    public void displayBody(StellarBody obj) {
        MyShape newShape = new MyShape(obj.Title, new Ellipse2D.Float( // Create a new shape, missing history and flags
                PREF_X / 2f + obj.Position.get(0) - obj.Radius.floatValue(),
                PREF_Y / 2f + obj.Position.get(1) - obj.Radius.floatValue(),
                obj.Radius.floatValue() * 2, obj.Radius.floatValue() * 2), obj.COLOR);
        if (Shapes.isEmpty()) {Shapes.add(newShape);} // Most likely this will be the sun
        boolean Found = false;
        ArrayList<MyShape> tmpShapes = (ArrayList<MyShape>) Shapes.clone(); // Duplicated Shapes so that we can iterate + edit
        for (MyShape shp : tmpShapes) {
            if (shp.Title.equals(newShape.Title)) { // Find matching shape, copy history
                Found = true;
                newShape.PositionHistory = shp.PositionHistory;
                if (!shp.isCollided) { // If it isn't collided, copy it. If it is, do nothing with it
                    Shapes.remove(shp);
                    Shapes.add(newShape);
                }
            }
        }
        if (!Found) {
            Shapes.add(newShape);
        }

        JLabel velocity = new JLabel(String.format("%.2fkm/s",obj.Movement.getMagnitude()));
        velocity.setFont(new Font("Times New Roman",Font.PLAIN,12));
        velocity.setBounds(PREF_X/2+obj.Position.get(0).intValue()+obj.Radius+10, PREF_Y/2+obj.Position.get(1).intValue()-(obj.Radius*2)-10, 80,20);
        velocityLabels.add(velocity);
    }
    public void move(StellarBody obj, Double TimeScale) {
            for (MyShape shape : Shapes) {
                if (shape.Title.equals(obj.Title) && !shape.isCollided) {
                    obj.move(TimeScale);
                    Double[] mv = obj.Movement.evaluate(TimeScale);
                    shape.translate(mv[0], mv[1]);
                }
            }
    }
    public void collision(ArrayList<StellarBody> bodies) { // Goes through `bodies` and checks each for a collision
        ArrayList<String> Check = new ArrayList<>();
        for (MyShape obj : Shapes) {
            for (MyShape shape : Shapes) {
                if (!Objects.equals(shape.Title, obj.Title) &&
                        !(Check.contains(String.format("%s->%s", shape.Title, obj.Title)) ||
                        Check.contains(String.format("%s->%s", obj.Title, shape.Title))) &&
                        (!shape.isCollided && !obj.isCollided)) {
                    AffineTransform at = new AffineTransform();

                    GeneralPath pathObj = new GeneralPath();
                    pathObj.append(obj.Shape.getPathIterator(at), false);
                    double x = (obj.PosX - pathObj.getBounds().getMinX());
                    double y = (obj.PosY - pathObj.getBounds().getMinY());
                    at.translate(x, y);
                    pathObj.reset();
                    pathObj.append(obj.Shape.getPathIterator(at), false);

                    at = new AffineTransform();
                    GeneralPath pathShape = new GeneralPath();
                    pathShape.append(shape.Shape.getPathIterator(at), false);
                    x = pathShape.getBounds().getMinX()!=0.0?(shape.PosX - pathShape.getBounds().getMinX()):0.00;
                    y = pathShape.getBounds().getMinY()!=0.0?(shape.PosY - pathShape.getBounds().getMinY()):0.00;
                    at.translate(x,y);
                    pathShape.reset();
                    pathShape.append(shape.Shape.getPathIterator(at), false);

                    Area a1 = new Area(pathObj);
                    Area a2 = new Area(pathShape);
                    a2.intersect(a1);

                    if (!a2.isEmpty()) {
                        StellarBody mainBody = null;
                        StellarBody secondaryBody = null;
                        for (StellarBody body : bodies) {
                            if (body.Title.equals(obj.Title)) {mainBody = body;}
                            if (body.Title.equals(shape.Title)) {secondaryBody = body;}
                        }
                        if (Objects.requireNonNull(mainBody).Mass > Objects.requireNonNull(secondaryBody).Mass) {
                            shape.isCollided = true;
                            mainBody.Mass += secondaryBody.Mass;
                            secondaryBody.Movement.setMagnitude(0.0);
                            obj.PositionHistory.add(new Double[] {
                                    obj.PosX+obj.Shape.getBounds().width/2.0, obj.PosY+obj.Shape.getBounds().width/2.0, 1.0
                            });
                            CollidedBodies.add(secondaryBody);
                            bodies.remove(secondaryBody);
                        }
                        else if (mainBody.Mass < secondaryBody.Mass) {
                            obj.isCollided = true;
                            secondaryBody.Mass += mainBody.Mass;
                            mainBody.Movement.setMagnitude(0.0);
                            shape.PositionHistory.add(new Double[] {
                                    shape.PosX+shape.Shape.getBounds().width/2.0, shape.PosY+shape.Shape.getBounds().width/2.0, 1.0
                            });
                            CollidedBodies.add(mainBody);
                            bodies.remove(mainBody);
                        }
                    }
                    Check.add(String.format("%s->%s", obj.Title, shape.Title));
                }
            }
        }
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g2 = g!=null?(Graphics2D)g:g2;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (!Lines.isEmpty()) {
            for (Line line : Lines) {
                g2.setColor(Color.BLUE);
                g2.setStroke(new BasicStroke(3));
                g2.drawLine(line.Start.get(0).intValue(),line.Start.get(1).intValue(),line.End.get(0).intValue(),line.End.get(1).intValue());
                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(1));
            }
        }
        for (MyShape shape : Shapes) {
            shape.draw(g2, false);
        }
        if (SimComplete) {
            for (StellarBody body : Bodies) {
                drawHistory(g2, body);
            }
            for (StellarBody body : CollidedBodies) {
                drawHistory(g2, body);
            }
        }
    }

    public void refresh() { // refreshes the canvas in real-time. god this took me ages to find the solution to
        paintImmediately(0,0,getWidth(),getHeight());
        revalidate();
    }
    public void drawHistory(Graphics2D g2, StellarBody obj) {
        for (MyShape shape : Shapes) {
//            if (!obj.Title.equals("Sun") && !shape.Title.equals("Sun")) {
                if (shape.Title.equals(obj.Title)) {
                    Iterator<Double[]> it = shape.PositionHistory.iterator();
                    Double[] prevPosition = {0.0, 0.0, 0.0};
                    Double[] curPosition;
                    if (it.hasNext()) {
                        prevPosition = it.next();
                    }
                    int i = 0;
                    while (it.hasNext()) {
                        curPosition = it.next();

                        g2.setColor(obj.COLOR);
                        this.line(prevPosition, curPosition);
                        if (i%10 == 0) { // Only draws an arrow every 10 steps
                            Line change = new Line(curPosition, prevPosition); // Create direction arrows for history path
                            double leftWingAngle = change.Movement.coefficient() + 30;
                            double rightWingAngle = change.Movement.coefficient() - 30;
                            double leftXLength = 10 * Math.cos(Math.toRadians(leftWingAngle));
                            double leftYLength = 10 * Math.sin(Math.toRadians(leftWingAngle));
                            double rightXLength = 10 * Math.cos(Math.toRadians(rightWingAngle));
                            double rightYLength = 10 * Math.sin(Math.toRadians(rightWingAngle));
                            this.line(curPosition, new Double[]{
                                    curPosition[0] + leftXLength, curPosition[1] + leftYLength});
                            this.line(curPosition, new Double[]{
                                    curPosition[0] + rightXLength, curPosition[1] + rightYLength});
                        }

                        g2.setColor(Color.GREEN);
                        if (curPosition[2] == 1) {
                            g2.fillRoundRect(curPosition[0].intValue()-5, curPosition[1].intValue()-5, 10,10, 6, 6);
                        }
                        i++;
                        prevPosition = curPosition;
                    }
//                }
            }
            shape.draw(g2, true);
        }
        returnToMainMenu.setBounds(PREF_X/2-80,20,160,40);
        this.add(returnToMainMenu);
    }
    public void line(Double[] Start, Double[] End) {
        g2.drawLine(Start[0].intValue(),Start[1].intValue(), End[0].intValue(),End[1].intValue());
    }
    public void line(ArrayList<Float> Start, ArrayList<Float> End)  {
        g2.drawLine(Start.get(0).intValue(),Start.get(1).intValue(), End.get(0).intValue(),End.get(1).intValue());
    }
}


class MyShape {
    public Path2D path = new Path2D.Double();
    public Shape Shape;
    public String Title;
    public double PosX;
    public double PosY;
    public double initialPosX;
    public double initialPosY;
    public Path2D initialPath;
    public boolean isCollided = false;
    public ArrayList<Double[]> PositionHistory = new ArrayList<>();
    public Color COLOR;

    public MyShape(String name, Shape shape, Color color){
        Title = name;
        path.append(shape, true);
        Shape = shape;
        PosX = shape.getBounds().getMinX();
        PosY = shape.getBounds().getMinY();
        initialPosX = PosX;
        initialPosY = PosY;
        initialPath = path;
        COLOR = color;
        PositionHistory.add(new Double[]
                {PosX+Shape.getBounds().width/2.0, PosY+Shape.getBounds().width/2.0,0.0});
    }
    public void translate(Double deltaX, Double deltaY) {
        path.transform(AffineTransform.getTranslateInstance(deltaX, deltaY));
        PosX += deltaX;
        PosY += deltaY;
        PositionHistory.add(new Double[]
                {PosX+Shape.getBounds().width/2.0, PosY+Shape.getBounds().width/2.0,0.0});
    }
    public void draw(Graphics2D g2, boolean isFinal) {
        if (!isFinal) {
            if (!isCollided) {
                g2.setColor(Color.BLACK);
                g2.draw(path);
                this.fill(g2);
            }
            if (isCollided) {
                g2.setColor(Color.BLUE);
                g2.draw(path);
            }
        }
        else {
            if (isCollided) {
                g2.setColor(Color.BLUE);
                g2.draw(path);
            }
        }
    }
    public void fill(Graphics2D g2) {
        if (!isCollided) {
            g2.setColor(COLOR);
            g2.fill(path);
        }
    }
}