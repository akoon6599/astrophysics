package physics;

import display.MyShape;

import java.awt.Color;
import java.util.ArrayList;

public class StellarBody extends Object{
    static final Double GravConstant = 6.674*Math.pow(10, -11);
    public String Classification;
    public Integer Radius;
    public Color COLOR;
    public boolean STATIC;
    public boolean ORBITER;
    public boolean TRACKVEL;
    public StellarBody orbitingPoint = null;
    public ArrayList<StellarBody> MOONS = new ArrayList<>();
    public MyShape myShape = null;




    public StellarBody(Float posx, Float posy, String ttl, String cls, Double mass, Integer radius, String angle, Double mag, Color color, boolean Static, boolean track) {
        super(angle, mag);
        Classification = cls;
        Position.add(posx);
        Position.add(posy);
        InitialPosition.add(posx);
        InitialPosition.add(posy);
        Title = ttl;
        Mass = mass;
        InitialMass = mass;
        Radius = radius;
        COLOR = color;
        STATIC = Static;
        TRACKVEL = track;
        ORBITER = false;
    }
    public StellarBody(Float posx, Float posy, String ttl, String cls, Double mass, Integer radius, String angle, Double mag, Color color, boolean Static, boolean track, StellarBody orbitingPoint) {
        super(angle, mag);
        Classification = cls;
        Position.add(posx);
        Position.add(posy);
        InitialPosition.add(posx);
        InitialPosition.add(posy);
        Title = ttl;
        Mass = mass;
        InitialMass = mass;
        Radius = radius;
        COLOR = color;
        STATIC = Static;
        TRACKVEL = track;

        ORBITER = true;
        this.orbitingPoint = orbitingPoint;
        orbitingPoint.MOONS.add(this);
    }
    @Override
    public StellarBody clone() {
        StellarBody rB;
        if (!ORBITER) {
            rB = new StellarBody(getInitialPosition()[0], getInitialPosition()[1], Title,
                    Classification, getInitialMass(), Radius, getInitialMovement().getAngle(), getInitialMovement().getMagnitude(),
                    COLOR, STATIC, TRACKVEL);
        }
        else {
            rB = new StellarBody(getInitialPosition()[0], getInitialPosition()[1], Title,
                    Classification, getInitialMass(), Radius, getInitialMovement().getAngle(), getInitialMovement().getMagnitude(),
                    COLOR, STATIC, TRACKVEL, orbitingPoint);
        }
        rB.myShape = myShape;
        return rB;
    }

    public void effect_movement(StellarBody obj, Double TimeScale, Double DistanceScale) { // Called by main body, passes orbiter
        if (!obj.STATIC) {
            // Find a line connecting this and the other StellarBody
            Line gDirection = new Line(this, obj);

            // Calculate gravitational force and magnitude, multiplied by Time Dilation value
            double gMagnitude = (GravConstant * (this.Mass * obj.Mass)) / (Math.pow(gDirection.Distance, 2)*DistanceScale) * TimeScale;
            double gAccel = gMagnitude/obj.Mass;

            // Split that into X and Y components based on the angle of the connecting line
            double gxMagnitude = gAccel * Math.cos(Math.toRadians(gDirection.Movement.coefficient()));
            double gyMagnitude = gAccel * Math.sin(Math.toRadians(gDirection.Movement.coefficient()));

            // Get current X and Y velocity components
            double oxMagnitude = obj.Movement.xMove;
            double oyMagnitude = obj.Movement.yMove;

            // Add the acceleration to the original velocity and find new angle of Movement
            double nxMagnitude = oxMagnitude + gxMagnitude;
            double nyMagnitude = oyMagnitude + gyMagnitude;
            double newAngle = Math.toDegrees(Math.atan2(nyMagnitude, nxMagnitude));

            // Determine the total magnitude of new Movement
            Double nMagnitudePrimary = nyMagnitude / Math.sin(Math.toRadians(newAngle)); // Redundant creations for nMagnitude
            Double nMagnitudeSecondary = nxMagnitude / Math.cos(Math.toRadians(newAngle)); // Just because I can and also for accuracy
            Double nMagnitudeTertiary = Math.sqrt(Math.pow(nxMagnitude, 2) + Math.pow(nyMagnitude, 2));

            // Check if redundant calculations are within .01% margin of error
            // Declare an error if calculations do not match, but do not throw an Exception in order to continue simulation
            double nMagnitude;
            if (Math.abs(nMagnitudePrimary - nMagnitudeSecondary) < nMagnitudePrimary * (1e-4)) {
                nMagnitude = nMagnitudePrimary;
            } else if (Math.abs(nMagnitudePrimary - nMagnitudeTertiary) < nMagnitudePrimary * (1e-4)) {
                nMagnitude = nMagnitudeSecondary;
                System.out.printf("MAGNITUDE ERROR LV1 BODY %s %s:%s:%s%n", obj.Title, nMagnitudePrimary, nMagnitudeSecondary, nMagnitudeTertiary);
            } else {
                nMagnitude = nMagnitudeTertiary;
                System.out.printf("MAGNITUDE ERROR LV2 BODY %s %s:%s:%s%n", obj.Title, nMagnitudePrimary, nMagnitudeSecondary, nMagnitudeTertiary);
            }

            // Set other StellarBody Movement to the new values
            obj.Movement = new Movement(String.format("%.2fd", newAngle), nMagnitude);
        }
    }

    public void find_orbit(StellarBody obj, Double DistanceScale) { // Called by main body, passes orbiter - requires a relative 90-degree angle between orbiter motion and main body
        // Find line connecting two StellarBodies
        Line Tether = new Line(this, obj);

        // Find force of gravity and rotational velocity necessary to form a circular orbit
        double gMagnitude = (GravConstant * this.Mass) / (Tether.Distance*DistanceScale);
        double rV = Math.sqrt(gMagnitude);

        // Set orbiting StellarBody velocity to required velocity
        obj.Movement.setMagnitude(rV);
    }

//    public void orbitPoint(StellarBody obj, double[] Point) {
//        StellarBody fakeBody = new StellarBody(Point[0], Point[1], "HIDDEN", "NULL")
//    }
}


