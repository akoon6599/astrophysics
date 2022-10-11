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

