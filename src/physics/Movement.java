package physics;

import java.sql.Time;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        Double angle = this.coefficient();
        this.xMove = Math.cos(angle) / this.Magnitude;
        this.yMove = Math.sin(angle) / this.Magnitude;
        return new Double[] {this.xMove*TimeScale, this.yMove* TimeScale};
    }

    public Double coefficient() {
        return Double.parseDouble(this.Angle.replace("d", ""));
    }
}
