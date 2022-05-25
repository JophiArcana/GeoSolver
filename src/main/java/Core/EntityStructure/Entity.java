package Core.EntityStructure;

import Core.AlgSystem.UnicardinalRings.Symbolic;
import Core.EntityStructure.UnicardinalStructure.*;
import Core.EntityStructure.MulticardinalStructure.Multicardinal;
import Core.Propositions.PropositionStructure.Proposition;
import Core.Utilities.Utils;
import com.google.common.collect.TreeMultiset;

import java.util.*;
import javafx.util.*;

public interface Entity extends Comparable<Entity> {
    HashSet<String> nameSet = new HashSet<>();

    interface InputType {}
    interface ExpressionType {}

    boolean equals(Entity ent);

    Entity simplify();
    Unicardinal expression(ExpressionType varType);
    ArrayList<Expression<Symbolic>> symbolic();

    default void substitute(InputType inputType, Pair<Multicardinal, Multicardinal> multicardinalPair) {
        TreeMultiset<Entity> elements = this.getInputs().get(inputType);
        if (elements.remove(multicardinalPair.getKey())) {
            elements.add(multicardinalPair.getValue());
        }
    }

    int getNaturalDegreesOfFreedom();
    int getConstrainedDegreesOfFreedom();
    HashSet<Proposition> getConstraints();

    HashMap<InputType, TreeMultiset<Entity>> getInputs();
    InputType[] getInputTypes();

    default int compareTo(Entity ent) {
        return Utils.PRIORITY_COMPARATOR.compare(this, ent);
    }
}

