package Core.GeoSystem.Points.Functions;

import Core.AlgeSystem.*;
import Core.GeoSystem.Points.*;
import Core.Utilities.*;

import java.util.*;
import java.util.function.Function;

public class Centroid extends Center {
    public static final Function<HashMap<String, ArrayList<ArrayList<Expression>>>, ArrayList<Expression>> formula = args -> {
        ArrayList<ArrayList<Expression>> argTerms = args.get("Points");
        return new ArrayList<>(Collections.singletonList(ASEngine.div(ASEngine.add(Utils.map(argTerms, arg -> arg.get(0)).toArray()),
                argTerms.size())));
    };

    public Centroid(Point ... args) {
        super(args);
    }

    public Function<HashMap<String, ArrayList<ArrayList<Expression>>>, ArrayList<Expression>> getFormula() {
        return Centroid.formula;
    }
}
