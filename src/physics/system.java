package physics;

import java.util.ArrayList;

public class system {
    static final Double TimeScale = 1.00;

    public static void main(String[] args) {

        ArrayList<Object> bodies = new ArrayList<>();

        StellarBody Sun = new StellarBody(0.0f,0.0f, "Sun", "Star", 9.188*Math.pow(10,30),40, new Formula("0x + 0y"));
        StellarBody Earth = new StellarBody(-100f,-100f,"Earth","Planet",9.8,10, new Formula("-4.30x + 3.45y"));
        bodies.add(Sun);
        bodies.add(Earth);

        Sun.effect(Earth);
    }
}

