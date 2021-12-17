package Core.Utilities;

import Core.AlgSystem.Constants.*;
import Core.AlgSystem.UnicardinalTypes.*;
import Core.AlgSystem.Operators.*;
import Core.EntityTypes.*;
import com.google.common.collect.TreeMultiset;
import javafx.util.Pair;

import java.util.*;
import java.util.function.Function;


public class AlgeEngine<T extends Expression<T>> {
    public final Class<T> TYPE;
    
    public AlgeEngine(Class<T> type) {
        this.TYPE = type;
    }
    
    public Univariate<T> X() {
        return new Univariate<>("\u5929", TYPE);
    }
    public static final double EPSILON = Math.pow(10, -9);

    /** SECTION: Simplification Optimization ======================================================================== */

    public double numberOfOperations(Expression<T> expr) {
        if (expr instanceof Add<T> addExpr) {
            int operations = expr.getInputs().get(Add.Parameter.TERMS).size() - 1;
            for (Entity ent : expr.getInputs().get(Add.Parameter.TERMS)) {
                operations += numberOfOperations((Expression<T>) ent);
            }
            if (!addExpr.constant.equalsZero()) {
                operations += 1;
            }
            return operations;
        } else if (expr instanceof Mul<T> mulExpr) {
            int operations = expr.getInputs().get(Mul.Parameter.TERMS).size() - 1;
            for (Entity ent : expr.getInputs().get(Mul.Parameter.TERMS)) {
                operations += numberOfOperations((Expression<T>) ent);
            }
            if (!mulExpr.constant.equalsOne()) {
                operations += 1;
            }
            return operations;
        } else if (expr instanceof Pow<T> powExpr) {
            return this.numberOfOperations(powExpr.base) + Math.log(powExpr.exponent.abs()) / Math.log(2);
        } else if (expr instanceof Univariate<T>) {
            return 1;
        } else if (expr instanceof Constant<T>) {
            return 0;
        } else {
            return Integer.MAX_VALUE;
        }
    }

    public Expression<T> reduce(Expression<T> expr) {
        if (expr instanceof Add<T> addExpr) {
            Expression<T> gcd = this.greatestCommonDivisor(addExpr.constant,
                    this.greatestCommonDivisor(Utils.cast(addExpr.inputs.get(Add.Parameter.TERMS))));
            ArrayList<Expression<T>> normalizedTerms;
            if (gcd.equalsOne()) {
                return expr;
            } else {
                normalizedTerms = Utils.map(addExpr.inputs.get(Add.Parameter.TERMS), arg -> this.div(arg, gcd));
                normalizedTerms.add(this.div(addExpr.constant, gcd));

                if (normalizedTerms.size() <= 16) {
                    GCDGraph<T> reducedGraph = this.GCDReduction(normalizedTerms);
                    normalizedTerms = Utils.setParse(reducedGraph.elements, reducedGraph.binaryRepresentation);
                }

                return new Mul<>(Arrays.asList(gcd, new Add<>(normalizedTerms, TYPE).close().reduce()), TYPE).close();
            }
        } else if (expr instanceof Mul<T> mulExpr) {
            Expression.Factorization<T> factorization = mulExpr.normalize();
            Constant<T> exponentGCD = this.constantGreatestCommonDivisor(new ArrayList<>(factorization.terms.values()));
            if (exponentGCD.equalsOne()) {
                return mulExpr;
            } else {
                ArrayList<Expression<T>> terms = new ArrayList<>();
                factorization.terms.forEach((base, exponent) -> terms.add(this.pow(base, exponent.div(exponentGCD))));
                return new Mul<>(Arrays.asList(factorization.constant,
                        new Pow<>(new Mul<>(terms, TYPE).close().reduce(), exponentGCD, TYPE).close()), TYPE).close();
            }
        } else if (expr instanceof Pow<T> || expr instanceof Constant<T> || expr instanceof Univariate<T>) {
            return expr;
        } else {
            return null;
        }
    }

    public Expression<T> expand(Expression<T> expr) {
        if (expr instanceof Add<T> addExpr) {
            ArrayList<Expression<T>> expansions = Utils.map(addExpr.inputs.get(Add.Parameter.TERMS), arg -> ((Expression<T>) arg).expand());
            expansions.add(addExpr.constant);
            return new Add<>(expansions, TYPE).close();
        } else if (expr instanceof Mul<T> mulExpr) {
            TreeMultiset<Entity> inputTerms = mulExpr.inputs.get(Mul.Parameter.TERMS);
            if (inputTerms.size() == 1) {
                return new Mul<>(Arrays.asList(mulExpr.constant,
                        ((Expression<T>) mulExpr.inputs.get(Mul.Parameter.TERMS).firstEntry().getElement()).expand()), TYPE).close();
            }

            ArrayList<Expression<T>> expandableTerms = new ArrayList<>(), rest = new ArrayList<>();
            inputTerms.forEach(term -> ((term instanceof Add) ? expandableTerms : rest).add((Expression<T>) term));
            Expression<T> singleton = new Mul<>(rest, TYPE).close();

            if (!singleton.equalsOne()) {
                expandableTerms.add(0, singleton);
            }
            if (expandableTerms.size() == 1) {
                return new Mul<>(Arrays.asList(singleton, mulExpr.constant), TYPE).close();
            } else if (expandableTerms.size() == 2) {
                ArrayList<Expression<T>>    expansion1Terms = this.additiveTerms(expandableTerms.get(0).expand()),
                                            expansion2Terms = this.additiveTerms(expandableTerms.get(1).expand()),
                                            expandedTerms = new ArrayList<>();
                for (Expression<T> term1 : expansion1Terms) {
                    for (Expression<T> term2 : expansion2Terms) {
                        expandedTerms.add(new Mul<>(Arrays.asList(term1, term2), TYPE).close());
                    }
                }
                return new Mul<>(Arrays.asList(new Add<>(expandedTerms, TYPE).close(), mulExpr.constant), TYPE).close();
            } else {
                Expression<T> product = mulExpr.constant;
                // System.out.println(expandableTerms);
                for (Expression<T> ent : expandableTerms) {
                    // System.out.println(ent + " of " + expandableTerms);
                    product = this.expand(new Mul<>(Arrays.asList(product, ent), TYPE).close());
                }
                return product;
            }
        } else if (expr instanceof Pow<T> powExpr) {
            if (powExpr.base instanceof Add<T> addExpr && powExpr.exponent instanceof Complex<T> cpx
                    && cpx.integer() && cpx.re.intValue() > 0) {
                // System.out.println("Expanding " + addExpr + " " + cpx);
                int n = cpx.re.intValue();
                if (n == 2) {
                    ArrayList<Expression<T>> expansion = this.additiveTerms(addExpr), expandedTerms = new ArrayList<>();
                    for (int i = 0; i < expansion.size(); i++) {
                        for (int j = 0; j < i; j++) {
                            Expression<T> product = this.mul(expansion.get(i), expansion.get(j));
                            expandedTerms.add(new Mul<>(Arrays.asList(product, this.complex(2, 0)), TYPE).close());
                        }
                        expandedTerms.add(this.pow(expansion.get(i), 2));
                    }
                    // System.out.println("Expanded terms: " + expandedTerms);
                    return this.add(expandedTerms.toArray());
                } else {
                    Expression<T> sqrt = this.pow(powExpr.base, n / 2).expand();
                    Expression<T> product = this.pow(sqrt, 2).expand();
                    if (n % 2 == 1) {
                        product = this.mul(product, powExpr.base).expand();
                    }
                    return product;
                }
            } else if (powExpr.base instanceof Mul<T> mulExpr) {
                ArrayList<Expression<T>> terms = Utils.map(mulExpr.inputs.get(Mul.Parameter.TERMS), arg -> this.pow(arg, powExpr.exponent));
                terms.add(mulExpr.constant.pow(powExpr.exponent));
                return new Mul<>(terms, TYPE).close();
            } else {
                return powExpr;
            }
        } else if (expr instanceof Constant<T> || expr instanceof Univariate<T>) {
            return expr;
        } else {
            return null;
        }
    }

    public ArrayList<Expression<T>> additiveTerms(Expression<T> expr) {
        ArrayList<Expression<T>> expansionTerms = new ArrayList<>();
        if (expr instanceof Add<T> addExpr) {
            addExpr.inputs.get(Add.Parameter.TERMS).forEach(arg -> expansionTerms.add((Expression<T>) arg));
            if (!addExpr.constant.equalsZero()) {
                expansionTerms.add(addExpr.constant);
            }
        } else {
            expansionTerms.add(expr);
        }
        return expansionTerms;
    }

    /** SECTION: Order of Growth ==================================================================================== */

    public Expression<T> orderOfGrowth(Expression<T> expr, Univariate<T> s) {
        Constant<T> ONE = Constant.ONE(TYPE);
        if (expr == null) {
            return null;
        } else if (expr instanceof Add<T> addExpr) {
            TreeMap<Expression<T>, Constant<T>> orders = new TreeMap<>(new OrderOfGrowthComparator<>(s, TYPE));
            if (!addExpr.constant.equalsZero()) {
                orders.put(ONE, addExpr.constant);
            }
            for (Entity ent : addExpr.inputs.get(Add.Parameter.TERMS)) {
                Expression<T> termOrder = this.orderOfGrowth((Expression<T>) ent, s);
                Expression<T> baseOrder = termOrder;
                if (termOrder instanceof Mul<T> mulOrder) {
                    baseOrder = mulOrder.baseForm().getValue();
                    orders.put(baseOrder, orders.getOrDefault(baseOrder, Constant.ZERO(TYPE)).add(mulOrder.constant));
                } else {
                    orders.put(termOrder, orders.getOrDefault(termOrder, Constant.ZERO(TYPE)).add(ONE));
                }
                if (orders.get(baseOrder).equalsZero()) {
                    orders.remove(baseOrder);
                }
            }
            Map.Entry<Expression<T>, Constant<T>> greatestOrder = orders.lastEntry();
            return this.mul(greatestOrder.getValue(), greatestOrder.getKey());
        } else if (expr instanceof Mul<T> mulExpr) {
            ArrayList<Expression<T>> termOrders = Utils.map(mulExpr.inputs.get(Mul.Parameter.TERMS), arg ->
                    this.orderOfGrowth((Expression<T>) arg, s));
            return this.mul(mulExpr.constant, this.mul(termOrders.toArray()));
        } else if (expr instanceof Pow<T> powExpr) {
            return this.pow(this.orderOfGrowth(powExpr.base, s), powExpr.exponent);
        } else if (expr instanceof Univariate<T> || expr instanceof Constant<T>) {
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

    private Expression<T> greatestCommonDivisor(Expression<T> e1, Expression<T> e2) {
        Constant<T> ONE = Constant.ONE(TYPE);
        if (e1.equalsZero()) {
            Constant<T> const2 = e2.baseForm().getKey();
            if (const2 instanceof Infinity<T> inf) {
                return (inf.signum(this.X()) == 1) ? e2 : this.negate(e2);
            } else {
                Complex<T> cpx = (Complex<T>) const2;
                if (cpx.re.doubleValue() > 0) {
                    return e2;
                } else if (cpx.re.doubleValue() == 0) {
                    return this.div(e2, new Complex<>(0, (int) Math.signum(cpx.im.doubleValue()), TYPE));
                } else {
                    return this.negate(e2);
                }
            }
        } else if (e1.equalsOne() || e2.equalsOne()) {
            return ONE;
        } else if (e1 instanceof Constant<T> const1) {
            return const1.gcd(e2.baseForm().getKey());
        } else if (e2 instanceof Constant<T>) {
            return this.greatestCommonDivisor(e2, e1);
        } else {
            Expression.Factorization<T> e1Norm = e1.normalize(),
                                        e2Norm = e2.normalize();
            Constant<T> constant = e1Norm.constant.gcd(e2Norm.constant);
            Expression<T> product = ONE;
            TreeSet<Expression<T>> terms = new TreeSet<>(Utils.PRIORITY_COMPARATOR) {{
                addAll(e1Norm.terms.keySet());
                addAll(e2Norm.terms.keySet());
            }};
            for (Expression<T> term : terms) {
                Expression<T>   e1Exponent = e1Norm.terms.get(term),
                                e2Exponent = e2Norm.terms.get(term);
                Expression<T> exponent = (Utils.getGrowthComparator(TYPE).compare(e1Exponent, e2Exponent) < 0) ? e1Exponent : e2Exponent;
                if (exponent != null && !exponent.equalsZero()) {
                    product = this.mul(product, this.pow(term, exponent));
                }
            }
            return this.mul(constant, product);
        }
    }

    public Expression<T> greatestCommonDivisor(List<Expression<T>> args) {
        return switch (args.size()) {
            case 1 -> (args.get(0).baseForm().getKey().compareTo(Constant.ZERO(TYPE)) < 0) ? this.negate(args.get(0)) : args.get(0);
            case 2 -> this.greatestCommonDivisor(args.get(0), args.get(1));
            default -> this.greatestCommonDivisor(this.greatestCommonDivisor(args.subList(0, args.size() / 2)),
                    this.greatestCommonDivisor(args.subList(args.size() / 2, args.size())));
        };
    }

    public Constant<T> constantGreatestCommonDivisor(List<Constant<T>> args) {
        Constant<T> GCD = args.get(0);
        for (Constant<T> arg : args.subList(1, args.size())) {
            GCD = GCD.gcd(arg);
        }
        return GCD;
    }

    private static class GCDGraph<T extends Expression<T>> {
        Expression<T>[] elements = new Expression[16];
        int binaryRepresentation;
        TreeSet<Pair<Integer, Double>> GCDList;

        HashSet<Integer> ignoredSubsets = new HashSet<>();

        public GCDGraph(ArrayList<Expression<T>> elements, Comparator<Pair<Integer, Double>> comparator) {
            for (int i = 0; i < elements.size(); i++) {
                this.elements[i] = elements.get(i);
            }
            this.binaryRepresentation = (1 << elements.size()) - 1;
            this.GCDList = new TreeSet<>(comparator);
        }

        public String toString() {
            return Utils.setParse(this.elements, this.binaryRepresentation) + "=" + this.GCDList;
        }
    }

    private final Comparator<Pair<Integer, Double>> GCDGraphComparator = (o1, o2) -> {
        if (!Objects.equals(o1.getValue(), o2.getValue())) {
            return Double.compare(o2.getValue(), o1.getValue());
        } else {
            int size1 = Utils.countSetBits(o1.getKey()),
                size2 = Utils.countSetBits(o2.getKey());
            if (size1 != size2) {
                return size1 - size2;
            } else {
                return o1.getKey() - o2.getKey();
            }
        }
    };

    private void addToGCDGraph(ArrayList<ArrayList<Integer>> subsets, int anchor, GCDGraph<T> graph) {
        Constant<T> ONE = Constant.ONE(TYPE);
        HashMap<Integer, Expression<T>> ring = Utils.setMap(graph.elements, graph.binaryRepresentation);

        int zeroAnchor = (anchor == 0) ? 0 : 1;
        for (int initialSubsetSize = 2 - zeroAnchor; initialSubsetSize < subsets.size(); initialSubsetSize++) {
            ArrayList<Integer> subsetList = subsets.get(initialSubsetSize);
            int subsetSize = initialSubsetSize + zeroAnchor;
            boolean allOnes = true;

            HashMap<Integer, Expression<T>> newRing = new HashMap<>();
            for (int subset : subsetList) {
                if (!graph.ignoredSubsets.contains(subset | anchor)) {
                    int upper = (subset & (subset - 1)) | anchor;
                    int lower = (initialSubsetSize == 1) ? subset : ((subset ^ Integer.highestOneBit(subset)) | anchor);
                    subset |= anchor;

                    Expression<T> GCD = this.greatestCommonDivisor(
                            ring.getOrDefault(lower, ONE),
                            ring.getOrDefault(upper, ONE)
                    );
                    allOnes &= GCD.equalsOne();

                    if (GCD.equalsOne()) {
                        graph.ignoredSubsets.addAll(Utils.supersets(subset, graph.binaryRepresentation));
                    } else {
                        newRing.put(subset, GCD);
                        graph.GCDList.add(new Pair<>(subset, this.numberOfOperations(GCD) * subsetSize));
                    }
                }
            }
            if (allOnes) {
                return;
            }
            ring = newRing;
        }
    }

    public GCDGraph<T> fullGCDGraph(ArrayList<Expression<T>> args) {
        ArrayList<ArrayList<Integer>> subsets = Utils.sortedContiguousSubsets(args.size());
        GCDGraph<T> graph = new GCDGraph<>(args, this.GCDGraphComparator);
        this.addToGCDGraph(subsets, 0, graph);
        return graph;
    }

    public GCDGraph<T> reduceGCDGraph(GCDGraph<T> graph) {
        TreeSet<Pair<Integer, Double>> GCDList = graph.GCDList;

        while (GCDList.size() > 0 && graph.GCDList.first().getValue() > 1) {
            int removedElements = GCDList.first().getKey();
            graph.binaryRepresentation ^= removedElements;
            GCDList.removeIf(pair -> (pair.getKey() & removedElements) != 0);

            Expression<T> sum = this.add(Utils.setParse(graph.elements, removedElements).toArray());
            ArrayList<ArrayList<Integer>> subsetList = Utils.sortedSubsetsBinary(graph.binaryRepresentation);
            int anchorPosition = removedElements & -removedElements;
            graph.binaryRepresentation |= anchorPosition;
            graph.elements[Integer.numberOfTrailingZeros(removedElements)] = sum;

            this.addToGCDGraph(subsetList, anchorPosition, graph);
        }
        return graph;
    }

    public GCDGraph<T> GCDReduction(ArrayList<Expression<T>> args) {
        return reduceGCDGraph(fullGCDGraph(args));
    }

    /** SECTION: Basic operations =================================================================================== */

    public Constant<T> complex(Number re, Number im) {
        return new Complex<>(re, im, TYPE);
    }

    public Constant<T> infinity(Expression<T> expr) {
        return (Constant<T>) new Infinity<>(expr, TYPE).close().expressionSimplify();
    }

    public Expression<T> add(Object ... args) {
        ArrayList<Expression<T>> exprArgs = new ArrayList<>();
        for (Object obj : args) {
            Expression<T> exprArg = this.objectConversion(obj);
            if (!exprArg.equalsZero()) {
                exprArgs.add(exprArg);
            }
        }
        return switch (exprArgs.size()) {
            case 0 -> Constant.ZERO(TYPE);
            case 1 -> exprArgs.get(0);
            default -> new Add<>(exprArgs, TYPE).close().expressionSimplify();
        };
    }

    public Expression<T> sub(Object o1, Object o2) {
        Expression<T>   expr1 = this.objectConversion(o1),
                        expr2 = this.objectConversion(o2);
        if (expr1.equalsZero()) {
            return this.negate(expr2);
        } else if (expr2.equalsZero()) {
            return expr1;
        } else if (expr1 instanceof Constant<T> const1 && expr2 instanceof Constant<T> const2) {
            return const1.sub(const2);
        } else {
            return new Add<>(Arrays.asList(expr1, this.negate(expr2)), TYPE).close().expressionSimplify();
        }
    }

    public Expression<T> mul(Object ... args) {
        ArrayList<Expression<T>> exprArgs = new ArrayList<>();
        for (Object obj : args) {
            Expression<T> exprArg = this.objectConversion(obj);
            if (exprArg.equalsZero()) {
                return Constant.ZERO(TYPE);
            } else if (!exprArg.equalsOne()) {
                exprArgs.add(exprArg);
            }
        }
        return switch (exprArgs.size()) {
            case 0 -> Constant.ONE(TYPE);
            case 1 -> exprArgs.get(0);
            default -> new Mul<>(exprArgs, TYPE).close().expressionSimplify();
        };
    }

    public Expression<T> div(Object o1, Object o2) {
        Expression<T>   expr1 = this.objectConversion(o1),
                        expr2 = this.objectConversion(o2);
        if (expr1.equalsOne()) {
            return this.invert(expr2);
        } else if (expr2.equalsOne()) {
            return expr1;
        } else if (expr1 instanceof Constant<T> const1 && expr2 instanceof Constant<T> const2) {
            return const1.div(const2);
        } else {
            // System.out.println("Dividing " + o1 + " " + o2);
            return new Mul<>(Arrays.asList(expr1, this.invert(expr2)), TYPE).close().expressionSimplify();
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
        Constant<T> exponentExpr = (Constant<T>) this.objectConversion(exponent);
        if (baseExpr instanceof Constant<T> baseConst) {
            return baseConst.pow(exponentExpr);
        } else {
            // System.out.println("Pow " + base + " " + exponent);
            return new Pow<>(baseExpr, exponentExpr, TYPE).close().expressionSimplify();
        }
    }

    public Expression<T> exp(Object obj) {
        return this.pow(Constant.E(TYPE), obj);
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
                        this.add(Utils.map(addExpr.inputs.get(Add.Parameter.TERMS), this::real).toArray()));
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
                        this.add(Utils.map(addExpr.inputs.get(Add.Parameter.TERMS), this::imaginary).toArray()));
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
            if (expr == null || expr instanceof Univariate<T>) {
                return expr;
            } else if (expr instanceof Constant<T> constExpr) {
                return constExpr.conjugate();
            } else {
                HashMap<Entity.InputType, ArrayList<Entity>> conjugateInputs = new HashMap<>();
                for (Entity.InputType inputType : expr.getInputTypes()) {
                    conjugateInputs.put(inputType, Utils.map(expr.getInputs().get(inputType), this::conjugate));
                }
                return (Expression<T>) expr.create(conjugateInputs);
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

