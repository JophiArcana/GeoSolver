package Core.EntityStructure;

import Core.Propositions.PropositionStructure.Proposition;
import com.google.common.collect.TreeMultiset;

import java.util.*;

public abstract class Immutable implements Entity {
    /** SECTION: Static Data ======================================================================================== */
    public static final int naturalDegreesOfFreedom = 0;

    /** SECTION: Instance Variables ================================================================================= */
    public int constrainedDegreesOfFreedom;
    public HashSet<Proposition> constraints = new HashSet<>();
    public HashMap<InputType, TreeMultiset<Entity>> inputs = new HashMap<>();

    /** SECTION: Abstract Constructor =============================================================================== */
    public Immutable() {
        this.constrainedDegreesOfFreedom = Immutable.naturalDegreesOfFreedom;
        for (InputType inputType : this.getInputTypes()) {
            this.inputs.put(inputType, TreeMultiset.create());
        }
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public Entity simplify() {
        return this;
    }

    public boolean equals(Entity ent) {
        if (ent instanceof Immutable immutable) {
            return this.compareTo(immutable) == 0;
        } else {
            return false;
        }
    }

    public int getNaturalDegreesOfFreedom() {
        return Immutable.naturalDegreesOfFreedom;
    }

    public int getConstrainedDegreesOfFreedom() {
        return this.constrainedDegreesOfFreedom;
    }

    public HashSet<Proposition> getConstraints() {
        return this.constraints;
    }

    public HashMap<InputType, TreeMultiset<Entity>> getInputs() {
        return this.inputs;
    }
}


