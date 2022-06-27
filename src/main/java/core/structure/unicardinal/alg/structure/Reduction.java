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
    public TreeMap<Expression, Double> terms = new TreeMap<>(new UnicardinalComparator());
    public int degree = 0;

    /** SECTION: Abstract Constructor =============================================================================== */
    protected Reduction(Iterable<? extends Expression> args) {
        super();
        this.construct(args);
        this.constructorClose();
        this.computeValue();
    }

    protected Reduction(Expression... args) {
        super();
        this.construct(args);
        this.constructorClose();
        this.computeValue();
    }

    private void constructorClose() {
        TreeMultiset<Expression> inputTerms = this.getInputs(Reduction.TERMS);
        for (Map.Entry<Expression, Double> entry : new ArrayList<>(this.terms.entrySet())) {
            if (entry.getValue() == 0) {
                this.terms.remove(entry.getKey());
            } else {
                Expression term = this.createAccumulation(entry.getValue(), entry.getKey());
                inputTerms.add(term);
                term.reverseDependencies().add(this);
            }
        }
    }

    /** SECTION: Interface ========================================================================================== */
    protected abstract void construct(Iterable<? extends Expression> args);
    protected abstract void construct(Expression... args);
    protected abstract Real identity();
    public abstract Expression createAccumulation(double coefficient, Expression expr);

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public List<InputType<?>> getInputTypes() {
        return Reduction.inputTypes;
    }

    /** SUBSECTION: Expression ====================================================================================== */
    public Expression close() {
        if (this.terms.size() == 0) {
            return this.identity();
        } else if (this.terms.size() == 1) {
            return (Expression) this.inputs.get(Reduction.TERMS).firstEntry().getElement();
        } else {
            return this;
        }
    }

    public int getDegree() {
        return this.degree;
    }
}
