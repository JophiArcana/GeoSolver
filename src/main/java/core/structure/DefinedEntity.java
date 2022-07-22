package core.structure;

import core.Propositions.PropositionStructure.Proposition;
import com.google.common.collect.TreeMultiset;
import core.structure.equalitypivot.EqualityPivot;

import java.util.*;

public abstract class DefinedEntity implements Entity {
    /** SECTION: Static Data ======================================================================================== */
    public static final int naturalDegreesOfFreedom = 0;

    /** SECTION: Instance Variables ================================================================================= */
    public EqualityPivot<?> equalityPivot;

    public int constrainedDegreesOfFreedom;
    public HashSet<Proposition> constraints = new HashSet<>();
    public HashMap<InputType<?>, TreeMultiset<EqualityPivot<?>>> inputs = new HashMap<>();

    /** SECTION: Abstract Constructor =============================================================================== */
    public DefinedEntity() {
        this.constrainedDegreesOfFreedom = DefinedEntity.naturalDegreesOfFreedom;
        this.inputSetup();
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public EqualityPivot<?> getEqualityPivot() {
        return this.equalityPivot;
    }

    public void setEqualityPivot(EqualityPivot<?> pivot) {
        this.equalityPivot = pivot;
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

    public HashMap<InputType<?>, TreeMultiset<EqualityPivot<?>>> getInputs() {
        return this.inputs;
    }
}


