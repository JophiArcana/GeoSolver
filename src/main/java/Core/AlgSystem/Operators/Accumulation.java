package Core.AlgSystem.Operators;

import Core.AlgSystem.UnicardinalTypes.*;

public abstract class Accumulation<T> extends DefinedExpression<T> {
    /** SECTION: Static Data ======================================================================================== */
    public enum Parameter implements InputType {
        TERMS
    }
    public static final InputType[] inputTypes = {Parameter.TERMS};

    /** SECTION: Instance Variables ================================================================================= */
    public int degree = 0;

    /** SECTION: Abstract Constructor =============================================================================== */
    protected Accumulation(Iterable<Expression<T>> args, Class<T> type) {
        super(type);
        this.construct(args);
    }

    /** SECTION: Interface ========================================================================================== */
    protected abstract void construct(Iterable<Expression<T>> args);

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public InputType[] getInputTypes() {
        return Accumulation.inputTypes;
    }

    /** SUBSECTION: Expression ====================================================================================== */
    public int getDegree() {
        return this.degree;
    }
}
