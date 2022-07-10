package core.structure.unicardinal.alg.structure;

import core.structure.unicardinal.alg.*;
import core.util.Utils;

import java.util.*;

public abstract class Reduction extends DefinedExpression {
    /** SECTION: Static Data ======================================================================================== */
    public static final InputType<Expression> TERMS = new InputType<>(Expression.class, Utils.UNICARDINAL_COMPARATOR);

    public static final List<InputType<?>> inputTypes = List.of(Reduction.TERMS);

    /** SECTION: Instance Variables ================================================================================= */
    public int degree = 0;

    /** SECTION: Abstract Constructor =============================================================================== */
    protected Reduction(Collection<? extends Expression> args) {
        super();
        this.getInputs(Reduction.TERMS).addAll(args);

        args.forEach(arg -> arg.reverseComputationalDependencies().add(this));
    }

    /** SECTION: Interface ========================================================================================== */
    protected abstract int identity();

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public List<InputType<?>> getInputTypes() {
        return Reduction.inputTypes;
    }

    /** SUBSECTION: Expression ====================================================================================== */
    public int getDegree() {
        return this.degree;
    }
}
