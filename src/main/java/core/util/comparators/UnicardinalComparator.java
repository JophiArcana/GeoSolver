package core.util.comparators;

import core.structure.unicardinal.Constant;
import core.structure.unicardinal.Unicardinal;
import core.structure.unicardinal.Variable;
import core.util.Utils;

import java.util.Comparator;

public class UnicardinalComparator implements Comparator<Unicardinal> {
    public int compare(Unicardinal u1, Unicardinal u2) {
        if (u1 == u2) {
            return 0;
        } else if (u1 instanceof Constant c1 && u2 instanceof Constant c2) {
            return Constant.compare(c1, c2);
        } else if (u1.getDegree() != u2.getDegree()) {
            return -(u1.getDegree() - u2.getDegree());
        } else if (u1.getClass() != u2.getClass()) {
            return -(Utils.classCode(u1) - Utils.classCode(u2));
        } else if (u1 instanceof Variable var1 && u2 instanceof Variable var2) {
            return var1.name.compareTo(var2.name);
        } else {
            return Utils.compareInputs(u1, u2);
        }
    }
}
