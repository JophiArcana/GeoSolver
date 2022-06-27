package core.Propositions.PropositionStructure;

import core.structure.Entity;

import java.util.*;

public abstract class Condition extends Proposition {
    // public abstract void carry();
    // public abstract void backPropagate();

    public HashMap<? extends Entity, ? extends Condition> getChildren() {
        return new HashMap<>();
    }

    public void clear() {
        for (Map.Entry<? extends Entity, ? extends Condition> entry : this.getChildren().entrySet()) {
            entry.getKey().getConstraints().remove(entry.getValue());
            entry.getValue().clear();
        }
    }
}
