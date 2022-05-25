package Core.EntityStructure;

import Core.Propositions.PropositionStructure.Proposition;
import com.google.common.collect.TreeMultiset;

import java.util.HashMap;
import java.util.HashSet;

public abstract class Mutable implements Entity {
    /** SECTION: Static Data ======================================================================================== */
    public enum Parameter implements InputType {
        VAR
    }
    public static final InputType[] inputTypes = {Parameter.VAR};

    /** SECTION: Instance Variables ================================================================================= */
    public int constrainedDegreesOfFreedom;
    public HashSet<Proposition> constraints = new HashSet<>();
    public HashMap<InputType, TreeMultiset<Entity>> inputs = new HashMap<>();
    public String name;

    /** SECTION: Abstract Constructor =============================================================================== */
    public Mutable(String n) {
        assert Entity.nameSet.add(n): "Name " + n + " already used";
        this.constrainedDegreesOfFreedom = getNaturalDegreesOfFreedom();
        for (InputType inputType : getInputTypes()) {
            inputs.put(inputType, TreeMultiset.create());
        }
        this.name = n;
        this.inputs.get(Parameter.VAR).add(this);
    }

    /** SECTION: Print Format ======================================================================================= */
    public String toString() {
        return this.name;
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public Entity simplify() {
        return this;
    }

    public boolean equals(Entity ent) {
        if (this.getClass() == ent.getClass()) {
            return this.name.equals(((Mutable) ent).name);
        } else {
            return false;
        }
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

    public InputType[] getInputTypes() {
        return Mutable.inputTypes;
    }
}


