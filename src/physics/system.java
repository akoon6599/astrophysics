package physics;

import display.Global;
import display.Start;

import javax.swing.*;

import java.awt.*;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

public class system {
    static final Double TimeScale = 1.00;
    static final Double DistScale = 0.05;
    static final long CycleDelay = 20; // Milliseconds
    static ArrayList<StellarBody> Bodies;
    static StellarBody Sun;

    public static void main(String[] args) throws InterruptedException { // TODO: modulate the addition of bodies
        Bodies = new ArrayList<>();
        Sun = new StellarBody(0.0f,0.0f, "Sun", "Star", 1.988*Math.pow(10,7), 100, "0.00d", 0.0, Color.GRAY.darker());
        StellarBody Earth = new StellarBody(250f,0f,"Earth","Planet",1.2*Math.pow(10,5), 25, "90.00d", 0.0, Color.GREEN.darker());
        StellarBody Moon = new StellarBody(-400f, 0f, "Body1", "Planet", 2*Math.pow(10,4), 50, "270.00d", 6.20, Color.RED);
        StellarBody Test = new StellarBody(-400f, 0f, "Body2", "Planet", 2*Math.pow(10,4), 50, "270.00d", 6.20, Color.RED);
        StellarBody Test2 = new StellarBody(-400f, 0f, "Body3", "Planet", 2*Math.pow(10,4), 50, "270.00d", 6.20, Color.RED);
        StellarBody Test3 = new StellarBody(-400f, 0f, "Body4", "Planet", 2*Math.pow(10,4), 50, "270.00d", 6.20, Color.RED);

        Bodies.add(Earth);
        Bodies.add(Test);
        Bodies.add(Test2);
        Bodies.add(Test3);
        Sun.find_orbit(Earth, TimeScale, DistScale);
//        Sun.find_orbit(Moon, TimeScale, DistScale);
        Bodies.add(Moon);
//        Bodies.add(Comet);

//        Global global = new Global(Bodies);


//        global.display_body(Sun);
        display(new Start(Bodies));
//        display(global);
//        simulate(global, 1);
    }

    private static void simulate(Global global, int Cycles) throws InterruptedException {
        Thread.sleep(1000); // give screen a chance to open before starting sim
        Instant start = Instant.now();
        for (int currentCycle = 1; currentCycle <= Cycles; currentCycle++) {
            System.out.printf("%n%nHow Many Shapes Exist? : %s%n%n", global.Shapes.size());
            Instant startCycle = Instant.now();
            for (StellarBody body : Bodies) {
                Bodies.forEach(item -> {if (!item.Title.equals(body.Title)) // i hate lambdas
                        {
                            item.effect_movement(body, TimeScale, DistScale); // Each body effects every other body
                        }
                });
                Sun.effect_movement(body, TimeScale, DistScale); // then the Sun effects every body
                global.move(body, TimeScale);
                global.display_body(body);
            }

            global.collision(Bodies, Sun); // Check for collisions
            global.refresh(); // Repaint screen
            Thread.sleep(CycleDelay);
            Instant endCycle = Instant.now();
            System.out.printf("%nCycle %s End: %s milliseconds%n%n", currentCycle, Duration.between(startCycle, endCycle).toMillis());
        }
        Instant end = Instant.now();
        System.out.printf("Simulation End: %s seconds%n", Duration.between(start, end).toMillis()/1000.0);
        // Initiate drawing historical paths for all objects
        global.SimComplete = true;
        global.refresh();
    }

    private static void display(Global global) {
        java.awt.EventQueue.invokeLater(() -> {
            JFrame frame = new JFrame("Physics Simulator");
            frame.getContentPane().add(global);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
    private static void display(Start start) {
        java.awt.EventQueue.invokeLater(() -> {
            start.pack();
            start.setLocationRelativeTo(null);
            start.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            start.setVisible(true);
        });
    }
}

