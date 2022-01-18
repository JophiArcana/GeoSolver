package Core.GeoSystem.Lines;

import Core.AlgSystem.Operators.Scale;
import Core.AlgSystem.UnicardinalRings.Symbolic;
import Core.AlgSystem.UnicardinalTypes.*;
import Core.EntityTypes.Entity;
import Core.GeoSystem.Lines.LineTypes.Line;
import Core.GeoSystem.MulticardinalTypes.DefinedMulticardinal;
import Core.GeoSystem.Points.PointTypes.*;
import Core.Utilities.*;

import java.util.*;
import java.util.function.Function;

public class Connect extends DefinedMulticardinal implements Line {
    /** SECTION: Static Data ======================================================================================== */
    public enum Parameter implements InputType {
        POINTS
    }
    public static final InputType[] inputTypes = {Parameter.POINTS};

    private static class ConnectPointDual extends DefinedPoint {
        /** SECTION: Static Data ==================================================================================== */
        private static ArrayList<Expression<Symbolic>> formula(HashMap<InputType, ArrayList<ArrayList<Expression<Symbolic>>>> args) {
            final AlgEngine<Symbolic> ENGINE = Utils.getEngine(Symbolic.class);
            ArrayList<Expression<Symbolic>> exprs = Utils.map(args.get(Parameter.POINTS), arg -> arg.get(0));
            Expression<Symbolic> a = exprs.get(0), b = exprs.get(1);
            Expression<Symbolic> num = ENGINE.conjugate(ENGINE.sub(b, a));
            Expression<Symbolic> den = ENGINE.imaginary(ENGINE.mul(ENGINE.conjugate(a), b));
            return new ArrayList<>(List.of(Scale.create(Constant.I(Symbolic.class), ENGINE.div(num, den), Symbolic.class)));
        }

        /** SECTION: Instance Variables ============================================================================= */
        public Point a, b;

        /** SECTION: Factory Methods ================================================================================ */
        public static ConnectPointDual create(Point a, Point b) {
            return new ConnectPointDual(a, b);
        }

        /** SECTION: Factory Methods ================================================================================ */
        private ConnectPointDual(Point a, Point b) {
            super(Utils.overline(a.getName() + b.getName()) + "\u209A");
            this.a = a;
            this.b = b;
        }

        /** SECTION: Implementation ================================================================================= */
        /** SUBSECTION: ENTITY ====================================================================================== */
        public Entity simplify() {
            return this;
        }

        public InputType[] getInputTypes() {
            return Connect.inputTypes;
        }

        /** SUBSECTION: DefinedPoint ================================================================================ */
        public Function<HashMap<InputType, ArrayList<ArrayList<Expression<Symbolic>>>>, ArrayList<Expression<Symbolic>>> getFormula() {
            return ConnectPointDual::formula;
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
