package core.structure.multicardinal.geo.line.function;

import core.structure.multicardinal.geo.line.structure.Line;
import core.structure.multicardinal.geo.point.structure.*;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;
import core.structure.unicardinal.alg.symbolic.operator.SymbolicPow;
import core.structure.Entity;
import core.structure.multicardinal.*;
import core.util.*;

import java.util.*;
import java.util.function.Function;

public class Connect extends DefinedMulticardinal implements Line {
    /** SECTION: Static Data ======================================================================================== */
    public static final InputType<Point> POINTS = new InputType<>(Point.class, Utils.MULTICARDINAL_COMPARATOR);

    public static final List<InputType<?>> inputTypes = List.of(Connect.POINTS);

    public static class ConnectPointDual extends DefinedPoint {
        /** SECTION: Static Data ==================================================================================== */
        private static List<SymbolicExpression> formula(HashMap<InputType<?>, ArrayList<List<SymbolicExpression>>> args) {
            ArrayList<List<SymbolicExpression>> exprs = args.get(Connect.POINTS);
            SymbolicExpression px = exprs.get(0).get(0), py = exprs.get(0).get(1);
            SymbolicExpression qx = exprs.get(1).get(0), qy = exprs.get(1).get(1);
            SymbolicExpression denominator = SymbolicPow.create(Utils.ENGINE.sub(Utils.ENGINE.mul(px, qy), Utils.ENGINE.mul(py, qx)), -1);
            return List.of(
                    Utils.ENGINE.mul(Utils.ENGINE.sub(qy, py), denominator),
                    Utils.ENGINE.mul(Utils.ENGINE.sub(px, qx), denominator)
            );
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
            this.getInputs(Connect.POINTS).addAll(List.of(a, b));
        }

        /** SECTION: Implementation ================================================================================= */
        /** SUBSECTION: ENTITY ====================================================================================== */
        public Entity simplify() {
            return this;
        }

        public List<InputType<?>> getInputTypes() {
            return Connect.inputTypes;
        }

        /** SUBSECTION: DefinedPoint ================================================================================ */
        public Function<HashMap<InputType<?>, ArrayList<List<SymbolicExpression>>>, List<SymbolicExpression>> getFormula() {
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

    public static Connect create(Point a, Point b, boolean anon) {
        return new Connect(a, b, anon);
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected Connect(Point a, Point b) {
        super(Utils.overline(a.getName() + b.getName()));
        this.getInputs(Connect.POINTS).addAll(List.of(a, b));
        this.pointDual = ConnectPointDual.create(this.a, this.b);
    }

    protected Connect(Point a, Point b, boolean anon) {
        super(Utils.overline(a.getName() + b.getName()), anon);
        this.getInputs(Connect.POINTS).addAll(List.of(a, b));
        this.pointDual = ConnectPointDual.create(this.a, this.b);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public Entity simplify() {
        return this;
    }

    public List<InputType<?>> getInputTypes() {
        return Connect.inputTypes;
    }

    /** SUBSECTION: Line ============================================================================================ */
    public Point pointDual() {
        return this.pointDual;
    }
}
