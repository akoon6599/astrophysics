package physics;

import jdk.jshell.spi.ExecutionControl;

import java.util.ArrayList;

// IMPORTANT: LIMITED TO 2D SPACE
public class Object {
    public String Title;
    public Double Mass;
    protected Double InitialMass;
    public ArrayList<Float> Position = new ArrayList<>(2);
    protected ArrayList<Float> InitialPosition = new ArrayList<>(2);
    public Movement Movement;
    protected Movement InitialMovement;

    public Object(String angle, Double mag) {
        this.Movement = new Movement(angle, mag);
        this.InitialMovement = this.Movement;
    }
    public void overrideInitialMovement(Movement mv) { // Only to be used for finalize_body()
        InitialMovement = mv;
    }
    public void overrideInitialPosition(ArrayList<Float> pos) {
        InitialPosition = new ArrayList<>(2);
        InitialPosition.add(pos.get(0));
        InitialPosition.add(pos.get(1));
    }
    public void overrideInitialMass(Double mass) {
        InitialMass = mass;
    }
    public void reset() throws ExecutionControl.NotImplementedException {
        this.Position = InitialPosition;
        this.Movement = InitialMovement;
    }
    public float[] getInitialPosition() {
        float[] rtn = new float[2];
        rtn[0] = this.InitialPosition.get(0);
        rtn[1] = this.InitialPosition.get(1);
        return rtn;
    }
    public Movement getInitialMovement() {
        return InitialMovement;
    }
    public Double getInitialMass() {
        return InitialMass;
    }

    public void move(Double TimeScale) {
        Double[] mv = this.Movement.evaluate(TimeScale);
        this.Position.set(0, this.Position.get(0) + mv[0].floatValue());
        this.Position.set(1, this.Position.get(1) + mv[1].floatValue());
    }
}

