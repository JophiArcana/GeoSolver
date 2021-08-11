
import Core.AlgeSystem.Functions.Add;
import Core.AlgeSystem.UnicardinalTypes.*;
import Core.AlgeSystem.UnicardinalRings.*;
import Core.GeoSystem.Lines.LineTypes.Linear;
import Core.GeoSystem.MulticardinalTypes.Multicardinal;
import Core.GeoSystem.Points.PointTypes.*;
import Core.Utilities.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static Core.Utilities.GeoEngine.*;

public class Test {
    public static void main(String[] args) {
        Univariate<Symbolic> x = new Univariate<>("x", Symbolic.class);
        Univariate<Symbolic> y = new Univariate<>("y", Symbolic.class);
        Univariate<Symbolic> z = new Univariate<>("z", Symbolic.class);
        AlgeEngine<Symbolic> e1 = Utils.getEngine(Symbolic.class);
        AlgeEngine<Distance> e2 = Utils.getEngine(Distance.class);

        // System.out.println(e1.expand(e1.mul(e1.add(e1.mul(2, x), y), e1.add(x, y))));

        Phantom p = new Phantom("P");
        Phantom q = new Phantom("Q");
        Phantom r = new Phantom("R");
        Point m = centroid("M", p, q);
        Point c = centroid("C", p, m);
        Point o = circumcenter(p, q, r);

        System.out.println(o.expression());

        /**Expression<Distance> expr = (Expression<Distance>) o.expression().get(0);
        Expression<Distance> conjugate = e2.conjugate(expr);
        System.out.println(expr);
        System.out.println(conjugate);

        Expression<Distance> numerator = (Expression<Distance>) expr.getInputs().get("Terms").firstEntry().getElement();
        Expression<Distance> constant = (Expression<Distance>) expr.getInputs().get("Constant").firstEntry().getElement();
        System.out.println(numerator);
        System.out.println(constant);

        Expression<Distance> expr2 = e2.mul(numerator, constant);

        System.out.println("\n\n");

        System.out.println(e2.real(expr2));*/

        /**Expression<Distance> expr = (Expression<Distance>) o.expression(Multicardinal.X);
        expr = e2.mul(expr, 2);
        ArrayList<Expression<Distance>> exprInputs = Utils.cast(expr.getInputs().get("Terms"));
        exprInputs = Utils.cast(exprInputs.get(1).getInputs().get("Terms"));
        System.out.println(exprInputs);
        ArrayList<Expression<Symbolic>> exprInputs2 = new ArrayList<>(Arrays.asList(e1.mul(x, y), e1.mul(y, z), e1.mul(z, x)));
        // System.out.println(e2.fullGCDGraph(exprInputs));
        System.out.println(e2.GCDReduction(exprInputs));
        System.out.println(e1.GCDReduction(new ArrayList<>(Arrays.asList(x, y))));

        Linear l = new Linear("L");
        System.out.println(l.expression());*/
    }
}
