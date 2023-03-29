package physics;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Line {
    public Color color;
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
        color = obj.COLOR;
    }
    public Line(ArrayList<Float> Start, ArrayList<Float> End, float[] CENTER) {
        Start.set(0, Start.get(0)+CENTER[0]);
        Start.set(1, Start.get(1)+CENTER[1]);
        End.set(0, End.get(0)+CENTER[0]);
        End.set(1, End.get(1)+CENTER[1]);
        this.Start = Start;
        this.End = End;
        this.Distance = Math.sqrt(Math.pow(End.get(0) - Start.get(0),2) + Math.pow(End.get(1) - Start.get(1), 2));
        double angle = Math.toDegrees(Math.atan2(End.get(1) - Start.get(1), End.get(0) - Start.get(0)));
        if (angle >= 360) {
            angle -= 360;
        }
        if (angle < 0) {
            angle += 360;
        }

        this.Movement = new Movement(String.format("%.2fd", angle), 0.00);
    }

    public Line(Double[] startPos, Double[] endPos, StellarBody obj) {
        Double oX = startPos[0];
        Double oY = startPos[1];
        Double sX = endPos[0];
        Double sY = endPos[1];

        this.Start = new ArrayList<>();
        Arrays.stream(startPos).toList().forEach(e -> {
            float fl = e.floatValue();
            Start.add(fl);
        });
        this.End = new ArrayList<>();
        Arrays.stream(endPos).toList().forEach(e -> {
            float fl = e.floatValue();
            End.add(fl);
        });

        double angle = Math.toDegrees(Math.atan2(sY - oY, sX - oX));
        this.Distance = Math.sqrt(Math.pow(sY - oY, 2) + Math.pow(sX - oX, 2));
        if (angle >= 360) {
            angle -= 360;
        }
        if (angle < 0) {
            angle += 360;
        }

        this.Movement = new Movement(String.format("%.2fd", angle), 0.00);
        this.color = obj.COLOR;
    }
}
