package Core.Propositions;

import Core.EntityStructure.*;
import Core.EntityStructure.UnicardinalStructure.Expression;
import Core.EntityStructure.UnicardinalStructure.Variable;
import Core.GeoSystem.Lines.LineStructure.*;
import Core.GeoSystem.Points.PointStructure.*;
import Core.Propositions.PropositionStructure.ExtendedCondition;
import Core.Utilities.Utils;

import java.util.*;

public class SetEquality<T extends Entity> extends ExtendedCondition {
    Mutable pivot;
    ArrayList<T> elements;

    public SetEquality(Collection<T> elements) {
        this.elements = new ArrayList<>(elements);
        T element = this.elements.get(0);
        String pivotName = Utils.randomHash();
        if (element instanceof Expression<?> expr) {
            this.pivot = Variable.create(pivotName, expr.getType());
        } else if (element instanceof Point) {
            this.pivot = PointVariable.create(pivotName);
        } else if (element instanceof Line) {
            this.pivot = LineVariable.create(pivotName);
        } else if
    }
}
