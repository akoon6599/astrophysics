package physics;

import java.util.ArrayList;
import java.util.Arrays;

public class StellarBody extends Object{
    static final Double GravConstant = 6.674*Math.pow(10, -11);
    public String Classification;
    public Double Gravity;
    public Integer Radius;

    // aila sucks


    public StellarBody(Float posx, Float posy, String ttl, String cls, Double mass, Double gravity, Integer radius, String angle, Double mag) {
        super(angle, mag);
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
        Double magnitude = 1.00;

        // --- Find Angle Between
        double angleDif = obj.Movement.coefficient()-direct.Movement.coefficient();
        double angleEffect = 0.00;
        if (Math.abs(angleDif) > 180) {angleEffect = 360.0-angleDif;}
        else if (Math.abs(angleDif) <= 180) {angleEffect = angleDif;}



        obj.Movement = new Movement(new Formula(String.format("%.2fx + %.2fy", movx, movy)), magnitude);
    }
}


