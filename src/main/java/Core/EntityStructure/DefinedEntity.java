package Core.EntityStructure;

import Core.Propositions.PropositionStructure.Proposition;
import Core.Utilities.Utils;
import com.google.common.collect.TreeMultiset;

import java.util.*;

public abstract class DefinedEntity implements Entity {
    /** SECTION: Static Data ======================================================================================== */
    public static final int naturalDegreesOfFreedom = 0;

    /** SECTION: Instance Variables ================================================================================= */
    public int constrainedDegreesOfFreedom;
    public HashSet<Proposition> constraints = new HashSet<>();
    public HashMap<InputType, TreeMultiset<Entity>> inputs = new HashMap<>();

    /** SECTION: Abstract Constructor =============================================================================== */
    public DefinedEntity() {
        this.constrainedDegreesOfFreedom = DefinedEntity.naturalDegreesOfFreedom;
        for (InputType inputType : this.getInputTypes()) {
            this.inputs.put(inputType, TreeMultiset.create());
        }
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public boolean equals(Entity ent) {
        return Utils.PRIORITY_COMPARATOR.compare(this, ent) == 0;
    }

    public int getNaturalDegreesOfFreedom() {
        return DefinedEntity.naturalDegreesOfFreedom;
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


