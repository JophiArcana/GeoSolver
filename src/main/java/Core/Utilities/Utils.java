package Core.Utilities;

import Core.AlgSystem.Constants.*;
import Core.AlgSystem.UnicardinalTypes.*;
import Core.AlgSystem.Operators.*;
import Core.EntityTypes.Entity;
import Core.GeoSystem.Lines.LineTypes.*;
import Core.GeoSystem.Points.Functions.*;
import Core.GeoSystem.Points.PointTypes.*;

import java.util.*;
import java.util.function.Function;

public class Utils {
    public static final Comparator<Entity> PRIORITY_COMPARATOR = new PriorityComparator();

    public static final ArrayList<Class<? extends Entity>> CLASS_IDS = new ArrayList<>(Arrays.asList(
        /** SECTION: Expressions ==================================================================================== */
            Complex.class,
            Infinity.class,
            Univariate.class,
            Add.class,
            Pow.class,
            Mul.class,

        /** SECTION: Points ========================================================================================= */
            Coordinate.class,
            Phantom.class,
            Midpoint.class,
            Centroid.class,
            Circumcenter.class,

        /** SECTION: Lines ========================================================================================== */
            Axis.class,
            Linear.class
    ));

    public static final HashSet<Class<? extends Unicardinal>> CLOSED_FORM = new HashSet<>(Arrays.asList(
            Complex.class,
            Infinity.class,
            Univariate.class
    ));

    private static final HashMap<Class<? extends Expression<?>>, AlgEngine> ENGINES = new HashMap<>() {{
        for (Class cls : Unicardinal.RINGS.keySet()) {
            put(cls, new AlgEngine<>(cls));
        }
    }};

    public static <T> AlgEngine<T> getEngine(Class<T> type) {
        return Utils.ENGINES.get(type);
    }

    public static String className(Object o) {
        Class cls = o.getClass();
        if (cls.getEnclosingClass() != null) {
            return cls.getEnclosingClass().getSimpleName();
        } else {
            return cls.getSimpleName();
        }
    }

    public static int classCode(Object o) {
        return Utils.CLASS_IDS.indexOf(o.getClass());
    }

    public static <T, S> ArrayList<S> map(Iterable<T> list, Function<T, S> function) {
        ArrayList<S> result = new ArrayList<>();
        list.forEach(arg -> result.add(function.apply(arg)));
        return result;
    }

    public static <T, S> ArrayList<S> cast(Iterable<T> args) {
        ArrayList<S> list = new ArrayList<>();
        args.forEach(arg -> list.add((S) arg));
        return list;
    }

    public static Number integerize(Number arg) {
        if (Math.abs(arg.doubleValue() - Math.round(arg.doubleValue())) < AlgEngine.EPSILON) {
            return (int) Math.round(arg.doubleValue());
        } else {
            return arg;
        }
    }

    public static Number[] integerize(Number ... args) {
        Number[] rounded = new Number[args.length];
        for (int i = 0; i < args.length; i++) {
            rounded[i] = Utils.integerize(args[i]);
        }
        return rounded;
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

    public static ArrayList<ArrayList<Integer>> sortedSubsetsBinary(int set) {
        int k = Utils.countSetBits(set);
        if (k == 0) {
            return new ArrayList<>(Collections.singletonList(new ArrayList<>(Collections.singletonList(0))));
        } else {
            int upper_bits = set & (set - 1);
            int lowest_bit = set - upper_bits;
            ArrayList<ArrayList<Integer>> lower = Utils.sortedSubsetsBinary(upper_bits);
            ArrayList<ArrayList<Integer>> upper = Utils.map(lower, list -> Utils.map(list, arg -> arg | lowest_bit));
            for (int i = 0; i < k - 1; i++) {
                lower.get(i + 1).addAll(upper.get(i));
            }
            lower.add(upper.get(lower.size() - 1));
            return lower;
        }
    }

    public static ArrayList<ArrayList<Integer>> sortedContiguousSubsets(int setSize) {
        ArrayList<ArrayList<Integer>> subsets = new ArrayList<>();
        int bound = 1 << setSize;
        subsets.add(new ArrayList<>(Collections.singletonList(0)));
        for (int i = 1; i <= setSize; i++) {
            ArrayList<Integer> subsetList = new ArrayList<>();
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

    public static <T> ArrayList<ArrayList<HashSet<T>>> sortedSubsets(ArrayList<T> args) {
        if (args.size() == 0) {
            return new ArrayList<>(Collections.singletonList(new ArrayList<>(Collections.singletonList(new HashSet<>()))));
        } else {
            ArrayList<ArrayList<HashSet<T>>> lower = Utils.sortedSubsets(new ArrayList<>(args.subList(0, args.size() - 1)));
            ArrayList<ArrayList<HashSet<T>>> upper = new ArrayList<>();
            for (ArrayList<HashSet<T>> list : lower) {
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
        int k = Utils.countSetBits(n);
        if (k == 0) {
            return new ArrayList<>(Collections.singletonList(seed));
        } else {
            int upper_bits = n & (n - 1);
            int lowest_bit = n - upper_bits;
            ArrayList<Integer> lower = Utils.supersetHelper(seed, upper_bits);
            ArrayList<Integer> upper = Utils.map(lower, arg -> arg | lowest_bit);
            lower.addAll(upper);
            return lower;
        }
    }

    public static int countSetBits(int n) {
        int count = 0;
        while (n > 0) {
            n &= (n - 1);
            count++;
        }
        return count;
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

    public static String overline(String s) {
        StringBuilder overlined = new StringBuilder();
        for (char c : s.toCharArray()) {
            overlined.append(c).append("\u0305");
        }
        return overlined.toString();
    }
}