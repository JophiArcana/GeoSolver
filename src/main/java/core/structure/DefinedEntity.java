package core.structure;

import core.Diagram;
import core.Propositions.PropositionStructure.Proposition;
import com.google.common.collect.TreeMultiset;

import java.util.*;

public abstract class DefinedEntity implements Entity {
    /** SECTION: Static Data ======================================================================================== */
    public static final int naturalDegreesOfFreedom = 0;

    /** SECTION: Instance Variables ================================================================================= */
    public Diagram diagram;
    public HashSet<Entity> reverseDependencies = new HashSet<>();
    public int constrainedDegreesOfFreedom;
    public HashSet<Proposition> constraints = new HashSet<>();
    public HashMap<InputType<?>, TreeMultiset<? extends Entity>> inputs = new HashMap<>();

    /** SECTION: Abstract Constructor =============================================================================== */
    public DefinedEntity(Diagram d) {
        this.diagram = d;
        this.constrainedDegreesOfFreedom = DefinedEntity.naturalDegreesOfFreedom;
        this.inputSetup();
    }

    /** SECTION: Implementation ===================================================================================== */
    /**
     * SUBSECTION: Entity ==========================================================================================
     */
    public HashSet<Entity> reverseDependencies() {
        return this.reverseDependencies;
    }

    public Diagram getDiagram() {
        return this.diagram;
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

    public HashMap<InputType<?>, TreeMultiset<? extends Entity>> getInputs() {
        return this.inputs;
    }
}


