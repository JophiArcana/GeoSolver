package Core.Propositions.PropositionStructure;

import Core.EntityStructure.Entity;

import java.util.*;

public abstract class Condition extends Proposition {
    public abstract void carry();
    public abstract void backPropagate();

    public abstract HashMap<Entity, ? extends Condition> getChildren();

    public void clear() {
        for (Map.Entry<Entity, ? extends Condition> entry : this.getChildren().entrySet()) {
            entry.getKey().getConstraints().remove(entry.getValue());
            entry.getValue().clear();
        }
    }
}
