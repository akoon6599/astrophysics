package display;

import physics.StellarBody;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;

public class Screen extends JPanel {
    private static final int PREF_X = 600;
    private static final int PREF_Y = 600;
    ArrayList<Integer> center;
    private ArrayList<MyShape> shapes = new ArrayList<>();
    private final JPanel frame;

    public Screen() {
        this.frame = new JPanel();
        this.frame.setLayout(null);
        this.center = new ArrayList<>();
        this.center.add(PREF_X/2);
        this.center.add(PREF_Y/2);
    }

    public void display_body(StellarBody obj) {
        shapes.add(new MyShape(new Ellipse2D.Float(this.center.get(0)-obj.Position.get(0)-obj.Radius.floatValue(),
                this.center.get(1)-obj.Position.get(1)-obj.Radius.floatValue(), obj.Radius.floatValue()*2, obj.Radius.floatValue()*2)));
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (MyShape shape : shapes) {
            shape.draw(g2);
        }
    }
    @Override
    public Dimension getPreferredSize() {
        if (isPreferredSizeSet()) {
            return super.getPreferredSize();
        }
        return new Dimension(PREF_X, PREF_Y);
    }
}

class MyShape {
    private Path2D path = new Path2D.Double();

    public MyShape(Shape shape){path.append(shape, true);}
    public void translate(int deltaX, int deltaY) {
        path.transform(AffineTransform.getTranslateInstance(deltaX, deltaY));
    }
    public void draw(Graphics2D g2) {g2.draw(path);}
}