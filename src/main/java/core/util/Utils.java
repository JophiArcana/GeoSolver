package core.util;

import com.google.common.collect.Multiset;
import core.structure.equalitypivot.EqualityPivot;
import core.structure.multicardinal.geo.circle.structure.*;
import core.structure.multicardinal.geo.line.function.*;
import core.structure.multicardinal.geo.line.structure.*;
import core.structure.multicardinal.geo.point.structure.*;
import core.structure.multicardinal.geo.point.function.*;
import core.structure.multicardinal.geo.triangle.Triangle;
import core.structure.unicardinal.Unicardinal;
import core.structure.unicardinal.Variable;
import core.structure.Entity;
import com.google.common.collect.TreeMultiset;
import com.google.common.hash.Hashing;
import core.structure.unicardinal.alg.directed.DirectedVariable;
import core.structure.unicardinal.alg.directed.constant.DirectedReal;
import core.structure.unicardinal.alg.directed.function.Directed;
import core.structure.unicardinal.alg.directed.operator.DirectedAdd;
import core.structure.unicardinal.alg.directed.operator.DirectedScale;
import core.structure.unicardinal.alg.symbolic.SymbolicVariable;
import core.structure.unicardinal.alg.symbolic.constant.SymbolicInfinity;
import core.structure.unicardinal.alg.symbolic.constant.SymbolicReal;
import core.structure.unicardinal.alg.symbolic.operator.*;
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

    public static final HashSet<Class<? extends Unicardinal>> CLOSED_FORM = new HashSet<>(List.of(
            SymbolicInfinity.class,
            Variable.class
    ));

    public static String className(Object o) {
        Class<?> cls = o.getClass();
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
        HashMap<Entity.InputType<?>, TreeMultiset<EqualityPivot<?>>>
                inputs1 = e1.getInputs(),
                inputs2 = e2.getInputs();
        for (Entity.InputType<? extends Entity> inputType : e1.getInputTypes()) {
            Iterator<Multiset.Entry<EqualityPivot<? extends Entity>>>
                    iter1 = inputs1.get(inputType).entrySet().iterator(),
                    iter2 = inputs2.get(inputType).entrySet().iterator();
            while (iter1.hasNext()) {
                Multiset.Entry<EqualityPivot<? extends Entity>>
                        entry1 = iter1.next(),
                        entry2 = iter2.next();
                int elementComparison = ((EqualityPivot) entry1.getElement()).compareTo(entry2.getElement());
                if (elementComparison != 0) {
                    return elementComparison;
                } else if (entry1.getCount() != entry2.getCount()) {
                    return -(entry1.getCount() - entry2.getCount());
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

    public static <T, S extends T> Collection<S> cast(Collection<T> args) {
        return (Collection<S>) args;
    }

    public static <T> void addToMultiset(TreeMap<T, Double> map, T key, double count) {
        map.put(key, map.getOrDefault(key, 0.0) + count);
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

    public static <T> ArrayList<List<HashSet<T>>> sortedSubsets(List<T> args) {
        if (args.size() == 0) {
            return new ArrayList<>(List.of(List.of(new HashSet<>())));
        } else {
            ArrayList<List<HashSet<T>>> lower = Utils.sortedSubsets(args.subList(0, args.size() - 1));
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