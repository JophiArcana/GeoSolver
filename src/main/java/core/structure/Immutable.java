package core.structure;

import core.Diagram;
import core.Propositions.PropositionStructure.Proposition;
import com.google.common.collect.TreeMultiset;
import core.structure.equalitypivot.EqualityPivot;
import core.structure.equalitypivot.LockedEqualityPivot;
import core.structure.unicardinal.Unicardinal;

import java.util.*;

public abstract class Immutable implements Entity {
    /** SECTION: Static Data ======================================================================================== */
    public static final int naturalDegreesOfFreedom = 0;

    /** SECTION: Instance Variables ================================================================================= */
    public LockedEqualityPivot<?, ? extends Immutable> equalityPivot;

    public int constrainedDegreesOfFreedom;
    public HashSet<Proposition> constraints = new HashSet<>();
    public HashMap<InputType<?>, TreeMultiset<EqualityPivot<?>>> inputs = new HashMap<>();

    /** SECTION: Abstract Constructor =============================================================================== */
    public Immutable() {
        this.constrainedDegreesOfFreedom = Immutable.naturalDegreesOfFreedom;
        this.inputSetup();
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public void updateLocalVariables(EqualityPivot<?> consumedPivot, EqualityPivot<?> consumerPivot) {
    }

    public EqualityPivot<?> getEqualityPivot() {
        return this.equalityPivot;
    }

    public void setEqualityPivot(EqualityPivot<?> pivot) {
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

    public HashMap<InputType<?>, TreeMultiset<EqualityPivot<?>>> getInputs() {
        return this.inputs;
    }
}


