package Core.EntityTypes;

import Core.Property;
import Core.Utilities.Utils;
import com.google.common.collect.TreeMultiset;

import java.util.*;
import java.util.function.Function;

public abstract class Immutable implements Entity {
    /** SECTION: Static Data ======================================================================================== */
    public static final int naturalDegreesOfFreedom = 0;

    /** SECTION: Instance Variables ================================================================================= */
    public int constrainedDegreesOfFreedom;
    public ArrayList<Function<Entity, Property>> constraints = new ArrayList<>();
    public HashMap<InputType, TreeMultiset<Entity>> inputs = new HashMap<>();

    /** SECTION: Factory Methods ==================================================================================== */
    public Entity createEntity(HashMap<InputType, ArrayList<Entity>> args) {
        return this;
    }

    /** SECTION: Abstract Constructor =============================================================================== */
    public Immutable() {
        this.constrainedDegreesOfFreedom = Immutable.naturalDegreesOfFreedom;
        for (InputType inputType : this.getInputTypes()) {
            this.inputs.put(inputType, TreeMultiset.create(Utils.PRIORITY_COMPARATOR));
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

    public ArrayList<Function<Entity, Property>> getConstraints() {
        return this.constraints;
    }

    public HashMap<InputType, TreeMultiset<Entity>> getInputs() {
        return this.inputs;
    }

    /** SECTION: Comparison ========================================================================================= */
    public abstract int compareTo(Immutable immutable);
}


