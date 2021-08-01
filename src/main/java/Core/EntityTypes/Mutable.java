package Core.EntityTypes;

import Core.AlgeSystem.UnicardinalTypes.Unicardinal;
import Core.Property;
import Core.Utilities.Utils;
import com.google.common.collect.TreeMultiset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;

public abstract class Mutable implements Entity {
    public static final String[] inputTypes = new String[] {"Variable"};

    public Entity create(HashMap<String, ArrayList<Entity>> args) {
        return args.get("Variable").get(0);
    }

    public static final Function<HashMap<String, ArrayList<ArrayList<Unicardinal>>>, ArrayList<Unicardinal>> formula = args ->
            args.get("Variable").get(0);

    public int constrainedDegreesOfFreedom;
    public ArrayList<Function<Entity, Property>> constraints = new ArrayList<>();
    public HashMap<String, TreeMultiset<Entity>> inputs = new HashMap<>();
    public String name;

    public Mutable(String n) {
        this.constrainedDegreesOfFreedom = getNaturalDegreesOfFreedom();
        for (String inputType : getInputTypes()) {
            inputs.put(inputType, TreeMultiset.create(Utils.PRIORITY_COMPARATOR));
        }
        this.name = n;
        this.inputs.get("Variable").add(this);
    }

    public String toString() {
        return this.name;
    }

    public boolean equals(Entity ent) {
        if (this.getClass() == ent.getClass()) {
            return this.name.equals(((Mutable) ent).name);
        } else {
            return false;
        }
    }

    public Entity simplify() {
        return this;
    }

    public int getConstrainedDegreesOfFreedom() {
        return constrainedDegreesOfFreedom;
    }

    public ArrayList<Function<Entity, Property>> getConstraints() {
        return constraints;
    }

    public Function<HashMap<String, ArrayList<ArrayList<Unicardinal>>>, ArrayList<Unicardinal>> getFormula() {
        return formula;
    }

    public HashMap<String, TreeMultiset<Entity>> getInputs() {
        return inputs;
    }

    public String[] getInputTypes() {
        return Mutable.inputTypes;
    }
}


