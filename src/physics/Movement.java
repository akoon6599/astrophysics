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

    public Double[] evaluate(Double TimeScale) {
        double angle = Math.toRadians(this.coefficient());
        this.xMove = Math.cos(angle) * this.Magnitude;
        this.yMove = Math.sin(angle) * this.Magnitude;
        return new Double[] {this.xMove*TimeScale, this.yMove*TimeScale};
    }

    public Double coefficient() {
        double rtn = Double.parseDouble(this.Angle.replace("d", ""));
        return !Double.isNaN(rtn)?rtn:0.00;
    }
}
