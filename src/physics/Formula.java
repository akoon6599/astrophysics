package physics;

import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Formula {
    private final String Equation;

    public String getEquation() {return Equation;}

    public Formula(String eq) {
        this.Equation = eq;
    }

    public Double evaluate(Double x) { //TODO: PEMDAS ffs
        double result = 0.0;
        String eq = Equation;

        Matcher matcher = Pattern.compile("\s[-+*/]\s").matcher(eq);
        Hashtable<Integer, Character> operators = new Hashtable<>();
        int i = 0;
        while (matcher.find()) {
            operators.put(i, eq.charAt(matcher.start()+1));
            i++;
        }
        String[] terms = eq.split("\s[-+*/]\s");
        int term_index = 0;
        for (String term : terms) {
            if (term.isBlank() || term.isEmpty()) {
//                System.out.println("term skip ;");
                continue;}

            term = term.replace("x", String.format("(%.2f)",x));
            Matcher multmatch = Pattern.compile(".*(([0-9]+.[0-9]{2})\\(([0-9.]+)\\)).*").matcher(term);
            if (multmatch.matches()){
                double coeff = Double.parseDouble(multmatch.group(2));
                double mult = Double.parseDouble(multmatch.group(3));
                double evmatch = coeff * mult;
                term = new StringBuilder(term).replace(multmatch.start(1), multmatch.end(1), String.valueOf(evmatch)).toString();
                // cast to SB, replace the a(b) section, return to string
            }

            Matcher parmatch = Pattern.compile(".*(\\(([0-9.]+)\\)).*").matcher(term);
            if (parmatch.matches()) {
                double coeff = Double.parseDouble(parmatch.group(2));
                term = new StringBuilder(term).replace(parmatch.start(1), parmatch.end(1), String.valueOf(coeff)).toString();
            }

            if (term_index==0) { // first term exception
                if (term.contains("log")) {
                    Matcher logmatch = Pattern.compile(".*log_([0-9]+)\\((.*)\\).*").matcher(term);
                    //noinspection ResultOfMethodCallIgnored
                    logmatch.matches();
                    double base = Double.parseDouble(logmatch.group(1));
                    String exp = logmatch.group(2);
                    double evexp;
                    if (exp.contains("^")) {
                         evexp = Math.pow(Double.parseDouble(exp.split("\\^")[0]),
                                Double.parseDouble(exp.split("\\^")[1]));
                    }
                    else {evexp=Double.parseDouble(exp);}
                    double evlog = (Math.log10(evexp))/(Math.log10(base));
                    result += evlog;
                }
                else if (term.contains("^")) {
                    double evexp = Math.pow(Double.parseDouble(term.split("\\^")[0]),
                            Double.parseDouble(term.split("\\^")[1]));
                    result += evexp;
                }
                else {
                    result += Double.parseDouble(term);
                }
            }
            else {
                if (term.contains("log")) {
                    Matcher logmatch = Pattern.compile(".*log_([0-9]+)\\((.*)\\).*").matcher(term);
                    //noinspection ResultOfMethodCallIgnored
                    logmatch.matches();
                    double base = Double.parseDouble(logmatch.group(1));
                    String exp = logmatch.group(2);
                    double evexp;
                    if (exp.contains("^")) {
                        evexp = Math.pow(Double.parseDouble(exp.split("\\^")[0]),
                                Double.parseDouble(exp.split("\\^")[1]));
                    }
                    else {evexp = Double.parseDouble(exp);}
                    double evlog = (Math.log10(evexp))/(Math.log(base));
                    if (operators.get(term_index-1)!=null&&operators.get(term_index-1)=='+') {result += evlog;}
                    else if (operators.get(term_index-1)!=null&&operators.get(term_index-1)=='-') {result -= evlog;}
                }
                else if (term.contains("^")) {
                    double evpow = Math.pow(Double.parseDouble(term.split("\\^")[0]),
                            Double.parseDouble(term.split("\\^")[1]));
                    if (operators.get(term_index-1)!=null&&operators.get(term_index-1)=='+') {result += evpow;}
                    else if (operators.get(term_index-1)!=null&&operators.get(term_index-1)=='-') {result -= evpow;}
                }
                else {
                    if (operators.get(term_index-1)!=null&&operators.get(term_index-1)=='+') {result += Double.parseDouble(term);}
                    else if (operators.get(term_index-1)!=null&&operators.get(term_index-1)=='-') {result -= Double.parseDouble(term);}
                }
            }
            term_index++;
        }
        return result;
    }

    //  ------- Not sure why this code is completely borked, keeping it commented for legacy/review
    //  Switching from complex formulas to a simple always-tangent method, less accurate but less masochist

//    public String find_derivative() {
//        StringBuilder der = new StringBuilder();
//        String eq = this.Equation;
//        Matcher matcher = Pattern.compile("\s[-+*/]\s").matcher(eq);
//        Hashtable<Integer, Character> operators = new Hashtable<>();
//        int i = 0;
//        while (matcher.find()) {
//            operators.put(i, eq.charAt(matcher.start()+1));
//            i++;
//        }
//
//        String[] terms = eq.split("\s[-+*/]\s"); // actual derivitive thingymajigger
//        int index_term = 0;
//        for (String term : terms) {
////            System.out.println("g " + term);
//            if (!term.contains("x")) {operators.remove(index_term);} // clear non-derivative operators
//            // determine which rule needs to be used
//            if (term.contains("log")) { // priority order - functions w/ parenthesis first
//                der.append(log_derivative(term, operators, index_term));
//            }
//            else if (term.contains("^")) {
//                der.append(power_derivative(term, operators, index_term));
//            }
//            else if (Pattern.compile(".*(\\([0-9]+.[0-9]{2}\\))?x.*").matcher(term).matches()) {
////                System.out.println("aasd");
//                der.append(term.replace("x","").replace("(","").replace(")",""));
//            }
//
//            der.append(String.format(" %s ", operators.get(index_term)!=null?operators.get(index_term):"")); // add operator unless end of equation
//            index_term++;
//        }
//
//        // combine operators and signs
//        String[] nterms = der.toString().split("\s[+-]\s");
//        StringBuilder nder = new StringBuilder();
//        int ni = 0;
//        int nindex = 0;
//        for (String term : nterms) { // i legitimately lost track of how this worked while still writing it good luck
//            if (nindex==0) { // skip first term, no operator
//                nindex++;
//                nder.append(term);
//                continue;
//            }
//            if (operators.get(ni)!=null) { // remove now-empty/last terms
//                if (operators.get(ni).equals('+') && term.charAt(0)=='-') { // check if sign should change
//                    operators.put(ni, '-'); // overwrite operator w/ opposite
//                    term = new StringBuilder(term).deleteCharAt(0).toString(); // cast term to SB, delete first character, return to string
//                }
//                else if (operators.get(ni).equals('-') && term.charAt(0)=='-') {
//                    operators.put(ni, '+');
//                    term = new StringBuilder(term).deleteCharAt(0).toString();
//                }
//            }
//            nder.append(String.format(" %s %s", operators.get(ni)!=null?operators.get(ni):"", term)); // append order flipped here for my sanity
//            ni++;
//            nindex++;
//        }
//
//        // remove extraneous endings
//        nder.deleteCharAt(nder.length()-1); // remove extra space
//        if ((""+nder.charAt(nder.length()-1)).matches("[-+*/]")) {nder.delete(nder.length()-2,nder.length());} // if operator exists at end, delete last 2 chars
//
//        return nder.toString();
//    }
//    private String power_derivative(String term, Hashtable<Integer, Character> operators, int term_index) {
//        StringBuilder der = new StringBuilder();
//        // base form: ax^b
//
//        if (term.contains("x")) {
//            double coeff;
//            try {coeff = Double.parseDouble(term.split("x")[0]);} // split into coefficient and power
//            catch (Exception ex) {coeff = 1.00;}
//            double power = Double.parseDouble(term.split("x")[1].replace("^",""));
//
//            double n_coeff = coeff*(power); // power rule
//            double n_power = power-1;
//
//            if (n_power==0.0) {der.append(String.format("%.2f",n_coeff));} //  check if power is redundant i.e. 0 or 1
//            else if (n_power==1.0){der.append(String.format("%.2fx",n_coeff));}
//            else {der.append(String.format("%.2fx^%.2f", n_coeff, n_power));}
//        }
//        else {operators.remove(term_index);} // not sure about effectiveness of this, essentially failsafe?
//
//        return der.toString();
//    }
//    private StringBuilder log_derivative(String term, Hashtable<Integer, Character> operators, int term_index) {
//        double coeff = 1.0;
//        // base form: log_a(x)
//        Matcher match = Pattern.compile(".*log_([0-9]+)\\((.*)\\).*").matcher(term); //
//        //noinspection ResultOfMethodCallIgnored
//        match.matches(); // Matcher requires .matches() to be called
//        double base = Double.parseDouble(match.group(1)); // separate base and log
//        String log = match.group(2);
//
//        if (log.contains("^")) {coeff *= Double.parseDouble(log.split("\\^")[1]);} // log power rules
//
//        return new StringBuilder(String.format("%.1f/(%s)", coeff, String.format("%.2fx",Math.log(base)))); // make term
//    }
}
