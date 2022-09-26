package physics;

import java.text.Normalizer;
import java.util.ArrayList;

// IMPORTANT: LIMITED TO 2D SPACE
public class Object {
    String Title;
    Double Mass;
    ArrayList<Float> Position;
    Integer Size;
    Movement Movement;

    public Object(Formula init_mv) {
        this.Movement = new Movement(init_mv);
    }

    public void move() {
        System.out.println(Movement.Equation.getEquation());
        System.out.println(Movement.Tangent.getEquation());
    }
}

class Movement {
    Formula Equation;
    Formula Tangent;

    public Movement(Formula mv) {
        this.Equation = mv;
//        new Formula(mv.find_derivative()).evaluate(0.00);
        this.Tangent = find_tangent(mv, 0.0);
    }
    protected Formula find_tangent(Formula mv, Double aval) {
        return new Formula(String.format("%.2f + %.2f(x%s)", mv.evaluate(aval), new Formula(mv.find_derivative()).evaluate(aval), aval!=0.0?String.format(" - %.2f",aval):""));
    }
}