package Core.GeoSystem.Lines;

import Core.AlgSystem.UnicardinalRings.Symbolic;
import Core.AlgSystem.UnicardinalTypes.Expression;
import Core.EntityTypes.Entity;
import Core.GeoSystem.Lines.LineTypes.Line;
import Core.GeoSystem.MulticardinalTypes.DefinedMulticardinal;
import Core.GeoSystem.Points.PointTypes.*;
import Core.Utilities.*;

import java.util.*;

public class Connect extends DefinedMulticardinal implements Line {
    /** SECTION: Static Data ======================================================================================== */
    public enum Parameter implements InputType {
        POINTS
    }
    public static final InputType[] inputTypes = {Parameter.POINTS};

    private static class ConnectPointDual extends DefinedPoint {
        /** SUBSECTION: Instance Variables ========================================================================== */
        public Point a, b;

        /** SUBSECTION: Factory Methods ============================================================================= */
        public static ConnectPointDual create(Point a, Point b) {
            return new ConnectPointDual(a, b);
        }

        /** SUBSECTION: Factory Methods ============================================================================= */
        private ConnectPointDual(Point a, Point b) {
            super(Utils.overline(a.getName() + b.getName()) + "\u209A");
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

        public InputType[] getInputTypes() {
            return Connect.inputTypes;
        }
    }

    /** SECTION: Instance Variables ================================================================================= */
    public Point a, b;
    public ConnectPointDual pointDual;

    /** SECTION: Factory Methods ==================================================================================== */
    public static Connect create(Point a, Point b) {
        return new Connect(a, b);
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected Connect(Point a, Point b) {
        super(Utils.overline(a.getName() + b.getName()));
        this.inputs.get(Parameter.POINTS).add(a);
        this.inputs.get(Parameter.POINTS).add(b);
        this.pointDual = ConnectPointDual.create(this.a, this.b);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public Entity simplify() {
        return this;
    }

    public InputType[] getInputTypes() {
        return Connect.inputTypes;
    }

    /** SUBSECTION: Line ============================================================================================ */
    public Point pointDual() {
        return this.pointDual;
    }
}
