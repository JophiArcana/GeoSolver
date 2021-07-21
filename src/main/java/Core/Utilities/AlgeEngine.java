package Core.Utilities;

import Core.AlgeSystem.Constants.Complex;
import Core.AlgeSystem.Constants.Infinity;
import Core.AlgeSystem.UnicardinalRings.Symbolic;
import Core.AlgeSystem.UnicardinalTypes.Constant;
import Core.AlgeSystem.UnicardinalTypes.Expression;
import Core.AlgeSystem.UnicardinalTypes.Univariate;
import Core.AlgeSystem.Functions.*;
import Core.AlgeSystem.UnicardinalTypes.Unicardinal;
import Core.EntityTypes.*;
import com.google.common.collect.TreeMultiset;

import java.util.*;
import java.util.function.Function;

public class AlgeEngine<T extends Expression<T>> {
    public final Class<T> TYPE;
    
    public AlgeEngine(Class<T> type) {
        this.TYPE = type;
    }
    
    public Univariate<T> X() {
        return new Univariate<>("x", TYPE);
    }
    public static final double EPSILON = Math.pow(10, -9);

    /** SECTION: Simplification Optimization ======================================================================== */

    public int numberOfOperations(Expression<T> expr) {
        if (expr instanceof Add<T> addExpr) {
            int operations = 0;
            for (Entity ent : addExpr.inputs.get("Terms")) {
                operations += this.numberOfOperations((Expression<T>) ent);
            }
            operations += (addExpr.inputs.get("Terms").size() - 1);
            if (!addExpr.constant.equals(Constant.ZERO(TYPE))) {
                operations += 1;
            }
            return operations;
        } else if (expr instanceof Mul<T> mulExpr) {
            int operations = 0;
            for (Entity ent : mulExpr.inputs.get("Terms")) {
                operations += this.numberOfOperations((Expression<T>) ent);
            }
            operations += (mulExpr.inputs.get("Terms").size() - 1);
            if (!mulExpr.constant.equals(Constant.ONE(TYPE))) {
                operations += 1;
            }
            return operations;
        } else if (expr instanceof Pow<T> powExpr) {
            return this.numberOfOperations(powExpr.base) + this.numberOfOperations(powExpr.exponent) + 1;
        } else if (expr instanceof Log<T> logExpr) {
            return this.numberOfOperations(logExpr.input) + 1;
        } else if (expr instanceof Constant || expr instanceof Univariate) {
            return 0;
        } else {
            return Integer.MAX_VALUE;
        }
    }

    public Expression<T> expand(Expression<T> expr) {
        if (expr instanceof Add<T> addExpr) {
            ArrayList<Expression<T>> expansions = Utils.map(addExpr.inputs.get("Terms"), arg -> ((Expression<T>) arg).expand());
            expansions.add(addExpr.constant);
            return new Add<>(expansions, TYPE).reduction();
        } else if (expr instanceof Mul<T> mulExpr) {
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
                if (inputTerms.size() == 2) {
                    ArrayList<Expression<T>> expansion1Terms = Utils.additiveTerms(
                            ((Expression<T>) inputTerms.firstEntry().getElement()).expand());
                    ArrayList<Expression<T>> expansion2Terms = Utils.additiveTerms(
                            ((Expression<T>) inputTerms.lastEntry().getElement()).expand());
                    ArrayList<Expression<T>> expandedTerms = new ArrayList<>();
                    for (Expression<T> term1 : expansion1Terms) {
                        for (Expression<T> term2 : expansion2Terms) {
                            expandedTerms.add(new Mul<>(Arrays.asList(term1, term2), TYPE).reduction());
                        }
                    }
                    Expression<T> sum = new Add<>(expandedTerms, TYPE);
                    return this.mul(sum, mulExpr.constant);
                } else {
                    Expression<T> product = mulExpr.constant;
                    for (Entity ent : inputTerms) {
                        product = this.expand(new Mul<>(Arrays.asList(product, (Expression<T>) ent), TYPE).reduction());
                    }
                    return product;
                }
            } else {
                return mulExpr;
            }
        } else if (expr instanceof Pow<T> powExpr) {
            if (powExpr.base instanceof Add<T> addExpr && powExpr.exponent instanceof Complex<T> cpx
                    && cpx.integer() && cpx.re.intValue() > 0) {
                int n = cpx.re.intValue();
                if (n == 2) {
                    ArrayList<Expression<T>> expansion = Utils.additiveTerms(addExpr);
                    ArrayList<Expression<T>> expandedTerms = new ArrayList<>();
                    for (int i = 0; i < expansion.size(); i++) {
                        for (int j = 0; j < i; j++) {
                            Expression<T> product = this.mul(expansion.get(i), expansion.get(j));
                            expandedTerms.add(product);
                            expandedTerms.add(product);
                        }
                        expandedTerms.add(this.pow(expansion.get(i), 2));
                    }
                    return this.add(expandedTerms.toArray());
                } else {
                    Expression<T> sqrt = this.pow(powExpr.base, n / 2).expand();
                    Expression<T> product = this.pow(sqrt, 2).expand();
                    if (n % 2 == 1) {
                        product = this.mul(product, powExpr.base).expand();
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

    public Expression<T> orderOfGrowth(Expression<T> expr, Univariate<T> s) {
        if (expr == null) {
            return null;
        } else if (expr instanceof Add<T> addExpr) {
            TreeMap<Expression<T>, Constant<T>> orders = new TreeMap<>(new OrderOfGrowthComparator<>(s, TYPE));
            if (!addExpr.constant.equals(Constant.ZERO(TYPE))) {
                orders.put(Constant.ONE(TYPE), addExpr.constant);
            }
            for (Entity ent : addExpr.inputs.get("Terms")) {
                Expression<T> termOrder = this.orderOfGrowth((Expression<T>) ent, s);
                Expression<T> baseOrder = termOrder;
                if (termOrder instanceof Mul<T> mulOrder) {
                    baseOrder = mulOrder.baseForm();
                    orders.put(baseOrder, orders.get(baseOrder).add(mulOrder.constant));
                } else {
                    orders.put(termOrder, orders.get(termOrder).add(Constant.ONE(TYPE)));
                }
                if (orders.get(baseOrder).equals(Constant.ZERO(TYPE))) {
                    orders.remove(baseOrder);
                }
            }
            Map.Entry<Expression<T>, Constant<T>> greatestOrder = orders.lastEntry();
            return this.mul(greatestOrder.getValue(), greatestOrder.getKey());
        } else if (expr instanceof Mul<T> mulExpr) {
            ArrayList<Expression<T>> termOrders = Utils.map(mulExpr.inputs.get("Terms"), arg ->
                    this.orderOfGrowth((Expression<T>) arg, s));
            return this.mul(mulExpr.constant, this.mul(termOrders.toArray()));
        } else if (expr instanceof Pow<T> powExpr) {
            return this.pow(this.orderOfGrowth(powExpr.base, s), powExpr.exponent);
        } else if (expr instanceof Log<T> logExpr) {
            return this.log(this.orderOfGrowth(logExpr.input, s));
        } else if (expr instanceof Univariate || expr instanceof Constant) {
            return expr;
        } else {
            /** TODO: Implement Order of Growth for other Functions */
            return null;
        }
    }

    public Expression<T> orderOfGrowth(Expression<T> expr) {
        if (expr == null) {
            return null;
        } else {
            ArrayList<Mutable> variables = new ArrayList<>(expr.variables());
            return this.orderOfGrowth(expr, (Univariate<T>) variables.get(0));
        }
    }

    /** SECTION: Greatest Common Divisor ============================================================================ */

    public Expression<T> greatestCommonDivisor(Expression<T> e1, Expression<T> e2) {
        if (e1.equals(Constant.ZERO(TYPE))) {
            return e2;
        } else if (e2.equals(Constant.ZERO(TYPE))) {
            return e1;
        } else {
            Expression.Factorization<T> e1Norm = e1.normalize();
            Expression.Factorization<T> e2Norm = e2.normalize();
            Constant<T> constant = e1Norm.constant.gcd(e2Norm.constant);
            Expression<T> product = Constant.ONE(TYPE);
            TreeSet<Expression<T>> terms = new TreeSet<>(Utils.PRIORITY_COMPARATOR);
            terms.addAll(e1Norm.terms.keySet());
            terms.addAll(e2Norm.terms.keySet());
            for (Expression<T> term : terms) {
                Expression<T> e1Exponent = e1Norm.terms.get(term);
                Expression<T> e2Exponent = e2Norm.terms.get(term);
                Expression<T> exponent = (Utils.getGrowthComparator(TYPE).compare(e1Exponent, e2Exponent) < 0) ? e1Exponent : e2Exponent;
                if (exponent != null && !exponent.equals(Constant.ZERO(TYPE))) {
                    product = this.mul(product, this.pow(term, exponent));
                }
            }
            return this.mul(constant, product);
        }
    }

    public Expression<T> greatestCommonDivisor(List<Expression<T>> args) {
        if (args.size() == 1) {
            return args.get(0);
        } else if (args.size() == 2) {
            return this.greatestCommonDivisor(args.get(0), args.get(1));
        } else {
            return this.greatestCommonDivisor(args.get(0), this.greatestCommonDivisor(args.subList(1, args.size())));
        }
    }

    /** SECTION: Basic operations =================================================================================== */

    public Constant<T> complex(Number re, Number im) {
        return new Complex<>(re, im, TYPE);
    }

    public Constant<T> infinity(Expression<T> expr) {
        return (Constant<T>) new Infinity<>(expr, TYPE).simplify();
    }

    public Expression<T> add(Object ... args) {
        ArrayList<Expression<T>> exprArgs = new ArrayList<>();
        for (Object obj : args) {
            Expression<T> exprArg = this.objectConversion(obj);
            if (!exprArg.equals(Constant.ZERO(TYPE))) {
                exprArgs.add(exprArg);
            }
        }
        return switch (exprArgs.size()) {
            case 0 -> Constant.ZERO(TYPE);
            case 1 -> exprArgs.get(0);
            default -> (Expression<T>) new Add<>(exprArgs, TYPE).simplify();
        };
    }

    public Expression<T> sub(Object o1, Object o2) {
        Expression<T> expr1 = this.objectConversion(o1);
        Expression<T> expr2 = this.objectConversion(o2);
        if (expr1.equals(Constant.ZERO(TYPE))) {
            return this.negate(expr2);
        } else if (expr2.equals(Constant.ZERO(TYPE))) {
            return expr1;
        } else if (expr1 instanceof Constant<T> const1 && expr2 instanceof Constant<T> const2) {
            return const1.sub(const2);
        } else {
            return (Expression<T>) new Add<>(Arrays.asList(expr1, this.negate(expr2)), TYPE).simplify();
        }
    }

    public Expression<T> mul(Object ... args) {
        ArrayList<Expression<T>> exprArgs = new ArrayList<>();
        for (Object obj : args) {
            Expression<T> exprArg = this.objectConversion(obj);
            if (exprArg.equals(Constant.ZERO(TYPE))) {
                return Constant.ZERO(TYPE);
            } else if (!exprArg.equals(Constant.ONE(TYPE))) {
                exprArgs.add(exprArg);
            }
        }
        return switch (exprArgs.size()) {
            case 0 -> Constant.ONE(TYPE);
            case 1 -> exprArgs.get(0);
            default -> (Expression<T>) new Mul<>(exprArgs, TYPE).simplify();
        };
    }

    public Expression<T> div(Object o1, Object o2) {
        Expression<T> expr1 = this.objectConversion(o1);
        Expression<T> expr2 = this.objectConversion(o2);
        if (expr1.equals(Constant.ONE(TYPE))) {
            return this.invert(expr2);
        } else if (expr2.equals(Constant.ONE(TYPE))) {
            return expr1;
        } else if (expr1 instanceof Constant<T> const1 && expr2 instanceof Constant<T> const2) {
            return const1.div(const2);
        } else {
            return (Expression<T>) new Mul<>(Arrays.asList(expr1, this.invert(expr2)), TYPE).simplify();
        }
    }

    public Expression<T> negate(Object obj) {
        return this.mul(this.objectConversion(obj), Constant.NONE(TYPE));
    }

    public Expression<T> invert(Object obj) {
        return this.pow(this.objectConversion(obj), Constant.NONE(TYPE));
    }

    /** SECTION: Cyclic Sum ========================================================================================= */

    public Expression<T> cyclicSum(Function<ArrayList<Expression<T>>, Expression<T>> func, ArrayList<Expression<T>> args) {
        ArrayList<Expression<T>> terms = new ArrayList<>();
        for (int i = 0; i < args.size(); i++) {
            terms.add(func.apply(args));
            args.add(0, args.remove(args.size() - 1));
        }
        return this.add(terms.toArray());
    }

    /** SECTION: Exponential Functions ============================================================================== */

    public Expression<T> pow(Object base, Object exponent) {
        Expression<T> baseExpr = this.objectConversion(base);
        Expression<T> exponentExpr = this.objectConversion(exponent);
        if (baseExpr instanceof Constant<T> baseConst && exponentExpr instanceof Constant<T> expConst) {
            return baseConst.pow(expConst);
        } else {
            return (Expression<T>) new Pow<>(baseExpr, exponentExpr, TYPE).simplify();
        }
    }

    public Expression<T> exp(Object obj) {
        return this.pow(Constant.E(TYPE), obj);
    }

    public Expression<T> log(Object obj) {
        Expression<T> expr = this.objectConversion(obj);
        if (expr instanceof Constant<T> constExpr) {
            return constExpr.log();
        } else {
            return (Expression<T>) new Log<>(expr, TYPE).simplify();
        }
    }

    /** SECTION: Real, Imaginary, Conjugate ========================================================================= */

    public Expression<T> real(Object obj) {
        if (obj instanceof Number n) {
            return new Complex<>(n, 0, TYPE);
        } else {
            Expression<T> expr = (Expression<T>) obj;
            if (expr == null || expr instanceof Univariate) {
                return expr;
            } else if (expr instanceof Complex<T> cpx) {
                return this.complex(cpx.re, 0);
            } else if (expr instanceof Infinity<T> inf) {
                return this.infinity(this.real(inf.expression));
            } else if (expr instanceof Add<T> addExpr) {
                return this.add(this.real(addExpr.constant),
                        this.add(Utils.map(addExpr.inputs.get("Terms"), this::real).toArray()));
            } else if (expr instanceof Log<T> logExpr) {
                Expression<T> re = this.real(logExpr.input);
                Expression<T> im = this.imaginary(logExpr.input);
                return this.mul(0.5, this.log(this.add(this.pow(re, 2), this.pow(im, 2))));
            } else {
                return this.mul(0.5, this.add(expr, this.conjugate(expr)));
            }
        }
    }

    public Expression<T> imaginary(Object obj) {
        if (obj instanceof Number) {
            return Constant.ZERO(TYPE);
        } else {
            Expression<T> expr = (Expression<T>) obj;
            if (expr == null) {
                return null;
            } else if (expr instanceof Complex<T> cpx) {
                return this.complex(cpx.im, 0);
            } else if (expr instanceof Infinity<T> inf) {
                return this.infinity(this.imaginary(inf.expression));
            } else if (expr instanceof Univariate<T>) {
                return Constant.ZERO(TYPE);
            } else if (expr instanceof Add<T> addExpr) {
                return this.add(this.imaginary(addExpr.constant),
                        this.add(Utils.map(addExpr.inputs.get("Terms"), this::imaginary).toArray()));
            } else if (expr instanceof Log<T> logExpr) {
                Expression<T> re = this.real(logExpr.input);
                Expression<T> im = this.imaginary(logExpr.input);
                return this.log(this.div(logExpr.input, this.pow(this.add(this.pow(re, 2), this.pow(im, 2)), 0.5)));
            } else {
                return this.mul(-0.5, Constant.I(TYPE), this.sub(expr, this.conjugate(expr)));
            }
        }
    }

    public Expression<T> conjugate(Object obj) {
        if (obj instanceof Number n) {
            return new Complex<>(n, 0, TYPE);
        } else {
            Expression<T> expr = (Expression<T>) obj;
            if (expr == null || expr instanceof Univariate) {
                return expr;
            } else if (expr instanceof Constant<T> constExpr) {
                return constExpr.conjugate();
            } else {
                HashMap<String, ArrayList<ArrayList<Unicardinal>>> conjugateInputs = new HashMap<>();
                for (String inputType : expr.getInputTypes()) {
                    ArrayList<ArrayList<Unicardinal>> inputList = new ArrayList<>();
                    for (Entity ent : expr.getInputs().get(inputType)) {
                        inputList.add(new ArrayList<>(Collections.singletonList(this.conjugate(ent))));
                    }
                    conjugateInputs.put(inputType, inputList);
                }
                return (Expression<T>) expr.getFormula().apply(conjugateInputs).get(0);
            }
        }
    }

    public Expression<T> abs(Object obj) {
        if (obj instanceof Number n) {
            return this.complex(n, 0);
        } else if (obj instanceof Expression) {
            return this.pow(this.add(this.pow(this.real(obj), 2), this.pow(this.imaginary(obj), 2)), 0.5);
        } else {
            return null;
        }
    }

    /** SECTION: Object Conversion ================================================================================== */
    
    public Expression<T> objectConversion(Object obj) {
        assert obj instanceof Number || obj instanceof Expression || obj == null;
        if (obj instanceof Number n) {
            return new Complex<>(n, 0, TYPE);
        } else if (obj != null) {
            return (Expression<T>) obj;
        } else {
            return Constant.ZERO(TYPE);
        }
    }
}

