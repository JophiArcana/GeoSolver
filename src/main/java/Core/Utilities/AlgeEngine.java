package Core.Utilities;

import Core.AlgeSystem.*;
import Core.AlgeSystem.Functions.*;
import Core.EntityTypes.*;
import com.google.common.collect.TreeMultiset;

import java.util.*;
import java.util.function.Function;

public class AlgeEngine {
    public static final Univariate X = new Univariate("x");
    public static final double EPSILON = Math.pow(10, -9);

    /** SECTION: Simplification Optimization ======================================================================== */

    public static int numberOfOperations(Expression expr) {
        if (expr instanceof Add addExpr) {
            int operations = 0;
            for (Entity ent : addExpr.inputs.get("Terms")) {
                operations += AlgeEngine.numberOfOperations((Expression) ent);
            }
            operations += (addExpr.inputs.get("Terms").size() - 1);
            if (!addExpr.constant.equals(Constant.ZERO)) {
                operations += 1;
            }
            return operations;
        } else if (expr instanceof Mul mulExpr) {
            int operations = 0;
            for (Entity ent : mulExpr.inputs.get("Terms")) {
                operations += AlgeEngine.numberOfOperations((Expression) ent);
            }
            operations += (mulExpr.inputs.get("Terms").size() - 1);
            if (!mulExpr.constant.equals(Constant.ONE)) {
                operations += 1;
            }
            return operations;
        } else if (expr instanceof Pow powExpr) {
            return AlgeEngine.numberOfOperations(powExpr.base) + AlgeEngine.numberOfOperations(powExpr.exponent) + 1;
        } else if (expr instanceof Log logExpr) {
            return AlgeEngine.numberOfOperations(logExpr.input) + 1;
        } else if (expr instanceof Constant || expr instanceof Univariate) {
            return 0;
        } else {
            return Integer.MAX_VALUE;
        }
    }

    public static Expression expand(Expression expr) {
        if (expr instanceof Add addExpr) {
            ArrayList<Expression> expansions = Utils.map(addExpr.inputs.get("Terms"), arg -> ((Expression) arg).expand());
            expansions.add(addExpr.constant);
            System.out.println("Expression: " + expr + "\nTerms: " + expansions +
                    "\nWithout reduction: " + new Add(expansions.toArray(new Expression[0])).reduction());
            System.out.println("Calculated");
            return (new Add(expansions.toArray(new Expression[0]))).reduction();
        } else if (expr instanceof Mul mulExpr) {
            TreeMultiset<Entity> inputTerms = mulExpr.inputs.get("Terms");
            if (inputTerms.size() == 1) {
                return mulExpr;
            }
            boolean containsAddTerm = false;
            for (Entity ent : inputTerms) {
                if (ent instanceof Add) {
                    containsAddTerm = true;
                    break;
                }
            }
            if (containsAddTerm) {
                switch (inputTerms.size()) {
                    case 1 -> {
                        return mulExpr;
                    }
                    case 2 -> {
                        ArrayList<Expression> expansion1Terms = Utils.additiveTerms(
                                ((Expression) inputTerms.firstEntry().getElement()).expand());
                        ArrayList<Expression> expansion2Terms = Utils.additiveTerms(
                                ((Expression) inputTerms.lastEntry().getElement()).expand());
                        System.out.println(expansion1Terms + " " + expansion2Terms);
                        ArrayList<Expression> expandedTerms = new ArrayList<>();
                        for (Expression term1 : expansion1Terms) {
                            for (Expression term2 : expansion2Terms) {
                                expandedTerms.add((new Mul(term1, term2)).reduction());
                            }
                        }
                        System.out.println(expandedTerms);
                        Expression sum = new Add(expandedTerms.toArray(new Expression[0]));
                        System.out.println("Sum of expanded terms: " + sum.reduction());
                        System.out.println("Normalized: " + sum.normalize());
                        /**System.out.println("Addition: " + sum);
                        // System.out.println("Addition: " + AlgeEngine.add(expandedTerms.toArray()));
                        System.out.println("Constant: " + mulExpr.constant);
                        System.out.println("Reduced Product: " + new Mul(sum, mulExpr.constant).reduction());
                        System.out.println("Expanded Product: " + new Mul(sum, mulExpr.constant).expand());
                        System.out.println("Product: " + AlgeEngine.mul(sum, mulExpr.constant));*/
                        return AlgeEngine.mul(sum, mulExpr.constant);
                    }
                    default -> {
                        Expression product = mulExpr.constant;
                        for (Entity ent : inputTerms) {
                            product = AlgeEngine.expand((new Mul(product, (Expression) ent)).reduction());
                        }
                        return product;
                    }
                }
            } else {
                return mulExpr;
            }
        } else if (expr instanceof Pow powExpr) {
            if (powExpr.base instanceof Add addExpr && powExpr.exponent instanceof Complex cpx && cpx.integer()) {
                int n = cpx.re.intValue();
                if (n == 2) {
                    ArrayList<Expression> expansion = Utils.additiveTerms(addExpr);
                    ArrayList<Expression> expandedTerms = new ArrayList<>();
                    for (int i = 0; i < expansion.size(); i++) {
                        for (int j = 0; j < i; j++) {
                            Expression product = AlgeEngine.mul(expansion.get(i), expansion.get(j));
                            expandedTerms.add(product);
                            expandedTerms.add(product);
                        }
                        expandedTerms.add(AlgeEngine.pow(expansion.get(i), 2));
                    }
                    return AlgeEngine.add(expandedTerms.toArray());
                } else {
                    Expression sqrt = AlgeEngine.pow(powExpr.base, n / 2).expand();
                    Expression product = AlgeEngine.pow(sqrt, 2).expand();
                    if (n % 2 == 1) {
                        product = AlgeEngine.mul(product, powExpr.base).expand();
                    }
                    return product;
                }
            } else {
                return powExpr;
            }
        } else if (expr instanceof Log || expr instanceof Constant || expr instanceof Univariate) {
            return expr;
        } else {
            return null;
        }
    }

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
            ArrayList<Expression> termOrders = Utils.map(mulExpr.inputs.get("Terms"), arg ->
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
        ArrayList<Expression> exprArgs = new ArrayList<>();
        for (Object obj : args) {
            Expression exprArg = Utils.objectConversion(obj);
            if (exprArg instanceof Mul) {
                assert !exprArg.equals(Constant.ZERO);
            }
            if (!exprArg.equals(Constant.ZERO)) {
                exprArgs.add(exprArg);
            }
        }
        return switch (exprArgs.size()) {
            case 0 -> Constant.ZERO;
            case 1 -> exprArgs.get(0);
            default -> (Expression) (new Add(exprArgs.toArray(new Expression[0]))).simplify();
        };
    }

    public static Expression sub(Object o1, Object o2) {
        Expression expr1 = Utils.objectConversion(o1);
        Expression expr2 = Utils.objectConversion(o2);
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
        ArrayList<Expression> exprArgs = new ArrayList<>();
        for (Object obj : args) {
            Expression exprArg = Utils.objectConversion(obj);
            if (exprArg.equals(Constant.ZERO)) {
                return Constant.ZERO;
            } else if (!exprArg.equals(Constant.ONE)) {
                exprArgs.add(exprArg);
            }
        }
        return switch (exprArgs.size()) {
            case 0 -> Constant.ONE;
            case 1 -> exprArgs.get(0);
            default -> (Expression) (new Mul(exprArgs.toArray(new Expression[0]))).simplify();
        };
    }

    public static Expression div(Object o1, Object o2) {
        Expression expr1 = Utils.objectConversion(o1);
        Expression expr2 = Utils.objectConversion(o2);
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
        return AlgeEngine.mul(Utils.objectConversion(obj), Constant.NONE);
    }

    public static Expression invert(Object obj) {
        return AlgeEngine.pow(Utils.objectConversion(obj), Constant.NONE);
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
        Expression baseExpr = Utils.objectConversion(base);
        Expression exponentExpr = Utils.objectConversion(exponent);
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
        Expression expr = Utils.objectConversion(obj);
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
                        AlgeEngine.add(Utils.map(addExpr.inputs.get("Terms"), AlgeEngine::real).toArray()));
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
                        AlgeEngine.add(Utils.map(addExpr.inputs.get("Terms"), AlgeEngine::imaginary).toArray()));
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

