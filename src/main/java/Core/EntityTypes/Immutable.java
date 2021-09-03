package Core.EntityTypes;

import Core.AlgeSystem.UnicardinalTypes.Unicardinal;

import Core.Property;
import Core.Utilities.Utils;
import com.google.common.collect.TreeMultiset;

import java.util.*;
import java.util.function.Function;

public abstract class Immutable implements Entity {
    public static final int naturalDegreesOfFreedom = 0;

    public Entity create(HashMap<String, ArrayList<Entity>> args) {
        return this;
    }

    public int constrainedDegreesOfFreedom;
    public ArrayList<Function<Entity, Property>> constraints = new ArrayList<>();
    public HashMap<String, TreeMultiset<Entity>> inputs = new HashMap<>();

    public Immutable() {
        this.constrainedDegreesOfFreedom = Immutable.naturalDegreesOfFreedom;
        for (String inputType : this.getInputTypes()) {
            this.inputs.put(inputType, TreeMultiset.create(Utils.PRIORITY_COMPARATOR));
        }
    }

    public abstract int compareTo(Immutable immutable);

    public boolean equals(Entity ent) {
        if (ent instanceof Immutable immutable) {
            return this.compareTo(immutable) == 0;
        } else {
            return false;
        }
    }

    public Entity simplify() {
        return this;
    }

    public int getNaturalDegreesOfFreedom() {
        return Immutable.naturalDegreesOfFreedom;
    }

    public int getConstrainedDegreesOfFreedom() {
        return constrainedDegreesOfFreedom;
    }

    public ArrayList<Function<Entity, Property>> getConstraints() {
        return constraints;
    }

    public HashMap<String, TreeMultiset<Entity>> getInputs() {
        return inputs;
    }
}


