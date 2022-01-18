package Core.EntityTypes;

import Core.AlgSystem.UnicardinalRings.Symbolic;
import Core.AlgSystem.UnicardinalTypes.*;
import Core.GeoSystem.MulticardinalTypes.Multicardinal;
import Core.Property;
import Core.Utilities.Utils;
import com.google.common.collect.TreeMultiset;

import java.util.*;
import java.util.function.Function;

import javafx.util.*;

public interface Entity extends Comparable<Entity> {
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
    ArrayList<Function<Entity, Property>> getConstraints();

    HashMap<InputType, TreeMultiset<Entity>> getInputs();
    InputType[] getInputTypes();

    default int compareTo(Entity ent) {
        return Utils.PRIORITY_COMPARATOR.compare(this, ent);
    }
}

