package Core.Utilities;

import Core.EntityStructure.UnicardinalStructure.*;

import java.util.Comparator;

public class ExpressionComparator implements Comparator<Unicardinal> {
    public int compare(Unicardinal u1, Unicardinal u2) {
        Expression<?> e1 = (Expression<?>) u1, e2 = (Expression<?>) u2;
        Class<?> type1 = e1.getType(), type2 = e2.getType();

    }
}
