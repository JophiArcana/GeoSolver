package Core.EntityTypes;

import Core.AlgeSystem.Expression;
import Core.Property;
import Core.Utilities.Utils;
import com.google.common.collect.TreeMultiset;

import java.util.*;
import java.util.function.Function;

public abstract class Immutable implements Entity {
    public static final int naturalDegreesOfFreedom = 0;

    public int constrainedDegreesOfFreedom;
    public ArrayList<Function<Entity, Property>> constraints = new ArrayList<>();
    public Function<HashMap<String, ArrayList<ArrayList<Expression>>>, ArrayList<Expression>> formula;
    public HashMap<String, TreeMultiset<Entity>> inputs = new HashMap<>();

    public Immutable() {
        this.constrainedDegreesOfFreedom = Immutable.naturalDegreesOfFreedom;
        this.formula = args -> this.expression();
        for (String inputType : this.getInputTypes()) {
            this.inputs.put(inputType, TreeMultiset.create(Utils.PRIORITY_COMPARATOR));
        }
    }

    public Entity simplify() {
        return this;
    }

    public int getNaturalDegreesOfFreedom() {
        return Immutable.naturalDegreesOfFreedom;
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

    public abstract int compareTo(Entity ent);
}
