package core.Propositions.equalitypivot.multicardinal;

import core.Propositions.equalitypivot.LockedPivot;
import core.structure.multicardinal.Multicardinal;

public class LockedMulticardinalPivot<T extends Multicardinal, S extends T> extends LockedPivot<T, S> implements MulticardinalPivot<T> {
    public static <T extends Multicardinal, S extends T> LockedMulticardinalPivot<T, S> of(S lockedElement) {
        return new LockedMulticardinalPivot<>(lockedElement);
    }

    protected LockedMulticardinalPivot(S lockedElement) {
        super(lockedElement);
    }
}
