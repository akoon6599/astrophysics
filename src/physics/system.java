package physics;

import display.Global;
import javax.swing.*;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

public class system {
    static final Double TimeScale = 1.00;
    static final Double DistScale = 10.00;
    static final long CycleDelay = 50; // Milliseconds
    static ArrayList<StellarBody> Bodies;
    static StellarBody Sun;

    public static void main(String[] args) throws InterruptedException { // TODO: modulate the addition of bodies
        Bodies = new ArrayList<>();
        Sun = new StellarBody(0.0f,0.0f, "Sun", "Star", 1.988*Math.pow(10,12), 100, "0.00d", 0.0);
        StellarBody Earth = new StellarBody(200f,0f,"Earth","Planet",1.2*Math.pow(10,5), 25, "270.00d", 20.0);
//        StellarBody Moon = new StellarBody(-140f, -230f, "Body1", "Planet", 2.53*Math.pow(10,4), 45, "10.00d", 15.0);
//        Sun = new StellarBody(0f, 0f, "Sun", "Star", 2*Math.pow(10,11), 100, "0.00d", 1.0);
        StellarBody Moon = new StellarBody(-160f, -195f, "Body1", "Planet", 2*Math.pow(10,5), 50, "310.00d", 12.00);
//        StellarBody Earth = new StellarBody(80f, 150f, "Earth", "Planet", 2*Math.pow(10,5), 10, "180.00d", 10.00);
        Bodies.add(Earth);
        Sun.find_orbit(Earth, TimeScale, DistScale);
        Bodies.add(Moon);
        // gravity still forces bodies to only move counter-clockwise, regardless of actual movement
        Global global = new Global(Bodies);
        global.display_body(Earth);
        global.display_body(Moon);
//        global.display_body(Test);

        global.display_body(Sun);
        display(global);
        simulate(global, 50);
    }

    private static void simulate(Global global, int Cycles) throws InterruptedException {
        Thread.sleep(1000); // give screen a chance to open before starting sim
        Instant start = Instant.now();
        for (int currentCycle = 1; currentCycle <= Cycles; currentCycle++) {
            System.out.printf("%n%nHow Many Shapes Exist? : %s%n%n", global.Shapes.size());
            Instant startCycle = Instant.now();
            for (StellarBody body : Bodies) { // Sun effects each body then moves it - TODO: refactor to all-body effects
//                Bodies.forEach(item -> {if (!item.Title.equals(body.Title)) // i hate lambdas
//                        {
//                            System.out.println(item.Movement.Magnitude);
//                            item.effect_movement(body, TimeScale, DistScale);
//                            System.out.println(item.Movement.Magnitude);
//                        }
//                });
                Sun.effect_movement(body, TimeScale, DistScale);
                global.move(body, TimeScale);
                global.display_body(body);
            }

            global.collision(Bodies, Sun); // Check for collisions
            global.refresh(); // Repaint screen
            Thread.sleep(CycleDelay);
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
        JFrame frame = new JFrame("Physics Simulator");
        frame.getContentPane().add(global);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

