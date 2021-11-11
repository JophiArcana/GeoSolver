
import Core.AlgeSystem.Constants.Complex;
import Core.AlgeSystem.Functions.Add;
import Core.AlgeSystem.UnicardinalTypes.*;
import Core.AlgeSystem.UnicardinalRings.*;
import Core.GeoSystem.Lines.LineTypes.Linear;
import Core.GeoSystem.MulticardinalTypes.Multicardinal;
import Core.GeoSystem.Points.PointTypes.*;
import Core.Utilities.*;

import java.util.*;

import static Core.Utilities.GeoEngine.*;

public class Test {
    public static void main(String[] args) {
        Univariate<Symbolic> x = new Univariate<>("x", Symbolic.class);
        Univariate<Symbolic> y = new Univariate<>("y", Symbolic.class);
        Univariate<Symbolic> z = new Univariate<>("z", Symbolic.class);
        AlgeEngine<Symbolic> e1 = Utils.getEngine(Symbolic.class);

        /** Expression<Symbolic> term1 = e1.mul(x, new Complex<>(0, -1, Symbolic.class));
        Expression<Symbolic> term2 = y;
        Expression<Symbolic> term3 = e1.negate(z);
        Expression<Symbolic> term4 = Constant.ZERO(Symbolic.class);
        System.out.println(e1.greatestCommonDivisor(Arrays.asList(term1, term2, term3, term4))); */

        // System.out.println(e1.expand(e1.mul(e1.add(e1.mul(2, x), y), e1.add(x, y))));

        Phantom p = new Phantom("P");
        Phantom q = new Phantom("Q");
        Phantom r = new Phantom("R");
        Point m = centroid("M", p, q);
        Point c = centroid("C", p, m);
        Point o = circumcenter(p, q, r);

        System.out.println(o.expression());

        // Expression<Symbolic> expr = (Expression<Symbolic>) o.expression(Multicardinal.X);
        // expr = e1.mul(expr, 2);
        // System.out.println(expr.getInputs().get("Terms"));

        /**Expression<Symbolic> expr = (Expression<Symbolic>) o.expression().get(0);
        Expression<Symbolic> conjugate = e1.conjugate(expr);
        System.out.println(expr);
        System.out.println(conjugate);

        Expression<Symbolic> numerator = (Expression<Symbolic>) expr.getInputs().get("Terms").firstEntry().getElement();
        Expression<Symbolic> constant = (Expression<Symbolic>) expr.getInputs().get("Constant").firstEntry().getElement();
        System.out.println(numerator);
        System.out.println(constant);

        Expression<Symbolic> expr2 = e1.mul(numerator, constant);

        System.out.println("\n\n");

        System.out.println(e1.real(expr2));*/

        Expression<Symbolic> expr = (Expression<Symbolic>) o.expression(Multicardinal.X);
        System.out.println(expr);

        expr = e1.mul(expr, 2);
        ArrayList<Expression<Symbolic>> exprInputs = Utils.cast(expr.getInputs().get("Terms"));
        exprInputs = Utils.cast(exprInputs.get(0).getInputs().get("Terms"));
        System.out.println(exprInputs);
        // ArrayList<Expression<Symbolic>> exprInputs2 = new ArrayList<>(Arrays.asList(e1.mul(x, y), e1.mul(y, z), e1.mul(z, x)));
        // System.out.println(e1.fullGCDGraph(exprInputs));

        System.out.println(e1.GCDReduction(exprInputs));
        // System.out.println(e1.GCDReduction(new ArrayList<>(Arrays.asList(x, y, z))));

        /**Linear l = new Linear("L");
        System.out.println(l.symbolic());

        ArrayList<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3));
        list.add(null);
        System.out.println(list);*/
    }
}
