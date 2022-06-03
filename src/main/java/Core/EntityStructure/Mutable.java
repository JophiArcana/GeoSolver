package Core.EntityStructure;

import Core.Diagram;
import Core.Propositions.PropositionStructure.Proposition;
import com.google.common.collect.TreeMultiset;

import java.util.*;

public abstract class Mutable implements Entity {
    /** SECTION: Static Data ======================================================================================== */
    public static final InputType[] inputTypes = {};

    /** SECTION: Instance Variables ================================================================================= */
    public Diagram diagram;
    public int constrainedDegreesOfFreedom;
    public HashSet<Proposition> constraints = new HashSet<>();
    public HashMap<InputType, TreeMultiset<? extends Entity>> inputs = new HashMap<>();
    public String name;

    /** SECTION: Abstract Constructor =============================================================================== */
    public Mutable(Diagram d, String n) {
        assert d.nameSet.add(n): "Name " + n + " already in use.";
        this.diagram = d;
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
    public Entity simplify() {
        return this;
    }

    public int getConstrainedDegreesOfFreedom() {
        return this.constrainedDegreesOfFreedom;
    }

    public HashSet<Proposition> getConstraints() {
        return this.constraints;
    }

    public HashMap<InputType, TreeMultiset<? extends Entity>> getInputs() {
        return this.inputs;
    }

    public InputType[][] getInputTypes() {
        return Mutable.inputTypes;
    }
}


