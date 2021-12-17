package Core.GeoSystem.MulticardinalTypes;

import Core.AlgSystem.UnicardinalTypes.Unicardinal;
import Core.EntityTypes.*;
import Core.Utilities.Utils;

import java.util.ArrayList;

public abstract class DefinedMulticardinal extends DefinedEntity implements Multicardinal {
    public String name;
    public ArrayList<Unicardinal> expression;

    public DefinedMulticardinal(String n) {
        super();
        this.name = n;
    }

    public String toString() {
        ArrayList<String> allInputs = new ArrayList<>(0);
        for (InputType inputType : this.getInputTypes()) {
            for (Entity ent : this.getInputs().get(inputType)) {
                allInputs.add(ent.toString());
            }
        }
        return Utils.className(this) + "(" + String.join(", ", allInputs.toArray(new String[0])) + ")";
    }

    public ArrayList<Unicardinal> expression() {
        if (this.expression == null) {
            this.expression = this.getExpression();
        }
        return this.expression;
    }

    protected abstract ArrayList<Unicardinal> getExpression();

    public boolean equals(Entity ent) {
        return Utils.compare(this, ent) == 0;
    }

    public String getName() {
        return this.name;
    }
}
