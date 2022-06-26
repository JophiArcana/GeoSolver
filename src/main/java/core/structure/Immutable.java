package core.structure;

import core.Propositions.PropositionStructure.Proposition;
import com.google.common.collect.TreeMultiset;

import java.util.*;

public abstract class Immutable implements Entity {
    /** SECTION: Static Data ======================================================================================== */
    public static final int naturalDegreesOfFreedom = 0;

    /** SECTION: Instance Variables ================================================================================= */
    public int constrainedDegreesOfFreedom;
    public HashSet<Proposition> constraints = new HashSet<>();
    public HashMap<InputType<?>, TreeMultiset<? extends Entity>> inputs = new HashMap<>();

    /** SECTION: Abstract Constructor =============================================================================== */
    public Immutable() {
        this.constrainedDegreesOfFreedom = Immutable.naturalDegreesOfFreedom;
        this.inputSetup();
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public HashSet<Entity> reverseDependencies() {
        return null;
    }

    public Entity simplify() {
        return this;
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

    public HashMap<InputType<?>, TreeMultiset<? extends Entity>> getInputs() {
        return this.inputs;
    }
}


