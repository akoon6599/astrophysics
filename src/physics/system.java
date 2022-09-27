package physics;

import display.Screen;
import javax.swing.*;

import java.util.ArrayList;

public class system {
    static final Double TimeScale = 1.00;

    public static void main(String[] args) {
        Screen screen = new Screen();
        ArrayList<Object> bodies = new ArrayList<>();

//        StellarBody Sun = new StellarBody(0.0f,0.0f, "Sun", "Star", 1.988*Math.pow(10,10),274.0, 695700000, new Formula("0x + 0y"));
//        StellarBody Earth = new StellarBody(100f,100f,"Earth","Planet",5.972*Math.pow(10,4),9.798, 6378137, new Formula("-4.30x + 3.45y"));
        StellarBody Sun = new StellarBody(0f, 0f, "Sun", "Star", 1.0, 1.0, 100, new Formula("0.00x + 0.00y"));
        StellarBody Earth = new StellarBody(150f, -120f, "Earth", "Planet", 1.0, 1.0, 10, new Formula("1.00x + 1.00y"));
        bodies.add(Sun); // TODO: this ^ does not work, find why
        bodies.add(Earth);
        screen.display_body(Sun);
        screen.display_body(Earth);
        Sun.effect_movement(Earth, TimeScale);

        JFrame frame = new JFrame("Draw Chit");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(screen);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

