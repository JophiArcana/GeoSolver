package core.util;

import java.util.*;

import core.structure.Entity;
import core.structure.unicardinal.alg.Constant;
import core.structure.unicardinal.alg.Expression;
import core.structure.unicardinal.alg.Variable;
import core.structure.unicardinal.alg.structure.Accumulation;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;
import core.structure.unicardinal.alg.structure.Reduction;
import core.Diagram;
import core.structure.unicardinal.alg.symbolic.operator.*;
import javafx.util.*;


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
                    mul.terms.forEach((term, count) -> put((SymbolicExpression) term, count));
            }};
        } else {
            return new HashMap<>(Map.of(expr, 1.0));
        }
    }

    public SymbolicExpression greatestCommonDivisor(List<SymbolicExpression> args) {
        if (args.size() == 2) {
            return this.greatestCommonDivisor(args.get(0), args.get(1));
        } else if (args.size() == 3) {
            return this.greatestCommonDivisor(this.greatestCommonDivisor(args.get(0), args.get(1)), args.get(2));
        } else {
            return this.greatestCommonDivisor(
                    this.greatestCommonDivisor(args.subList(0, args.size() / 2)),
                    this.greatestCommonDivisor(args.subList(args.size() / 2, args.size()))
            );
        }
    }

    /** SECTION: Subset Graph Computing ============================================================================= */
    private abstract static class SubsetGraph<T extends Entity, U> {
        static Comparator<Pair<Integer, Double>> COMPARATOR = (o1, o2) -> {
            if (!o1.getValue().equals(o2.getValue())) {
                return Double.compare(o2.getValue(), o1.getValue());
            } else {
                int size1 = Integer.bitCount(o1.getKey()), size2 = Integer.bitCount(o2.getKey());
                if (size1 != size2) {
                    return size1 - size2;
                } else {
                    return o1.getKey() - o2.getKey();
                }
            }
        };

        Diagram diagram;
        T[] elements;
        HashMap<Integer, U> baseCases = new HashMap<>();
        int binaryRepresentation;
        TreeSet<Pair<Integer, Double>> weightMap = new TreeSet<>(SubsetGraph.COMPARATOR);

        HashSet<Integer> ignoredSubsets = new HashSet<>();

        public SubsetGraph(ArrayList<T> elements) {
            this.diagram = elements.get(0).getDiagram();
            this.elements = (T[]) elements.toArray();
            this.binaryRepresentation = (1 << this.elements.length) - 1;
            for (int i = 0; i < this.elements.length; i++) {
                this.baseCases.put(1 << i, this.baseCase(this.elements[i]));
            }
            AlgEngine.addToSubsetGraph(Utils.sortedContiguousSubsets(this.elements.length), 0, this);
            AlgEngine.reduceSubsetGraph(this);
        }

        public String toString() {
            return Utils.setParse(this.elements, this.binaryRepresentation) + "=" + this.weightMap;
        }

        public abstract T reduction(ArrayList<T> list);
        public abstract U baseCase(T t);
        public abstract U induct(U u1, U u2);
        public abstract double weightFunction(U u);
        public abstract U unit();
        public abstract boolean isUnitary(U u);
    }

    /**
     *
     * @param subsets   Subsets that (once unioned with anchor) whose weights need to be computed
     * @param anchor    At most one bit representing the element that needs to be computed in the new subsets
     * @param graph     Data structure containing the elements, bit representation representing the complete set,
     *                  TreeSet of weights corresponding to subsets, and which ignored subsets guaranteed to have
     *                  unitary output value U
     */
    private static <T extends Entity, U> void addToSubsetGraph(ArrayList<List<Integer>> subsets, int anchor, SubsetGraph<T, U> graph) {
        U unit = graph.unit();
        HashMap<Integer, U> ring = graph.baseCases;

        int anchorSize = (anchor == 0) ? 0 : 1;
        for (int startingSubsetSize = 2 - anchorSize; startingSubsetSize < subsets.size(); startingSubsetSize++) {
            List<Integer> subsetList = subsets.get(startingSubsetSize);
            int subsetSize = startingSubsetSize + anchorSize;
            boolean allOnes = true;

            HashMap<Integer, U> newRing = new HashMap<>();
            for (int subset : subsetList) {
                if (!graph.ignoredSubsets.contains(subset)) {
                    int upper = (subset & (subset - 1)) | anchor;
                    int lower = (startingSubsetSize == 1) ? subset : ((subset ^ Integer.highestOneBit(subset)) | anchor);
                    subset |= anchor;

                    U result = graph.induct(ring.getOrDefault(lower, unit), ring.getOrDefault(upper, unit));
                    allOnes &= graph.isUnitary(result);

                    if (graph.isUnitary(result)) {
                        graph.ignoredSubsets.addAll(Utils.supersets(subset, graph.binaryRepresentation));
                    } else {
                        newRing.put(subset, result);
                        graph.weightMap.add(new Pair<>(subset, graph.weightFunction(result) * (subsetSize - 1)));
                    }
                }
            }
            if (allOnes) {
                return;
            }
            ring = newRing;
        }
    }

    public static <T extends Entity, U> void reduceSubsetGraph(SubsetGraph<T, U> graph) {
        while (graph.weightMap.size() > 0) {
            int removedElements = graph.weightMap.first().getKey();
            graph.binaryRepresentation ^= removedElements;
            graph.weightMap.removeIf(entry -> ((entry.getKey() & removedElements) != 0));

            T reduction = graph.reduction(Utils.setParse(graph.elements, removedElements));
            ArrayList<List<Integer>> subsetList = Utils.sortedSubsetsBinary(graph.binaryRepresentation);

            int anchorPosition = removedElements & -removedElements;
            graph.binaryRepresentation |= anchorPosition;
            graph.elements[Integer.numberOfTrailingZeros(removedElements)] = reduction;

            AlgEngine.addToSubsetGraph(subsetList, anchorPosition, graph);
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