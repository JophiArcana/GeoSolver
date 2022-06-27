package core.Propositions;

import core.structure.*;
import core.structure.multicardinal.geo.circle.structure.*;
import core.structure.multicardinal.geo.line.structure.*;
import core.structure.multicardinal.geo.point.structure.*;
import core.Propositions.PropositionStructure.*;
import core.structure.unicardinal.alg.directed.*;
import core.structure.unicardinal.alg.symbolic.*;
import core.util.Utils;

import java.util.*;

public class SetEquality<T extends Entity> extends ExtendedCondition {
    public Mutable pivot;
    public ArrayList<T> elements;
    public HashMap<T, Equality<T>> children;

    public SetEquality(Collection<T> elements) {
        this.elements = new ArrayList<>(elements);
        T first = this.elements.get(0);

        String pivotName = Utils.randomHash();

        this.pivot = switch (first) {
            case DirectedExpression directedExpression -> DirectedVariable.create(pivotName, directedExpression.doubleValue());
            case SymbolicExpression symbolicExpression -> SymbolicVariable.create(pivotName, symbolicExpression.doubleValue());
            case Point point -> PointVariable.create(pivotName, point.symbolic().get(0).doubleValue(), point.symbolic().get(1).doubleValue());
            case Line line -> LineVariable.create(pivotName, line.symbolic().get(0).doubleValue(), line.symbolic().get(1).doubleValue());
            case Circle circle -> CircleVariable.create(pivotName, circle.symbolic().get(0).doubleValue(), circle.symbolic().get(1).doubleValue(), circle.symbolic().get(2).doubleValue());
            case default -> null;
        };

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
