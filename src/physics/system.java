package physics;

import display.Global;
import javax.swing.*;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;

public class system {
    static final Double TimeScale = 1.00;
    static ArrayList<StellarBody> Bodies;
    static StellarBody Sun;

    public static void main(String[] args) throws InterruptedException {
        Global global = new Global();
        Bodies = new ArrayList<>();

//        StellarBody Sun = new StellarBody(0.0f,0.0f, "Sun", "Star", 1.988*Math.pow(10,10),274.0, 695700000, new Formula("0x + 0y"));
//        StellarBody Earth = new StellarBody(100f,100f,"Earth","Planet",5.972*Math.pow(10,4),9.798, 6378137, new Formula("-4.30x + 3.45y"));
        Sun = new StellarBody(0f, 0f, "Sun", "Star", 10000000000.0, 420.0, 100, "0.00d", 1.0);
        StellarBody Earth = new StellarBody(150f, 150f, "Earth", "Planet", 100000.0, 1.0, 10, "00.00d", 100.0);
//      TODO: effect mv does not work when only single dimensions are different, find why
        Bodies.add(Earth);
        global.display_body(Earth);
        global.display_body(Sun);
        display(global);
//        System.out.println(Earth.Position);
//        global.move(Earth, TimeScale);
//        System.out.println(Earth.Position);
        Bodies.add(Earth);
//        System.out.printf("%s, %f%n", Earth.Movement.Angle, Earth.Movement.Magnitude);
        Sun.effect_movement(Earth, TimeScale);
//        System.out.printf("%s, %f%n", Earth.Movement.Angle, Earth.Movement.Magnitude);
        System.out.println(Earth.Position);
        global.move(Earth, TimeScale);
        System.out.println(Earth.Position);
    }

    private static void simulate(Global global, int Cycles) {
        for (int currentCycle = 0; currentCycle <= Cycles; currentCycle++) {
            break;
        }
    }


    private static void display(Global global) {
        JFrame frame = new JFrame("Draw Chit");
        frame.getContentPane().add(global);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

