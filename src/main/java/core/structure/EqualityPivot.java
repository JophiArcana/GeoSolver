package core.structure;

import core.Propositions.PropositionStructure.Proposition;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;

import java.util.*;

public class EqualityPivot<T extends Entity> implements Proposition {
    public HashSet<T> elements = new HashSet<>();

    public HashSet<Entity> reverseDependencies = new HashSet<>();

    public T simplestElement;
    public List<SymbolicExpression> symbolic;
}
