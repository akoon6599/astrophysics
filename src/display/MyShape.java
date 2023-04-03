package display;

import physics.Line;
import physics.StellarBody;
import physics.system;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;

public class MyShape {
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

    public MyShape(String name, Shape shape, Color color) {
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
                {PosX + Shape.getBounds().width / 2.0, PosY + Shape.getBounds().width / 2.0, 0.0});
    }

    @Override
    public MyShape clone() {
        MyShape rms = new MyShape(Title, Shape, COLOR);
        rms.path = path;
        rms.PosX = PosX;
        rms.PosY = PosY;
        rms.initialPosX = initialPosX;
        rms.initialPosY = initialPosY;
        rms.initialPath = initialPath;
        rms.isCollided = isCollided;
        rms.PositionHistory = new ArrayList<>(PositionHistory);
        return rms;
    }

    public void translate(Double deltaX, Double deltaY) {
        path.transform(AffineTransform.getTranslateInstance(deltaX, deltaY));
        PosX += deltaX;
        PosY += deltaY;
        PositionHistory.add(new Double[]
                {PosX + Shape.getBounds().width / 2.0, PosY + Shape.getBounds().width / 2.0, 0.0});
    }

    public void draw(Graphics2D g2, boolean isFinal) {
        if (!isFinal) {
            if (!isCollided) {
                g2.setColor(Color.BLACK);
                g2.draw(path);
                this.fill(g2);
            }
            else {
                g2.setColor(Color.BLUE);
                g2.draw(path);
            }
        } else {
            if (isCollided) {
                g2.setColor(Color.BLUE);
                g2.draw(path);
            }
            else {
                g2.setColor(COLOR);
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