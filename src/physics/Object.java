package physics;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.util.ArrayList;

// IMPORTANT: LIMITED TO 2D SPACE
public class Object {
    String Title;
    Double Mass;
    ArrayList<Float> Position;
    Movement Movement;

    public Object(Formula init_mv) {
        this.Movement = new Movement(init_mv);

    }

    public void move(double time_step) {
        this.Position.set(0, this.Position.get(0) + Movement.EquationX.evaluate(time_step).floatValue());
        this.Position.set(1, this.Position.get(1) + Movement.EquationY.evaluate(time_step).floatValue());
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

        float xcoeff = ((Double)Math.sqrt(Math.pow(x1, 2) + Math.pow(x2, 2))).floatValue();
        float ycoeff = ((Double)Math.sqrt(Math.pow(y1, 2) + Math.pow(y2, 2))).floatValue();
        StringBuilder st = new StringBuilder("%.2fx + %.2fy");

        // 0 Origin y failsafe
        if (y1.compareTo(0f)==0) { // If y1=0, y2=neg
            if (y2.compareTo(0f)<0) {st.replace(6, 7, "-");}
        }
        else if (y2.compareTo(0f)==0) { // If y1=neg, y2=0
            if (y2.compareTo(0f)<0) {st.replace(6, 7, "-");}
        }
        else {
            if ((y1.compareTo(0f)<0 && y2.compareTo(0f)>0) || (y1.compareTo(0f)>0 && y2.compareTo(0f)<0)) {st.replace(6, 7, "-");}
        }

        // 0 Origin x failsafe
        if (x1.compareTo(0f)==0) {
            if (x2.compareTo(0f)<0) {st.insert(0, '-');} // If x1=0, x2=neg
        }
        else if (x2.compareTo(0f)==0) {
            if (x1.compareTo(0f)<0) {st.insert(0, '-');} // if x1=neg, x2=0
        }
        else { // if x1,x2!=0
            if ((x1.compareTo(0f)<0 && x2.compareTo(0f)>0) || (x1.compareTo(0f)>0 && x2.compareTo(0f)<0)) {st.insert(0, '-');}
        }

        this.Movement = new Movement(new Formula(String.format(st.toString(), xcoeff, ycoeff)));
        this.Distance = ((Double)Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1))).floatValue(); // measure vector directly between points
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
            this.EquationY = new Formula("0.00y");
        }

    }
}