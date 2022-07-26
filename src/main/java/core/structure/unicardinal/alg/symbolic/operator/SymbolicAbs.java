package core.structure.unicardinal.alg.symbolic.operator;

import core.Diagram;
import core.Propositions.equalitypivot.EqualityPivot;
import core.Propositions.equalitypivot.unicardinal.UnicardinalPivot;
import core.structure.unicardinal.*;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;

import java.util.*;

public class SymbolicAbs extends DefinedUnicardinal implements SymbolicExpression {
    /** SECTION: Static Data ======================================================================================== */
    public static final UnicardinalInputType<SymbolicExpression> EXPRESSION = new UnicardinalInputType<>();

    public static final List<InputType<?>> inputTypes = List.of(SymbolicAbs.EXPRESSION);

    /** SECTION: Instance Variables ================================================================================= */
    public UnicardinalPivot<SymbolicExpression> expression;

    /** SECTION: Factory Methods ==================================================================================== */
    public static UnicardinalPivot<SymbolicExpression> create(UnicardinalPivot<SymbolicExpression> expression) {
        return new SymbolicAbs(expression).close();
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected SymbolicAbs(UnicardinalPivot<SymbolicExpression> expression) {
        super();
        this.expression = expression;

        Unicardinal.createComputationalEdge(this, this.expression);
        this.expression.reverseDependencies().add(this);
    }

    /** SECTION: Print Format ======================================================================================= */
    public String toString() {
        return "|" + this.expression + "|";
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public void updateLocalVariables(EqualityPivot<?> consumedPivot, EqualityPivot<?> consumerPivot) {
        this.expression = (UnicardinalPivot<SymbolicExpression>) consumerPivot; // Only one local variable that can possibly hold consumedPivot
    }

    public List<InputType<?>> getInputTypes() {
        return SymbolicAbs.inputTypes;
    }

    /** SUBSECTION: Unicardinal ===================================================================================== */
    public void computeValue() {
        this.value.set(Math.abs(this.expression.doubleValue()));
    }

    /** SUBSECTION: Expression ====================================================================================== */
    public UnicardinalPivot<SymbolicExpression> expand() {
        return SymbolicAbs.create((UnicardinalPivot<SymbolicExpression>) this.expression.element().expand());
    }

    public UnicardinalPivot<SymbolicExpression> close() {
        UnicardinalPivot<?> result = null;
        if (this.expression.element() instanceof SymbolicPow powExpr && powExpr.coefficient % 2 == 0) {
            result = this.expression;
        } else if (this.expression.element() instanceof SymbolicScale scaleExpr) {
            result = SymbolicScale.create(Math.abs(scaleExpr.coefficient), SymbolicAbs.create((UnicardinalPivot<SymbolicExpression>) scaleExpr.expression));
        }
        if (result == null) {
            return Diagram.retrieve(this);
        } else {
            return (UnicardinalPivot<SymbolicExpression>) result.merge((UnicardinalPivot<?>) this.equalityPivot);
        }
    }

    public int getDegree() {
        return this.expression.element().getDegree();
    }
}
