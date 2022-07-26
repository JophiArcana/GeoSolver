package core.structure.multicardinal.geo.point.structure;

import core.Propositions.equalitypivot.EqualityPivot;
import core.Propositions.equalitypivot.LockedPivot;
import core.structure.multicardinal.*;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;
import core.structure.unicardinal.alg.symbolic.SymbolicConstant;

import java.util.*;

public class Coordinate extends MultiConstant implements Point {
    /** SECTION: Static Data ======================================================================================== */
    public static final InputType<SymbolicExpression> X = new InputType<>();
    public static final InputType<SymbolicExpression> Y = new InputType<>();

    public static final List<InputType<?>> inputTypes = List.of(Coordinate.X, Coordinate.Y);

    public static int compare(Coordinate c1, Coordinate c2) {
        if (c1.x.lockedElement.value != c2.x.lockedElement.value) {
            return Double.compare(c1.x.lockedElement.value, c2.x.lockedElement.value);
        } else {
            return Double.compare(c1.y.lockedElement.value, c2.y.lockedElement.value);
        }
    }

    /** SECTION: Instance Variables ================================================================================= */
    public final LockedPivot<SymbolicExpression, SymbolicConstant> x, y;

    /** SECTION: Factory Methods ==================================================================================== */
    public static Coordinate create(String n, double x, double y) {
        return new Coordinate(n, x, y, true);
    }

    public static Coordinate create(String n, double x, double y, boolean anon) {
        return new Coordinate(n, x, y, anon);
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected Coordinate(String n, double x, double y, boolean anon) {
        super(n, anon);
        this.x = SymbolicConstant.create(x);
        this.y = SymbolicConstant.create(y);
        this.getInputs(Coordinate.X).add(this.x);
        this.getInputs(Coordinate.Y).add(this.y);
        if (!this.anonymous) {
            this.node = new PointNode(this);
        }
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public List<EqualityPivot<SymbolicExpression>> symbolic() {
        return List.of(this.x, this.y);
    }

    public List<InputType<?>> getInputTypes() {
        return Coordinate.inputTypes;
    }
}
