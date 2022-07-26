package core.util;

import java.util.*;

import core.structure.unicardinal.Constant;
import core.structure.unicardinal.Unicardinal;
import core.structure.unicardinal.Variable;
import core.structure.unicardinal.alg.structure.Accumulation;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;
import core.structure.unicardinal.alg.structure.Reduction;
import core.structure.unicardinal.alg.symbolic.operator.*;


public class AlgEngine {
    public static final double LOG2 = Math.log(2);

    /** SECTION: Simplification Optimization ======================================================================== */
    public double numberOfOperations(UnicardinalPivot<?> expr) {
        switch (expr.element()) {
            case Reduction reduction:
                Collection<UnicardinalPivot<?>> terms = Utils.cast(reduction.getInputs(Reduction.TERMS));
                double operations = (double) terms.size() - 1;
                for (UnicardinalPivot<?> term : terms) {
                    operations += this.numberOfOperations(term);
                }
                return operations;
            case Accumulation accumulation:
                return Math.abs(Math.log(Math.abs(accumulation.coefficient))) / LOG2 + this.numberOfOperations(accumulation.expression);
            case Variable variable:
                return 1;
            case Constant constant:
                return 0;
            default:
                return Double.MAX_VALUE;
        }
    }

    /** SECTION: Basic operations =================================================================================== */
    public <T extends Unicardinal> UnicardinalPivot<T> add(Collection<UnicardinalPivot<T>> args) {
        return (T) args.iterator().next().createAdd(args);
    }

    public <T extends Unicardinal> T add(T arg1, T arg2) {
        return (T) arg1.createAdd(List.of(arg1, arg2));
    }

    public <T extends Unicardinal> T sub(T arg1, T arg2) {
        return (T) arg1.createAdd(List.of(arg1, arg2.createScale(-1, arg2)));
    }

    public SymbolicExpression mul(Collection<SymbolicExpression> args) {
        return SymbolicMul.create(args);
    }

    public SymbolicExpression mul(SymbolicExpression arg1, SymbolicExpression arg2) {
        return SymbolicMul.create(arg1, arg2);
    }

    public SymbolicExpression div(SymbolicExpression arg1, SymbolicExpression arg2) {
        return SymbolicMul.create(arg1, SymbolicPow.create(arg2, -1));
    }

    public SymbolicExpression pow(SymbolicExpression base, double exponent) {
        return SymbolicPow.create(base, exponent);
    }

    public <T extends Unicardinal> T negate(T arg) {
        return (T) arg.createScale(-1, arg);
    }

    public SymbolicExpression invert(SymbolicExpression arg) {
        return SymbolicPow.create(arg, -1);
    }

    /** SECTION: Cyclic Sum ========================================================================================= */
    public UnicardinalPivot<SymbolicExpression> norm(UnicardinalPivot<SymbolicExpression> x, UnicardinalPivot<SymbolicExpression> y) {
        return SymbolicPow.create(SymbolicAdd.create(SymbolicPow.create(x, 2), SymbolicPow.create(y, 2)), 0.5);
    }

    public UnicardinalPivot<SymbolicExpression> norm2(UnicardinalPivot<SymbolicExpression> x, UnicardinalPivot<SymbolicExpression> y) {
        return SymbolicAdd.create(SymbolicPow.create(x, 2), SymbolicPow.create(y, 2));
    }

    public UnicardinalPivot<SymbolicExpression> dot(List<UnicardinalPivot<SymbolicExpression>> u, List<UnicardinalPivot<SymbolicExpression>> v) {
        assert u.size() == v.size();
        UnicardinalPivot<SymbolicExpression>[] terms = new UnicardinalPivot[u.size()];
        for (int i = 0; i < u.size(); i++) {
            terms[i] = SymbolicMul.create(u.get(i), v.get(i));
        }
        return SymbolicAdd.create(terms);
    }
}