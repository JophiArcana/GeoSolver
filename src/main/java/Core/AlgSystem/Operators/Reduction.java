package Core.AlgSystem.Operators;

import Core.AlgSystem.UnicardinalRings.Symbolic;
import Core.AlgSystem.UnicardinalStructure.*;
import Core.EntityStructure.UnicardinalStructure.DefinedExpression;
import Core.EntityStructure.UnicardinalStructure.Expression;

import java.util.TreeMap;

public abstract class Reduction<T> extends DefinedExpression<T> {
    /** SECTION: Static Data ======================================================================================== */
    public enum Parameter implements InputType {
        TERMS
    }
    public static final InputType[] inputTypes = {Parameter.TERMS};

    /** SECTION: Instance Variables ================================================================================= */
    public TreeMap<Expression<T>, Double> terms = new TreeMap<>();
    public int degree = 0;

    /** SECTION: Abstract Constructor =============================================================================== */
    protected Reduction(Iterable<Expression<T>> args, Class<T> type) {
        super(type);
        this.construct(args);
    }

    /** SECTION: Interface ========================================================================================== */
    protected abstract void construct(Iterable<Expression<T>> args);

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public InputType[] getInputTypes() {
        return Reduction.inputTypes;
    }

    /** SUBSECTION: Expression ====================================================================================== */
    public int getDegree() {
        if (this.TYPE == Symbolic.class) {
            return this.degree;
        } else {
            return 0;
        }
    }
}
