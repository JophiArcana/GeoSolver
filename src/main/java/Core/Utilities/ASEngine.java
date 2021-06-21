package Core.Utilities;

import Core.AlgeSystem.*;
import Core.AlgeSystem.Functions.*;
import Core.EntityTypes.*;

import java.util.*;

public class ASEngine {
    public static final Symbol X = new Symbol("X");
    public static final double EPSILON = Math.pow(10, -9);

    /** SECTION: Order of Growth ==================================================================================== */

    public static Expression orderOfGrowth(Expression expr, Symbol s) {
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
                Expression termOrder = ASEngine.orderOfGrowth((Expression) ent, s);
                Expression baseOrder = termOrder;
                if (termOrder instanceof Mul mulOrder) {
                    baseOrder = mulOrder.baseForm();
                    orders.put(baseOrder, (Constant) ASEngine.add(orders.get(baseOrder), mulOrder.constant));
                } else {
                    orders.put(termOrder, (Constant) ASEngine.add(orders.get(termOrder), Constant.ONE));
                }
                if (orders.get(baseOrder).equals(Constant.ZERO)) {
                    orders.remove(baseOrder);
                }
            }
            Map.Entry<Expression, Constant> greatestOrder = orders.lastEntry();
            return ASEngine.mul(greatestOrder.getValue(), greatestOrder.getKey());
        } else if (expr instanceof Mul mulExpr) {
            ArrayList<Expression> termOrders = Utils.map(new ArrayList<>(mulExpr.inputs.get("Terms")), arg ->
                    ASEngine.orderOfGrowth((Expression) arg, s));
            return ASEngine.mul(mulExpr.constant, ASEngine.mul(termOrders.toArray()));
        } else if (expr instanceof Pow powExpr) {
            return ASEngine.pow(ASEngine.orderOfGrowth(powExpr.base, s), powExpr.exponent);
        } else if (expr instanceof Log logExpr) {
            return ASEngine.log(ASEngine.orderOfGrowth(logExpr.input, s));
        } else if (expr instanceof Symbol || expr instanceof Constant) {
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
        return ASEngine.orderOfGrowth(expr, (Symbol) variables.get(0));
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
                    product = ASEngine.mul(product, ASEngine.pow(term, exponent));
                }
            }
            return ASEngine.mul(constant, product);
        }
    }

    public static Expression greatestCommonDivisor(Expression ... args) {
        if (args.length == 1) {
            return args[0];
        } else if (args.length == 2) {
            return ASEngine.greatestCommonDivisor(args[0], args[1]);
        } else {
            Expression[] rest = new Expression[args.length - 1];
            System.arraycopy(args, 1, rest, 0, args.length - 1);
            return ASEngine.greatestCommonDivisor(args[0], ASEngine.greatestCommonDivisor(rest));
        }
    }

    /** SECTION: Basic operations =================================================================================== */

    public static Expression add(Object ... args) {
        Expression[] exprArgs = new Expression[args.length];
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof Number n) {
                exprArgs[i] = new Complex(n, 0);
            } else if (!(args[i] instanceof Expression)) {
                exprArgs[i] = Constant.ZERO;
            } else {
                exprArgs[i] = (Expression) args[i];
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
        Expression e1 = (Expression) o1;
        Expression e2 = (Expression) o2;
        e1 = (e1 == null) ? Constant.ZERO : (Expression) e1.simplify();
        e2 = (e2 == null) ? Constant.ZERO : (Expression) e2.simplify();
        if (e1.equals(Constant.ZERO)) {
            return ASEngine.negate(e2);
        } else if (e2.equals(Constant.ZERO)) {
            return e1;
        } else if (e1 instanceof Constant const1 && e2 instanceof Constant const2) {
            return const1.sub(const2);
        } else {
            return (Expression) (new Add(e1, ASEngine.negate(e2))).simplify();
        }
    }

    public static Expression mul(Object ... args) {
        Expression[] exprArgs = new Expression[args.length];
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof Number n) {
                exprArgs[i] = new Complex(n, 0);
            } else if (!(args[i] instanceof Expression)) {
                exprArgs[i] = Constant.ZERO;
            } else {
                exprArgs[i] = (Expression) args[i];
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
        Expression e1 = (Expression) o1;
        Expression e2 = (Expression) o2;
        e1 = (e1 == null) ? Constant.ONE : (Expression) e1.simplify();
        e2 = (e2 == null) ? Constant.ONE : (Expression) e2.simplify();
        if (e1.equals(Constant.ONE)) {
            return ASEngine.invert(e2);
        } else if (e2.equals(Constant.ONE)) {
            return e1;
        } else if (e1 instanceof Constant const1 && e2 instanceof Constant const2) {
            return const1.div(const2);
        } else {
            return (Expression) (new Mul(e1, ASEngine.invert(e2))).simplify();
        }
    }

    public static Expression negate(Object obj) {
        if (obj instanceof Number n) {
            obj = new Complex(n, 0);
        }
        Expression expr = (Expression) obj;
        return (expr == null) ? null : ASEngine.mul(expr, Constant.NONE);
    }

    public static Expression invert(Object obj) {
        if (obj instanceof Number n) {
            obj = new Complex(n, 0);
        }
        Expression expr = (Expression) obj;
        return (expr == null) ? null : ASEngine.pow(expr, Constant.NONE);
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
        return ASEngine.pow(Constant.E, obj);
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
                return (Constant) (new Infinity(ASEngine.real(inf.expression))).simplify();
            } else if (expr instanceof Symbol symbolExpr) {
                return symbolExpr;
            } else if (expr instanceof Add addExpr) {
                return ASEngine.add(ASEngine.real(addExpr.constant),
                        ASEngine.add(Utils.map(new ArrayList<>(addExpr.inputs.get("Terms")), ASEngine::real).toArray()));
            } else if (expr instanceof Log logExpr) {
                Expression re = ASEngine.real(logExpr.input);
                Expression im = ASEngine.imaginary(logExpr.input);
                return ASEngine.mul(0.5, ASEngine.log(ASEngine.add(ASEngine.pow(re, 2), ASEngine.pow(im, 2))));
            } else {
                return ASEngine.mul(0.5, ASEngine.add(expr, ASEngine.conjugate(expr)));
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
                return (Constant) (new Infinity(ASEngine.imaginary(inf.expression))).simplify();
            } else if (expr instanceof Symbol) {
                return Constant.ZERO;
            } else if (expr instanceof Add addExpr) {
                return ASEngine.add(ASEngine.imaginary(addExpr.constant),
                        ASEngine.add(Utils.map(new ArrayList<>(addExpr.inputs.get("Terms")), ASEngine::imaginary).toArray()));
            } else if (expr instanceof Log logExpr) {
                Expression re = ASEngine.real(logExpr.input);
                Expression im = ASEngine.imaginary(logExpr.input);
                return ASEngine.log(ASEngine.div(logExpr.input,
                        ASEngine.pow(ASEngine.add(ASEngine.pow(re, 2), ASEngine.pow(im, 2)), 0.5)));
            } else {
                return ASEngine.mul(-0.5, Constant.I, ASEngine.sub(expr, ASEngine.conjugate(expr)));
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
            } else if (expr instanceof Symbol symbolExpr) {
                return symbolExpr;
            } else if (expr instanceof Constant constExpr) {
                return constExpr.conjugate();
            } else {
                HashMap<String, ArrayList<ArrayList<Expression>>> conjugateInputs = new HashMap<>();
                for (String inputType : expr.getInputTypes()) {
                    ArrayList<ArrayList<Expression>> inputList = new ArrayList<>();
                    for (Entity ent : expr.getInputs().get(inputType)) {
                        inputList.add(new ArrayList<>(Collections.singletonList(ASEngine.conjugate(ent))));
                    }
                    conjugateInputs.put(inputType, inputList);
                }
                return expr.getFormula().apply(conjugateInputs).get(0);
            }
        }
    }
}

