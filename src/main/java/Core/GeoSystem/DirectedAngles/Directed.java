package Core.GeoSystem.DirectedAngles;

import Core.AlgeSystem.UnicardinalRings.*;
import Core.AlgeSystem.UnicardinalTypes.*;
import Core.EntityTypes.Entity;
import Core.GeoSystem.Points.PointTypes.Point;
import Core.Utilities.*;

import java.util.*;

public class Directed extends DefinedExpression<DirectedAngle> {
    public static final String[] inputTypes = new String[] {"Points"};

    public Entity create(HashMap<String, ArrayList<Entity>> args) {
        return new Directed((Point) args.get("Points").get(0), (Point) args.get("Points").get(1));
    }

    public Point a, b;

    public Directed(Point a, Point b) {
        super(DirectedAngle.class);
        this.a = a;
        this.b = b;
    }

    public ArrayList<Expression<Symbolic>> symbolic() {
        final AlgeEngine<Symbolic> ENGINE = Utils.getEngine(Symbolic.class);
        Expression<Symbolic> vector = ENGINE.sub(b.expression().get(0), a.expression().get(0));
        return new ArrayList<>(Collections.singletonList(ENGINE.div(ENGINE.imaginary(vector), ENGINE.real(vector))));
    }

    public Expression<DirectedAngle> close() {
        return this;
    }

    public Factorization<DirectedAngle> normalize() {
        TreeMap<Expression<DirectedAngle>, Constant<DirectedAngle>> terms = new TreeMap<>(Utils.PRIORITY_COMPARATOR);
        terms.put(this, Constant.ONE(DirectedAngle.class));
        return new Factorization<>(Constant.ONE(DirectedAngle.class), terms, DirectedAngle.class);
    }

    public Expression<DirectedAngle> derivative(Univariate<DirectedAngle> var) {
        return Constant.ZERO(DirectedAngle.class);
    }

    public String[] getInputTypes() {
        return Directed.inputTypes;
    }
}
