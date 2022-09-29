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
        Line gDirection = new Line(this.Position, obj.Position);
        Float radius = gDirection.Distance;
        double gMagnitude = GravConstant * ((this.Mass * obj.Mass)/Math.pow(radius, 2));

        // --- Find Angle Between
        double angleDif = obj.Movement.coefficient()-gDirection.Movement.coefficient();
        double angleEffect = 0.00;
        if (Math.abs(angleDif) > 180) {angleEffect = Math.toRadians(360.0-angleDif);}
        else if (Math.abs(angleDif) <= 180) {angleEffect = Math.toRadians(angleDif);}

        Double[] magnitude = obj.Movement.evaluate(TimeScale);
        double nxMagnitude = magnitude[0] * (gMagnitude*Math.cos(angleEffect));
        double nyMagnitude = magnitude[1] * (gMagnitude*Math.sin(angleEffect));
        double nMagnitude = Math.sqrt(Math.pow(nxMagnitude,2) + Math.pow(nyMagnitude,2));
        double newAngle = Math.atan(nyMagnitude / nxMagnitude);  // TODO: THIS IS BREAKING MY BRAIN, ask mcvay
        System.out.println("b");
        System.out.println(newAngle);
        System.out.printf("%f, %f%n", obj.Movement.Magnitude, nMagnitude);
        obj.Movement = new Movement(String.format("%.2fd", newAngle), nMagnitude);

    }
}


