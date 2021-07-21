package Core.Utilities;

import Core.AlgeSystem.Constants.Complex;
import Core.AlgeSystem.Constants.Infinity;
import Core.AlgeSystem.UnicardinalRings.DirectedAngle;
import Core.AlgeSystem.UnicardinalRings.Distance;
import Core.AlgeSystem.UnicardinalRings.Symbolic;
import Core.AlgeSystem.UnicardinalTypes.Constant;
import Core.AlgeSystem.UnicardinalTypes.Expression;
import Core.AlgeSystem.UnicardinalTypes.Unicardinal;
import Core.AlgeSystem.UnicardinalTypes.Univariate;
import Core.AlgeSystem.Functions.*;
import Core.EntityTypes.Entity;
import Core.GeoSystem.Lines.*;
import Core.GeoSystem.Points.Functions.*;
import Core.GeoSystem.Points.PointTypes.Coordinate;
import Core.GeoSystem.Points.PointTypes.Phantom;

import java.util.*;
import java.util.function.Function;

public class Utils {
    public static final Comparator<Entity> PRIORITY_COMPARATOR = new PriorityComparator();
    public static final AlgeEngine<Symbolic> SYMBOLIC_ENGINE = new AlgeEngine<>(Symbolic.class);

    public static final ArrayList<Class<? extends Entity>> CLASS_IDS = new ArrayList<>(Arrays.asList(
        /** SECTION: Expressions ==================================================================================== */
            Complex.class,
            Infinity.class,
            Univariate.class,
            Log.class,
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
            Linear.class
    ));

    public static final HashSet<Class<? extends Unicardinal>> CLOSED_FORM = new HashSet<>(Arrays.asList(
            Complex.class,
            Infinity.class,
            Univariate.class,
            Log.class
    ));

    public static final HashSet<Class<? extends Expression<?>>> UNICARDINAL_RINGS = new HashSet<>(Arrays.asList(
            Symbolic.class,
            Distance.class,
            DirectedAngle.class
    ));

    private static final HashMap<Class<? extends Expression<?>>, OrderOfGrowthComparator> GROWTH_COMPARATORS = new HashMap<>() {{
        for (Class cls : Utils.UNICARDINAL_RINGS) {
            put(cls, new OrderOfGrowthComparator<>(cls));
        }
    }};

    private static final HashMap<Class<? extends Expression<?>>, AlgeEngine> ENGINES = new HashMap<>() {{
        for (Class cls : Utils.UNICARDINAL_RINGS) {
            put(cls, new AlgeEngine<>(cls));
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

    public static <T extends Expression<T>> Expression<T> objectConversion(Object obj, Class<T> type) {
        assert obj instanceof Number || obj instanceof Expression || obj == null;
        if (obj instanceof Number n) {
            return new Complex<>(n, 0, type);
        } else if (obj != null) {
            return (Expression<T>) obj;
        } else {
            return Constant.ZERO(type);
        }
    }

    public static <T extends Expression<T>> ArrayList<Expression<T>> additiveTerms(Expression<T> expr) {
        ArrayList<Expression<T>> expansionTerms = new ArrayList<>();
        if (expr instanceof Add<T> addExpr) {
            addExpr.inputs.get("Terms").forEach(arg -> expansionTerms.add((Expression<T>) arg));
            if (!addExpr.constant.equals(Constant.ZERO(expr.getType()))) {
                expansionTerms.add(addExpr.constant);
            }
        } else {
            expansionTerms.add(expr);
        }
        return expansionTerms;
    }
}