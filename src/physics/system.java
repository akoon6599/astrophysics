package physics;

import display.Global;
import display.Start;

import javax.swing.*;

import java.awt.*;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class system {
//    public static final Double TimeScale = 3.1536e14;
    public static final Double DistScale = 5.2e14;
    public static Double TimeScale = .1;
    public static long CycleDelay = 2; // Milliseconds
    public static int CYCLES = 55;
    static ArrayList<StellarBody> Bodies = new ArrayList<>();
    static Global GLOBAL;
    static Start START;
    public static HashMap<String, StellarBody> DefaultBodies = new HashMap<>();
    public static void initDefaultBodies() {
        StellarBody Sun = new StellarBody(0.0f,0.0f, "Sun", "Star", 1.99e30, 30, "0.00d", 0.0, Color.GRAY.darker(), true, false);
        StellarBody Earth = new StellarBody(304.2f,0.0f,"Earth","Planet",5.96e24, 10, "120.00d", 10.0, Color.GREEN.darker(), false, true, Sun);
        StellarBody Venus = new StellarBody(214.8f,0.0f,"Venus","Planet",4.86e24,10,"90.00d",10.0,Color.BLUE,false, false, Sun);
        StellarBody Mercury = new StellarBody(115.8f, 0.0f, "Mercury", "Planet", 0.33e24,5,"90.00d",10.0,Color.GRAY,false, false, Sun);
        StellarBody Mars = new StellarBody(-498.4f,0.0f,"Mars","Planet",0.6417e24,15,"90.00d",10.0,Color.RED,false, false, Sun);
        Sun.find_orbit(Mercury, DistScale);
        Sun.find_orbit(Venus,DistScale);
        Sun.find_orbit(Earth,DistScale);
        Sun.find_orbit(Mars,DistScale);

        DefaultBodies.put("Sun", Sun);
        DefaultBodies.put("Earth", Earth);
        DefaultBodies.put("Venus", Venus);
        DefaultBodies.put("Mercury", Mercury);
        DefaultBodies.put("Mars", Mars);
    }

    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl", "true");
        initDefaultBodies();

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
        long[] CycleDelays = new long[Cycles];
        Thread.sleep(1000); // give screen a chance to open before starting sim
        Instant start = Instant.now();


        for (int currentCycle = 0; currentCycle < Cycles; currentCycle++) {
            Instant startCycle = Instant.now();
            global.collision(Bodies);
            update_frame(global);
            display_velocity(global, currentCycle);

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
                body.effect_movement(item, TimeScale, DistScale); // Each body effects every other body
            }
            });
            if (!body.STATIC) {
                global.move(body, TimeScale);
                global.displayBody(body);
            }
        }

        global.removeAll();
        for (StellarBody body : Bodies) {
            if (body.TRACKVEL) {
                global.add(global.velocityLabels.get(Bodies.indexOf(body)));
            }
        }
        global.validate();
        global.velocityLabels.clear();
    }
    private static void display_velocity(Global global, int Frame) {
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
        global.Frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        global.Frame.setVisible(true);
        global.setVisible(true);
    }
}

