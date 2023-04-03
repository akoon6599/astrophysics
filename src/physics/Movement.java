package physics;

public class Movement {
    String Angle;
    Double Magnitude;
    Double xMove;
    Double yMove;

    public Movement(String ag, Double mag) {
        this.Angle = ag;
        this.Magnitude = mag;
        this.xMove = Math.cos(Math.toRadians(this.coefficient()))*this.Magnitude;
        this.yMove = Math.sin(Math.toRadians(this.coefficient()))*this.Magnitude;
    }

    public Double[] evaluate(Double TimeScale) { // returns the new X and Y movement components, does not update anything
        double angle = Math.toRadians(this.coefficient());
        return new Double[] {Math.cos(angle) * this.Magnitude * TimeScale, Math.sin(angle) * this.Magnitude * TimeScale};
    }

    public void setMagnitude(Double mg) { // Updates Magnitude and X and Y movement components
        this.Magnitude = mg;
        this.xMove = Math.cos(Math.toRadians(this.coefficient()))*this.Magnitude;
        this.yMove = Math.sin(Math.toRadians(this.coefficient()))*this.Magnitude;
    }
    public Double getMagnitude() {return this.Magnitude;}
    public String getAngle() {return this.Angle;}

    public Double coefficient() { // Parse stored Angle format to a Double
        double rtn = Double.parseDouble(this.Angle.replace("d", ""));
        if (rtn >= 360) {rtn -= 360;}
        if (rtn < 0) {rtn += 360;}
        this.Angle = String.format("%.2fd", rtn);
        return !Double.isNaN(rtn)?rtn:0.00;
    }
}
