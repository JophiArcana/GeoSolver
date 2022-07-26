package core.Propositions.equalitypivot;

import core.structure.Entity;

import java.util.HashSet;

public abstract class ImmutablePivot<T extends Entity> implements EqualityPivot<T> {
    public final HashSet<T> elements = new HashSet<>();
    public final T constantElement;

    public final HashSet<Entity> reverseDependencies = new HashSet<>();

    protected ImmutablePivot(T constantElement) {
        this.constantElement = constantElement;
        this.elements.add(this.constantElement);
    }

    public String toString() {
        return this.constantElement.toString();
    }

    public HashSet<T> elements() {
        return this.elements;
    }

    public T element() {
        return this.constantElement;
    }

    public HashSet<Entity> reverseDependencies() {
        return this.reverseDependencies;
    }
}
