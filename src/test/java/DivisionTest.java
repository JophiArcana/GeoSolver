
import Core.AlgSystem.UnicardinalTypes.*;
import Core.AlgSystem.UnicardinalRings.Symbolic;
import Core.Utilities.AlgEngine;
import Core.Utilities.Utils;

import java.util.List;

public class DivisionTest {
    public static void main(String[] args) {
        AlgEngine<Symbolic> e1 = Utils.getEngine(Symbolic.class);
        Univariate<Symbolic> x = Univariate.create("x", Symbolic.class);
        Univariate<Symbolic> y = Univariate.create("y", Symbolic.class);
        Expression<Symbolic> expr = e1.mul(List.of(x, y, e1.sub(x, y)));
        System.out.println(expr);
        System.out.println(e1.numberOfOperations(y));
    }
}
