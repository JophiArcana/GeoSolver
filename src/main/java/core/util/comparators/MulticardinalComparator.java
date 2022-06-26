package core.util.comparators;

import core.structure.*;
import core.structure.multicardinal.Multicardinal;
import core.util.Utils;

import java.util.Comparator;

public class MulticardinalComparator implements Comparator<Multicardinal> {
    public int compare(Multicardinal e1, Multicardinal e2) {
        if (e1.getClass() != e2.getClass()) {
            return -(Utils.classCode(e1) - Utils.classCode(e2));
        } else if (e1 instanceof Mutable) {
            return e1.getName().compareTo(e2.getName());
        } else {
            return Utils.compareInputs(e1, e2);
        }
    }
}
