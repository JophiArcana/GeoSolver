package core.Propositions.equalitypivot;

import core.structure.Entity;

import java.util.HashSet;

public abstract class LockedPivot<T extends Entity, S extends T> implements EqualityPivot<T> {
    public final HashSet<T> elements = new HashSet<>();
    public final S lockedElement;

    public final HashSet<Entity> reverseDependencies = new HashSet<>();

    protected LockedPivot(S lockedElement) {
        this.lockedElement = lockedElement;
        this.elements.add(this.lockedElement);
    }

    public String toString() {
        return this.lockedElement.toString();
    }

    public HashSet<T> elements() {
        return this.elements;
    }

    public T element() {
        return this.lockedElement;
    }

    public HashSet<Entity> reverseDependencies() {
        return this.reverseDependencies;
    }
}
