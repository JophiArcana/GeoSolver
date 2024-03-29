
import core.alg.UnicardinalStructure.*;
import core.alg.UnicardinalRings.*;
import core.structure.multicardinal.geo.point.structure.Point;
import core.structure.multicardinal.geo.point.structure.PointVariable;
import core.structure.unicardinal.Variable;
import core.util.*;

import static core.util.GeoEngine.*;

public class Test {
    public static void main(String[] args) {
        Variable<Symbolic> x = Variable.create("x", Symbolic.class);
        Variable<Symbolic> y = Variable.create("y", Symbolic.class);
        Variable<Symbolic> z = Variable.create("z", Symbolic.class);
        AlgEngine<Symbolic> e1 = Utils.getEngine(Symbolic.class);

        /** Expression<Symbolic> term1 = e1.mul(x, new Complex<>(0, -1, Symbolic.class));
        Expression<Symbolic> term2 = y;
        Expression<Symbolic> term3 = e1.negate(z);
        Expression<Symbolic> term4 = Constant.ZERO(Symbolic.class);
        System.out.println(e1.greatestCommonDivisor(Arrays.asList(term1, term2, term3, term4))); */

        // System.out.println(e1.expand(e1.mul(e1.add(e1.mul(2, x), y), e1.add(x, y))));

        PointVariable p = PointVariable.create("P");
        PointVariable q = PointVariable.create("Q");
        PointVariable r = PointVariable.create("R");

        /**Expression<Symbolic> p_expr = p.symbolic().get(0);
        Expression<Symbolic> q_expr = q.symbolic().get(0);

        Expression<Symbolic> expr = Mul.create(List.of(e1.conjugate(p_expr), q_expr), Symbolic.class);
        System.out.println(expr);
        System.out.println(e1.conjugate(expr));

        Expression<Symbolic> k = e1.sub(expr, e1.conjugate(expr));

        System.out.println(k);
        System.out.println(k.expand());
        System.out.println(e1.imaginary(expr).expressionSimplify());*/

        Point m = centroid("M", p, q);
        Point c = centroid("C", p, m);
        Point o = circumcenter("O", p, q, r);

        System.out.println(o.symbolic());

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

        System.out.println();

        /** Add<Symbolic> expr = (Add<Symbolic>) Scale.create(2, (Expression<Symbolic>) o.expression(Point.PointExpressionType.X), Symbolic.class);
        System.out.println(expr);
        System.out.println(e1.numberOfOperations(expr));

        System.out.println();

        ArrayList<Mul<Symbolic>> terms = Utils.cast(expr.inputs.get(Accumulation.Parameter.TERMS));

        System.out.println(terms.get(0).expand());
        System.out.println("\n" + terms.get(1).expand()); */

        /**System.out.println();

        Scale<Symbolic> sc = (Scale<Symbolic>) ((Pow<Symbolic>) terms.get(0).inputs.get(Accumulation.Parameter.TERMS).lastEntry().getElement()).base;
        System.out.println(sc.expression);*/

        /**expr = e1.mul(expr, 2);
        ArrayList<Expression<Symbolic>> exprInputs = Utils.cast(expr.getInputs().get("Terms"));
        exprInputs = Utils.cast(exprInputs.get(0).getInputs().get("Terms"));
        System.out.println(exprInputs);
        // ArrayList<Expression<Symbolic>> exprInputs2 = new ArrayList<>(Arrays.asList(e1.mul(x, y), e1.mul(y, z), e1.mul(z, x)));
        // System.out.println(e1.fullGCDGraph(exprInputs));

        System.out.println(e1.GCDReduction(exprInputs));*/
        // System.out.println(e1.GCDReduction(new ArrayList<>(Arrays.asList(x, y, z))));

        /**Linear l = new Linear("L");
        System.out.println(l.symbolic());*/
    }
}
