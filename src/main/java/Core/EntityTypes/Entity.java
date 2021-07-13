package Core.EntityTypes;

import Core.AlgeSystem.ExpressionTypes.Expression;
import Core.Property;
import Core.Utilities.Utils;
import com.google.common.collect.TreeMultiset;

import java.util.*;
import java.util.function.Function;

public interface Entity {
    Entity simplify();
    ArrayList<Expression> expression();

    int getNaturalDegreesOfFreedom();
    int getConstrainedDegreesOfFreedom();
    ArrayList<Function<Entity, Property>> getConstraints();
    Function<HashMap<String, ArrayList<ArrayList<Expression>>>, ArrayList<Expression>> getFormula();
    HashMap<String, TreeMultiset<Entity>> getInputs();
    String[] getInputTypes();

    default HashSet<Mutable> variables() {
        Entity simplified = this.simplify();
        HashSet<Mutable> vars = new HashSet<>();
        if (simplified instanceof Mutable) {
            vars.add((Mutable) this);
        } else if (simplified instanceof DefinedEntity) {
            for (TreeMultiset<Entity> input : simplified.getInputs().values()) {
                for (Entity ent : input) {
                    vars.addAll(ent.variables());
                }
            }
        }
        return vars;
    }

    default boolean equals(Entity ent) {
        return Utils.compare(this, ent) == 0;
    }
}

