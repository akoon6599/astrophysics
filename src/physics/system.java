package physics;

import display.Global;
import display.Start;

import javax.swing.*;

import java.awt.*;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class system {
//    public static final Double TimeScale = 3.1536e14;
    public static final Double DistScale = 5.2e14;
    public static final Double TimeScale = 6*DistScale;
    static final long CycleDelay = 5; // Milliseconds
    public static int CYCLES = 2500;
    static long[] CycleDelays = new long[CYCLES];
    static ArrayList<StellarBody> Bodies = new ArrayList<>();
    static Global GLOBAL;
    static Start START;
//Perihelion is too low for all planets for some reason. Far more elliptical than should be
    public static void main(String[] args) {
        StellarBody Sun = new StellarBody(0.0f,0.0f, "Sun", "Star", 1.99e30, 30, "0.00d", 0.0, Color.GRAY.darker(), true);
        StellarBody Earth = new StellarBody(299.2f,0f,"Earth","Planet",5.96e24, 10, "90.00d", 10.0, Color.GREEN.darker(), false, Sun);
         // TODO: also maybe like add a moon thing so that you can use find_orbit properly. will also probably help with the one above
        StellarBody Venus = new StellarBody(214.8f,0f,"Venus","Planet",4.86e24,10,"90.00d",10.0,Color.GRAY,false, Sun);
        StellarBody Mercury = new StellarBody(-115.8f, 0.0f, "Mercury", "Planet", 0.33e24,5,"-90.00d",17.0,Color.GRAY,false, Sun);

        Bodies.add(Sun);
//        Bodies.add(Mercury);
//        Bodies.add(Venus);
        Bodies.add(Earth);
//        Sun.find_orbit(Bodies.get(Bodies.indexOf(Mercury)), DistScale, TimeScale);
//        Sun.find_orbit(Bodies.get(Bodies.indexOf(Venus)),DistScale, TimeScale);
        Sun.find_orbit(Bodies.get(Bodies.indexOf(Earth)),DistScale, TimeScale);

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

        for (int currentCycle = 0; currentCycle < Cycles; currentCycle++) {
            for (StellarBody body : Bodies) {
                System.out.printf("POSITION: %s:%s%n", body.Position.get(0), body.Position.get(1));
            }
            Instant startCycle = Instant.now();
            global.collision(Bodies);
            update_frame(global);

            Thread.sleep(CycleDelay);
            Instant endCycle = Instant.now();
            System.out.printf("%nCycle %s End: %s milliseconds%n%n", currentCycle, Duration.between(startCycle, endCycle).toMillis());
            if (currentCycle!=0) {CycleDelays[currentCycle] = Duration.between(startCycle, endCycle).toMillis();}
        }
        Instant end = Instant.now();
        System.out.printf("Simulation End: %s seconds%n", Duration.between(start, end).toMillis()/1000.0);
        // Initiate drawing historical paths for all objects
        global.SimComplete = true;
        CycleDelays[0] = CycleDelay;
        int maxDev = 0;
        int maxCycle = 0;
        for (int i=0;i<CycleDelays.length;i++) {
            long delay=CycleDelays[i];
            int dif = (int)Math.abs(CycleDelay-delay);
            if (maxDev < dif) {
                maxDev = dif;
                maxCycle = i;
            }
        }
        System.out.printf("Maximum Deviation Between Cycle Delay and Real Time: %s milliseconds on Cycle %s%n", maxDev, maxCycle);
    }

    private static void update_frame(Global global) {
        global.refresh();
        for (StellarBody body : Bodies) {
            Bodies.forEach(item -> {if (!item.Title.equals(body.Title))
            {
                System.out.printf("%s;%s%n",body.Title,item.Title);
                body.effect_movement(item, TimeScale, DistScale); // Each body effects every other body
            }
            });
            if (!body.STATIC) {
                global.move(body, TimeScale, DistScale);
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
        global.Frame.add(global);
        global.validate();
        global.Frame.setVisible(true);
        global.setVisible(true);
    }
}

