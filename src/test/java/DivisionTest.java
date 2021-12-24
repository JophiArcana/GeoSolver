
import Core.AlgSystem.UnicardinalTypes.*;
import Core.AlgSystem.UnicardinalRings.Symbolic;
import Core.Utilities.AlgEngine;
import Core.Utilities.OrderOfGrowthComparator;
import Core.Utilities.Utils;

public class DivisionTest {
    public static void main(String[] args) {
        AlgEngine<Symbolic> e1 = Utils.getEngine(Symbolic.class);
        OrderOfGrowthComparator<Symbolic> c1 = Utils.getGrowthComparator(Symbolic.class);
        Univariate<Symbolic> x = new Univariate<>("x", Symbolic.class);
        Univariate<Symbolic> y = new Univariate<>("y", Symbolic.class);
        Expression<Symbolic> expr = e1.mul(x, y, e1.sub(x, y));
        System.out.println(expr);
        System.out.println(e1.numberOfOperations(y));
    }
}
