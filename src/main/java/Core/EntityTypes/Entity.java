package Core.EntityTypes;

import Core.AlgeSystem.UnicardinalRings.Symbolic;
import Core.AlgeSystem.UnicardinalTypes.*;
import Core.Property;
import Core.Utilities.Utils;
import com.google.common.collect.TreeMultiset;

import java.util.*;
import java.util.function.Function;

import javafx.util.*;


public interface Entity {
    boolean equals(Entity ent);

    Entity simplify();
    ArrayList<Unicardinal> expression();
    Unicardinal expression(String varType);
    ArrayList<Expression<Symbolic>> symbolic();

    int getNaturalDegreesOfFreedom();
    int getConstrainedDegreesOfFreedom();
    ArrayList<Function<Entity, Property>> getConstraints();

    Entity create(HashMap<String, ArrayList<Entity>> args);

    HashMap<String, TreeMultiset<Entity>> getInputs();
    String[] getInputTypes();

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
            HashMap<String, ArrayList<Entity>> substitutionInputs = new HashMap<>();
            for (String inputType : this.getInputTypes()) {
                substitutionInputs.put(inputType, Utils.map(this.getInputs().get(inputType), arg -> arg.substitute(entityPair)));
            }
            return this.create(substitutionInputs);
        }
    }
}

