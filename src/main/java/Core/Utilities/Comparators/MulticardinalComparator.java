package Core.Utilities.Comparators;

import Core.EntityStructure.*;
import Core.EntityStructure.MulticardinalStructure.Multicardinal;
import Core.Utilities.Utils;

import java.util.Comparator;

public class MulticardinalComparator<T extends Multicardinal> implements Comparator<T> {
    public final Comparator<Immutable> CONSTANT_COMPARATOR;

    public MulticardinalComparator(Comparator<Immutable> comparator) {
        this.CONSTANT_COMPARATOR = comparator;
    }

    public int compare(T e1, T e2) {
        if (e1.getClass() != e2.getClass()) {
            return -(Utils.classCode(e1) - Utils.classCode(e2));
        } else if (e1 instanceof Immutable) {
            return this.CONSTANT_COMPARATOR.compare((Immutable) e1, (Immutable) e2);
        } else if (e1 instanceof Mutable) {
            return ((Mutable) e1).name.compareTo(((Mutable) e2).name);
        } else {
            return Utils.compareInputs(e1, e2);
        }
    }
}
