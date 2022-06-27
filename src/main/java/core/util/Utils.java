package core.util;

import core.structure.multicardinal.geo.circle.structure.*;
import core.structure.multicardinal.geo.line.function.*;
import core.structure.multicardinal.geo.line.structure.*;
import core.structure.multicardinal.geo.point.structure.*;
import core.structure.multicardinal.geo.point.function.*;
import core.structure.multicardinal.geo.triangle.Triangle;
import core.structure.unicardinal.alg.Variable;
import core.structure.unicardinal.alg.directed.*;
import core.structure.unicardinal.alg.directed.constant.*;
import core.structure.unicardinal.alg.directed.function.*;
import core.structure.unicardinal.alg.directed.operator.*;
import core.structure.unicardinal.alg.symbolic.*;
import core.structure.unicardinal.alg.symbolic.constant.*;
import core.structure.unicardinal.alg.symbolic.operator.*;
import core.structure.Entity;
import core.structure.unicardinal.*;
import com.google.common.collect.TreeMultiset;
import com.google.common.hash.Hashing;
import core.structure.unicardinal.alg.symbolic.operator.SymbolicScale;
import core.util.comparators.MulticardinalComparator;
import core.util.comparators.UnicardinalComparator;

import java.util.*;
import java.util.function.Function;

public class Utils {
    public static final AlgEngine ENGINE = new AlgEngine();

    public static final UnicardinalComparator UNICARDINAL_COMPARATOR = new UnicardinalComparator();
    public static final MulticardinalComparator MULTICARDINAL_COMPARATOR = new MulticardinalComparator();

    public static final ArrayList<Class<? extends Entity>> CLASS_IDS = new ArrayList<>(Arrays.asList(
        /** SECTION: Directed Expression ============================================================================ */
            DirectedReal.class,
            DirectedVariable.class,
            DirectedScale.class,
            DirectedAdd.class,
            Directed.class,

        /** SECTION: Symbolic Expression ============================================================================ */
            SymbolicReal.class,
            SymbolicInfinity.class,
            SymbolicVariable.class,
            SymbolicAbs.class,
            SymbolicScale.class,
            SymbolicAdd.class,
            SymbolicPow.class,
            SymbolicMul.class,

        /** SECTION: Points ========================================================================================= */
            Coordinate.class,
            PointVariable.class,
            Midpoint.class,
            Connect.ConnectPointDual.class,
            Triangle.Centroid.class,
            Triangle.Orthocenter.class,
            Triangle.Incenter.class,
            Triangle.Circumcircle.class,

        /** SECTION: Lines ========================================================================================== */
            Axis.class,
            LineVariable.class,
            Connect.class,

        /** SECTION: Circles ======================================================================================== */
            Disc.class,
            CircleVariable.class,
            Triangle.Incircle.class,
            Triangle.Circumcircle.class
    ));

    public static final HashMap<Class<? extends Entity>, Integer> CLASS_ID_MAP = new HashMap<>() {{
        for (int i = 0; i < Utils.CLASS_IDS.size(); i++) {
            put(Utils.CLASS_IDS.get(i), i);
        }
    }};

    public static final HashSet<Class<? extends Unicardinal>> CLOSED_FORM = new HashSet<>(Arrays.asList(
            SymbolicInfinity.class,
            Variable.class
    ));

    public static String className(Object o) {
        Class cls = o.getClass();
        if (cls.getEnclosingClass() != null) {
            return cls.getEnclosingClass().getSimpleName();
        } else {
            return cls.getSimpleName();
        }
    }

    public static int classCode(Object o) {
        return Utils.CLASS_ID_MAP.get(o.getClass());
    }

    public static int compareInputs(Entity e1, Entity e2) {
        assert e1.getClass() == e2.getClass(): "Classes must be identical.";
        HashMap<Entity.InputType<?>, TreeMultiset<? extends Entity>> inputs1 = e1.getInputs();
        HashMap<Entity.InputType<?>, TreeMultiset<? extends Entity>> inputs2 = e2.getInputs();
        for (Entity.InputType<? extends Entity> inputType : e1.getInputTypes()) {
            Iterator<? extends Entity> iter1 = inputs1.get(inputType).iterator();
            Iterator<? extends Entity> iter2 = inputs2.get(inputType).iterator();
            while (iter1.hasNext()) {
                int inputComparison = inputType.compare(iter1.next(), iter2.next());
                if (inputComparison != 0) {
                    return inputComparison;
                }
            }
        }
        return 0;
    }

    public static <T, S> List<S> map(Collection<T> list, Function<T, S> function) {
        S[] result = (S[]) new Object[list.size()];
        int i = 0;
        for (T t : list) {
            result[i] = function.apply(t);
            i++;
        }
        return List.of(result);
    }

    public static <T, S> ArrayList<S> cast(Iterable<T> args) {
        ArrayList<S> list = new ArrayList<>();
        args.forEach(arg -> list.add((S) arg));
        return list;
    }

    public static double gcd(double x, double y) {
        x = Math.abs(x);
        y = Math.abs(y);
        double upper = Math.max(x, y), lower = Math.min(x, y);
        while (lower > AlgEngine.EPSILON) {
            double remainder = upper % lower;
            upper = lower;
            lower = remainder;
        }
        return upper;
    }

    public static int binomial(int a, int b) {
        if (b > a / 2) {
            return binomial(a, a - b);
        } else {
            int numerator = 1;
            int denominator = 1;
            for (int i = 0; i < b; i++) {
                numerator *= (a - i);
                denominator *= (b - i);
            }
            return numerator / denominator;
        }
    }

    public static ArrayList<List<Integer>> sortedSubsetsBinary(int set) {
        int k = Integer.bitCount(set);
        if (k == 0) {
            return new ArrayList<>(Collections.singletonList(new ArrayList<>(Collections.singletonList(0))));
        } else {
            int upper_bits = set & (set - 1);
            int lowest_bit = set - upper_bits;
            ArrayList<List<Integer>> lower = Utils.sortedSubsetsBinary(upper_bits);
            List<List<Integer>> upper = Utils.map(lower, list -> Utils.map(list, arg -> arg | lowest_bit));
            for (int i = 0; i < k - 1; i++) {
                lower.get(i + 1).addAll(upper.get(i));
            }
            lower.add(upper.get(lower.size() - 1));
            return lower;
        }
    }

    public static ArrayList<List<Integer>> sortedContiguousSubsets(int setSize) {
        ArrayList<List<Integer>> subsets = new ArrayList<>();
        int bound = 1 << setSize;
        subsets.add(new ArrayList<>(Collections.singletonList(0)));
        for (int i = 1; i <= setSize; i++) {
            List<Integer> subsetList = new ArrayList<>();
            int k = (1 << i) - 1;
            while (k < bound) {
                subsetList.add(k);
                int lowest_bit = k & -k;
                int ripple = k + lowest_bit;
                k = ripple | (((k ^ ripple) >> 2) / lowest_bit);
            }
            subsets.add(subsetList);
        }
        return subsets;
    }

    public static <T> ArrayList<List<HashSet<T>>> sortedSubsets(ArrayList<T> args) {
        if (args.size() == 0) {
            return new ArrayList<>(Collections.singletonList(new ArrayList<>(Collections.singletonList(new HashSet<>()))));
        } else {
            ArrayList<List<HashSet<T>>> lower = Utils.sortedSubsets(new ArrayList<>(args.subList(0, args.size() - 1)));
            ArrayList<List<HashSet<T>>> upper = new ArrayList<>();
            for (List<HashSet<T>> list : lower) {
                upper.add(Utils.map(list, set -> {
                    HashSet<T> duplicate = new HashSet<>(set);
                    duplicate.add(args.get(args.size() - 1));
                    return duplicate;
                }));
            }
            for (int i = 0; i < lower.size() - 1; i++) {
                lower.get(i + 1).addAll(upper.get(i));
            }
            lower.add(upper.get(lower.size() - 1));
            return lower;
        }
    }

    public static ArrayList<Integer> subsets(int n) {
        return Utils.supersetHelper(0, n);
    }

    public static ArrayList<Integer> supersets(int subset, int superset) {
        return Utils.supersetHelper(subset, subset ^ superset);
    }

    private static ArrayList<Integer> supersetHelper(int seed, int n) {
        int k = Integer.bitCount(n);
        if (k == 0) {
            return new ArrayList<>(Collections.singletonList(seed));
        } else {
            int upper_bits = n & (n - 1);
            int lowest_bit = n - upper_bits;
            ArrayList<Integer> lower = Utils.supersetHelper(seed, upper_bits);
            List<Integer> upper = Utils.map(lower, arg -> arg | lowest_bit);
            lower.addAll(upper);
            return lower;
        }
    }

    public static <T> HashMap<Integer, T> setMap(T[] arr, int set) {
        HashMap<Integer, T> elements = new HashMap<>();
        while (set != 0) {
            elements.put(set & -set, arr[Integer.numberOfTrailingZeros(set)]);
            set &= (set - 1);
        }
        return elements;
    }

    public static <T> ArrayList<T> setParse(T[] arr, int set) {
        ArrayList<T> elements = new ArrayList<>();
        while (set != 0) {
            elements.add(arr[Integer.numberOfTrailingZeros(set)]);
            set &= (set - 1);
        }
        return elements;
    }

    public static <T, U> List<U> cyclic(List<T> args, Function<List<T>, U> func) {
        U[] terms = (U[]) new Object[args.size()];
        for (int i = 0; i < args.size(); i++) {
            terms[i] = func.apply(args);
            args.add(0, args.remove(args.size() - 1));
        }
        return List.of(terms);
    }

    public static String overline(String s) {
        StringBuilder overlined = new StringBuilder();
        for (char c : s.toCharArray()) {
            overlined.append(c).append("\u0305");
        }
        return overlined.toString();
    }

    public static String randomHash() {
        return Hashing.sha256().hashInt(new Random().nextInt()).toString();
    }
}