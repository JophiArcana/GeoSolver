package Core.GeoSystem.Points.Functions;

import Core.AlgSystem.UnicardinalRings.*;
import Core.AlgSystem.UnicardinalTypes.*;
import Core.EntityTypes.Entity;
import Core.GeoSystem.Points.PointTypes.*;
import Core.Utilities.*;

import java.util.*;

public class Centroid extends Center {
    public Entity createEntity(HashMap<InputType, ArrayList<Entity>> args) {
        return new Centroid(this.name, args.get(Parameter.POINTS).toArray(new Point[0]));
    }

    public Centroid(String n, Point ... args) {
        super(n, args);
    }

    public Centroid(Point ... args) {
        super("", args);
    }

    public Entity simplify() {
        if (this.inputs.get(Parameter.POINTS).size() == 2) {
            TreeSet<Entity> pointSet = new TreeSet<>(this.inputs.get(Parameter.POINTS).elementSet());
            return new Midpoint(this.name, (Point) pointSet.first(), (Point) pointSet.last());
        } else {
            return this;
        }
    }

    protected ArrayList<Expression<Symbolic>> computeSymbolic() {
        AlgeEngine<Symbolic> ENGINE = Utils.getEngine(Symbolic.class);
        ArrayList<Expression<Symbolic>> argTerms = Utils.map(this.inputs.get(Parameter.POINTS), arg -> arg.symbolic().get(0));
        return new ArrayList<>(Collections.singletonList(ENGINE.div(ENGINE.add(argTerms.toArray()), argTerms.size())));
    }
}
