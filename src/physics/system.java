package physics;

import display.Global;
import javax.swing.*;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

public class system {
    static final Double TimeScale = 1.00;
    static final Double DistScale = 1.00;
    static ArrayList<StellarBody> Bodies;
    static StellarBody Sun;

    public static void main(String[] args) throws InterruptedException {
        Bodies = new ArrayList<>();
        Sun = new StellarBody(0.0f,0.0f, "Sun", "Star", 1.988*Math.pow(10,11), 100, "0.00d", 0.0);
        StellarBody Earth = new StellarBody(200f,0f,"Earth","Planet",5.972*Math.pow(10,4), 25, "270.00d", 100.0);
        StellarBody Moon = new StellarBody(-140f, -230f, "Body1", "Planet", 2.53*Math.pow(10,4), 45, "10.00d", 70.0);
//        Sun = new StellarBody(0f, 0f, "Sun", "Star", 2*Math.pow(10,11), 100, "0.00d", 1.0);
//        StellarBody Moon = new StellarBody(-160f, -195f, "Body1", "Planet", 2*Math.pow(10,7), 50, "90.00d", 12.00);
//        StellarBody Earth = new StellarBody(80f, 150f, "Earth", "Planet", 2*Math.pow(10,5), 10, "180.00d", 10.00);
//     TODO: effect mv does not work when only single dimensions are different, find why
        // TODO : THE MV EFFECT NEVER GETS NEGATIVE THATS ONE OF THE BIG PROBLEMS IM GENIUS
        Bodies.add(Earth);
        Bodies.add(Moon);

        Global global = new Global(Bodies);
        global.display_body(Earth);
        global.display_body(Moon);
//        global.display_body(Test);

        global.display_body(Sun);
        display(global);
        simulate(global, 5);
    }

    private static void simulate(Global global, int Cycles) throws InterruptedException {
        Thread.sleep(1000); // give screen a chance to open before starting sim
        Instant start = Instant.now();
        for (int currentCycle = 1; currentCycle <= Cycles; currentCycle++) {
            System.out.printf("%n%nHow Many Shapes Exist? : %s%n%n", global.Shapes.size());
            Instant startCycle = Instant.now();
            for (StellarBody body : Bodies) {
                Sun.effect_movement(body, TimeScale, DistScale);
                global.move(body, TimeScale);
                global.display_body(body);
            }

            global.collision(Bodies, Sun);
            global.refresh();
            Thread.sleep(400);
            Instant endCycle = Instant.now();
            System.out.printf("%nCycle End: %s milliseconds%n%n", Duration.between(startCycle, endCycle).toMillis());
        }
        Instant end = Instant.now();
        System.out.printf("Simulation End: %s seconds%n", Duration.between(start, end).toMillis()/1000.0);
        // Initiate drawing historical paths for all objects
        global.SimComplete = true;
        global.refresh();
    }

    private static void display(Global global) {
        JFrame frame = new JFrame("Draw Chit");
        frame.getContentPane().add(global);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

