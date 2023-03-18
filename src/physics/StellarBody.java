package physics;

import java.awt.Color;
import java.sql.Time;
import java.util.Arrays;
import java.util.Objects;

public class StellarBody extends Object{
    static final Double GravConstant = 6.674*Math.pow(10, -11);
    public String Classification;
    public Integer Radius;
    public Color COLOR;
    public boolean STATIC;
    public boolean ORBITER;
    public StellarBody orbitingBody = null;



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
        this.ORBITER = false;
    }
    public StellarBody(Float posx, Float posy, String ttl, String cls, Double mass, Integer radius, String angle, Double mag, Color color, boolean Static, StellarBody orbitingBody) {
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
        this.ORBITER = true;
        this.orbitingBody = orbitingBody;
    }
    @Override
    public StellarBody clone() {
        return new StellarBody(this.getInitialPosition()[0], this.getInitialPosition()[1], this.Title,
                this.Classification, this.getInitialMass(), this.Radius, this.getInitialMovement().getAngle(), this.getInitialMovement().getMagnitude(),
                this.COLOR, this.STATIC);
    }

    public void effect_movement(StellarBody obj, Double TimeScale, Double DistanceScale) { // Called by main body, passes orbiter
        if (!obj.STATIC) {
            if (!obj.ORBITER || ((Objects.nonNull(obj.orbitingBody)&&obj.orbitingBody == this))) {
                Line gDirection = new Line(this, obj);
                System.out.println(gDirection.Distance);
                double gMagnitude = (GravConstant * (this.Mass * obj.Mass)) / Math.pow(gDirection.Distance * DistanceScale, 2) / 1e10 * (TimeScale / DistanceScale);

                double gxMagnitude = gMagnitude * Math.cos(Math.toRadians(gDirection.Movement.coefficient()));
                double gyMagnitude = gMagnitude * Math.sin(Math.toRadians(gDirection.Movement.coefficient()));
                System.out.printf("GMAG;%s:%s,%s%n", obj.Title, gMagnitude, gxMagnitude);

                double oxMagnitude = obj.Movement.xMove;
                double oyMagnitude = obj.Movement.yMove;

                double nxMagnitude = oxMagnitude + gxMagnitude;
                double nyMagnitude = oyMagnitude + gyMagnitude;
                double newAngle = Math.toDegrees(Math.atan2(nyMagnitude, nxMagnitude));
                System.out.printf("NM:%s;%s:%s%n", oyMagnitude, gyMagnitude, nyMagnitude);
                Double nMagnitudePrimary = nyMagnitude / Math.sin(Math.toRadians(newAngle)); // Redundant creations for nMagnitude
                Double nMagnitudeSecondary = nxMagnitude / Math.cos(Math.toRadians(newAngle)); // Just because I can and also for accuracy
                Double nMagnitudeTertiary = Math.sqrt(Math.pow(nxMagnitude, 2) + Math.pow(nyMagnitude, 2));
                double nMagnitude;
                if (Math.abs(nMagnitudePrimary - nMagnitudeSecondary) < nMagnitudePrimary * (1e-4) &&
                        Math.abs(nMagnitudePrimary - nMagnitudeTertiary) < nMagnitudePrimary * (1e-4)) {
                    nMagnitude = nMagnitudePrimary;
                } else {
                    nMagnitude = 0;
                    System.out.printf("MAGNITUDE ERROR BODY %s %s:%s:%s%n", obj.Title, nMagnitudePrimary, nMagnitudeSecondary, nMagnitudeTertiary);
                }
                obj.Movement = new Movement(String.format("%.2fd", newAngle), nMagnitude);
                if (!this.STATIC) {
                    System.out.printf("EMS;%s:%s%n", obj.Title, nMagnitude);
                    System.out.printf("MAG:[%s,%s]%n", nxMagnitude, nyMagnitude);
                }
            }
        }
    }

    public void find_orbit(StellarBody obj, Double DistanceScale, Double TimeScale) { // Called by main body, passes orbiter - requires a relative 90-degree angle between orbiter motion and main body
        Line Tether = new Line(this, obj);
        double gMagnitude = (GravConstant * this.Mass) / (Tether.Distance*DistanceScale);
        double rV = Math.sqrt(gMagnitude);
        System.out.printf("FO:%s:%s%n",obj.Title,rV);
        obj.Movement.setMagnitude(rV);
    }
}


