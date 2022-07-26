package core.Propositions.equalitypivot.unicardinal;

import core.Propositions.equalitypivot.LockedPivot;
import core.structure.unicardinal.Unicardinal;

import java.util.HashSet;

public class LockedUnicardinalPivot<T extends Unicardinal, S extends T> extends LockedPivot<T, S> implements UnicardinalPivot<T> {
    public static <T extends Unicardinal, S extends T> LockedUnicardinalPivot<T, S> of(S lockedElement) {
        return new LockedUnicardinalPivot<>(lockedElement);
    }

    public final HashSet<Unicardinal> reverseComputationalDependencies = new HashSet<>();

    protected LockedUnicardinalPivot(S lockedElement) {
        super(lockedElement);
    }

    public HashSet<Unicardinal> reverseComputationalDependencies() {
        return this.reverseComputationalDependencies;
    }
}
