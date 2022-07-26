package core.Propositions.equalitypivot.multicardinal;

import core.Propositions.equalitypivot.EqualityPivot;
import core.Propositions.equalitypivot.unicardinal.UnicardinalPivot;
import core.structure.multicardinal.Multicardinal;
import core.util.Utils;
import javafx.scene.Node;

import javax.annotation.Nonnull;

public interface MulticardinalPivot<T extends Multicardinal> extends EqualityPivot<T>, Comparable<MulticardinalPivot<T>> {
    /**
    static <T extends Multicardinal> MulticardinalPivot<T> merge(MulticardinalPivot<T> p, MulticardinalPivot<T> q) {

    }
     */
    default int compareTo(@Nonnull MulticardinalPivot<T> p) {
        return Utils.MULTICARDINAL_COMPARATOR.compare(this.element(), p.element());
    }

    MulticardinalPivot<?> merge(MulticardinalPivot<?> pivot);

    Node getNode();
}
