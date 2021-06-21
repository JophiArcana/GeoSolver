package Core.Utilities;

import Core.AlgeSystem.Functions.*;
import Core.AlgeSystem.*;
import Core.EntityTypes.Entity;

import java.util.*;
import java.util.function.Function;

public class Utils {
    public static final Comparator<Entity> PRIORITY_COMPARATOR = new PriorityComparator();
    public static final Comparator<Expression> GROWTH_COMPARATOR = new OrderOfGrowthComparator();

    public static final HashMap<Class, Integer> CLASS_IDS = new HashMap<>() {{
        Class[] classes = new Class[] {
                Complex.class,
                Infinity.class,
                Symbol.class,
                Log.class,
                Add.class,
                Pow.class,
                Monomial.class,
                Polynomial.class,
                Mul.class
        };
        for (int i = 0; i < classes.length; i++) {
            put(classes[i], i);
        }
    }};

    public static final HashSet<Class> CLOSED_FORM = new HashSet<>(Arrays.asList(
            Complex.class,
            Infinity.class,
            Symbol.class,
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
        if (!(o instanceof Entity)) {
            return -1;
        } else {
            return CLASS_IDS.get(o.getClass());
        }
    }

    public static int compare(Entity e1, Entity e2) {
        return Utils.PRIORITY_COMPARATOR.compare(e1.simplify(), e2.simplify());
    }

    public static <T, S> ArrayList<S> map(ArrayList<T> list, Function<T, S> function) {
        ArrayList<S> result = new ArrayList<>();
        list.forEach(arg -> result.add(function.apply(arg)));
        return result;
    }

    public static Number integerize(Number arg) {
        if (Math.abs(arg.doubleValue() - Math.round(arg.doubleValue())) < ASEngine.EPSILON) {
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
}