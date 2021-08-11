package Core.Utilities;

import Core.AlgeSystem.Constants.*;
import Core.AlgeSystem.UnicardinalTypes.*;
import Core.AlgeSystem.Functions.*;
import Core.EntityTypes.Entity;
import Core.GeoSystem.Lines.LineTypes.Axis;
import Core.GeoSystem.Lines.LineTypes.Linear;
import Core.GeoSystem.Points.Functions.*;
import Core.GeoSystem.Points.PointTypes.*;
import com.google.common.base.CharMatcher;

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
            Monomial.class,
            Polynomial.class,
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

    private static final HashMap<Class<? extends Expression<?>>, AlgeEngine> ENGINES = new HashMap<>() {{
        for (Class cls : Unicardinal.RINGS.keySet()) {
            put(cls, new AlgeEngine<>(cls));
        }
    }};

    private static final HashMap<Class<? extends Expression<?>>, OrderOfGrowthComparator> GROWTH_COMPARATORS = new HashMap<>() {{
        for (Class cls : Unicardinal.RINGS.keySet()) {
            put(cls, new OrderOfGrowthComparator<>(cls));
        }
    }};

    public static <T extends Expression<T>> AlgeEngine<T> getEngine(Class<T> type) {
        return Utils.ENGINES.get(type);
    }

    public static <T extends Expression<T>> OrderOfGrowthComparator<T> getGrowthComparator(Class<T> type) {
        return Utils.GROWTH_COMPARATORS.get(type);
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

    public static int compare(Entity e1, Entity e2) {
        return Utils.PRIORITY_COMPARATOR.compare(e1, e2);
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
        if (Math.abs(arg.doubleValue() - Math.round(arg.doubleValue())) < AlgeEngine.EPSILON) {
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

    public static Comparator<Integer> binaryComparator = (o1, o2) -> {
        CharMatcher cm = CharMatcher.is('1');
        int c1 = cm.countIn(Integer.toBinaryString(o1));
        int c2 = cm.countIn(Integer.toBinaryString(o2));
        return (c1 == c2) ? o1 - o2 : c1 - c2;
    };

    private static int binomial(int a, int b) {
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

    public static <T> ArrayList<ArrayList<HashSet<T>>> binarySortedSubsets(ArrayList<T> args) {
        if (args.size() == 0) {
            return new ArrayList<>(Collections.singletonList(new ArrayList<>(Collections.singletonList(new HashSet<>()))));
        } else {
            ArrayList<ArrayList<HashSet<T>>> lower = Utils.binarySortedSubsets(new ArrayList<>(args.subList(0, args.size() - 1)));
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

    public static <T> ArrayList<HashSet<T>> subsets(Collection<T> args) {
        args = new HashSet<>(args);
        if (args.size() == 0) {
            return new ArrayList<>(Collections.singletonList(new HashSet<>()));
        } else {
            T arg;
            args.remove(arg = args.stream().findAny().get());
            ArrayList<HashSet<T>> lower = Utils.subsets(args);
            ArrayList<HashSet<T>> upper = Utils.map(lower, set -> {
                HashSet<T> copy = new HashSet<>(set);
                copy.add(arg);
                return copy;
            });
            lower.addAll(upper);
            return lower;
        }
    }

    public static <T> ArrayList<HashSet<T>> supersets(Collection<T> subset, Collection<T> superset) {
        Set<T> exclusion = new HashSet<>(superset);
        exclusion.removeAll(subset);
        ArrayList<HashSet<T>> exclusionSets = Utils.subsets(exclusion);
        return Utils.map(exclusionSets, set -> {
            HashSet<T> copy = new HashSet<>(subset);
            copy.addAll(set);
            return copy;
        });
    }
}