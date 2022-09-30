package physics;

import display.Global;
import javax.swing.*;

import java.util.ArrayList;

public class system {
    static final Double TimeScale = 1.00;
    static final Double DistScale = 1.00;
    static ArrayList<StellarBody> Bodies;
    static StellarBody Sun;

    public static void main(String[] args) throws InterruptedException {
        Global global = new Global();
        Bodies = new ArrayList<>();

//        StellarBody Sun = new StellarBody(0.0f,0.0f, "Sun", "Star", 1.988*Math.pow(10,10),274.0, 695700000, new Formula("0x + 0y"));
//        StellarBody Earth = new StellarBody(100f,100f,"Earth","Planet",5.972*Math.pow(10,4),9.798, 6378137, new Formula("-4.30x + 3.45y"));
        Sun = new StellarBody(0f, 0f, "Sun", "Star", 2*Math.pow(10,11), 120.0, 100, "0.00d", 1.0);
        StellarBody Moon = new StellarBody(-140f, -195f, "Body1", "Planet", 2*Math.pow(10,7), 1.0, 50,"70.00d", 3.00);
        StellarBody Earth = new StellarBody(80f, 150f, "Earth", "Planet", 2*Math.pow(10,5), 1.0, 10, "180.00d", 1.00);
//        StellarBody Test = new StellarBody(291f, 240f, "t1", "t1", 1.0, 1.0, 100, "0.00d", 1.0);
//      TODO: effect mv does not work when only single dimensions are different, find why
        Bodies.add(Earth);
        Bodies.add(Moon);
        global.display_body(Earth);
        global.display_body(Moon);
//        global.display_body(Test);

        global.display_body(Sun);
        display(global);
        simulate(global, 20);
        System.out.println("end");
    }

    private static void simulate(Global global, int Cycles) throws InterruptedException {
        for (int currentCycle = 0; currentCycle <= Cycles; currentCycle++) {
            for (StellarBody body : Bodies) {
                global.move(body, TimeScale);
                global.display_body(body);
            }

            global.collision(Bodies, Sun);
            global.refresh();
            Thread.sleep(500);
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

