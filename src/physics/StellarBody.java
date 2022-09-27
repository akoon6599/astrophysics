package physics;

import java.util.ArrayList;

public class StellarBody extends Object{
    static final Double GravConstant = 6.674*Math.pow(10, -11);
    String Classification;
    Double Gravity;
    Integer Radius;


    public StellarBody(Float posx, Float posy, String ttl, String cls, Double mass, Double gravity, Integer radius, Formula init_mv) {
        super(new Formula(init_mv.getEquation()));
        this.Classification = cls;
        ArrayList<Float> pos = new ArrayList<>();
        pos.add(posx);
        pos.add(posy);
        this.Position = pos;
        this.Title = ttl;
        this.Mass = mass;
        this.Gravity = gravity;
        this.Radius = radius;
    }

    public void effect_movement(StellarBody obj, Double TimeScale) {
        Line direct = new Line(this.Position, obj.Position);
        Float dis = direct.Distance;
        double g = GravConstant * ((this.Mass * obj.Mass)/Math.pow(dis, 2));

        // --- Vector Math Time
        // Find Angle
        double movx = Double.parseDouble(obj.Movement.EquationX.getEquation().replace("x", ""));
        double movy = Double.parseDouble(obj.Movement.EquationY.getEquation().replace("x", ""));
        double gravx = Double.parseDouble(direct.Movement.EquationX.getEquation().replace("x", ""));
        double gravy = Double.parseDouble(direct.Movement.EquationY.getEquation().replace("x", ""));
        double dotmult = movx*gravx + movy+gravy;
        double absx = Math.sqrt(Math.pow(movx, 2) + Math.pow(gravx, 2));
        double absy = Math.sqrt(Math.pow(movy, 2) + Math.pow(gravy, 2));
        double angle = Math.acos(dotmult / (absx*absy));
        // Apply axis acceleration
        movx += g*Math.sin(angle) * TimeScale;
        movy += g*Math.sin(angle) * TimeScale;

        obj.Movement = new Movement(new Formula(String.format("%.2fx + %.2fy", movx, movy)));
    }
}


