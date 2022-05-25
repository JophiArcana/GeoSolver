package Core.GeoSystem.Lines.LineFunctions;

import Core.AlgSystem.Operators.MulReduction.Pow;
import Core.AlgSystem.UnicardinalRings.Symbolic;
import Core.EntityStructure.Entity;
import Core.EntityStructure.UnicardinalStructure.*;
import Core.GeoSystem.Lines.LineStructure.*;
import Core.EntityStructure.MulticardinalStructure.*;
import Core.GeoSystem.Points.PointStructure.*;
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
            ArrayList<ArrayList<Expression<Symbolic>>> exprs = args.get(Parameter.POINTS);
            Expression<Symbolic> px = exprs.get(0).get(0), py = exprs.get(0).get(1);
            Expression<Symbolic> qx = exprs.get(1).get(0), qy = exprs.get(1).get(1);
            Expression<Symbolic> denominator = Pow.create(ENGINE.sub(ENGINE.mul(px, qy), ENGINE.mul(py, qx)), -1, Symbolic.class);
            return new ArrayList<>(List.of(
                    ENGINE.mul(ENGINE.sub(qy, py), denominator),
                    ENGINE.mul(ENGINE.sub(qx, px), denominator)
            ));
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
