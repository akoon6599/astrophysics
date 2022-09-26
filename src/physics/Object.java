package physics;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

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

    public void move(double time_step) {
        System.out.println(this.Position);
        this.Position.set(0, this.Position.get(0) + Movement.EquationX.evaluate(time_step).floatValue());
        this.Position.set(1, this.Position.get(1) + Movement.EquationY.evaluate(time_step).floatValue());
        System.out.println(this.Position);
    }
}

class Movement {
    Formula EquationX;
    Formula EquationY;

    public Movement(Formula mv) {
        Matcher matcher = Pattern.compile("(-?[0-9]+\\.[0-9]{2}([xy])) [+-] ([0-9]+\\.[0-9]{2}([xy]))").matcher(mv.getEquation());
        if (matcher.matches()) {
            this.EquationX = new Formula(matcher.group(2).equals("x")?matcher.group(1):matcher.group(3));
            this.EquationY = new Formula(matcher.group(2).equals("y")?matcher.group(1).replace('y','x'):matcher.group(3).replace('y','x'));
        }
        else {
            this.EquationX = new Formula("0.00x");
            this.EquationY = new Formula("0.00x");
        }

    }
}