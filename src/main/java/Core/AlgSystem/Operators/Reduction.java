package Core.AlgSystem.Operators;

import Core.AlgSystem.UnicardinalRings.Symbolic;
import Core.Diagram;
import Core.EntityStructure.UnicardinalStructure.*;
import Core.Utilities.Comparators.UnicardinalComparator;

import java.util.TreeMap;

public abstract class Reduction<T> extends DefinedOperator<T> {
    /** SECTION: Static Data ======================================================================================== */
    public enum Parameter implements InputType {
        TERMS
    }
    public static final InputType[] staticInputTypes = {Parameter.TERMS};

    /** SECTION: Instance Variables ================================================================================= */
    public TreeMap<Expression<T>, Double> terms = new TreeMap<>(new UnicardinalComparator<>());
    public int degree = 0;

    /** SECTION: Abstract Constructor =============================================================================== */
    protected Reduction(Diagram d, Iterable<Expression<T>> args, Class<T> type) {
        super(d, type);
        this.construct(args);
    }

    /** SECTION: Interface ========================================================================================== */
    protected abstract void construct(Iterable<Expression<T>> args);

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public InputType[][] getInputTypes() {
        return this.inputTypes;
    }

    /** SUBSECTION: DefinedOperator ================================================================================= */
    public InputType[] getStaticInputTypes() {
        return Reduction.staticInputTypes;
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
