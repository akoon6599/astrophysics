package physics;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Movement {
    Formula EquationX;
    Formula EquationY;

    public Movement(Formula mv) {
        Matcher matcher = Pattern.compile("(-?[0-9]+\\.[0-9]{2}([xy])) ([+-]) ([0-9]+\\.[0-9]{2}([xy]))").matcher(mv.getEquation());
        if (matcher.matches()) {
            this.EquationX = new Formula(matcher.group(2).equals("x") ? matcher.group(1) : matcher.group(4));
            this.EquationY = new Formula(matcher.group(2).equals("y") ? matcher.group(1).replace('y', 'x') : matcher.group(4).replace('y', 'x'));
        } else {
            this.EquationX = new Formula("0.00x");
            this.EquationY = new Formula("0.00y");
        }
        if (matcher.group(3).equals("-")) {
            this.EquationY = new Formula("-"+this.EquationY.getEquation());
        }

    }

    public Double[] coefficients() {
        return new Double[]{Double.parseDouble(this.EquationX.getEquation().replaceAll("[xy]", "")),
                Double.parseDouble(this.EquationY.getEquation().replaceAll("[xy]", ""))};
    }
}
