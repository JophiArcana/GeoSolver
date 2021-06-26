package Core.GeoSystem.Points.Functions;

import Core.AlgeSystem.*;
import Core.EntityTypes.Entity;
import Core.GeoSystem.Points.*;
import Core.Utilities.*;

import java.util.*;
import java.util.function.Function;

import static Core.Utilities.AlgeEngine.*;

public class Centroid extends Center {
    public static final Function<HashMap<String, ArrayList<ArrayList<Expression>>>, ArrayList<Expression>> formula = args -> {
        ArrayList<Expression> argTerms = Utils.map(args.get("Points"), arg -> arg.get(0));
        return new ArrayList<>(Collections.singletonList(div(add(argTerms.toArray()),
                argTerms.size())));
    };

    public Centroid(Point ... args) {
        super(args);
    }

    public Entity simplify() {
        if (this.inputs.get("Points").size() == 2) {
            TreeSet<Entity> pointSet = new TreeSet<>(this.inputs.get("Points").elementSet());
            return new Midpoint((Point) pointSet.first(), (Point) pointSet.last());
        } else {
            return this;
        }
    }

    public Function<HashMap<String, ArrayList<ArrayList<Expression>>>, ArrayList<Expression>> getFormula() {
        return Centroid.formula;
    }
}
