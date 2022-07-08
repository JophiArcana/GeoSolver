package core.util;

import java.util.*;

import core.structure.unicardinal.alg.Constant;
import core.structure.unicardinal.alg.Expression;
import core.structure.unicardinal.alg.Variable;
import core.structure.unicardinal.alg.structure.Accumulation;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;
import core.structure.unicardinal.alg.structure.Reduction;
import core.structure.unicardinal.alg.symbolic.operator.*;


public class AlgEngine {
    public static final double EPSILON = 1E-9;
    public static final double LOG2 = Math.log(2);

    /** SECTION: Simplification Optimization ======================================================================== */
    public double numberOfOperations(Expression expr) {
        switch (expr) {
            case Reduction reduction:
                ArrayList<Expression> terms = Utils.cast(reduction.inputs.get(Reduction.TERMS));
                double operations = (double) terms.size() - 1;
                for (Expression term : terms) {
                    operations += this.numberOfOperations(term);
                }
                return operations;
            case Accumulation accumulation:
                return Math.abs(Math.log(Math.abs(accumulation.coefficient))) / LOG2 + this.numberOfOperations(accumulation.expression);
            case Variable variable:
                return 1;
            case Constant constant:
                return 0;
            case null:
            default:
                return Double.MAX_VALUE;
        }
    }

    /** SECTION: Greatest Common Divisor ============================================================================ */
    public SymbolicExpression greatestCommonDivisor(SymbolicExpression e1, SymbolicExpression e2) {
        if (e1 instanceof SymbolicScale sc1 && e2 instanceof SymbolicScale sc2) {
            return SymbolicScale.create(
                    Utils.gcd(sc1.coefficient, sc2.coefficient),
                    this.greatestCommonDivisor((SymbolicExpression) sc1.expression, (SymbolicExpression) sc2.expression)
            );
        } else if (e1 instanceof SymbolicScale sc) {
            return this.greatestCommonDivisor((SymbolicExpression) sc.expression, e2);
        } else if (e2 instanceof SymbolicScale sc) {
            return this.greatestCommonDivisor(e1, (SymbolicExpression) sc.expression);
        } else {
            HashMap<SymbolicExpression, Double> factors1 = form(e1), factors2 = form(e2);
            HashSet<SymbolicExpression> terms = new HashSet<>(factors1.keySet());
            terms.addAll(factors2.keySet());

            HashMap<SymbolicExpression, Double> result = new HashMap<>();
            for (SymbolicExpression term : terms) {
                double exponent = Math.min(factors1.getOrDefault(term, 0.0), factors2.getOrDefault(term, 0.0));
                if (exponent != 0) {
                    result.put(term, exponent);
                }
            }
            return SymbolicMul.create(Utils.map(result.entrySet(), entry -> SymbolicPow.create(entry.getKey(), entry.getValue())));
        }
    }

    public HashMap<SymbolicExpression, Double> form(SymbolicExpression expr) {
        if (expr instanceof SymbolicPow pow) {
            return new HashMap<>(Map.of((SymbolicExpression) pow.expression, pow.coefficient));
        } else if (expr instanceof SymbolicMul mul) {
            return new HashMap<>() {{
                    mul.getInputs(Reduction.TERMS).forEach(arg -> {
                        if (arg instanceof SymbolicPow powExpr) {
                            put((SymbolicExpression) powExpr.expression, powExpr.coefficient);
                        } else {
                            put((SymbolicExpression) arg, 1.0);
                        }
                    });
            }};
        } else {
            return new HashMap<>(Map.of(expr, 1.0));
        }
    }

    /** SECTION: Basic operations =================================================================================== */
    public <T extends Expression> T add(Iterable<T> args) {
        return (T) args.iterator().next().createAdd(args);
    }

    public <T extends Expression> T add(T arg1, T arg2) {
        return (T) arg1.createAdd(List.of(arg1, arg2));
    }

    public <T extends Expression> T sub(T arg1, T arg2) {
        return (T) arg1.createAdd(List.of(arg1, arg2.createScale(-1, arg2)));
    }

    public SymbolicExpression mul(Iterable<SymbolicExpression> args) {
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

    public <T extends Expression> T negate(T arg) {
        return (T) arg.createScale(-1, arg);
    }

    public SymbolicExpression invert(SymbolicExpression arg) {
        return SymbolicPow.create(arg, -1);
    }

    /** SECTION: Cyclic Sum ========================================================================================= */
    public SymbolicExpression norm(SymbolicExpression x, SymbolicExpression y) {
        return SymbolicPow.create(SymbolicAdd.create(SymbolicPow.create(x, 2), SymbolicPow.create(y, 2)), 0.5);
    }

    public SymbolicExpression norm2(SymbolicExpression x, SymbolicExpression y) {
        return SymbolicAdd.create(SymbolicPow.create(x, 2), SymbolicPow.create(y, 2));
    }

    public SymbolicExpression dot(List<SymbolicExpression> u, List<SymbolicExpression> v) {
        assert u.size() == v.size();
        SymbolicExpression[] terms = new SymbolicExpression[u.size()];
        for (int i = 0; i < u.size(); i++) {
            terms[i] = SymbolicMul.create(u.get(i), v.get(i));
        }
        return SymbolicAdd.create(terms);
    }
}