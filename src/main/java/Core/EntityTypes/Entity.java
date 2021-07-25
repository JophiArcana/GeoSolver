package Core.EntityTypes;

import Core.AlgeSystem.UnicardinalTypes.Expression;
import Core.AlgeSystem.UnicardinalTypes.Unicardinal;
import Core.AlgeSystem.UnicardinalTypes.Univariate;
import Core.Property;
import Core.Utilities.Utils;
import com.google.common.collect.TreeMultiset;

import java.util.*;
import java.util.function.Function;


public interface Entity {
    boolean equals(Entity ent);

    Entity simplify();
    ArrayList<Unicardinal> expression();
    Unicardinal expression(String varType);

    int getNaturalDegreesOfFreedom();
    int getConstrainedDegreesOfFreedom();
    ArrayList<Function<Entity, Property>> getConstraints();

    Entity create(HashMap<String, ArrayList<Entity>> args);
    Function<HashMap<String, ArrayList<ArrayList<Unicardinal>>>, ArrayList<Unicardinal>> getFormula();

    HashMap<String, TreeMultiset<Entity>> getInputs();
    String[] getInputTypes();

    default TreeSet<Mutable> variables() {
        TreeSet<Mutable> vars = new TreeSet<>(Utils.PRIORITY_COMPARATOR);
        if (this instanceof Mutable var) {
            vars.add(var);
        } else if (this instanceof DefinedEntity) {
            for (TreeMultiset<Entity> input : this.getInputs().values()) {
                for (Entity ent : input) {
                    vars.addAll(ent.variables());
                }
            }
        }
        return vars;
    }
}

