package core.structure.unicardinal.alg.structure;

import core.structure.unicardinal.alg.*;
import core.util.Utils;
import core.util.comparators.UnicardinalComparator;
import com.google.common.collect.TreeMultiset;

import java.util.*;

public abstract class Reduction extends DefinedExpression {
    /** SECTION: Static Data ======================================================================================== */
    public static final InputType<Expression> TERMS = new InputType<>(Expression.class, Utils.UNICARDINAL_COMPARATOR);

    public static final List<InputType<?>> inputTypes = List.of(Reduction.TERMS);

    /** SECTION: Instance Variables ================================================================================= */
    protected static final TreeMap<Expression, Double> TERM_MAP = new TreeMap<>(new UnicardinalComparator());
    protected static double CONSTANT;
    public int degree = 0;

    /** SECTION: Abstract Constructor =============================================================================== */
    protected Reduction(Iterable<? extends Expression> args) {
        super();
        Reduction.CONSTANT = 1;
        Reduction.TERM_MAP.clear();
        this.construct(args);
        TreeMultiset<Expression> inputTerms = this.getInputs(Reduction.TERMS);
        for (Map.Entry<Expression, Double> entry : Reduction.TERM_MAP.entrySet()) {
            if (entry.getValue() != 0) {
                Expression term = this.createAccumulation(entry.getValue(), entry.getKey());
                inputTerms.add(term);
                term.reverseSymbolicDependencies().add(this);
            }
        }
        this.computeValue();
    }

    /** SECTION: Interface ========================================================================================== */
    protected abstract void construct(Iterable<? extends Expression> args);
    protected abstract int identity();
    public abstract Expression createAccumulation(double coefficient, Expression expr);

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public List<InputType<?>> getInputTypes() {
        return Reduction.inputTypes;
    }

    /** SUBSECTION: Expression ====================================================================================== */
    public Expression close() {
        TreeMultiset<Expression> inputTerms = this.getInputs(Reduction.TERMS);
        return switch (inputTerms.size()) {
            case 0 -> this.createReal(this.identity());
            case 1 -> inputTerms.elementSet().first();
            default -> this;
        };
    }

    public int getDegree() {
        return this.degree;
    }
}
