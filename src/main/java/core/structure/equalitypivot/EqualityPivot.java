package core.structure.equalitypivot;

import core.Propositions.PropositionStructure.Proposition;
import core.structure.Entity;
import core.structure.multicardinal.Multicardinal;
import core.structure.unicardinal.Unicardinal;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;
import core.util.Utils;

import javax.annotation.Nonnull;
import java.util.*;

public class EqualityPivot<T extends Entity> implements Proposition, Comparable<EqualityPivot<T>> {
    @SafeVarargs
    public static <T extends Entity> EqualityPivot<T> of(T... args) {

    }

    public static <T extends Entity> EqualityPivot<T> merge(EqualityPivot<T> p, EqualityPivot<T> q) {

    }

    public final boolean isUnicardinal;

    public final HashSet<T> elements = new HashSet<>();
    public T simplestElement;

    public final HashSet<Entity> reverseDependencies = new HashSet<>();
    public HashSet<Entity> reverseComputationalDependencies;

    @SafeVarargs
    protected EqualityPivot(T... elements) {
        this.isUnicardinal = elements[0] instanceof Unicardinal;
        if (this.isUnicardinal) {
            this.reverseComputationalDependencies = new HashSet<>();
        }
        for (T element : elements) {

        }
    }

    public int compareTo(@Nonnull EqualityPivot<T> pivot) {
        if (this == pivot) {
            return 0;
        } else if (this.isUnicardinal) {
            return Utils.UNICARDINAL_COMPARATOR.compare((Unicardinal) this.simplestElement, (Unicardinal) pivot.simplestElement);
        } else {
            return Utils.MULTICARDINAL_COMPARATOR.compare((Multicardinal) this.simplestElement, (Multicardinal) pivot.simplestElement);
        }
    }

    public List<EqualityPivot<SymbolicExpression>> symbolic() {
        return this.simplestElement.symbolic();
    }

    public double doubleValue() {
        if (this.isUnicardinal) {
            return ((Unicardinal) this.simplestElement).doubleValue();
        } else {
            return Double.POSITIVE_INFINITY;
        }
    }
}
