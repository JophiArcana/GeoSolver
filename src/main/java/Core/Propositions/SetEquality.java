package Core.Propositions;

import Core.EntityStructure.*;
import Core.EntityStructure.UnicardinalStructure.*;
import Core.GeoSystem.Circles.CircleStructure.*;
import Core.GeoSystem.Lines.LineStructure.*;
import Core.GeoSystem.Points.PointStructure.*;
import Core.Propositions.PropositionStructure.*;
import Core.Utilities.Utils;

import java.util.*;

public class SetEquality<T extends Entity> extends ExtendedCondition {
    public Mutable pivot;
    public ArrayList<T> elements;
    public HashMap<T, Equality<T>> children;

    public SetEquality(Collection<T> elements) {
        this.elements = new ArrayList<>(elements);
        T first = this.elements.get(0);
        String pivotName = Utils.randomHash();
        if (first instanceof Expression<?> expr) {
            this.pivot = Variable.create(pivotName, expr.getType());
        } else if (first instanceof Point) {
            this.pivot = PointVariable.create(pivotName);
        } else if (first instanceof Line) {
            this.pivot = LineVariable.create(pivotName);
        } else if (first instanceof Circle) {
            this.pivot = CircleVariable.create(pivotName);
        }
        T pivotCast = (T) this.pivot;
        for (T element : this.elements) {
            Equality<T> childCondition = new Equality<>(element, pivotCast);
            this.children.put(element, childCondition);
            element.getConstraints().add(childCondition);
        }
    }

    public Entity getConcurrency() {
        return this.pivot;
    }

    public HashMap<? extends Entity, ? extends Condition> getChildren() {
        return this.children;
    }
}
