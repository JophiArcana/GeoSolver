package Core.GeoSystem.Points.Functions;

import Core.AlgeSystem.UnicardinalTypes.Unicardinal;
import Core.EntityTypes.Entity;
import Core.AlgeSystem.UnicardinalRings.Distance;
import Core.GeoSystem.Points.PointTypes.*;
import Core.Utilities.*;

import java.util.*;
import java.util.function.Function;

public class Centroid extends Center {
    public Entity create(HashMap<String, ArrayList<Entity>> args) {
        return new Centroid(this.name, args.get("Points").toArray(new Point[0]));
    }

    public static ArrayList<Unicardinal> formula(HashMap<String, ArrayList<ArrayList<Unicardinal>>> args) {
        AlgeEngine<Distance> ENGINE = Utils.getEngine(Distance.class);
        ArrayList<Unicardinal> argTerms = Utils.map(args.get("Points"), arg -> arg.get(0));
        return new ArrayList<>(Collections.singletonList(ENGINE.div(ENGINE.add(argTerms.toArray()),
                argTerms.size())));
    }

    public Centroid(String n, Point ... args) {
        super(n, args);
    }

    public Centroid(Point ... args) {
        super("", args);
    }

    public Entity simplify() {
        if (this.inputs.get("Points").size() == 2) {
            TreeSet<Entity> pointSet = new TreeSet<>(this.inputs.get("Points").elementSet());
            return new Midpoint(this.name, (Point) pointSet.first(), (Point) pointSet.last());
        } else {
            return this;
        }
    }

    public Function<HashMap<String, ArrayList<ArrayList<Unicardinal>>>, ArrayList<Unicardinal>> getFormula() {
        return Centroid::formula;
    }
}
