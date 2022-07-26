package core.structure;

import core.Propositions.Proposition;
import com.google.common.collect.TreeMultiset;
import core.Propositions.equalitypivot.EqualityPivot;
import core.Propositions.equalitypivot.LockedPivot;

import java.util.*;

public abstract class Mutable implements Entity {
    /** SECTION: Static Data ======================================================================================== */
    public static final List<InputType<?>> inputTypes = null;

    /** SECTION: Instance Variables ================================================================================= */
    public LockedPivot<?, ? extends Mutable> equalityPivot;

    public int constrainedDegreesOfFreedom;
    public HashSet<Proposition> constraints = new HashSet<>();
    public HashMap<InputType<?>, TreeMultiset<? extends EqualityPivot<?>>> inputs = new HashMap<>();
    public String name;

    /** SECTION: Abstract Constructor =============================================================================== */
    public Mutable(String n) {
        this.name = n;
        this.constrainedDegreesOfFreedom = getNaturalDegreesOfFreedom();
        this.inputSetup();
    }

    /** SECTION: Print Format ======================================================================================= */
    public String toString() {
        return this.name;
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
        this.equalityPivot = (LockedPivot<?, ? extends Mutable>) pivot;
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

    public List<InputType<?>> getInputTypes() {
        return Mutable.inputTypes;
    }
}


