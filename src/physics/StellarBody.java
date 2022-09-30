package physics;

import java.util.ArrayList;
import java.util.Arrays;

public class StellarBody extends Object{
    static final Double GravConstant = 6.674*Math.pow(10, -11);
    public String Classification;
    public Integer Radius;

    // aila sucks


    public StellarBody(Float posx, Float posy, String ttl, String cls, Double mass, Integer radius, String angle, Double mag) {
        super(angle, mag);
        this.Classification = cls;
        ArrayList<Float> pos = new ArrayList<>();
        pos.add(posx);
        pos.add(posy);
        this.Position = pos;
        this.Title = ttl;
        this.Mass = mass;
        this.Radius = radius;
    }

    public void effect_movement(StellarBody obj, Double TimeScale, Double DistanceScale) {
        Line gDirection = new Line(this.Position, obj.Position);
        Double radius = gDirection.Distance;
        double gMagnitude = GravConstant * ((this.Mass * obj.Mass)/(Math.pow(radius, 2)*DistanceScale));
        double gAngle = 360 - gDirection.Movement.coefficient();
        System.out.printf("%n%nSHAPE: %s%n", obj.Title);
        System.out.println("Gravity: "+gMagnitude);
        // --- Find Angle Between
        System.out.printf("Angle of Gravity: %s %s%n", gAngle, obj.Movement.coefficient());
        double angleDif = Math.abs(gAngle - obj.Movement.coefficient()) + 180;
        System.out.println("Angle Difference: "+angleDif);

        // --- Do Angle Math
        double angleEffect;
        angleEffect = Math.toRadians(angleDif);
        double gxMagnitude = (gMagnitude * Math.sin(angleEffect)) * TimeScale;
        double gyMagnitude = (gMagnitude * Math.cos(angleEffect)) * TimeScale;
        Double[] magnitude = obj.Movement.evaluate(TimeScale);
        double nxMagnitude = magnitude[0] + gxMagnitude;
        double nyMagnitude = magnitude[1] + gyMagnitude;
        double nMagnitude = Math.sqrt(Math.pow(nxMagnitude, 2) + Math.pow(nyMagnitude, 2));
        double newAngle = (obj.Movement.coefficient() - (Math.toDegrees(Math.atan(nxMagnitude / nyMagnitude)))); //this makes no sense, why is x over y
        double mg = obj.Movement.Magnitude;
        System.out.printf("Current Position: %s%n", obj.Position.toString());
        System.out.printf("Old Magnitude: %s & %.2f  -  New Magnitude: [%s,%s] & %.2f%n", Arrays.toString(magnitude), mg, nxMagnitude, nyMagnitude, nMagnitude);
        System.out.printf("Does this make sense? X: %.2f & %.2f = %.2f %n", magnitude[0], gxMagnitude, nxMagnitude);
        System.out.printf("Does this make sense? Y: %.2f & %.2f = %.2f %n", magnitude[1], gyMagnitude, nyMagnitude);
        System.out.printf("Old Angle: %s - Alleged Change: %s - New Angle: %s%n", obj.Movement.coefficient(), Math.toDegrees(Math.atan(nxMagnitude / nyMagnitude)), newAngle);


//        if (gAngle < 360 && gAngle > 180) { // split it based on the hemisphere for Y
//            System.out.println("Northern Hemisphere");
//            // Subtract or add X based on the quadrant
//            if (270 < gAngle) {
//                System.out.println("Positive Quadrant");
//                nMagnitude = Math.sqrt(Math.pow(nxMagnitude, 2) + Math.pow(nyMagnitude, 2));
////                System.out.printf("Old Magnitude: %s & %.2f  -  New Magnitude: [%s,%s] & %.2f%n", Arrays.toString(magnitude), mg, nxMagnitude, nyMagnitude, nMagnitude);
////                System.out.printf("Does this make sense? X: %.2f & %.2f = %.2f %n", magnitude[0], gxMagnitude, nxMagnitude);
////                System.out.printf("Does this make sense? Y: %.2f & %.2f = %.2f %n", magnitude[1], gyMagnitude, nyMagnitude);
////                System.out.printf("Old Angle: %s - Alleged Change: %s - New Angle: %s%n", obj.Movement.coefficient(), Math.toDegrees(Math.atan(nyMagnitude / nxMagnitude)), newAngle);
//            }
//            else if (270 > gAngle) {
//                System.out.println("Negative Quadrant");
//                nMagnitude = Math.sqrt(Math.pow(nxMagnitude, 2) + Math.pow(nyMagnitude, 2));
//                newAngle = obj.Movement.coefficient() - Math.toDegrees(Math.atan(nyMagnitude / nxMagnitude));
//                }
//            else {
//                // do if angledif == 270, straight up
//            }
//        }
//        else if (0 > gAngle) {
//            System.out.println("Southern Hemisphere");
//            // Subtract or add X based on the Quadrant
//            if (0 <= Math.abs(angleDif) && Math.abs(angleDif) < 90) {
//                System.out.println("Positive Quadrant");
//                nMagnitude = Math.sqrt(Math.pow(nxMagnitude, 2) + Math.pow(nyMagnitude, 2));
//                newAngle = obj.Movement.coefficient() + Math.toDegrees(Math.atan(nyMagnitude / nxMagnitude));
//            }
//            else if (90 <= Math.abs(angleDif) && Math.abs(angleDif) < 180) {
//                System.out.println("Negative Quadrant");
////                System.out.printf("Old Magnitude: %s & %.2f  -  New Magnitude: [%s,%s] & %.2f%n", Arrays.toString(magnitude), mg, nxMagnitude, nyMagnitude, nMagnitude);
////                System.out.printf("Does this make sense? X: %.2f & %.2f = %.2f %n", magnitude[0], gxMagnitude, nxMagnitude);
////                System.out.printf("Does this make sense? Y: %.2f & %.2f = %.2f %n", magnitude[1], gyMagnitude, nyMagnitude);
//                newAngle = obj.Movement.coefficient() + (Math.toDegrees(Math.atan(nyMagnitude / nxMagnitude)));
////                System.out.printf("Old Angle: %s - Alleged Change: %s - New Angle: %s%n", obj.Movement.coefficient(), Math.toDegrees(Math.atan(nyMagnitude / nxMagnitude)), newAngle);
//            }
//        }
//        else if (gAngle == 180) { // this could cause big problems if the nMagnitude goes negative
//            nMagnitude = obj.Movement.Magnitude - gMagnitude;
//            newAngle = obj.Movement.coefficient();
//        }
//        else if (gAngle == 0 || gAngle == 360) {
//            nMagnitude = obj.Movement.Magnitude + gMagnitude;
//            newAngle = obj.Movement.coefficient();
//        }
//        else if (gAngle == 90) {
//            System.out.println("Direct Left");
//            magnitude = obj.Movement.evaluate(TimeScale);
//            nxMagnitude = magnitude[0] + gxMagnitude;
//            nyMagnitude = magnitude[1];
//
//        }

        obj.Movement = new Movement(String.format("%.2fd", newAngle), nMagnitude);

    }
}


