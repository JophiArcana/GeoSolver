package core.Propositions.equalitypivot;

import core.Propositions.Proposition;
import core.Propositions.equalitypivot.unicardinal.UnicardinalPivot;
import core.structure.Entity;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;

import java.util.*;

public interface EqualityPivot<T extends Entity> extends Proposition {
    HashSet<T> elements();
    T element();

    HashSet<Entity> reverseDependencies();

    default List<UnicardinalPivot<SymbolicExpression>> symbolic() {
        return this.element().symbolic();
    }
}



