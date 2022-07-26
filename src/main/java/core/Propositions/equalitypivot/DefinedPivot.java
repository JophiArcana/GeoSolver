package core.Propositions.equalitypivot;

import core.structure.Entity;

import java.util.HashSet;

public abstract class DefinedPivot<T extends Entity> implements EqualityPivot<T> {
    public final HashSet<T> elements = new HashSet<>();
    public T simplestElement;

    public final HashSet<Entity> reverseDependencies = new HashSet<>();

    protected DefinedPivot(T element) {
        this.simplestElement = element;
        this.elements.add(this.simplestElement);
    }

    public String toString() {
        return this.simplestElement.toString();
    }

    public HashSet<T> elements() {
        return this.elements;
    }

    public T element() {
        return this.simplestElement;
    }

    public HashSet<Entity> reverseDependencies() {
        return this.reverseDependencies;
    }
}
