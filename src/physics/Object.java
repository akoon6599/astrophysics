package physics;

import java.util.ArrayList;

// IMPORTANT: LIMITED TO 2D SPACE
public class Object {
    public String Title;
    public Double Mass;
    public ArrayList<Float> Position;
    public Movement Movement;

    public Object(String angle, Double mag) {
        this.Movement = new Movement(angle, mag);

    }

    public void move(double time_step) {
        Double[] mv = this.Movement.evaluate(time_step);
        this.Position.set(0, this.Position.get(0) + mv[0].floatValue());
        this.Position.set(1, this.Position.get(1) + mv[1].floatValue());
    }
}

class Line {
    ArrayList<Float> Start;
    ArrayList<Float> End;
    Movement Movement;
    Float Distance;

    public Line(ArrayList<Float> pos1, ArrayList<Float> pos2) {
        this.Start = pos1;
        this.End = pos2;
        Float x1 = this.Start.get(0);
        Float y1 = this.Start.get(1);
        Float x2 = this.End.get(0);
        Float y2 = this.End.get(1);

        this.Distance = ((Double)Math.sqrt(Math.pow((x2-x1),2) + Math.pow((y2-y1),2))).floatValue(); // measure vector directly between points
        Double angle = Math.toDegrees(Math.acos((x1-x2) / (this.Distance)));
        this.Movement = new Movement(String.format("%.2fd", angle), 1.00);
    }
}

