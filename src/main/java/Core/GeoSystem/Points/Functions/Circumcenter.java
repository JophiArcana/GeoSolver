package Core.GeoSystem.Points.Functions;

import Core.AlgeSystem.ExpressionTypes.Expression;
import Core.EntityTypes.Entity;
import Core.GeoSystem.Points.PointTypes.Center;
import Core.GeoSystem.Points.PointTypes.Point;
import Core.Utilities.*;

import java.util.*;
import java.util.function.Function;

import static Core.Utilities.AlgeEngine.*;
import static Core.AlgeSystem.ExpressionTypes.Constant.*;

public class Circumcenter extends Center {
    public static final Function<HashMap<String, ArrayList<ArrayList<Expression>>>, ArrayList<Expression>> formula = args -> {
        ArrayList<Expression> argTerms = Utils.map(args.get("Points"), arg -> arg.get(0));
        Function<ArrayList<Expression>, Expression> funcN = terms -> sub(mul(pow(abs(terms.get(0)), 2), terms.get(1)),
                mul(pow(abs(terms.get(1)), 2), terms.get(0)));
        Function<ArrayList<Expression>, Expression> funcD = terms -> imaginary(mul(conjugate(terms.get(0)), terms.get(1)));

        Expression numerator = cyclicSum(funcN, argTerms);
        Expression denominator = mul(2, I, cyclicSum(funcD, argTerms));
        return new ArrayList<>(Collections.singletonList(div(numerator, denominator)));
    };

    public Circumcenter(String n, Point a, Point b, Point c) {
        super(n, a, b, c);
    }

    public Circumcenter(Point a, Point b, Point c) {
        super("", a, b, c);
    }

    public Entity simplify() {
        return this;
    }

    public Function<HashMap<String, ArrayList<ArrayList<Expression>>>, ArrayList<Expression>> getFormula() {
        return Circumcenter.formula;
    }
}
