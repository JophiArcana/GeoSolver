package core.Propositions.equalitypivot;

import core.structure.Entity;

import java.util.HashSet;

public class MutablePivot<T extends Entity> implements EqualityPivot<T> {
    public final HashSet<T> elements = new HashSet<>();
    public final T variableElement;

    public final HashSet<Entity> reverseDependencies = new HashSet<>();

    protected MutablePivot(T variableElement) {
        this.variableElement = variableElement;
        this.elements.add(this.variableElement);
    }

    public String toString() {
        return this.variableElement.toString();
    }

    public HashSet<T> elements() {
        return this.elements;
    }

    public T element() {
        return this.variableElement;
    }

    public HashSet<Entity> reverseDependencies() {
        return this.reverseDependencies;
    }
}
