package Core.EntityTypes;

import Core.AlgeSystem.ExpressionTypes.Expression;
import Core.Property;
import Core.Utilities.Utils;
import com.google.common.collect.TreeMultiset;

import java.util.*;
import java.util.function.Function;

public abstract class DefinedEntity implements Entity {
    public static final int naturalDegreesOfFreedom = 0;

    public int constrainedDegreesOfFreedom;
    public ArrayList<Function<Entity, Property>> constraints = new ArrayList<>();
    public HashMap<String, TreeMultiset<Entity>> inputs = new HashMap<>();

    public DefinedEntity() {
        this.constrainedDegreesOfFreedom = DefinedEntity.naturalDegreesOfFreedom;
        for (String inputType : this.getInputTypes()) {
            this.inputs.put(inputType, TreeMultiset.create(Utils.PRIORITY_COMPARATOR));
        }
    }

    public ArrayList<Expression> expression() {
        HashMap<String, ArrayList<ArrayList<Expression>>> expressionInputs = new HashMap<>();
        for (String inputType : this.getInputTypes()) {
            expressionInputs.put(inputType, Utils.map(this.getInputs().get(inputType), Entity::expression));
        }
        return this.getFormula().apply(expressionInputs);
    }

    public int getNaturalDegreesOfFreedom() {
        return DefinedEntity.naturalDegreesOfFreedom;
    }

    public int getConstrainedDegreesOfFreedom() {
        return this.constrainedDegreesOfFreedom;
    }

    public ArrayList<Function<Entity, Property>> getConstraints() {
        return this.constraints;
    }

    public HashMap<String, TreeMultiset<Entity>> getInputs() {
        return this.inputs;
    }
}