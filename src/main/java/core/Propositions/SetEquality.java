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

public class SetEquality implements Proposition {
    public Mutable pivot;
    public HashSet<Entity> elements;

    public SetEquality(Collection<Entity> elements) {
        this.elements = new HashSet<>(elements);
        Entity first = this.elements.iterator().next();

        String pivotName = Utils.randomHash();
        this.pivot = switch (first) {
            case DirectedExpression directedExpression -> DirectedVariable.create(pivotName, directedExpression.doubleValue());
            case SymbolicExpression symbolicExpression -> SymbolicVariable.create(pivotName, symbolicExpression.doubleValue());
            case Point point -> PointVariable.create(pivotName, point.doubleValue());
            case Line line -> LineVariable.create(pivotName, line.doubleValue());
            case Circle circle -> CircleVariable.create(pivotName, circle.doubleValue());
            case default -> null;
        };
    }
}
