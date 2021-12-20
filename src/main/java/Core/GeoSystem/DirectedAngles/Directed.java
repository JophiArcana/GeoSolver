package Core.GeoSystem.DirectedAngles;

import Core.AlgSystem.UnicardinalRings.*;
import Core.AlgSystem.UnicardinalTypes.*;
import Core.EntityTypes.Entity;
import Core.GeoSystem.Lines.LineTypes.Line;
import Core.Utilities.*;

import java.util.*;

public class Directed extends DefinedExpression<DirectedAngle> {
    public enum Parameter implements InputType {
        LINE
    }
    public static final InputType[] inputTypes = {Parameter.LINE};

    public Entity createEntity(HashMap<InputType, ArrayList<Entity>> args) {
        return new Directed((Line) args.get(Parameter.LINE).get(0));
    }

    public Line l;

    public Directed(Line l) {
        super(DirectedAngle.class);
        this.l = l;
    }

    public ArrayList<Expression<Symbolic>> symbolic() {
        final AlgeEngine<Symbolic> ENGINE = Utils.getEngine(Symbolic.class);
        Expression<Symbolic> dualExpression = this.l.pointDual().symbolic().get(0);
        return new ArrayList<>(Collections.singletonList(ENGINE.div(ENGINE.real(dualExpression), ENGINE.imaginary(dualExpression))));
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
