package core.structure.unicardinal.alg.structure;

import core.Diagram;
import core.structure.equalitypivot.*;
import core.structure.unicardinal.*;

import java.util.List;

public abstract class Accumulation extends DefinedUnicardinal {
    /** SECTION: Static Data ======================================================================================== */
    public static final InputType<Unicardinal> EXPRESSION = new InputType<>();

    public static final List<InputType<?>> inputTypes = List.of(Accumulation.EXPRESSION);

    /** SECTION: Instance Variables ================================================================================= */
    public final double coefficient;
    public EqualityPivot<? extends Unicardinal> expression;

    /** SECTION: Abstract Constructor =============================================================================== */
    protected Accumulation(double coefficient, EqualityPivot<? extends Unicardinal> expr) {
        super();
        this.coefficient = coefficient;
        this.expression = expr;
        this.getInputs(Accumulation.EXPRESSION).add(this.expression);

        Unicardinal.createComputationalEdge(this, this.expression);
        this.expression.reverseDependencies.add(this);
    }

    /** SECTION: Interface ========================================================================================== */
    protected abstract int identity();
    protected abstract LockedEqualityPivot<? extends Unicardinal, ? extends Constant> evaluateConstant(double coefficient, Constant expression);

    protected abstract Accumulation createRawAccumulation(double coefficient, EqualityPivot<? extends Unicardinal> expression);

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public void updateLocalVariables(EqualityPivot<?> consumedPivot, EqualityPivot<?> consumerPivot) {
        this.expression = (EqualityPivot<? extends Unicardinal>) consumerPivot; // Only one local variable that can possibly hold consumedPivot
    }

    public List<InputType<?>> getInputTypes() {
        return Accumulation.inputTypes;
    }

    /** SUBSECTION: Expression ====================================================================================== */
    public EqualityPivot<? extends Unicardinal> close() {
        EqualityPivot<? extends Unicardinal> result;
        if (this.coefficient == 0) {
            result = this.createReal(this.identity());
        } else if (this.expression.simplestElement instanceof Constant constExpr) {
            result = this.evaluateConstant(this.coefficient, constExpr);
        } else if (this.getClass() == this.expression.simplestElement.getClass()) {
            Accumulation acc = (Accumulation) this.expression.simplestElement;
            result = Diagram.retrieve(this.createRawAccumulation(this.coefficient * acc.coefficient, acc.expression));
        } else {
            result = Diagram.retrieve(this);
        }
        return (EqualityPivot<? extends Unicardinal>) EqualityPivot.merge((EqualityPivot) this.equalityPivot, (EqualityPivot) result);
    }
}


