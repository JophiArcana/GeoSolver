package core.structure.unicardinal.alg.symbolic.constant;

import core.Diagram;
import core.structure.equalitypivot.*;
import core.structure.unicardinal.Unicardinal;
import core.structure.unicardinal.alg.symbolic.SymbolicConstant;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;

public class SymbolicInfinity extends SymbolicConstant {
    /** SECTION: Static Data ======================================================================================== */
    public static final double INFINITY_VALUE = Math.sqrt(Double.MAX_VALUE);

    /** SECTION: Instance Variables ================================================================================= */
    public final double coefficient, degree;

    /** SECTION: Factory Methods ==================================================================================== */
    public static LockedEqualityPivot<SymbolicExpression, SymbolicInfinity> create() {
        return (LockedEqualityPivot<SymbolicExpression, SymbolicInfinity>) new SymbolicInfinity(1, 1).close();
    }

    public static LockedEqualityPivot<SymbolicExpression, SymbolicInfinity> create(double coefficient, double degree) {
        return (LockedEqualityPivot<SymbolicExpression, SymbolicInfinity>) new SymbolicInfinity(coefficient, degree).close();
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected SymbolicInfinity(double coefficient, double degree) {
        super(coefficient * Math.pow(SymbolicInfinity.INFINITY_VALUE, degree));
        this.coefficient = coefficient;
        this.degree = degree;
    }

    /** SECTION: Print Format ======================================================================================= */
    public String toString() {
        return "Infinity(" + this.coefficient + " * \u221E ** " + this.degree + ")";
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Expression ====================================================================================== */
    public EqualityPivot<? extends Unicardinal> close() {
        Unicardinal result;
        if (this.degree == 0 || this.coefficient == 0) {
            result = new SymbolicReal(this.coefficient);
        } else {
            result = this;
        }
        return Diagram.retrieve(result);
    }
}
