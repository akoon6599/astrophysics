package physics;

import display.Global;
import display.Start;

import javax.swing.*;

import java.awt.*;
import java.lang.reflect.Field;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

public class system {
    static final Double TimeScale = 1.0;
    static final Double DistScale = 0.05;
    static final long CycleDelay = 30; // Milliseconds
    static ArrayList<StellarBody> Bodies = new ArrayList<>();
    static StellarBody Sun;
    static Global GLOBAL;
    public static int CYCLES = 250;

    public static void main(String[] args) { // TODO: modularize position of planets in add body menu
        Sun = new StellarBody(0.0f,0.0f, "Sun", "Star", 1.988*Math.pow(10,7), 100, "0.00d", 0.0, Color.GRAY.darker(), true);
//        StellarBody Earth = new StellarBody(350f,0f,"Earth","Planet",1.2*Math.pow(10,6), 25, "90.00d", 0.0, Color.GREEN.darker());
        StellarBody Moon = new StellarBody(-400f, 0f, "Body1", "Planet", 5*Math.pow(10,4), 50, "270.00d", 6.20, Color.RED, false);
        StellarBody Test = new StellarBody(400f, 200f, "Body2", "Planet", 9.2*Math.pow(10,4), 50, "270.00d", 14.20, Color.RED, false);
//        StellarBody Test2 = new StellarBody(-400f, 0f, "Body3", "Planet", 2*Math.pow(10,4), 50, "270.00d", 6.20, Color.RED);
        StellarBody Test3 = new StellarBody(-200f, 500f, "Body4", "Planet", 2.15*Math.pow(10,4), 50, "348.00d", 5.0, Color.BLACK, false);
//


//        Bodies.add(Earth);
        Bodies.add(Test);
//        Bodies.add(Test2);
        Bodies.add(Test3);
//        Sun.find_orbit(Earth, TimeScale, DistScale);
////        Sun.find_orbit(Moon, TimeScale, DistScale);
        Sun.find_orbit(Test, TimeScale, DistScale);
        Bodies.add(Moon);
////        Bodies.add(Comet);


        display(new Start(Bodies));

//        GLOBAL = new Global(Bodies);
//        GLOBAL.display_body(Sun);
//        display(GLOBAL);
//        simulate(GLOBAL, 1);
    }
    public static void start_simulation(ArrayList<StellarBody> newBodies, int Cycles) throws InterruptedException {
        Bodies = newBodies;
        GLOBAL = new Global(Bodies);
        display(GLOBAL);
        GLOBAL.display_body(Sun);
        simulate(GLOBAL, Cycles);
        /*
        So, the problem here is that paintComponent SEEMS to only work+run if it's referring to the main frame - ie the
        object that started the gui display. The thing is, that's NOT the case in this instance. Start() is what begins
        the GUI, and so paintComponent never runs - never drawing the planets at all until the history is given, which
        only displays because it works through an entirely different concept than paintComponent.
        I can see there being 4 solutions:
        1) Merge Start() and Global() so that its just separate systems of the same class, both using paintComponent
        2) Rewrite Start() to use paintComponent, MIGHT fix issue but im not sure
        3) Change system so that Global() is the initiator but does not get enabled until Start() is done
        4) Rewrite Global() to not use paintComponent, may or may not be possible
        From what I can see right now, option 4 seems to be the most sane option while preserving the most of my
        previous work. Strongly consider 3, however, no guarantee that that will even solve the issue.
         */

//        System.exit(0);
    }

    private static void simulate(Global global, int Cycles) throws InterruptedException {
        Thread.sleep(1000); // give screen a chance to open before starting sim
        Instant start = Instant.now();
        global.display_body(Sun);

        update_frame(global);

        for (int currentCycle = 1; currentCycle <= Cycles; currentCycle++) {
            System.out.printf("%n%nHow Many Shapes Exist? : %s%n%n", global.Shapes.size());
            Instant startCycle = Instant.now();
            update_frame(global);

            global.collision(Bodies, Sun);
            Thread.sleep(CycleDelay);
            Instant endCycle = Instant.now();
            System.out.printf("%nCycle %s End: %s milliseconds%n%n", currentCycle, Duration.between(startCycle, endCycle).toMillis());
        }
        Instant end = Instant.now();
        System.out.printf("Simulation End: %s seconds%n", Duration.between(start, end).toMillis()/1000.0);
        // Initiate drawing historical paths for all objects
        global.SimComplete = true;
    }

    private static void update_frame(Global global) {
        global.refresh();
        for (StellarBody body : Bodies) {
            Bodies.forEach(item -> {if (!item.Title.equals(body.Title))
            {
                item.effect_movement(body, TimeScale, DistScale); // Each body effects every other body
            }
            });
            if (!body.STATIC) {
                Sun.effect_movement(body, TimeScale, DistScale); // then the Sun effects every body
                global.move(body, TimeScale);
                global.display_body(body);
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
    private static void display(Global global) {
        System.out.println("displayOutOfQueue");
        global.Frame.add(global);
        global.validate();
        global.Frame.setVisible(true);
        global.setVisible(true);
    }
}

