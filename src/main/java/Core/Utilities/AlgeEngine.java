package Core.Utilities;

import Core.AlgeSystem.Constants.*;
import Core.AlgeSystem.UnicardinalTypes.*;
import Core.AlgeSystem.Functions.*;
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

    public int numberOfOperations(Expression<T> expr) {
        if (expr instanceof Add<T> || expr instanceof Mul<T>) {
            int operations = expr.getInputs().get("Terms").size() - 1;
            for (Entity ent : expr.getInputs().get("Terms")) {
                operations += numberOfOperations((Expression<T>) ent);
            }
            if ((expr instanceof Add<T> addExpr && !addExpr.constant.equals(Constant.ZERO(TYPE)))
                    || (expr instanceof Mul<T> mulExpr && !mulExpr.constant.equals(Constant.ONE(TYPE)))) {
                operations += 1;
            }
            return operations;
        } else if (expr instanceof Pow<T> powExpr) {
            return this.numberOfOperations(powExpr.base) + this.numberOfOperations(powExpr.exponent) + 1;
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
                return new Mul<>(Arrays.asList(mulExpr.constant,
                        ((Expression<T>) mulExpr.inputs.get("Terms").firstEntry().getElement()).expand()), TYPE).reduction();
            }
            ArrayList<Expression<T>> expandableTerms = new ArrayList<>();
            ArrayList<Expression<T>> rest = new ArrayList<>();
            for (Entity term : inputTerms) {
                if (term instanceof Add) {
                    expandableTerms.add((Add<T>) term);
                } else {
                    rest.add((Expression<T>) term);
                }
            }
            Expression<T> singleton = new Mul<>(rest, TYPE).reduction();
            if (!singleton.equals(Constant.ONE(TYPE))) {
                expandableTerms.add(0, singleton);
            }
            if (expandableTerms.size() == 1) {
                return new Mul<>(Arrays.asList(singleton, mulExpr.constant), TYPE).reduction();
            } else if (expandableTerms.size() == 2) {
                ArrayList<Expression<T>> expansion1Terms = this.additiveTerms(expandableTerms.get(0).expand());
                ArrayList<Expression<T>> expansion2Terms = this.additiveTerms(expandableTerms.get(1).expand());
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
                // System.out.println(expandableTerms);
                for (Expression<T> ent : expandableTerms) {
                    // System.out.println(ent + " of " + expandableTerms);
                    product = this.expand(new Mul<>(Arrays.asList(product, ent), TYPE).reduction());
                }
                return product;
            }
        } else if (expr instanceof Pow<T> powExpr) {
            if (powExpr.base instanceof Add<T> addExpr && powExpr.exponent instanceof Complex<T> cpx
                    && cpx.integer() && cpx.re.intValue() > 0) {
                // System.out.println("Expanding " + addExpr + " " + cpx);
                int n = cpx.re.intValue();
                if (n == 2) {
                    ArrayList<Expression<T>> expansion = this.additiveTerms(addExpr);
                    ArrayList<Expression<T>> expandedTerms = new ArrayList<>();
                    for (int i = 0; i < expansion.size(); i++) {
                        for (int j = 0; j < i; j++) {
                            Expression<T> product = this.mul(expansion.get(i), expansion.get(j));
                            expandedTerms.add(new Mul<>(Arrays.asList(product, this.complex(2, 0)), TYPE).reduction());
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
            } else {
                return powExpr;
            }
        } else if (expr instanceof Constant || expr instanceof Univariate) {
            return expr;
        } else {
            return null;
        }
    }

    public ArrayList<Expression<T>> additiveTerms(Expression<T> expr) {
        ArrayList<Expression<T>> expansionTerms = new ArrayList<>();
        if (expr instanceof Add<T> addExpr) {
            addExpr.inputs.get("Terms").forEach(arg -> expansionTerms.add((Expression<T>) arg));
            if (!addExpr.constant.equals(Constant.ZERO(TYPE))) {
                expansionTerms.add(addExpr.constant);
            }
        } else {
            expansionTerms.add(expr);
        }
        return expansionTerms;
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
                    baseOrder = mulOrder.baseForm().getValue();
                    orders.put(baseOrder, orders.getOrDefault(baseOrder, Constant.ZERO(TYPE)).add(mulOrder.constant));
                } else {
                    orders.put(termOrder, orders.getOrDefault(termOrder, Constant.ZERO(TYPE)).add(Constant.ONE(TYPE)));
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

    private Expression<T> greatestCommonDivisorBase(Expression<T> e1, Expression<T> e2) {
        if (e1.equals(Constant.ZERO(TYPE))) {
            return e2;
        } else if (e2.equals(Constant.ZERO(TYPE))) {
            return e1;
        } else if (e1 instanceof Constant<T> const1) {
            return const1.gcd(e2.baseForm().getKey());
        } else if (e2 instanceof Constant<T> const2) {
            return const2.gcd(e1.baseForm().getKey());
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
        return switch (args.size()) {
            case 1 -> (args.get(0).baseForm().getKey().compareTo(Constant.ZERO(TYPE)) < 0) ? this.negate(args.get(0)) : args.get(0);
            case 2 -> this.greatestCommonDivisorBase(args.get(0), args.get(1));
            default -> this.greatestCommonDivisorBase(this.greatestCommonDivisor(args.subList(0, args.size() / 2)),
                    this.greatestCommonDivisor(args.subList(args.size() / 2, args.size())));
        };
        /**if (args.size() == 1) {
            return args.get(0);
        }
        Expression<T> gcd;
        if (args.size() == 2) {
            gcd = this.greatestCommonDivisorBase(args.get(0), args.get(1));
        } else {
            gcd = this.greatestCommonDivisorBase(this.greatestCommonDivisor(args.subList(0, args.size() / 2)),
                    this.greatestCommonDivisor(args.subList(args.size() / 2, args.size())));
        }
        return gcd;*/

        /**Constant<T> I = Constant.I(TYPE);
        ArrayList<Expression<T>> bases = Utils.map(args, arg -> arg.baseForm().getValue());
        Expression<T> weightedBase = Collections.max(bases, Utils.PRIORITY_COMPARATOR);
        Constant<T> weightedConstant = args.get(bases.indexOf(weightedBase)).baseForm().getKey().div(gcd.baseForm().getKey());
        Constant<T> maxRotation = null;
        int maxIndex = -1;
        for (int i = 0; i < 4; i++) {
            if (maxRotation == null || weightedConstant.compareTo(maxRotation) > 0) {
                maxRotation = weightedConstant;
                maxIndex = i;
            }
            weightedConstant = weightedConstant.div(I);
        }
        return this.mul(this.pow(I, maxIndex), gcd);*/

        /**Pair<Constant<T>, Expression<T>> gcdBaseForm = gcd.baseForm();
        ArrayList<Constant<T>> constants = Utils.map(args, arg -> arg.baseForm().getKey().div(gcdBaseForm.getKey()));
        Constant<T> I = Constant.I(TYPE);
        ArrayList<Integer> rotations = new ArrayList<>();
        int numberOfIntegers = Integer.MIN_VALUE;
        for (int i = 0; i < 4; i++) {
            int count = 0;
            for (Constant<T> constant : constants) {
                if (constant.positiveInteger()) {
                    count ++;
                }
            }
            if (count > numberOfIntegers) {
                numberOfIntegers = count;
                rotations.clear();
                rotations.add(i);
            } else if (count == numberOfIntegers) {
                rotations.add(i);
            }
            constants = Utils.map(constants, constant -> constant.div(I));
        }
        if (rotations.size() == 1) {
            return this.mul(this.pow(I, rotations.get(0)), gcd);
        } else {
            ArrayList<Expression<T>> bases = Utils.map(args, arg -> arg.baseForm().getValue());
            Expression<T> weightedBase = Collections.max(bases, Utils.PRIORITY_COMPARATOR);
            Constant<T> weightedConstant = constants.get(bases.indexOf(weightedBase));
            Constant<T> maxRotation = null;
            int maxIndex = -1;
            for (int i : rotations) {
                Constant<T> rotation = (Constant<T>) this.mul(this.pow(I, -i), weightedConstant);
                if (maxRotation == null || rotation.compareTo(maxRotation) > 0) {
                    maxRotation = rotation;
                    maxIndex = i;
                }
            }
            return this.mul(this.pow(I, maxIndex), gcd);
        }*/
    }

    private static class GCDValue<T extends Expression<T>> {
        Expression<T> GCD;
        int weight;

        public GCDValue(Expression<T> GCD, int weight) {
            this.GCD = GCD;
            this.weight = weight;
        }

        public String toString() {
            return "(" + GCD + ", " + weight + ")";
        }
    }

    private static class GCDNode<T extends Expression<T>> {
        HashSet<Expression<T>> elements;
        GCDValue<T> value;

        public GCDNode(HashSet<Expression<T>> elements, GCDValue<T> value) {
            this.elements = elements;
            this.value = value;
        }

        public String toString() {
            return "(" + elements + ", " + value + ")";
        }
    }

    private static class GCDGraph<T extends Expression<T>> {
        HashMap<HashSet<Expression<T>>, GCDValue<T>> GCDMap;
        TreeSet<GCDNode<T>> GCDList;

        HashSet<HashSet<Expression<T>>> ignoredSubsets = new HashSet<>();

        public GCDGraph(Comparator<GCDNode<T>> comparator) {
            this.GCDMap = new HashMap<>();
            this.GCDList = new TreeSet<>(comparator);
        }

        public String toString() {
            return "(" + GCDMap + ", " + GCDList + ")";
        }

        public void filter(Function<HashSet<Expression<T>>, Boolean> filter) {
            Iterator<GCDNode<T>> listIter = this.GCDList.iterator();
            Iterator<HashSet<Expression<T>>> mapIter = this.GCDMap.keySet().iterator();
            while (listIter.hasNext()) {
                if (filter.apply(listIter.next().elements)) {
                    listIter.remove();
                }
            }
            while (mapIter.hasNext()) {
                if (filter.apply(mapIter.next())) {
                    mapIter.remove();
                }
            }
        }
    }

    private final Comparator<GCDNode<T>> GCDGraphComparator = (o1, o2) -> {
        if (o1.value.weight != o2.value.weight) {
            return o2.value.weight - o1.value.weight;
        } else {
            if (o1.elements.size() != o2.elements.size()) {
                return o1.elements.size() - o2.elements.size();
            } else {
                return Utils.PRIORITY_COMPARATOR.compare(o2.value.GCD, o1.value.GCD);
            }
        }
    };

    private void addToGCDGraph(ArrayList<HashSet<Expression<T>>> subsets, Collection<Expression<T>> superset, GCDGraph<T> graph) {
        Constant<T> ONE = Constant.ONE(TYPE);

        for (HashSet<Expression<T>> subset : subsets) {
            if (!graph.ignoredSubsets.contains(subset)) {
                Expression<T> GCD;
                if (subset.size() == 2) {
                    GCD = this.greatestCommonDivisor(new ArrayList<>(subset));
                } else {
                    ArrayList<Expression<T>> elements = new ArrayList<>(subset);
                    Expression<T> GCD1 = graph.GCDMap.getOrDefault(new HashSet<>(elements.subList(0, elements.size() - 1)),
                            new GCDValue<>(ONE, 0)).GCD;
                    Expression<T> GCD2 = graph.GCDMap.getOrDefault(new HashSet<>(elements.subList(1, elements.size())),
                            new GCDValue<>(ONE, 0)).GCD;
                    GCD = this.greatestCommonDivisor(Arrays.asList(GCD1, GCD2));
                }
                if (GCD.equals(ONE)) {
                    graph.ignoredSubsets.addAll(Utils.supersets(subset, superset));
                } else {
                    GCDValue<T> gcdValue = new GCDValue<>(GCD, subset.size() * this.numberOfOperations(GCD));
                    graph.GCDMap.put(subset, gcdValue);
                    graph.GCDList.add(new GCDNode<>(subset, gcdValue));
                }
            }
        }
    }

    public GCDGraph<T> fullGCDGraph(ArrayList<Expression<T>> args) {
        ArrayList<ArrayList<HashSet<Expression<T>>>> subsetList = Utils.binarySortedSubsets(args);
        ArrayList<HashSet<Expression<T>>> subsets = new ArrayList<>();
        subsetList.subList(2, subsetList.size()).forEach(subsets::addAll);

        GCDGraph<T> graph = new GCDGraph<>(this.GCDGraphComparator);

        this.addToGCDGraph(subsets, args, graph);
        return graph;
    }

    public Pair<ArrayList<Expression<T>>, GCDGraph<T>> reduceGCDGraph(ArrayList<Expression<T>> args, GCDGraph<T> graph) {
        HashSet<Expression<T>> argSet = new HashSet<>(args);

        TreeSet<GCDNode<T>> GCDList = graph.GCDList;

        while (GCDList.size() > 0 && GCDList.first().value.weight > 1) {
            HashSet<Expression<T>> elements = new HashSet<>(GCDList.first().elements);
            argSet.removeAll(elements);
            graph.filter(subset -> {
                for (Expression<T> element : elements) {
                    if (subset.contains(element)) {
                        return true;
                    }
                }
                return false;
            });

            Expression<T> sum = this.add(elements.toArray());
            ArrayList<ArrayList<HashSet<Expression<T>>>> subsetList = Utils.binarySortedSubsets(new ArrayList<>(argSet));
            ArrayList<HashSet<Expression<T>>> subsets = new ArrayList<>();
            subsetList.subList(1, subsetList.size()).forEach(subsets::addAll);
            subsets.forEach(subset -> subset.add(sum));
            argSet.add(sum);

            this.addToGCDGraph(subsets, argSet, graph);
        }
        return new Pair<>(new ArrayList<>(argSet), graph);
    }

    public Pair<ArrayList<Expression<T>>, GCDGraph<T>> GCDReduction(ArrayList<Expression<T>> args) {
        System.out.println("Reducing " + args);
        return reduceGCDGraph(args, fullGCDGraph(args));
    }

    /** SECTION: Basic operations =================================================================================== */

    public Constant<T> complex(Number re, Number im) {
        return new Complex<>(re, im, TYPE);
    }

    public Constant<T> infinity(Expression<T> expr) {
        return (Constant<T>) new Infinity<>(expr, TYPE).expressionSimplify();
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
            default -> new Add<>(exprArgs, TYPE).expressionSimplify();
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
            return new Add<>(Arrays.asList(expr1, this.negate(expr2)), TYPE).expressionSimplify();
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
            default -> new Mul<>(exprArgs, TYPE).expressionSimplify();
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
            // System.out.println("Dividing " + o1 + " " + o2);
            return new Mul<>(Arrays.asList(expr1, this.invert(expr2)), TYPE).expressionSimplify();
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
            return new Pow<>(baseExpr, exponentExpr, TYPE).expressionSimplify();
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
                        this.add(Utils.map(addExpr.inputs.get("Terms"), this::real).toArray()));
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

