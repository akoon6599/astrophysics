package physics;

import java.util.ArrayList;

public class system {
    public static void main(String[] args) {
        ArrayList<Object> bodies = new ArrayList<>();

//        StellarBody Sun = new StellarBody(0.0f,0.0f, "Sun", "Star", 9.188*Math.pow(10,30),10, new Formula("2.00x^2.00"));
        StellarBody Earth = new StellarBody(100f,100f,"Earth","Planet",9.8,10, new Formula("x^4.00"));
//        bodies.add(Sun);
//        bodies.add(Earth);
//        Sun.move();
        Earth.move();

//        Formula a = new Formula("2x^2");
//        System.out.println(a.getEquation());
//        a.evaluate(0.0);
//        System.out.println(a.getEquation());
//        a.find_derivative();
//        System.out.println(a.getEquation());
    }
}

