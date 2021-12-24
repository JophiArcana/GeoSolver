package Core.GeoSystem.Lines;

import Core.AlgSystem.UnicardinalRings.Symbolic;
import Core.AlgSystem.UnicardinalTypes.Expression;
import Core.EntityTypes.Entity;
import Core.GeoSystem.Lines.LineTypes.Line;
import Core.GeoSystem.MulticardinalTypes.DefinedMulticardinal;
import Core.GeoSystem.Points.PointTypes.*;
import Core.Utilities.AlgEngine;
import Core.Utilities.Utils;

import java.util.*;

public class Connect extends DefinedMulticardinal implements Line {
    public enum Parameter implements InputType {
        POINTS
    }
    public static final InputType[] inputTypes = {Parameter.POINTS};

    public Entity createEntity(HashMap<InputType, ArrayList<Entity>> args) {
        return new Connect((Point) args.get(Parameter.POINTS).get(0), (Point) args.get(Parameter.POINTS).get(1));
    }

    public Point a, b;

    public static class ConnectPointDual extends DefinedPoint {
        public Point a, b;

        public ConnectPointDual(String n, Point a, Point b) {
            super(n + "\u209A");
            this.a = a;
            this.b = b;
        }

        public ArrayList<Expression<Symbolic>> computeSymbolic() {
            final AlgEngine<Symbolic> ENGINE = Utils.getEngine(Symbolic.class);
            Expression<Symbolic>    a_expr = this.a.symbolic().get(0),
                                    b_expr = this.b.symbolic().get(0);
            Expression<Symbolic>    a_expr_conjugate = ENGINE.conjugate(a_expr),
                                    b_expr_conjugate = ENGINE.conjugate(b_expr);
            Expression<Symbolic> num = ENGINE.mul(2, ENGINE.sub(a_expr_conjugate, b_expr_conjugate));
            Expression<Symbolic> den = ENGINE.sub(ENGINE.mul(a_expr_conjugate, b_expr), ENGINE.mul(a_expr, b_expr_conjugate));
            return new ArrayList<>(Collections.singletonList(ENGINE.div(num, den)));
        }

        public Entity simplify() {
            return this;
        }
    }

    public Connect(Point a, Point b) {
        super(Utils.overline(a.getName() + b.getName()));
    }

    /** TODO: Create class ConnectPointDual */
    public Point pointDual() {
        return null;
    }

    public Entity simplify() {
        return this;
    }

    public InputType[] getInputTypes() {
        return Connect.inputTypes;
    }
}
