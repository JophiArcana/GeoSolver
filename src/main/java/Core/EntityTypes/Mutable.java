package Core.EntityTypes;

import Core.Property;
import Core.Utilities.Utils;
import com.google.common.collect.TreeMultiset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;

public abstract class Mutable implements Entity {
    /** SECTION: Static Data ======================================================================================== */
    public enum Parameter implements InputType {
        VAR
    }
    public static final InputType[] inputTypes = {Parameter.VAR};

    /** SECTION: Instance Variables ================================================================================= */
    public int constrainedDegreesOfFreedom;
    public ArrayList<Function<Entity, Property>> constraints = new ArrayList<>();
    public HashMap<InputType, TreeMultiset<Entity>> inputs = new HashMap<>();
    public String name;

    /** SECTION: Factory Methods ==================================================================================== */
    public Entity createEntity(HashMap<InputType, ArrayList<Entity>> args) {
        return args.get(Parameter.VAR).get(0);
    }

    /** SECTION: Abstract Constructor =============================================================================== */
    public Mutable(String n) {
        this.constrainedDegreesOfFreedom = getNaturalDegreesOfFreedom();
        for (InputType inputType : getInputTypes()) {
            inputs.put(inputType, TreeMultiset.create(Utils.PRIORITY_COMPARATOR));
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

    public ArrayList<Function<Entity, Property>> getConstraints() {
        return this.constraints;
    }

    public HashMap<InputType, TreeMultiset<Entity>> getInputs() {
        return this.inputs;
    }

    public InputType[] getInputTypes() {
        return Mutable.inputTypes;
    }
}


