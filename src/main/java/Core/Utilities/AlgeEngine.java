package Core.Utilities;

import Core.AlgeSystem.*;
import Core.AlgeSystem.Functions.*;
import Core.EntityTypes.*;

import java.util.*;
import java.util.function.Function;

public class AlgeEngine {
    public static final Univariate X = new Univariate("x");
    public static final double EPSILON = Math.pow(10, -9);

    /** SECTION: Order of Growth ==================================================================================== */

    public static Expression orderOfGrowth(Expression expr, Univariate s) {
        if (expr == null) {
            return null;
        }
        expr = (Expression) expr.simplify();
        if (expr instanceof Add addExpr) {
            TreeMap<Expression, Constant> orders = new TreeMap<>(new OrderOfGrowthComparator(s));
            if (!addExpr.constant.equals(Constant.ZERO)) {
                orders.put(Constant.ONE, addExpr.constant);
            }
            for (Entity ent : addExpr.inputs.get("Terms")) {
                Expression termOrder = AlgeEngine.orderOfGrowth((Expression) ent, s);
                Expression baseOrder = termOrder;
                if (termOrder instanceof Mul mulOrder) {
                    baseOrder = mulOrder.baseForm();
                    orders.put(baseOrder, (Constant) AlgeEngine.add(orders.get(baseOrder), mulOrder.constant));
                } else {
                    orders.put(termOrder, (Constant) AlgeEngine.add(orders.get(termOrder), Constant.ONE));
                }
                if (orders.get(baseOrder).equals(Constant.ZERO)) {
                    orders.remove(baseOrder);
                }
            }
            Map.Entry<Expression, Constant> greatestOrder = orders.lastEntry();
            return AlgeEngine.mul(greatestOrder.getValue(), greatestOrder.getKey());
        } else if (expr instanceof Mul mulExpr) {
            ArrayList<Expression> termOrders = Utils.map(new ArrayList<>(mulExpr.inputs.get("Terms")), arg ->
                    AlgeEngine.orderOfGrowth((Expression) arg, s));
            return AlgeEngine.mul(mulExpr.constant, AlgeEngine.mul(termOrders.toArray()));
        } else if (expr instanceof Pow powExpr) {
            return AlgeEngine.pow(AlgeEngine.orderOfGrowth(powExpr.base, s), powExpr.exponent);
        } else if (expr instanceof Log logExpr) {
            return AlgeEngine.log(AlgeEngine.orderOfGrowth(logExpr.input, s));
        } else if (expr instanceof Univariate || expr instanceof Constant) {
            return expr;
        } else {
            /** TODO: Implement Order of Growth for other Functions */
            return null;
        }
    }

    public static Expression orderOfGrowth(Expression expr) {
        if (expr == null) {
            return null;
        }
        ArrayList<Mutable> variables = new ArrayList<>(expr.variables());
        return AlgeEngine.orderOfGrowth(expr, (Univariate) variables.get(0));
    }

    /** SECTION: Greatest Common Divisor ============================================================================ */

    public static Expression greatestCommonDivisor(Expression e1, Expression e2) {
        if (e1.equals(Constant.ZERO)) {
            return e2;
        } else if (e2.equals(Constant.ZERO)) {
            return e1;
        } else {
            Expression.Factorization e1Norm = e1.normalize();
            Expression.Factorization e2Norm = e2.normalize();
            Constant constant = e1Norm.constant.gcd(e2Norm.constant);
            Expression product = Constant.ONE;
            TreeSet<Expression> terms = new TreeSet<>(Utils.PRIORITY_COMPARATOR);
            terms.addAll(e1Norm.terms.keySet());
            terms.addAll(e2Norm.terms.keySet());
            for (Expression term : terms) {
                Expression e1Exponent = e1Norm.terms.get(term);
                Expression e2Exponent = e2Norm.terms.get(term);
                Expression exponent = (Utils.GROWTH_COMPARATOR.compare(e1Exponent, e2Exponent) < 0) ? e1Exponent : e2Exponent;
                if (exponent != null && !exponent.equals(Constant.ZERO)) {
                    product = AlgeEngine.mul(product, AlgeEngine.pow(term, exponent));
                }
            }
            return AlgeEngine.mul(constant, product);
        }
    }

    public static Expression greatestCommonDivisor(Expression ... args) {
        if (args.length == 1) {
            return args[0];
        } else if (args.length == 2) {
            return AlgeEngine.greatestCommonDivisor(args[0], args[1]);
        } else {
            Expression[] rest = new Expression[args.length - 1];
            System.arraycopy(args, 1, rest, 0, args.length - 1);
            return AlgeEngine.greatestCommonDivisor(args[0], AlgeEngine.greatestCommonDivisor(rest));
        }
    }

    /** SECTION: Basic operations =================================================================================== */

    public static Complex complex(Number re, Number im) {
        return new Complex(re, im);
    }

    public static Expression add(Object ... args) {
        Expression[] exprArgs = new Expression[args.length];
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof Number n) {
                exprArgs[i] = new Complex(n, 0);
            } else {
                exprArgs[i] = (args[i] instanceof Expression expr) ? expr : Constant.ZERO;
            }
        }
        return (Expression) (new Add(exprArgs)).simplify();
    }

    public static Expression sub(Object o1, Object o2) {
        if (o1 instanceof Number n1) {
            o1 = new Complex(n1, 0);
        }
        if (o2 instanceof Number n2) {
            o2 = new Complex(n2, 0);
        }
        Expression expr1 = (o1 instanceof Expression e1) ? (Expression) e1.simplify() : Constant.ZERO;
        Expression expr2 = (o2 instanceof Expression e2) ? (Expression) e2.simplify() : Constant.ZERO;
        if (expr1.equals(Constant.ZERO)) {
            return AlgeEngine.negate(expr2);
        } else if (expr2.equals(Constant.ZERO)) {
            return expr1;
        } else if (expr1 instanceof Constant const1 && expr2 instanceof Constant const2) {
            return const1.sub(const2);
        } else {
            return (Expression) (new Add(expr1, AlgeEngine.negate(expr2))).simplify();
        }
    }

    public static Expression mul(Object ... args) {
        Expression[] exprArgs = new Expression[args.length];
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof Number n) {
                exprArgs[i] = new Complex(n, 0);
            } else {
                exprArgs[i] = (args[i] instanceof Expression expr) ? expr : Constant.ZERO;
            }
        }
        return (Expression) (new Mul(exprArgs)).simplify();
    }

    public static Expression div(Object o1, Object o2) {
        if (o1 instanceof Number n1) {
            o1 = new Complex(n1, 0);
        }
        if (o2 instanceof Number n2) {
            o2 = new Complex(n2, 0);
        }
        Expression expr1 = (o1 instanceof Expression e1) ? (Expression) e1.simplify() : Constant.ZERO;
        Expression expr2 = (o2 instanceof Expression e2) ? (Expression) e2.simplify() : Constant.ZERO;
        if (expr1.equals(Constant.ONE)) {
            return AlgeEngine.invert(expr2);
        } else if (expr2.equals(Constant.ONE)) {
            return expr1;
        } else if (expr1 instanceof Constant const1 && expr2 instanceof Constant const2) {
            return const1.div(const2);
        } else {
            return (Expression) (new Mul(expr1, AlgeEngine.invert(expr2))).simplify();
        }
    }

    public static Expression negate(Object obj) {
        if (obj instanceof Number n) {
            obj = new Complex(n, 0);
        }
        return (obj instanceof Expression expr) ? AlgeEngine.mul(expr, Constant.NONE) : null;
    }

    public static Expression invert(Object obj) {
        if (obj instanceof Number n) {
            obj = new Complex(n, 0);
        }
        return (obj instanceof Expression expr) ? AlgeEngine.pow(expr, Constant.NONE) : null;
    }

    /** SECTION: Cyclic Sum ========================================================================================= */

    public static Expression cyclicSum(Function<ArrayList<Expression>, Expression> func, ArrayList<Expression> args) {
        ArrayList<Expression> terms = new ArrayList<>();
        for (int i = 0; i < args.size(); i++) {
            terms.add(func.apply(args));
            args.add(0, args.remove(args.size() - 1));
        }
        return AlgeEngine.add(terms.toArray());
    }

    /** SECTION: Exponential Functions ============================================================================== */

    public static Expression pow(Object base, Object exponent) {
        if (base instanceof Number n1) {
            base = new Complex(n1, 0);
        }
        if (exponent instanceof Number n2) {
            exponent = new Complex(n2, 0);
        }
        Expression baseExpr = (Expression) base;
        Expression exponentExpr = (Expression) exponent;
        baseExpr = (baseExpr == null) ? Constant.ZERO : (Expression) baseExpr.simplify();
        exponentExpr = (exponentExpr == null) ? Constant.ONE : (Expression) exponentExpr.simplify();
        if (baseExpr instanceof Constant baseConst && exponentExpr instanceof Constant expConst) {
            return baseConst.pow(expConst);
        } else {
            return (Expression) (new Pow(baseExpr, exponentExpr)).simplify();
        }
    }

    public static Expression exp(Object obj) {
        return AlgeEngine.pow(Constant.E, obj);
    }

    public static Expression log(Object obj) {
        if (obj instanceof Number n) {
            obj = new Complex(n, 0);
        }
        Expression expr = (Expression) obj;
        expr = (expr == null) ? Constant.ONE : (Expression) expr.simplify();
        if (expr instanceof Constant constExpr) {
            return constExpr.log();
        } else {
            return (Expression) (new Log(expr)).simplify();
        }
    }

    /** SECTION: Real, Imaginary, Conjugate ========================================================================= */

    public static Expression real(Object obj) {
        if (obj instanceof Number n) {
            return new Complex(n, 0);
        } else {
            Expression expr = (Expression) obj;
            if (expr == null) {
                return null;
            } else if (expr instanceof Complex cpx) {
                return new Complex(cpx.re, 0);
            } else if (expr instanceof Infinity inf) {
                return (Constant) (new Infinity(AlgeEngine.real(inf.expression))).simplify();
            } else if (expr instanceof Univariate univariateExpr) {
                return univariateExpr;
            } else if (expr instanceof Add addExpr) {
                return AlgeEngine.add(AlgeEngine.real(addExpr.constant),
                        AlgeEngine.add(Utils.map(new ArrayList<>(addExpr.inputs.get("Terms")), AlgeEngine::real).toArray()));
            } else if (expr instanceof Log logExpr) {
                Expression re = AlgeEngine.real(logExpr.input);
                Expression im = AlgeEngine.imaginary(logExpr.input);
                return AlgeEngine.mul(0.5, AlgeEngine.log(AlgeEngine.add(AlgeEngine.pow(re, 2), AlgeEngine.pow(im, 2))));
            } else {
                return AlgeEngine.mul(0.5, AlgeEngine.add(expr, AlgeEngine.conjugate(expr)));
            }
        }
    }

    public static Expression imaginary(Object obj) {
        if (obj instanceof Number) {
            return Constant.ZERO;
        } else {
            Expression expr = (Expression) obj;
            if (expr == null) {
                return null;
            } else if (expr instanceof Complex cpx) {
                return new Complex(cpx.im, 0);
            } else if (expr instanceof Infinity inf) {
                return (Constant) (new Infinity(AlgeEngine.imaginary(inf.expression))).simplify();
            } else if (expr instanceof Univariate) {
                return Constant.ZERO;
            } else if (expr instanceof Add addExpr) {
                return AlgeEngine.add(AlgeEngine.imaginary(addExpr.constant),
                        AlgeEngine.add(Utils.map(new ArrayList<>(addExpr.inputs.get("Terms")), AlgeEngine::imaginary).toArray()));
            } else if (expr instanceof Log logExpr) {
                Expression re = AlgeEngine.real(logExpr.input);
                Expression im = AlgeEngine.imaginary(logExpr.input);
                return AlgeEngine.log(AlgeEngine.div(logExpr.input,
                        AlgeEngine.pow(AlgeEngine.add(AlgeEngine.pow(re, 2), AlgeEngine.pow(im, 2)), 0.5)));
            } else {
                return AlgeEngine.mul(-0.5, Constant.I, AlgeEngine.sub(expr, AlgeEngine.conjugate(expr)));
            }
        }
    }

    public static Expression conjugate(Object obj) {
        if (obj instanceof Number n) {
            return new Complex(n, 0);
        } else {
            Expression expr = (Expression) obj;
            if (expr == null) {
                return null;
            } else if (expr instanceof Univariate univariateExpr) {
                return univariateExpr;
            } else if (expr instanceof Constant constExpr) {
                return constExpr.conjugate();
            } else {
                HashMap<String, ArrayList<ArrayList<Expression>>> conjugateInputs = new HashMap<>();
                for (String inputType : expr.getInputTypes()) {
                    ArrayList<ArrayList<Expression>> inputList = new ArrayList<>();
                    for (Entity ent : expr.getInputs().get(inputType)) {
                        inputList.add(new ArrayList<>(Collections.singletonList(AlgeEngine.conjugate(ent))));
                    }
                    conjugateInputs.put(inputType, inputList);
                }
                return expr.getFormula().apply(conjugateInputs).get(0);
            }
        }
    }

    public static Expression abs(Object obj) {
        if (obj instanceof Number n) {
            return new Complex(n, 0);
        } else if (obj instanceof Expression) {
            return AlgeEngine.pow(AlgeEngine.add(AlgeEngine.pow(AlgeEngine.real(obj), 2),
                    AlgeEngine.pow(AlgeEngine.imaginary(obj), 2)), 0.5);
        } else {
            return null;
        }
    }
}

