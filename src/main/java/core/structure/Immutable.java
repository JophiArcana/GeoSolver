package core.structure;

import core.Propositions.Proposition;
import com.google.common.collect.TreeMultiset;
import core.Propositions.equalitypivot.EqualityPivot;
import core.Propositions.equalitypivot.LockedPivot;

import java.util.*;

public abstract class Immutable implements Entity {
    /** SECTION: Static Data ======================================================================================== */
    public static final int naturalDegreesOfFreedom = 0;

    /** SECTION: Instance Variables ================================================================================= */
    public LockedPivot<?, ? extends Immutable> equalityPivot;

    public int constrainedDegreesOfFreedom;
    public HashSet<Proposition> constraints = new HashSet<>();
    public HashMap<InputType<?>, TreeMultiset<? extends EqualityPivot<?>>> inputs = new HashMap<>();

    /** SECTION: Abstract Constructor =============================================================================== */
    public Immutable() {
        this.constrainedDegreesOfFreedom = Immutable.naturalDegreesOfFreedom;
        this.inputSetup();
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public void deleteSymbolic() {
    }

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

    public HashMap<InputType<?>, TreeMultiset<? extends EqualityPivot<?>>> getInputs() {
        return this.inputs;
    }
}


