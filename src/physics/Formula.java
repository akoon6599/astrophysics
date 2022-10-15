package physics;

import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Formula {
    // BASE FORM: 0.00d
    private final String Equation;

    public String getEquation() {return Equation;}

    public Formula(String eq) {
        this.Equation = eq;
    }
}
