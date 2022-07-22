package core.util.comparators;

import core.structure.*;
import core.structure.multicardinal.Multicardinal;
import core.util.Utils;

import java.util.Comparator;

public class MulticardinalComparator implements Comparator<Multicardinal> {
    public int compare(Multicardinal m1, Multicardinal m2) {
        if (m1 == m2) {
            return 0;
        } else if (m1.getClass() != m2.getClass()) {
            return -(Utils.classCode(m1) - Utils.classCode(m2));
        } else if (m1 instanceof Mutable) {
            return m1.getName().compareTo(m2.getName());
        } else {
            return Utils.compareInputs(m1, m2);
        }
    }
}
