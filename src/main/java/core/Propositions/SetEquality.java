package core.Propositions;

import core.Diagram;
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

        Diagram d = first.getDiagram();
        String pivotName = Utils.randomHash();

        this.pivot = switch (first) {
            case DirectedExpression directedExpression -> DirectedVariable.create(d, pivotName, directedExpression.value());
            case SymbolicExpression symbolicExpression -> SymbolicVariable.create(d, pivotName, symbolicExpression.value());
            case Point point -> PointVariable.create(d, pivotName, point.symbolic().get(0).value(), point.symbolic().get(1).value());
            case Line line -> LineVariable.create(d, pivotName, line.symbolic().get(0).value(), line.symbolic().get(1).value());
            case Circle circle -> CircleVariable.create(d, pivotName, circle.symbolic().get(0).value(), circle.symbolic().get(1).value(), circle.symbolic().get(2).value());
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
