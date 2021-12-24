package Core.EntityTypes;

import Core.AlgSystem.UnicardinalRings.Symbolic;
import Core.AlgSystem.UnicardinalTypes.*;
import Core.Property;
import Core.Utilities.Utils;
import com.google.common.collect.TreeMultiset;

import java.util.*;
import java.util.function.Function;

import javafx.util.*;

public interface Entity {
    interface InputType {}
    interface ExpressionType {}

    boolean equals(Entity ent);

    Entity simplify();
    Unicardinal expression(ExpressionType varType);
    ArrayList<Expression<Symbolic>> symbolic();

    int getNaturalDegreesOfFreedom();
    int getConstrainedDegreesOfFreedom();
    ArrayList<Function<Entity, Property>> getConstraints();

    Entity createEntity(HashMap<InputType, ArrayList<Entity>> args);

    HashMap<InputType, TreeMultiset<Entity>> getInputs();
    InputType[] getInputTypes();

    default TreeSet<Mutable> variables() {
        TreeSet<Mutable> vars = new TreeSet<>(Utils.PRIORITY_COMPARATOR);
        if (this instanceof Mutable var) {
            vars.add(var);
        } else if (this instanceof DefinedEntity) {
            for (TreeMultiset<Entity> input : this.getInputs().values()) {
                for (Entity ent : input) {
                    vars.addAll(ent.variables());
                }
            }
        }
        return vars;
    }

    default Entity substitute(Pair<Entity, Entity> entityPair) {
        if (this.equals(entityPair.getKey())) {
            return entityPair.getValue();
        } else if (this instanceof Mutable || this instanceof Immutable) {
            return this;
        } else {
            HashMap<InputType, ArrayList<Entity>> substitutionInputs = new HashMap<>();
            for (InputType inputType : this.getInputTypes()) {
                substitutionInputs.put(inputType, Utils.map(this.getInputs().get(inputType), arg -> arg.substitute(entityPair)));
            }
            return this.createEntity(substitutionInputs);
        }
    }
}

