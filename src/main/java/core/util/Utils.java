package core.util;

import com.google.common.collect.Multiset;
import core.Propositions.equalitypivot.EqualityPivot;
import core.Propositions.equalitypivot.unicardinal.UnicardinalPivot;
import core.structure.Mutable;
import core.structure.equalitypivot.*;
import core.structure.multicardinal.*;
import core.structure.multicardinal.geo.circle.structure.*;
import core.structure.multicardinal.geo.line.function.*;
import core.structure.multicardinal.geo.line.structure.*;
import core.structure.multicardinal.geo.point.structure.*;
import core.structure.multicardinal.geo.point.function.*;
import core.structure.multicardinal.geo.triangle.Triangle;
import core.structure.unicardinal.*;
import core.structure.Entity;
import com.google.common.collect.TreeMultiset;
import com.google.common.hash.Hashing;
import core.structure.unicardinal.alg.directed.*;
import core.structure.unicardinal.alg.directed.function.Directed;
import core.structure.unicardinal.alg.directed.operator.*;
import core.structure.unicardinal.alg.symbolic.*;
import core.structure.unicardinal.alg.symbolic.operator.*;

import java.util.*;
import java.util.function.Function;

public class Utils {
    public static final AlgEngine ENGINE = new AlgEngine();

    /** SECTION: Entity Comparison ================================================================================== */
    public static final Comparator<Unicardinal> UNICARDINAL_COMPARATOR = (u1, u2) -> {
        if (u1 == u2) {
            return 0;
        } else if (u1 instanceof Constant c1 && u2 instanceof Constant c2) {
            return Double.compare(c1.value, c2.value);
        } else if (u1.getDegree() != u2.getDegree()) {
            return -(u1.getDegree() - u2.getDegree());
        } else {
            return Utils.compareEntities(u1, u2);
        }
    };

    public static final Comparator<Multicardinal> MULTICARDINAL_COMPARATOR = (m1, m2) -> {
        if (m1 == m2) {
            return 0;
        } else {
            return Utils.compareEntities(m1, m2);
        }
    };

    public static int compareEntities(Entity e1, Entity e2) {
        if (e1.getClass() != e2.getClass()) {
            return -(Utils.classCode(e1) - Utils.classCode(e2));
        } else if (e1 instanceof Mutable var1 && e2 instanceof Mutable var2) {
            return var1.name.compareTo(var2.name);
        } else {
            return Utils.compareInputs(e1, e2);
        }
    }

    public static int compareInputs(Entity e1, Entity e2) {
        assert e1.getClass() == e2.getClass(): "Classes must be identical.";
        for (Entity.InputType<? extends Entity> inputType : e1.getInputTypes()) {
            int comparison = 0;
            if (inputType instanceof Entity.UnicardinalInputType<?> unicardinalInputType) {
                comparison = unicardinalInputType.compare(e1.getInputs(unicardinalInputType), e2.getInputs(unicardinalInputType));
            } else if (inputType instanceof Entity.MulticardinalInputType<?> multicardinalInputType) {
                comparison = multicardinalInputType.compare(e1.getInputs(multicardinalInputType), e2.getInputs(multicardinalInputType));
            }
            if (comparison != 0) {
                return comparison;
            }
        }
        return 0;
    }

    public static final ArrayList<Class<? extends Entity>> CLASS_IDS = new ArrayList<>(Arrays.asList(
        /** SUBSECTION: Directed Expression ========================================================================= */
            DirectedConstant.class,
            DirectedVariable.class,
            DirectedScale.class,
            DirectedAdd.class,
            Directed.class,

        /** SUBSECTION: Symbolic Expression ========================================================================= */
            SymbolicConstant.class,
            SymbolicVariable.class,
            SymbolicAbs.class,
            SymbolicScale.class,
            SymbolicAdd.class,
            SymbolicPow.class,
            SymbolicMul.class,

        /** SUBSECTION: Points ====================================================================================== */
            Coordinate.class,
            PointVariable.class,
            Midpoint.class,
            Connect.ConnectPointDual.class,
            Triangle.Centroid.class,
            Triangle.Orthocenter.class,
            Triangle.Incenter.class,
            Triangle.Circumcircle.class,

        /** SUBSECTION: Lines ======================================================================================= */
            Axis.class,
            LineVariable.class,
            Connect.class,

        /** SUBSECTION: Circles ===================================================================================== */
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