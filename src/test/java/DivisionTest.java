
import core.alg.UnicardinalStructure.*;
import core.structure.unicardinal.alg.Expression;
import core.structure.unicardinal.alg.Variable;
import core.util.AlgEngine;
import core.util.Utils;

import java.util.List;

public class DivisionTest {
    public static void main(String[] args) {
        AlgEngine<Symbolic> e1 = Utils.getEngine(Symbolic.class);
        Variable<Symbolic> x = Variable.create("x", Symbolic.class);
        Variable<Symbolic> y = Variable.create("y", Symbolic.class);
        Expression<Symbolic> expr = e1.mul(List.of(x, y, e1.sub(x, y)));
        System.out.println(expr);
        System.out.println(e1.numberOfOperations(y));
    }
}
