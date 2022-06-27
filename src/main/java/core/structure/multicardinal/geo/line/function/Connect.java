package core.structure.multicardinal.geo.line.function;

import core.structure.multicardinal.geo.line.structure.Line;
import core.structure.multicardinal.geo.point.structure.*;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;
import core.structure.unicardinal.alg.symbolic.operator.SymbolicMul;
import core.structure.unicardinal.alg.symbolic.operator.SymbolicPow;
import core.structure.Entity;
import core.structure.multicardinal.*;
import core.util.*;

import java.util.*;

public class Connect extends DefinedMulticardinal implements Line {
    /** SECTION: Static Data ======================================================================================== */
    public static final InputType<Point> POINTS = new InputType<>(Point.class, Utils.MULTICARDINAL_COMPARATOR);

    public static final List<InputType<?>> inputTypes = List.of(Connect.POINTS);

    public class ConnectPointDual extends DefinedPoint {
        /** SECTION: Factory Methods ================================================================================ */
        public ConnectPointDual() {
            super(Utils.overline(Connect.this.a.getName() + Connect.this.b.getName()) + "\u209A", true);
            this.inputs = Connect.this.inputs;
            List<SymbolicExpression> a_symbolic = Connect.this.a.symbolic();
            List<SymbolicExpression> b_symbolic = Connect.this.b.symbolic();
            SymbolicExpression denominator = SymbolicPow.create(Utils.ENGINE.sub(
                    SymbolicMul.create(a_symbolic.get(0), b_symbolic.get(1)),
                    SymbolicMul.create(a_symbolic.get(1), b_symbolic.get(0))
            ), -1);
            this.symbolic = List.of(
                    SymbolicMul.create(Utils.ENGINE.sub(b_symbolic.get(1), a_symbolic.get(1)), denominator),
                    SymbolicMul.create(Utils.ENGINE.sub(a_symbolic.get(0), b_symbolic.get(0)), denominator)
            );
        }

        /** SECTION: Implementation ================================================================================= */
        /** SUBSECTION: ENTITY ====================================================================================== */
        public Entity simplify() {
            return this;
        }

        public List<InputType<?>> getInputTypes() {
            return Connect.inputTypes;
        }
    }

    /** SECTION: Instance Variables ================================================================================= */
    public Point a, b;
    public ConnectPointDual pointDual;

    /** SECTION: Factory Methods ==================================================================================== */
    public static Connect create(Point a, Point b) {
        return new Connect(a, b, true);
    }

    public static Connect create(Point a, Point b, boolean anon) {
        return new Connect(a, b, anon);
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected Connect(Point a, Point b, boolean anon) {
        super(Utils.overline(a.getName() + b.getName()), anon);
        this.getInputs(Connect.POINTS).addAll(List.of(a, b));
        this.pointDual = this.new ConnectPointDual();
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
