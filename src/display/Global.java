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
                PREF_X / 2f + obj.Position.get(0) - obj.Radius.floatValue(),
                PREF_Y / 2f + obj.Position.get(1) - obj.Radius.floatValue(),
                obj.Radius.floatValue() * 2, obj.Radius.floatValue() * 2)));
    }
    public void move(StellarBody obj, Double TimeScale) {
        obj.move(TimeScale);
        for (MyShape shape: this.shapes) {
            if (shape.Title.equals(obj.Title)) {
                Double[] mv = obj.Movement.evaluate(TimeScale);
                ArrayList<Float> pos = new ArrayList<>();
                pos.add(mv[0].floatValue());
                pos.add(mv[1].floatValue());
                obj.Position = pos;
                shape.translate(mv[0], mv[1]);
            }
        }
    }
    public void collision(ArrayList<StellarBody> bodies, StellarBody Sun) {
        ArrayList<String> Check = new ArrayList<>();
        for (MyShape obj : this.shapes) {
            for (MyShape shape : this.shapes) {
                if (!Objects.equals(shape.Title, obj.Title) &&
                        !(Check.contains(String.format("%s->%s", shape.Title, obj.Title))) ||
                        Check.contains(String.format("%s->%s", obj.Title, shape.Title))) {
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
                    if (shape.Title.equals("Sun")) {this.paths.add(pathShape);}
                    this.paths.add(pathObj);
                    if (!a2.isEmpty()) {
                        StellarBody mainBody = null;
                        StellarBody secondaryBody = null;
                        for (StellarBody body : bodies) {
                            if (body.Title.equals(obj.Title)) {mainBody = body;}
                            if (body.Title.equals(shape.Title)) {secondaryBody = body;}
                            else if (shape.Title.equals("Sun")) {secondaryBody = Sun;}
                        }
                        if (Objects.requireNonNull(mainBody).Mass > Objects.requireNonNull(secondaryBody).Mass) {
                            shape.Drawn = false;
                            mainBody.Mass += secondaryBody.Mass;
                        }
                        else if (mainBody.Mass < secondaryBody.Mass) {
                            obj.Drawn = false;
                            secondaryBody.Mass += mainBody.Mass;
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
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (MyShape shape : shapes) {
            if (shape.Drawn) {
                shape.draw(g2);
            }
        }
    }
    @Override
    public Dimension getPreferredSize() {
        if (isPreferredSizeSet()) {return super.getPreferredSize();}
        return new Dimension(PREF_X, PREF_Y);
    }

    public void refresh() { // refreshes the canvas in real-time. god this took me ages to find the solution to
        EventQueue.invokeLater(this::repaint); // I have literally 0 clue what this syntax is
    }   // IDEA just wouldnt shut up about it so I listened
}

class MyShape {
    private Path2D path = new Path2D.Double();
    public Shape Shape;
    public String Title;
    public double PosX;
    public double PosY;
    public boolean Drawn = true;

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