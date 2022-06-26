package core.structure.multicardinal.geo.point.function;

import core.structure.Entity;
import core.structure.multicardinal.geo.point.structure.DefinedPoint;
import core.structure.multicardinal.geo.point.structure.Point;
import core.structure.unicardinal.alg.symbolic.operator.SymbolicAdd;
import core.structure.unicardinal.alg.symbolic.operator.SymbolicScale;
import core.util.Utils;

import java.util.List;

public class Midpoint extends DefinedPoint {
    /** SECTION: Static Data ======================================================================================== */
    public static final InputType<Point> POINTS = new InputType<>(Point.class, Utils.MULTICARDINAL_COMPARATOR);

    public static final List<InputType<?>> inputTypes = List.of(Midpoint.POINTS);


    /** SECTION: Instance Variables ================================================================================= */
    public Point A, B;

    /** SECTION: Factory Methods ==================================================================================== */
    public static Midpoint create(String n, Point A, Point B) {
        return new Midpoint(n, A, B);
    }

    public static Midpoint create(String n, Point A, Point B, boolean anon) {
        return new Midpoint(n, A, B, anon);
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected Midpoint(String n, Point A, Point B) {
        super(A.getDiagram(), n);
        this.A = A;
        this.B = B;
        this.getInputs(Midpoint.POINTS).addAll(List.of(this.A, this.B));
        this.symbolic = List.of(
                SymbolicScale.create(0.5, SymbolicAdd.create(this.A.symbolic().get(0), this.B.symbolic().get(0))),
                SymbolicScale.create(0.5, SymbolicAdd.create(this.A.symbolic().get(1), this.B.symbolic().get(1)))
        );
    }

    protected Midpoint(String n, Point A, Point B, boolean anon) {
        super(A.getDiagram(), n, anon);
        this.A = A;
        this.B = B;
        this.getInputs(Midpoint.POINTS).addAll(List.of(this.A, this.B));
        this.symbolic = List.of(
                SymbolicScale.create(0.5, SymbolicAdd.create(this.A.symbolic().get(0), this.B.symbolic().get(0))),
                SymbolicScale.create(0.5, SymbolicAdd.create(this.A.symbolic().get(1), this.B.symbolic().get(1)))
        );
        if (!this.anonymous) {
            this.setNode();
        }
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public Entity simplify() {
        if (this.A == this.B) {
            return this.A;
        } else {
            return this;
        }
    }

    public List<InputType<?>> getInputTypes() {
        return Midpoint.inputTypes;
    }
}
