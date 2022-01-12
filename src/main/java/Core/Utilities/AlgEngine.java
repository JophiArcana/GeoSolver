package Core.Utilities;

import Core.AlgSystem.Constants.*;
import Core.AlgSystem.UnicardinalTypes.*;
import Core.AlgSystem.Operators.*;
import Core.EntityTypes.*;
import javafx.util.Pair;

import java.util.*;
import java.util.function.Function;


public class AlgEngine<T> {
    public final Class<T> TYPE;

    public AlgEngine(Class<T> type) {
        this.TYPE = type;
    }

    public Univariate<T> X() {
        return Univariate.create("\u5929", TYPE);
    }

    public static final double EPSILON = 1E-9;

    /** SECTION: Simplification Optimization ======================================================================== */
    public double numberOfOperations(Expression<T> expr) {
        if (expr instanceof Accumulation<T> accExpr) {
            ArrayList<Expression<T>> terms = Utils.cast(accExpr.inputs.get(Accumulation.Parameter.TERMS));
            double operations = (double) terms.size() - 1;
            for (Expression<T> term : terms) {
                operations += numberOfOperations(term);
            }
            return operations;
        } else if (expr instanceof Scale<T> scaleExpr) {
            return Math.abs(Math.log(scaleExpr.coefficient.abs()) / Math.log(2)) + this.numberOfOperations(scaleExpr.expression);
        } else if (expr instanceof Pow<T> powExpr) {
            return this.numberOfOperations(powExpr.base) + Math.abs(Math.log(powExpr.exponent) / Math.log(2));
        } else if (expr instanceof Univariate<T>) {
            return 1;
        } else if (expr instanceof Constant<T>) {
            return 0;
        } else {
            return Integer.MAX_VALUE;
        }
    }

    /** SECTION: Order of Growth ==================================================================================== */
    public Pair<Constant<T>, Double> orderOfGrowth(Expression<T> expr) {
        if (expr instanceof Add<T> addExpr) {
            Constant<T> coefficient = Constant.ZERO(TYPE);
            final double exponent = this.orderOfGrowth(addExpr.terms.firstKey()).getValue();
            for (Expression<T> term : Utils.<Entity, Expression<T>>cast(addExpr.inputs.get(Accumulation.Parameter.TERMS))) {
                coefficient = coefficient.add(this.orderOfGrowth(term).getKey());
            }
            return new Pair<>(coefficient, exponent);
        } else if (expr instanceof Scale<T> scaleExpr) {
            Pair<Constant<T>, Double> order = this.orderOfGrowth(scaleExpr.expression);
            return new Pair<>(scaleExpr.coefficient.mul(order.getKey()), order.getValue());
        } else if (expr instanceof Mul<T> mulExpr) {
            Constant<T> coefficient = Constant.ZERO(TYPE);
            double exponent = 0;
            for (Expression<T> term : Utils.<Entity, Expression<T>>cast(mulExpr.inputs.get(Accumulation.Parameter.TERMS))) {
                Pair<Constant<T>, Double> order = this.orderOfGrowth(term);
                coefficient = coefficient.add(order.getKey());
                exponent += order.getValue();
            }
            return new Pair<>(coefficient, exponent);
        } else if (expr instanceof Pow<T> powExpr) {
            Pair<Constant<T>, Double> order = this.orderOfGrowth(powExpr.base);
            return new Pair<>(order.getKey().pow(powExpr.exponent), order.getValue() * powExpr.exponent);
        } else if (expr instanceof Univariate<T>) {
            return new Pair<>(Constant.ONE(TYPE), 1.0);
        } else if (expr instanceof Constant<T> c) {
            return new Pair<>(c, 0.0);
        } else {
            /** TODO: Implement Order of Growth for other Functions */
            return null;
        }
    }

    /** SECTION: Greatest Common Divisor ============================================================================ */
    public Expression<T> greatestCommonDivisor(Expression<T> e1, Expression<T> e2) {
        if (e1 instanceof Scale<T> sc1 && e2 instanceof Scale<T> sc2) {
            return Scale.create(sc1.coefficient.gcd(sc2.coefficient), this.greatestCommonDivisor(sc1.expression, sc2.expression), TYPE);
        } else if (e1 instanceof Scale<T> sc) {
            return this.greatestCommonDivisor(sc.expression, e2);
        } else if (e2 instanceof Scale<T> sc) {
            return this.greatestCommonDivisor(e1, sc.expression);
        } else {
            HashMap<Expression<T>, Double> factors1 = form(e1), factors2 = form(e2);
            HashSet<Expression<T>> terms = new HashSet<>(factors1.keySet());
            terms.addAll(factors2.keySet());

            HashMap<Expression<T>, Double> result = new HashMap<>();
            for (Expression<T> term : terms) {
                double exponent = Math.max(factors1.getOrDefault(term, 0.0), factors2.getOrDefault(term, 0.0));
                if (exponent != 0) {
                    result.put(term, exponent);
                }
            }
            return Mul.create(Utils.map(result.entrySet(), entry -> Pow.create(entry.getKey(), entry.getValue(), TYPE)), TYPE);
        }
    }

    private HashMap<Expression<T>, Double> form(Expression<T> expr) {
        if (expr instanceof Pow<T> p) {
            return new HashMap<>(Map.of(p.base, p.exponent));
        } else if (expr instanceof Mul<T> m) {
            return new HashMap<>(m.terms);
        } else {
            return new HashMap<>(Map.of(expr, 1.0));
        }
    }

    public Expression<T> greatestCommonDivisor(List<Expression<T>> args) {
        if (args.size() == 2) {
            return this.greatestCommonDivisor(args.get(0), args.get(1));
        } else {
            return this.greatestCommonDivisor(
                    this.greatestCommonDivisor(args.subList(0, args.size() / 2)),
                    this.greatestCommonDivisor(args.subList(args.size() / 2, args.size()))
            );
        }
    }

    public Constant<T> constantGreatestCommonDivisor(List<Constant<T>> args) {
        Constant<T> GCD = args.get(0);
        for (Constant<T> arg : args.subList(1, args.size())) {
            GCD = GCD.gcd(arg);
        }
        return GCD;
    }

    private static class GCDGraph<T> {
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

            Expression<T> sum = Add.create(Utils.setParse(graph.elements, removedElements), TYPE);
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
        return Complex.create(re, im, TYPE);
    }

    public Constant<T> infinity(Expression<T> expr) {
        return Infinity.create(expr, TYPE);
    }

    public Expression<T> add(Iterable<Expression<T>> args) {
        return Add.create(args, TYPE);
    }

    public Expression<T> add(Expression<T> arg1, Expression<T> arg2) {
        return Add.create(List.of(arg1, arg2), TYPE);
    }

    public Expression<T> sub(Expression<T> arg1, Expression<T> arg2) {
        return Add.create(Arrays.asList(arg1, Scale.create(-1, arg2, TYPE)), TYPE);
    }

    public Expression<T> mul(Iterable<Expression<T>> args) {
        return Mul.create(args, TYPE);
    }

    public Expression<T> mul(Expression<T> arg1, Expression<T> arg2) {
        return Mul.create(List.of(arg1, arg2), TYPE);
    }

    public Expression<T> div(Expression<T> arg1, Expression<T> arg2) {
        return Mul.create(Arrays.asList(arg1, Pow.create(arg2, -1, TYPE)), TYPE);
    }

    public Expression<T> pow(Expression<T> base, double exponent) {
        return Pow.create(base, exponent, TYPE);
    }

    public Expression<T> negate(Expression<T> arg) {
        return Scale.create(Constant.NONE(TYPE), arg, TYPE);
    }

    public Expression<T> invert(Expression<T> arg) {
        return Pow.create(arg, -1, TYPE);
    }

    /** SECTION: Cyclic Sum ========================================================================================= */
    public Expression<T> cyclicSum(Function<ArrayList<Expression<T>>, Expression<T>> func, ArrayList<Expression<T>> args) {
        ArrayList<Expression<T>> terms = new ArrayList<>();
        for (int i = 0; i < args.size(); i++) {
            terms.add(func.apply(args));
            args.add(0, args.remove(args.size() - 1));
        }
        return Add.create(terms, TYPE);
    }

    /** SECTION: Real, Imaginary, Conjugate ========================================================================= */
    public Expression<T> real(Expression<T> arg) {
        if (arg instanceof Univariate) {
            return arg;
        } else if (arg instanceof Complex<T> cpx) {
            return Complex.create(cpx.re, 0, TYPE);
        } else if (arg instanceof Infinity<T> inf) {
            return Infinity.create(this.real(inf.expression), TYPE);
        } else if (arg instanceof Add<T> addExpr) {
            return Add.create(Utils.map(Utils.cast(addExpr.inputs.get(Accumulation.Parameter.TERMS)), this::real), TYPE);
        } else if (arg instanceof Scale<T> scaleExpr && scaleExpr.coefficient instanceof Complex<T> cpx) {
            if (cpx.re.equals(0)) {
                return Scale.create(Complex.create(-cpx.im.doubleValue(), 0, TYPE), this.imaginary(scaleExpr.expression), TYPE);
            } else if (cpx.im.equals(0)) {
                return Scale.create(Complex.create(cpx.re.doubleValue(), 0, TYPE), this.real(scaleExpr.expression), TYPE);
            } else {
                return Add.create(List.of(
                        Scale.create(Complex.create(cpx.re.doubleValue(), 0, TYPE), this.real(scaleExpr.expression), TYPE),
                        Scale.create(Complex.create(-cpx.im.doubleValue(), 0, TYPE), this.imaginary(scaleExpr.expression), TYPE)
                ), TYPE);
            }
        } else {
            return Scale.create(Complex.create(0.5, 0, TYPE), this.add(arg, this.conjugate(arg)), TYPE);
        }
    }

    public Expression<T> imaginary(Expression<T> arg) {
        if (arg instanceof Complex<T> cpx) {
            return Complex.create(cpx.im, 0, TYPE);
        } else if (arg instanceof Infinity<T> inf) {
            return Infinity.create(this.imaginary(inf.expression), TYPE);
        } else if (arg instanceof Univariate<T>) {
            return Constant.ZERO(TYPE);
        } else if (arg instanceof Add<T> addExpr) {
            return Add.create(Utils.map(Utils.cast(addExpr.inputs.get(Accumulation.Parameter.TERMS)), this::imaginary), TYPE);
        } else if (arg instanceof Scale<T> scaleExpr && scaleExpr.coefficient instanceof Complex<T> cpx) {
            if (cpx.re.equals(0)) {
                return Scale.create(Complex.create(cpx.im.doubleValue(), 0, TYPE), this.real(scaleExpr.expression), TYPE);
            } else if (cpx.im.equals(0)) {
                return Scale.create(Complex.create(cpx.re.doubleValue(), 0, TYPE), this.imaginary(scaleExpr.expression), TYPE);
            } else {
                return Add.create(List.of(
                        Scale.create(Complex.create(cpx.re.doubleValue(), 0, TYPE), this.imaginary(scaleExpr.expression), TYPE),
                        Scale.create(Complex.create(cpx.im.doubleValue(), 0, TYPE), this.real(scaleExpr.expression), TYPE)
                ), TYPE);
            }
        } else {
            return Scale.create(Complex.create(0, -0.5, TYPE), this.sub(arg, this.conjugate(arg)), TYPE);
        }
    }

    public Expression<T> conjugate(Expression<T> arg) {
        if (arg instanceof Constant<T> constExpr) {
            return constExpr.conjugate();
        } else if (arg instanceof Add<T> addExpr) {
            return Add.create(Utils.map(Utils.cast(addExpr.inputs.get(Accumulation.Parameter.TERMS)), this::conjugate), TYPE);
        } else if (arg instanceof Scale<T> scaleExpr) {
            return Scale.create(scaleExpr.coefficient.conjugate(), this.conjugate(scaleExpr.expression), TYPE);
        } else if (arg instanceof Mul<T> mulExpr) {
            return Mul.create(Utils.map(Utils.cast(mulExpr.inputs.get(Accumulation.Parameter.TERMS)), this::conjugate), TYPE);
        } else if (arg instanceof Pow<T> powExpr) {
            return Pow.create(this.conjugate(powExpr.base), powExpr.exponent, TYPE);
        } else {
            return arg;
        }
    }

    public Expression<T> norm(Expression<T> arg) {
        return Pow.create(Add.create(List.of(
                Pow.create(this.real(arg), 2, TYPE),
                Pow.create(this.imaginary(arg), 2, TYPE)
        ), TYPE), 0.5, TYPE);
    }

    public Expression<T> norm2(Expression<T> arg) {
        return Add.create(List.of(
                Pow.create(this.real(arg), 2, TYPE),
                Pow.create(this.imaginary(arg), 2, TYPE)
        ), TYPE);
    }
}