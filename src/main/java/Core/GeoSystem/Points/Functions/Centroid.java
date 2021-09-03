package Core.GeoSystem.Points.Functions;

import Core.AlgeSystem.UnicardinalRings.*;
import Core.AlgeSystem.UnicardinalTypes.*;
import Core.EntityTypes.Entity;
import Core.GeoSystem.Points.PointTypes.*;
import Core.Utilities.*;

import java.util.*;

public class Centroid extends Center {
    public Entity create(HashMap<String, ArrayList<Entity>> args) {
        return new Centroid(this.name, args.get("Points").toArray(new Point[0]));
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

    protected ArrayList<Unicardinal> getExpression() {
        AlgeEngine<Symbolic> ENGINE = Utils.getEngine(Symbolic.class);
        ArrayList<Unicardinal> argTerms = Utils.map(this.inputs.get("Points"), arg -> arg.symbolic().get(0));
        return new ArrayList<>(Collections.singletonList(ENGINE.div(ENGINE.add(argTerms.toArray()), argTerms.size())));
    }
}
