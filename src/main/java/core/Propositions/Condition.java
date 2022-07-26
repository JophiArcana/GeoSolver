package core.Propositions;

import core.structure.Entity;

import java.util.*;

public interface Condition extends Proposition {
    // void carry();
    // void backPropagate();

    HashMap<? extends Entity, ? extends Condition> getChildren();

    default void clear() {
        for (Map.Entry<? extends Entity, ? extends Condition> entry : this.getChildren().entrySet()) {
            entry.getKey().getConstraints().remove(entry.getValue());
            entry.getValue().clear();
        }
    }
}
