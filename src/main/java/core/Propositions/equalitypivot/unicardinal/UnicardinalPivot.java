package core.Propositions.equalitypivot.unicardinal;

import core.Propositions.equalitypivot.EqualityPivot;
import core.structure.unicardinal.Unicardinal;
import core.util.Utils;

import javax.annotation.Nonnull;
import java.util.HashSet;

public interface UnicardinalPivot<T extends Unicardinal> extends EqualityPivot<T>, Comparable<UnicardinalPivot<T>> {
    /**
    static <T extends Unicardinal> UnicardinalPivot<T> merge(UnicardinalPivot<T> p, UnicardinalPivot<T> q) {

    }
     */
    default int compareTo(@Nonnull UnicardinalPivot<T> p) {
        return Utils.UNICARDINAL_COMPARATOR.compare(this.element(), p.element());
    }

    UnicardinalPivot<?> merge(UnicardinalPivot<?> pivot);

    HashSet<Unicardinal> reverseComputationalDependencies();

    default double doubleValue() {
        return this.element().doubleValue();
    }
}
