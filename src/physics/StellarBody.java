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
        double angleDif = gDirection.Movement.coefficient() - obj.Movement.coefficient();
        double angleEffect = 0.00;
        Double[] magnitude;
        double nxMagnitude = 0;
        double nyMagnitude = 0;
        double nMagnitude = 0;
        double newAngle = 0;
        if (Math.abs(angleDif) < 360 && Math.abs(angleDif) > 180) { // split it based on the hemisphere for Y
            angleEffect = -Math.toRadians(360.0-angleDif);
            double gxMagnitude = (gMagnitude * Math.cos(angleEffect)) * TimeScale;
            double gyMagnitude = (gMagnitude * Math.sin(angleEffect)) * TimeScale;
            // Subtract or add X based on the quadrant
            if (-180 < angleDif && angleDif < -90) {
                magnitude = obj.Movement.evaluate(TimeScale);
                nxMagnitude = magnitude[0] - gxMagnitude;
                nyMagnitude = magnitude[1] - gyMagnitude;
                nMagnitude = Math.sqrt(Math.pow(nxMagnitude, 2) + Math.pow(nyMagnitude, 2));
                newAngle = obj.Movement.coefficient() - Math.toDegrees(Math.atan(nyMagnitude / nxMagnitude));
            }
            else if (-90 <= angleDif && angleDif < 0) {
                magnitude = obj.Movement.evaluate(TimeScale);
                nxMagnitude = magnitude[0] + gxMagnitude;
                nyMagnitude = magnitude[1] - gyMagnitude;
                nMagnitude = Math.sqrt(Math.pow(nxMagnitude, 2) + Math.pow(nyMagnitude, 2));
                newAngle = obj.Movement.coefficient() - Math.toDegrees(Math.atan(nyMagnitude / nxMagnitude));
            }
        }
        else if (Math.abs(angleDif) > 0 && Math.abs(angleDif) < 180) {
            angleEffect = Math.toRadians(angleDif);
            double gxMagnitude = (gMagnitude * Math.cos(angleEffect)) * TimeScale;
            double gyMagnitude = (gMagnitude * Math.sin(angleEffect)) * TimeScale;
            // Subtract or add X based on the Quadrant
            if (0 <= Math.abs(angleDif) && Math.abs(angleDif) < 90) {
                magnitude = obj.Movement.evaluate(TimeScale);
                nxMagnitude = magnitude[0] + gxMagnitude;
                nyMagnitude = magnitude[1] + gyMagnitude;
                nMagnitude = Math.sqrt(Math.pow(nxMagnitude, 2) + Math.pow(nyMagnitude, 2));
                newAngle = obj.Movement.coefficient() + Math.toDegrees(Math.atan(nyMagnitude / nxMagnitude));
            }
            else if (90 <= Math.abs(angleDif) && Math.abs(angleDif) < 180) {
                magnitude = obj.Movement.evaluate(TimeScale);
                nxMagnitude = magnitude[0] - gxMagnitude;
                nyMagnitude = magnitude[1] + gyMagnitude;
                nMagnitude = Math.sqrt(Math.pow(nxMagnitude, 2) + Math.pow(nyMagnitude, 2));
                newAngle = obj.Movement.coefficient() + Math.toDegrees(Math.atan(nyMagnitude / nxMagnitude));
            }
        }
        else if (Math.abs(angleDif) == 180) { // this could cause big problems if the nMagnitude goes negative
            nMagnitude = obj.Movement.Magnitude - gMagnitude;
            newAngle = obj.Movement.coefficient();
        }
        else if (Math.abs(angleDif) == 0 || Math.abs(angleDif) == 360) {
            nMagnitude = obj.Movement.Magnitude + gMagnitude;
            newAngle = obj.Movement.coefficient();
        }

        System.out.println("b");
        System.out.println(newAngle);
        System.out.printf("%f, %f%n", obj.Movement.Magnitude, gMagnitude);
        obj.Movement = new Movement(String.format("%.2fd", newAngle), nMagnitude);

    }
}


