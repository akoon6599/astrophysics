package physics;

import java.awt.Color;

public class StellarBody extends Object{
    static final Double GravConstant = 6.674*Math.pow(10, -11);
    public String Classification;
    public Integer Radius;
    public Color COLOR;
    public boolean STATIC;

    // aila sucks


    public StellarBody(Float posx, Float posy, String ttl, String cls, Double mass, Integer radius, String angle, Double mag, Color color, boolean Static) {
        super(angle, mag);
        this.Classification = cls;
        this.Position.add(posx);
        this.Position.add(posy);
        this.InitialPosition.add(posx);
        this.InitialPosition.add(posy);
        this.Title = ttl;
        this.Mass = mass;
        this.InitialMass = mass;
        this.Radius = radius;
        this.COLOR = color;
        this.STATIC = Static;
    }
    @Override
    public StellarBody clone() {
        return new StellarBody(this.getInitialPosition()[0], this.getInitialPosition()[1], this.Title,
                this.Classification, this.getInitialMass(), this.Radius, this.getInitialMovement().getAngle(), this.getInitialMovement().getMagnitude(),
                this.COLOR, this.STATIC);
    }

    public void effect_movement(StellarBody obj, Double TimeScale, Double DistanceScale) { // Called by main body, passes orbiter
        Line gDirection = new Line(this, obj);
        double gMagnitude = GravConstant * ((this.Mass * obj.Mass) / Math.pow(gDirection.Distance*(this.Title.equals("Sun")?DistanceScale:DistanceScale*0.25),2)) * TimeScale;

        double gxMagnitude = gMagnitude * Math.cos(Math.toRadians(gDirection.Movement.coefficient()));
        double gyMagnitude = gMagnitude * Math.sin(Math.toRadians(gDirection.Movement.coefficient()));

        obj.Movement.evaluate(TimeScale);
        double oxMagnitude = obj.Movement.xMove;
        double oyMagnitude = obj.Movement.yMove;

        double nxMagnitude = oxMagnitude + gxMagnitude;
        double nyMagnitude = oyMagnitude + gyMagnitude;
        double newAngle = Math.toDegrees(Math.atan2(nyMagnitude, nxMagnitude));
        Double nMagnitudePrimary = nyMagnitude / Math.sin(Math.toRadians(newAngle)); // Redundant creations for nMagnitude
        Double nMagnitudeSecondary = nxMagnitude / Math.cos(Math.toRadians(newAngle)); // Just because I can and also for accuracy
        Double nMagnitudeTertiary = Math.sqrt(Math.pow(nxMagnitude, 2) + Math.pow(nyMagnitude, 2));
        double nMagnitude;
        if (Math.abs(nMagnitudePrimary - nMagnitudeSecondary) < 1e-4 &&
                Math.abs(nMagnitudePrimary - nMagnitudeTertiary) < 1e-4) {
            nMagnitude = nMagnitudePrimary;}
        else {nMagnitude = 0;
            System.out.printf("MAGNITUDE ERROR BODY %s %s:%s:%s%n", obj.Title, nMagnitudePrimary, nMagnitudeSecondary, nMagnitudeTertiary);}
        obj.Movement = new Movement(String.format("%.2fd", newAngle), nMagnitude);
    }

    public void find_orbit(StellarBody obj, Double TimeScale, Double DistanceScale) { // Called by main body, passes orbiter - requires a relative 90-degree angle between orbiter motion and main body
        Line Tether = new Line(this, obj);
        double gMagnitude = GravConstant * ((this.Mass * obj.Mass) / Math.pow(Tether.Distance*DistanceScale,2)) * TimeScale;
        double rV = Math.sqrt(gMagnitude*Tether.Distance);
        obj.Movement.setMagnitude(rV);
    }
}


