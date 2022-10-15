package physics;

import java.util.ArrayList;

public class Line {
    public ArrayList<Float> Start;
    public ArrayList<Float> End;
    public Movement Movement;
    public Double Distance;

    public Line(StellarBody obj, StellarBody obj2) {
        this.Start = obj2.Position;
        this.End = obj.Position;
        Float sX = this.End.get(0);
        Float sY = this.End.get(1);
        Float oX = this.Start.get(0);
        Float oY = this.Start.get(1);
        double angle = Math.toDegrees(Math.atan2(sY - oY, sX - oX));
        this.Distance = Math.sqrt(Math.pow(sY - oY, 2) + Math.pow(sX - oX, 2));
        if (angle >= 360) {
            angle -= 360;
        }
        if (angle < 0) {
            angle += 360;
        }

        this.Movement = new Movement(String.format("%.2fd", angle), 0.00);
    }

    public Line(double[] startPos, double[] endPos) {
        Double oX = startPos[0];
        Double oY = startPos[1];
        Double sX = endPos[0];
        Double sY = endPos[1];
        double angle = Math.toDegrees(Math.atan2(sY - oY, sX - oX));
        this.Distance = Math.sqrt(Math.pow(sY - oY, 2) + Math.pow(sX - oX, 2));
        if (angle >= 360) {
            angle -= 360;
        }
        if (angle < 0) {
            angle += 360;
        }

        this.Movement = new Movement(String.format("%.2fd", angle), 0.00);
    }
}
