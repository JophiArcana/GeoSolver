package core.structure.unicardinal.alg.symbolic.operator;

import core.Diagram;
import core.structure.equalitypivot.EqualityPivot;
import core.structure.unicardinal.DefinedUnicardinal;
import core.structure.unicardinal.Unicardinal;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;

import java.util.*;

public class SymbolicAbs extends DefinedUnicardinal implements SymbolicExpression {
    /** SECTION: Static Data ======================================================================================== */
    public static final InputType<SymbolicExpression> EXPRESSION = new InputType<>();

    public static final List<InputType<?>> inputTypes = List.of(SymbolicAbs.EXPRESSION);

    /** SECTION: Instance Variables ================================================================================= */
    public EqualityPivot<SymbolicExpression> expression;

    /** SECTION: Factory Methods ==================================================================================== */
    public static EqualityPivot<SymbolicExpression> create(EqualityPivot<SymbolicExpression> expression) {
        return (EqualityPivot<SymbolicExpression>) new SymbolicAbs(expression).close();
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected SymbolicAbs(EqualityPivot<SymbolicExpression> expression) {
        super();
        this.expression = expression;

        Unicardinal.createComputationalEdge(this, this.expression);
        this.expression.reverseDependencies.add(this);
    }

    /** SECTION: Print Format ======================================================================================= */
    public String toString() {
        return "|" + this.expression + "|";
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public void updateLocalVariables(EqualityPivot<?> consumedPivot, EqualityPivot<?> consumerPivot) {
        this.expression = (EqualityPivot<SymbolicExpression>) consumerPivot; // Only one local variable that can possibly hold consumedPivot
    }

    public List<InputType<?>> getInputTypes() {
        return SymbolicAbs.inputTypes;
    }

    /** SUBSECTION: Unicardinal ===================================================================================== */
    public void computeValue() {
        this.value.set(Math.abs(this.expression.doubleValue()));
    }

    /** SUBSECTION: Expression ====================================================================================== */
    public EqualityPivot<? extends Unicardinal> expand() {
        return SymbolicAbs.create((EqualityPivot<SymbolicExpression>) this.expression.simplestElement.expand());
    }

    public EqualityPivot<? extends Unicardinal> close() {
        EqualityPivot<?> result;
        if (this.expression.simplestElement instanceof SymbolicPow powExpr && powExpr.coefficient % 2 == 0) {
            result = this.expression;
        } else if (this.expression.simplestElement instanceof SymbolicScale scaleExpr) {
            result = SymbolicScale.create(Math.abs(scaleExpr.coefficient), SymbolicAbs.create((EqualityPivot<SymbolicExpression>) scaleExpr.expression));
        } else {
            result = Diagram.retrieve(this);
        }
        return (EqualityPivot<? extends Unicardinal>) this.mergeResult(result);
    }

    public int getDegree() {
        return this.expression.simplestElement.getDegree();
    }
}
