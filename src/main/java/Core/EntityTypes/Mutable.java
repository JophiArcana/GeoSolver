package Core.EntityTypes;

import Core.AlgeSystem.Expression;
import Core.Property;
import Core.Utilities.Utils;
import com.google.common.collect.TreeMultiset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.function.Function;

public abstract class Mutable implements Entity {
    public static final String[] inputTypes = new String[] {"Variable"};
    public static final Function<HashMap<String, ArrayList<ArrayList<Expression>>>, ArrayList<Expression>> formula = args ->
            args.get("Variable").get(0);
    public abstract int getNaturalDegreesOfFreedom();

    public int constrainedDegreesOfFreedom;
    public ArrayList<Function<Entity, Property>> constraints = new ArrayList<>();
    public HashMap<String, TreeMultiset<Entity>> inputs = new HashMap<>();
    public String name;

    public Mutable(String n) {
        this.name = n;
        this.constrainedDegreesOfFreedom = getNaturalDegreesOfFreedom();
        for (String inputType : getInputTypes()) {
            inputs.put(inputType, TreeMultiset.create(Utils.PRIORITY_COMPARATOR));
        }
    }

    public String toString() {
        return this.name;
    }

    public Entity simplify() {
        return this;
    }

    public int compareTo(Entity ent) {
        if (this.getClass() != ent.getClass()) {
            return Integer.MIN_VALUE;
        } else {
            return name.compareTo(((Mutable) ent).getName());
        }
    }

    public int getConstrainedDegreesOfFreedom() {
        return constrainedDegreesOfFreedom;
    }

    public ArrayList<Function<Entity, Property>> getConstraints() {
        return constraints;
    }

    public Function<HashMap<String, ArrayList<ArrayList<Expression>>>, ArrayList<Expression>> getFormula() {
        return formula;
    }

    public HashMap<String, TreeMultiset<Entity>> getInputs() {
        return inputs;
    }

    public String[] getInputTypes() {
        return Mutable.inputTypes;
    }

    public String getName() {
        return name;
    }
}
