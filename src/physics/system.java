package physics;

import display.Global;
import display.Start;

import javax.swing.*;

import java.awt.*;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

public class system {
    public static final Double TimeScale = 1.0;
    public static final Double DistScale = 0.05;
    static final long CycleDelay = 5; // Milliseconds
    public static int CYCLES = 1000;
    static long[] CycleDelays = new long[CYCLES];
    static ArrayList<StellarBody> Bodies = new ArrayList<>();
    static Global GLOBAL;
    static Start START;

    public static void main(String[] args) { // TODO: maybe make a 3 body stable system. like a moon around earth around the sun. maybe. that will be hard
        StellarBody Sun = new StellarBody(0.0f,0.0f, "Sun", "Star", 1.988*Math.pow(10,7), 100, "0.00d", 0.0, Color.GRAY.darker(), true);
        StellarBody Earth = new StellarBody(350f,0f,"Earth","Planet",1.2*Math.pow(10,6), 25, "-90.00d", 7.8, Color.GREEN.darker(), false);
        StellarBody Moon = new StellarBody(-400f, 0f, "Body1", "Planet", 5*Math.pow(10,4), 15, "90.00d", 6.80, Color.RED, false);
//        StellarBody Test = new StellarBody(-400f, 0f, "Body2", "Planet", 2*Math.pow(10,4), 50, "270.00d", 6.20, Color.RED);
//        StellarBody Test2 = new StellarBody(-400f, 0f, "Body3", "Planet", 2*Math.pow(10,4), 50, "270.00d", 6.20, Color.RED);
//        StellarBody Test3 = new StellarBody(-200f, 500f, "Body4", "Planet", 2.15*Math.pow(10,4), 50, "348.00d", 5.0, Color.BLACK, false);
//          // TODO: also maybe like add a moon thing so that you can use find_orbit properly. will also probably help with the one above


        Bodies.add(Sun);
        Bodies.add(Earth);
        Bodies.add(Moon);
        Sun.find_orbit(Moon, TimeScale, DistScale);

        START = new Start(Bodies);
        display(START);
    }
    public static void start_simulation(ArrayList<StellarBody> newBodies, int Cycles) throws InterruptedException {
        Bodies = newBodies;
        GLOBAL = new Global(Bodies, START);
        display(GLOBAL);
        simulate(GLOBAL, Cycles);
    }

    private static void simulate(Global global, int Cycles) throws InterruptedException {
        Thread.sleep(1000); // give screen a chance to open before starting sim
        Instant start = Instant.now();

        update_frame(global);

        for (int currentCycle = 0; currentCycle < Cycles; currentCycle++) {
//            System.out.printf("%n%nHow Many Shapes Exist? : %s%n%n", global.Shapes.size());
            Instant startCycle = Instant.now();
            update_frame(global);

            global.collision(Bodies);
            Thread.sleep(CycleDelay);
            Instant endCycle = Instant.now();
            System.out.printf("%nCycle %s End: %s milliseconds%n%n", currentCycle, Duration.between(startCycle, endCycle).toMillis());
            if (currentCycle!=0) {CycleDelays[currentCycle] = Duration.between(startCycle, endCycle).toMillis();}
        }
        Instant end = Instant.now();
        System.out.printf("Simulation End: %s seconds%n", Duration.between(start, end).toMillis()/1000.0);
        // Initiate drawing historical paths for all objects
        global.SimComplete = true;
        int maxDev = 0;
        for (long delay : CycleDelays) {
            int dif = (int)Math.abs(delay-CycleDelay);
            maxDev = Math.max(dif, maxDev);
        }
        System.out.printf("Maximum Deviation Between Cycle Delay and Real Time: %s milliseconds%n", maxDev);
    }

    private static void update_frame(Global global) {
        global.refresh();
        for (StellarBody body : Bodies) {
            Bodies.forEach(item -> {if (!item.Title.equals(body.Title))
            {
                item.effect_movement(body, TimeScale, DistScale); // Each body effects every other body
            }
            });
            if (!body.STATIC) {global.move(body, TimeScale);
                global.displayBody(body);
            }
        }
    }

    private static void display(Start start) {
        java.awt.EventQueue.invokeLater(() -> {
            start.pack();
            start.setLocationRelativeTo(null);
            start.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            start.setVisible(true);
        });
    }
    public static void display(Global global) {
        System.out.println("displayOutOfQueue");
        global.Frame.add(global);
        global.validate();
        global.Frame.setVisible(true);
        global.setVisible(true);
    }
}

