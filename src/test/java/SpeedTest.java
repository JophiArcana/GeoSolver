
import core.alg.Operators.*;
import core.structure.multicardinal.geo.point.structure.Point;
import core.structure.multicardinal.geo.point.structure.PointVariable;
import core.structure.unicardinal.alg.structure.Scale;
import core.structure.unicardinal.alg.symbolic.operator.SymbolicMul;
import core.structure.unicardinal.alg.structure.Reduction;
import core.alg.UnicardinalRings.*;
import core.structure.unicardinal.alg.Expression;
import core.util.*;

import java.util.*;

import static core.util.GeoEngine.*;

public class SpeedTest {
    public static void main(String[] args) {
        AlgEngine<Symbolic> e1 = Utils.getEngine(Symbolic.class);

        PointVariable p = PointVariable.create("P");
        PointVariable q = PointVariable.create("Q");
        PointVariable r = PointVariable.create("R");
        Point o = circumcenter("O", p, q, r);

        Expression<Symbolic> expr = (Expression<Symbolic>) o.expression(Point.PointExpressionType.X);
        expr = Scale.create(2, expr, Symbolic.class);
        ArrayList<Expression<Symbolic>> exprInputs = Utils.cast(expr.getInputs().get(SymbolicMul.Parameter.TERMS));
        exprInputs = Utils.cast(exprInputs.get(0).getInputs().get(Reduction.Parameter.TERMS));

        int cycles = 100000;
        double startTime = System.nanoTime();
        for (int i = 0; i < cycles; i++) {
            e1.GCDReduction(exprInputs);
        }
        double endTime = System.nanoTime();
        System.out.println((endTime - startTime) / (1E6 * cycles) + "ms");
    }
}
