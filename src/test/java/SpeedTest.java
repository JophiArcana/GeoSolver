
import Core.AlgSystem.Operators.*;
import Core.AlgSystem.Operators.AddReduction.Scale;
import Core.AlgSystem.Operators.MulReduction.Mul;
import Core.AlgSystem.UnicardinalRings.*;
import Core.EntityStructure.UnicardinalStructure.Expression;
import Core.GeoSystem.Points.PointStructure.*;
import Core.Utilities.*;

import java.util.*;

import static Core.Utilities.GeoEngine.*;

public class SpeedTest {
    public static void main(String[] args) {
        AlgEngine<Symbolic> e1 = Utils.getEngine(Symbolic.class);

        PointVariable p = PointVariable.create("P");
        PointVariable q = PointVariable.create("Q");
        PointVariable r = PointVariable.create("R");
        Point o = circumcenter("O", p, q, r);

        Expression<Symbolic> expr = (Expression<Symbolic>) o.expression(Point.PointExpressionType.X);
        expr = Scale.create(2, expr, Symbolic.class);
        ArrayList<Expression<Symbolic>> exprInputs = Utils.cast(expr.getInputs().get(Mul.Parameter.TERMS));
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
