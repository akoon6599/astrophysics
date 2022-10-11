package display;

import physics.Line;
import physics.StellarBody;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

public class Global extends JPanel {
    protected static final int PREF_X = 1440;
    protected static final int PREF_Y = 1080;
    public ArrayList<MyShape> Shapes = new ArrayList<>();
    private final JPanel Frame;
    public boolean SimComplete = false;
    private ArrayList<StellarBody> Bodies;
    protected Graphics2D g2;


    public Global(ArrayList<StellarBody> bdy) {
        this.Frame = new JPanel();
        this.Bodies = bdy;
        this.Frame.setLayout(null);
    }

    public void display_body(StellarBody obj) {
        MyShape newShape = new MyShape(obj.Title, new Ellipse2D.Float( // Create a new shape, missing history and flags
                PREF_X / 2f + obj.Position.get(0) - obj.Radius.floatValue(),
                PREF_Y / 2f + obj.Position.get(1) - obj.Radius.floatValue(),
                obj.Radius.floatValue() * 2, obj.Radius.floatValue() * 2));
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

    }
    public void move(StellarBody obj, Double TimeScale) {
        for (MyShape shape: this.Shapes) {
            if (shape.Title.equals(obj.Title)) {
                if (!shape.isCollided) {
                    obj.move(TimeScale);
                    Double[] mv = obj.Movement.evaluate(TimeScale);
                    shape.translate(mv[0], mv[1]);
                }
            }
        }
    }
    public void collision(ArrayList<StellarBody> bodies, StellarBody Sun) { // Goes through `bodies` and checks each for a collision - Sun is passed due to not being in `bodies`
        ArrayList<String> Check = new ArrayList<>();
        for (MyShape obj : this.Shapes) {
            for (MyShape shape : this.Shapes) {
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
                            System.out.println(body.Title);
                            if (body.Title.equals(obj.Title)) {mainBody = body;}
                            else if (obj.Title.equals("Sun")) {mainBody = Sun;}
                            if (body.Title.equals(shape.Title)) {secondaryBody = body;}
                            else if (shape.Title.equals("Sun")) {secondaryBody = Sun;}
                        }
                        if (Objects.requireNonNull(mainBody).Mass > Objects.requireNonNull(secondaryBody).Mass) {
                            shape.isCollided = true;
                            mainBody.Mass += secondaryBody.Mass;
                            secondaryBody.Movement.setMagnitude(0.0);
                        }
                        else if (mainBody.Mass < secondaryBody.Mass) {
                            obj.isCollided = true;
                            secondaryBody.Mass += mainBody.Mass;
                            mainBody.Movement.setMagnitude(0.0);
                        }
                    }
                    Check.add(String.format("%s->%s", obj.Title, shape.Title));
                }
            }
        }
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        super.setBackground(Color.WHITE);
        this.g2 = (Graphics2D) g;
        this.g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (MyShape shape : Shapes) {
            shape.draw(this.g2, false);
        }
        if (this.SimComplete) {
            for (StellarBody body : this.Bodies) {
                this.drawHistory(this.g2, body);
            }
        }
    }
    @Override
    public Dimension getPreferredSize() {
        if (isPreferredSizeSet()) {return super.getPreferredSize();}
        return new Dimension(PREF_X, PREF_Y);
    }
    public void refresh() { // refreshes the canvas in real-time. god this took me ages to find the solution to
        EventQueue.invokeLater(this::repaint); // I have literally 0 clue what this syntax does in this context
    }   // IDEA just wouldn't shut up about it, so I listened
    public void drawHistory(Graphics2D g2, StellarBody obj) {
        for (MyShape shape : this.Shapes) {
            if (!obj.Title.equals("Sun") && !shape.Title.equals("Sun")) {
                if (shape.Title.equals(obj.Title)) {
                    Iterator<double[]> it = shape.PositionHistory.iterator();
                    double[] prevPosition = {0.0, 0.0};
                    double[] curPosition;
                    if (it.hasNext()) {
                        prevPosition = it.next();
                    }
                    int i = 0;
                    while (it.hasNext()) {
                        curPosition = it.next();

                        g2.setColor(Color.RED);
                        this.line(prevPosition, curPosition);
                        if (i%10 == 0) { // Only draws an arrow every 5 steps
                            Line change = new Line(curPosition, prevPosition); // Create direction arrows for history path
                            double leftWingAngle = change.Movement.coefficient() + 30;
                            double rightWingAngle = change.Movement.coefficient() - 30;
                            double leftXLength = 10 * Math.cos(Math.toRadians(leftWingAngle));
                            double leftYLength = 10 * Math.sin(Math.toRadians(leftWingAngle));
                            double rightXLength = 10 * Math.cos(Math.toRadians(rightWingAngle));
                            double rightYLength = 10 * Math.sin(Math.toRadians(rightWingAngle));
                            this.line(curPosition, new double[]{
                                    curPosition[0] + leftXLength, curPosition[1] + leftYLength});
                            this.line(curPosition, new double[]{
                                    curPosition[0] + rightXLength, curPosition[1] + rightYLength});
                        }
                        i++;
                        prevPosition = curPosition;
                    }
                }
            }
            shape.draw(g2, true);
        }
    }
    public void line(double[] Start, double[] End) {
        this.g2.drawLine((int)Start[0],(int)Start[1], (int)End[0],(int)End[1]);
    }
}

class MyShape {
    private Path2D path = new Path2D.Double();
    public Shape Shape;
    public String Title;
    public double PosX;
    public double PosY;
    public boolean isCollided = false;
    public ArrayList<double[]> PositionHistory = new ArrayList<>();

    public MyShape(String name, Shape shape){
        this.Title = name;
        path.append(shape, true);
        this.Shape = shape;
        this.PosX = shape.getBounds().getMinX();
        this.PosY = shape.getBounds().getMinY();
        this.PositionHistory.add(new double[]
                {this.PosX+this.Shape.getBounds().width/2.0, this.PosY+this.Shape.getBounds().width/2.0});
    }
    public void translate(Double deltaX, Double deltaY) {
        path.transform(AffineTransform.getTranslateInstance(deltaX, deltaY));
        this.PosX += deltaX;
        this.PosY += deltaY;
        this.PositionHistory.add(new double[]
                {this.PosX+this.Shape.getBounds().width/2.0, this.PosY+this.Shape.getBounds().width/2.0});
    }
    public void draw(Graphics2D g2, boolean isFinal) {
        if (!isFinal) {
            if (!this.isCollided) {
                g2.setColor(Color.BLACK);
                g2.draw(path);
                this.fill(g2);
            }
            if (this.isCollided) {
                g2.setColor(Color.BLUE);
                g2.draw(path);
            }
        }
        else {
            if (this.isCollided) {
                g2.setColor(Color.BLUE);
                g2.draw(path);
            }
        }
    }
    public void fill(Graphics2D g2) {
        if (!this.isCollided) {
            g2.setColor(Color.DARK_GRAY);
            g2.fill(path);
        }
    }
}