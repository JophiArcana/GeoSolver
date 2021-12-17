package Core.GeoSystem.DirectedAngles;

import Core.AlgSystem.UnicardinalRings.*;
import Core.AlgSystem.UnicardinalTypes.*;
import Core.EntityTypes.Entity;
import Core.GeoSystem.Points.PointTypes.Point;
import Core.Utilities.*;

import java.util.*;

public class Directed extends DefinedExpression<DirectedAngle> {
    public enum Parameter implements InputType {
        POINTS
    }
    public static final InputType[] inputTypes = {Parameter.POINTS};

    public Entity create(HashMap<InputType, ArrayList<Entity>> args) {
        return new Directed((Point) args.get(Parameter.POINTS).get(0), (Point) args.get(Parameter.POINTS).get(1));
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

    public InputType[] getInputTypes() {
        return Directed.inputTypes;
    }
}
