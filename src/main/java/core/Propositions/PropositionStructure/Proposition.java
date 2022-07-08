package core.Propositions.PropositionStructure;

import core.Propositions.SetEquality;
import core.structure.Entity;

public interface Proposition extends Entity {
    default SetEquality getSetEquality() {
        return null;
    }
}
