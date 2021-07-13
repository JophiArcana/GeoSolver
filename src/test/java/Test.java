
import Core.AlgeSystem.ExpressionTypes.*;
import Core.GeoSystem.MultiCardinalTypes.MultiCardinal;
import Core.GeoSystem.Points.PointTypes.Phantom;
import Core.GeoSystem.Points.PointTypes.Point;
import Core.Utilities.Utils;

import java.util.ArrayList;

import static Core.Utilities.AlgeEngine.*;
import static Core.Utilities.GeoEngine.*;

public class Test {
    public static void main(String[] args) {
        Univariate x = new Univariate("x");
        Univariate y = new Univariate("y");
        Univariate z = new Univariate("z");
        System.out.println(mul(x, add(1, pow(x, 2))));
        Expression expr1 = mul(x, y, y, x, log(x));
        Expression expr2 = pow(x, 2);
        Expression expr3 = exp(x);
        Expression expr4 = exp(mul(x, 2));
        System.out.println(add(expr1, expr2));
        System.out.println(exp(add(mul(3, log(x)), mul(2, log(y)), 3)));
        Phantom p = new Phantom("P");
        Phantom q = new Phantom("Q");
        Phantom r = new Phantom("R");
        System.out.println(p.name);
        Point m = centroid("M", p, q);
        Point c = centroid("C", p, m);
        Point o = circumcenter(p, q, r);
        System.out.println(c.expression());
        System.out.println(c);
        Expression expr = o.expression(MultiCardinal.X);
        System.out.println(expr);
        System.out.println(numberOfOperations(expr));
        expr = mul(expr, 2);
        System.out.println(expr);
        ArrayList<Expression> exprInputs = Utils.map(expr.getInputs().get("Terms"), Expression.class::cast);
        exprInputs = Utils.map(exprInputs.get(1).getInputs().get("Terms"), Expression.class::cast);
        System.out.println(exprInputs);
        System.out.println(numberOfOperations(o.expression().get(0)));
    }
}
