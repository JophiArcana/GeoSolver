package core.structure.equalitypivot;

import core.structure.Entity;

public class LockedEqualityPivot<T extends Entity, S extends T> extends EqualityPivot<T> {
    @SafeVarargs
    public static <T extends Entity, S extends T> LockedEqualityPivot<T, S> of(T lockedElement) {

    }

    public final S lockedElement;

    protected LockedEqualityPivot(S lockedElement) {
        super();
        this.lockedElement = lockedElement;
        this.simplestElement = lockedElement;
        this.elements.add(this.simplestElement);
    }
}
