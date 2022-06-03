package Core.Propositions;

import Core.EntityStructure.Entity;
import Core.Propositions.PropositionStructure.FundamentalCondition;

public class Equality<T extends Entity> extends FundamentalCondition {
    T entityA, entityB;

    public Equality(T entityA, T entityB) {
        this.entityA = entityA;
        this.entityB = entityB;
    }
}
