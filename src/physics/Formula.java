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
}
