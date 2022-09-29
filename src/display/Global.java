package display;

import physics.StellarBody;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Objects;

public class Global extends JPanel {
    protected static final int PREF_X = 600;
    protected static final int PREF_Y = 600;
    private ArrayList<MyShape> shapes = new ArrayList<>();
    public final JPanel Frame;
    public ArrayList<GeneralPath> paths = new ArrayList<>();


    public Global() {
        this.Frame = new JPanel();
        this.Frame.setLayout(null);
    }

    public void display_body(StellarBody obj) {
        shapes.add(new MyShape(obj.Title, new Ellipse2D.Float(
                PREF_X / 2 + obj.Position.get(0) - obj.Radius.floatValue(),
                PREF_Y / 2 + obj.Position.get(1) - obj.Radius.floatValue(),
                obj.Radius.floatValue() * 2, obj.Radius.floatValue() * 2)));
    }
    public void move(StellarBody obj, Double TimeScale) {
        obj.move(TimeScale);
        for (MyShape shape: this.shapes) {
            if (shape.Title.equals(obj.Title)) {
                Double[] mv = obj.Movement.evaluate(TimeScale);
                shape.translate(mv[0], mv[1]);
                this.collision(shape);
            }
        }
    }
    public void collision(MyShape obj) {
        for (MyShape shape: this.shapes) {
            if (!Objects.equals(shape.Title, obj.Title)) {
                AffineTransform at = new AffineTransform();

                GeneralPath pathObj = new GeneralPath();
                pathObj.append(obj.Shape.getPathIterator(at), false);
                double x = (obj.PosX - pathObj.getBounds().getMinX());
                double y = (obj.PosY - pathObj.getBounds().getMinY());
                at.translate(x, y);
                pathObj.reset();
                pathObj.append(obj.Shape.getPathIterator(at), false);
                this.paths.add(pathObj);

                at = new AffineTransform();
                GeneralPath pathShape = new GeneralPath();
                x = (shape.PosX - pathShape.getBounds().getMinX());
                y = (shape.PosY - pathShape.getBounds().getMinY());
                pathShape.reset();
                pathShape.append(shape.Shape.getPathIterator(at), true);
                this.paths.add(pathShape);

                Area a1 = new Area(pathObj);
                Area a2 = new Area(pathShape);
                System.out.printf("%s, %s%n", obj.Title, a1.getBounds());
                System.out.printf("%s, %s%n", shape.Title, a2.getBounds());
                a2.intersect(a1);
                if (!a2.isEmpty()) {
                    System.out.println("ASOTBF");
                }
            }
        }
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (MyShape shape : shapes) {
            shape.draw(g2);
        }
        for (GeneralPath path : this.paths) {
//            g2.fill(path);
            g2.draw(path.getBounds());
        }
    }
    @Override
    public Dimension getPreferredSize() {
        if (isPreferredSizeSet()) {return super.getPreferredSize();}
        return new Dimension(PREF_X, PREF_Y);
    }
}

class MyShape {
    private Path2D path = new Path2D.Double();
    public Shape Shape;
    public String Title;
    public double PosX;
    public double PosY;

    public MyShape(String name, Shape shape){
        this.Title = name;
        path.append(shape, true);
        this.Shape = shape;
        this.PosX = shape.getBounds().getMinX();
        this.PosY = shape.getBounds().getMinY();
    }
    public void translate(Double deltaX, Double deltaY) {
        path.transform(AffineTransform.getTranslateInstance(deltaX, deltaY));
        this.PosX += deltaX;
        this.PosY += deltaY;
    }
    public void draw(Graphics2D g2) {g2.draw(path);}
}