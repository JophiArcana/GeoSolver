
import Core.AlgeSystem.Functions.*;
import Core.AlgeSystem.UnicardinalRings.Distance;
import Core.AlgeSystem.UnicardinalTypes.*;
import Core.AlgeSystem.UnicardinalRings.Symbolic;
import Core.GeoSystem.Lines.LineTypes.Linear;
import Core.GeoSystem.MulticardinalTypes.Multicardinal;
import Core.GeoSystem.Points.PointTypes.Phantom;
import Core.GeoSystem.Points.PointTypes.Point;
import Core.Utilities.AlgeEngine;
import Core.Utilities.OrderOfGrowthComparator;
import Core.Utilities.Utils;

import java.util.ArrayList;

import static Core.Utilities.GeoEngine.*;

public class DivisionTest {
    public static void main(String[] args) {
        AlgeEngine<Symbolic> e1 = Utils.getEngine(Symbolic.class);
        OrderOfGrowthComparator<Symbolic> c1 = Utils.getGrowthComparator(Symbolic.class);
        Univariate<Symbolic> x = new Univariate<>("x", Symbolic.class);
        Univariate<Symbolic> y = new Univariate<>("y", Symbolic.class);
        // Univariate<Symbolic> z = new Univariate<>("z", Symbolic.class);
        // System.out.println(e1.mul(2, e1.pow(e1.add(1, x), 3)).expand());
        System.out.println(e1.mul(x, e1.pow(y, 2)));
        // System.out.println(e1.mul(x, y, y));
        // System.out.println(e1.orderOfGrowth(e1.pow(y, 2), x));
        // System.out.println(Utils.PRIORITY_COMPARATOR.compare(x, e1.pow(y, 2)));
        // System.out.println(c1.compare(x, e1.log(x)));
        System.out.println("Marker =============================================================================================");
        Expression<Symbolic> expr1 = e1.mul(x, y, y, x, e1.log(x));
        Expression<Symbolic> expr2 = e1.pow(x, 2);
        // Expression<Symbolic> expr3 = e1.exp(x);
        // Expression<Symbolic> expr4 = e1.exp(e1.mul(x, 2));
        System.out.println(e1.log(x).derivative(x));
        // System.out.println(e1.add(expr1, expr2));
        /**System.out.println(e1.exp(e1.add(e1.mul(3, e1.log(x)), e1.mul(2, e1.log(y)), 3)));
        Phantom p = new Phantom("P");
        Phantom q = new Phantom("Q");
        Phantom r = new Phantom("R");
        System.out.println(p.name);
        Point m = centroid("M", p, q);
        Point c = centroid("C", p, m);
        Point o = circumcenter(p, q, r);
        System.out.println(c.expression());
        System.out.println(c);

        AlgeEngine<Distance> e2 = Utils.getEngine(Distance.class);
        Expression<Distance> expr = (Expression<Distance>) o.expression(Multicardinal.X);
        System.out.println(expr);
        System.out.println(e2.numberOfOperations(expr));
        expr = e2.mul(expr, 2);
        System.out.println(expr);
        ArrayList<Expression<Distance>> exprInputs = Utils.cast(expr.getInputs().get("Terms"));
        exprInputs = Utils.cast(exprInputs.get(1).getInputs().get("Terms"));
        System.out.println(exprInputs);
        System.out.println(e2.numberOfOperations((Expression<Distance>) o.expression(Multicardinal.VALUE)));

        Linear l = new Linear("L");
        System.out.println(l.expression());*/
    }
}
