package physics;

public class Movement {
    String Angle;
    Double Magnitude;
    Double xMove;
    Double yMove;

    public Movement(String ag, Double mag) {
        this.Angle = ag;
        this.Magnitude = mag;
    }

    public Double[] evaluate(Double TimeScale) { // refreshes the xMove and yMove variables, does not do anything physics-side
        double angle = Math.toRadians(this.coefficient());
        this.xMove = Math.cos(angle) * this.Magnitude;
        this.yMove = Math.sin(angle) * this.Magnitude;
        return new Double[] {this.xMove*TimeScale, this.yMove*TimeScale};
    }

    public void setMagnitude(Double mg) {
        this.Magnitude = mg;
    }

    public Double coefficient() {
        double rtn = Double.parseDouble(this.Angle.replace("d", ""));
        if (rtn >= 360) {rtn -= 360;}
        if (rtn < 0) {rtn += 360;}
        this.Angle = String.format("%.2fd", rtn);
        return !Double.isNaN(rtn)?rtn:0.00;
    }
}
