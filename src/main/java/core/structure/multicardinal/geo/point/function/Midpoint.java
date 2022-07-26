package core.structure.multicardinal.geo.point.function;

import core.Propositions.equalitypivot.EqualityPivot;
import core.structure.multicardinal.geo.point.structure.*;
import core.structure.unicardinal.alg.symbolic.operator.*;

import java.util.List;

public class Midpoint extends DefinedPoint {
    /** SECTION: Static Data ======================================================================================== */
    public static final InputType<Point> POINTS = new InputType<>();

    public static final List<InputType<?>> inputTypes = List.of(Midpoint.POINTS);

    /** SECTION: Instance Variables ================================================================================= */
    public EqualityPivot<Point> A, B;

    /** SECTION: Factory Methods ==================================================================================== */
    public static Midpoint create(String n, EqualityPivot<Point> A, EqualityPivot<Point> B) {
        return new Midpoint(n, A, B, true);
    }

    public static Midpoint create(String n, EqualityPivot<Point> A, EqualityPivot<Point> B, boolean anon) {
        return new Midpoint(n, A, B, anon);
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected Midpoint(String n, EqualityPivot<Point> A, EqualityPivot<Point> B, boolean anon) {
        super(n, anon);
        this.A = A;
        this.B = B;
        this.getInputs(Midpoint.POINTS).addAll(List.of(this.A, this.B));
        this.symbolic = List.of(
                SymbolicScale.create(0.5, SymbolicAdd.create(this.A.symbolic().get(0), this.B.symbolic().get(0))),
                SymbolicScale.create(0.5, SymbolicAdd.create(this.A.symbolic().get(1), this.B.symbolic().get(1)))
        );
        if (!this.anonymous) {
            this.node = new PointNode(this);
        }
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public void updateLocalVariables(EqualityPivot<?> consumedPivot, EqualityPivot<?> consumerPivot) {
        if (this.A == consumedPivot) {
            this.A = (EqualityPivot<Point>) consumerPivot;
        }
        if (this.B == consumedPivot) {
            this.B = (EqualityPivot<Point>) consumerPivot;
        }
    }

    public List<InputType<?>> getInputTypes() {
        return Midpoint.inputTypes;
    }
}
