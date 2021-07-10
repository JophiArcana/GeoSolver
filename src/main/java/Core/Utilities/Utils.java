package Core.Utilities;

import Core.AlgeSystem.*;
import Core.AlgeSystem.Functions.*;
import Core.EntityTypes.Entity;
import Core.GeoSystem.Lines.*;
import Core.GeoSystem.Points.*;
import Core.GeoSystem.Points.Functions.*;

import java.util.*;
import java.util.function.Function;

public class Utils {
    public static final Comparator<Entity> PRIORITY_COMPARATOR = new PriorityComparator();
    public static final Comparator<Expression> GROWTH_COMPARATOR = new OrderOfGrowthComparator();

    public static final ArrayList<Class> CLASS_IDS = new ArrayList<>(Arrays.asList(
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

    public static final HashSet<Class> CLOSED_FORM = new HashSet<>(Arrays.asList(
            Complex.class,
            Infinity.class,
            Univariate.class,
            Log.class
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

    public static Expression objectConversion(Object obj) {
        if (obj instanceof Number n) {
            return new Complex(n, 0);
        } else if (obj instanceof Expression expr) {
            return expr;
        } else if (obj == null) {
            return Constant.ZERO;
        } else {
            return null;
        }
    }

    public static ArrayList<Expression> additiveTerms(Expression expr) {
        ArrayList<Expression> expansionTerms = new ArrayList<>();
        if (expr instanceof Add addExpr) {
            addExpr.inputs.get("Terms").forEach(arg -> expansionTerms.add((Expression) arg));
            if (!addExpr.constant.equals(Constant.ZERO)) {
                expansionTerms.add(addExpr.constant);
            }
        } else {
            expansionTerms.add(expr);
        }
        return expansionTerms;
    }
}