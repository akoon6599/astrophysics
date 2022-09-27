package physics;

import display.Global;
import javax.swing.*;

import java.util.ArrayList;

public class system {
    static final Double TimeScale = 1.00;

    public static void main(String[] args) {
        Global global = new Global();
        ArrayList<Object> bodies = new ArrayList<>();

//        StellarBody Sun = new StellarBody(0.0f,0.0f, "Sun", "Star", 1.988*Math.pow(10,10),274.0, 695700000, new Formula("0x + 0y"));
//        StellarBody Earth = new StellarBody(100f,100f,"Earth","Planet",5.972*Math.pow(10,4),9.798, 6378137, new Formula("-4.30x + 3.45y"));
        StellarBody Sun = new StellarBody(0f, -120f, "Sun", "Star", 1.0, 1.0, 100, new Formula("0.00x + 0.00y"));
        StellarBody Earth = new StellarBody(150f, -120f, "Earth", "Planet", 1.0, 1.0, 10, new Formula("10.00x - 100.00y"));
//        bodies.add(Sun); // TODO: this ^ does not work when only single dimensions are different, find why
        bodies.add(Earth);

        global.display_body(Sun);

        global.display_body(Earth);

        global.move(Earth, TimeScale);
//        screen.display_body(Earth);
        display(global);
    }

    private static void simulate(Global global, ArrayList<Object> bodies) {

    }
    private static void display(Global global) {
        JFrame frame = new JFrame("Draw Chit");
        frame.getContentPane().add(global);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

