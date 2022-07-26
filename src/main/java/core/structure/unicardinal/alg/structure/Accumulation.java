package core.structure.unicardinal.alg.structure;

import core.Diagram;
import core.Propositions.equalitypivot.EqualityPivot;
import core.Propositions.equalitypivot.unicardinal.*;
import core.structure.unicardinal.*;

import java.util.List;

public abstract class Accumulation extends DefinedUnicardinal {
    /** SECTION: Static Data ======================================================================================== */
    public static final UnicardinalInputType<Unicardinal> EXPRESSION = new UnicardinalInputType<>();

    public static final List<InputType<?>> inputTypes = List.of(Accumulation.EXPRESSION);

    /** SECTION: Instance Variables ================================================================================= */
    public final double coefficient;
    public UnicardinalPivot<?> expression;

    /** SECTION: Abstract Constructor =============================================================================== */
    protected Accumulation(double coefficient, UnicardinalPivot<?> expr) {
        super();
        this.coefficient = coefficient;
        this.expression = expr;
        this.getInputs(Accumulation.EXPRESSION).add(this.expression);

        Unicardinal.createComputationalEdge(this, this.expression);
        this.expression.reverseDependencies().add(this);
    }

    /** SECTION: Interface ========================================================================================== */
    protected abstract int identity();
    protected abstract LockedUnicardinalPivot<?, ? extends Constant> evaluateConstant(double coefficient, Constant expression);

    protected abstract Accumulation createRawAccumulation(double coefficient, UnicardinalPivot<?> expression);

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public void updateLocalVariables(EqualityPivot<?> consumedPivot, EqualityPivot<?> consumerPivot) {
        this.expression = (UnicardinalPivot<?>) consumerPivot; // Only one local variable that can possibly hold consumedPivot
    }

    public List<InputType<?>> getInputTypes() {
        return Accumulation.inputTypes;
    }

    /** SUBSECTION: Expression ====================================================================================== */
    public UnicardinalPivot<?> close() {
        UnicardinalPivot<?> result = null;
        if (this.coefficient == 0) {
            result = this.createConstant(this.identity());
        } else if (this.expression.element() instanceof Constant constExpr) {
            result = this.evaluateConstant(this.coefficient, constExpr);
        } else if (this.getClass() == this.expression.element().getClass()) {
            Accumulation acc = (Accumulation) this.expression.element();
            result = Diagram.retrieve(this.createRawAccumulation(this.coefficient * acc.coefficient, acc.expression));
        }
        if (result == null) {
            return Diagram.retrieve(this);
        } else {
            return result.merge((UnicardinalPivot<?>) this.equalityPivot);
        }
    }
}


