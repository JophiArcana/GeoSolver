package Core.GeoSystem;

import Core.EntityTypes.DefinedEntity;
import Core.EntityTypes.Entity;
import Core.Utilities.Utils;

import java.util.ArrayList;

public abstract class DefinedMultivariate extends DefinedEntity implements Multivariate {
    public String name;

    public DefinedMultivariate(String n) {
        this.name = n;
    }

    public String toString() {
        ArrayList<String> allInputs = new ArrayList<>(0);
        for (String inputType : this.getInputTypes()) {
            for (Entity ent : this.getInputs().get(inputType)) {
                allInputs.add(ent.toString());
            }
        }
        return Utils.className(this) + "(" + String.join(", ", allInputs.toArray(new String[0])) + ")";
    }

    public String getName() {
        return this.name;
    }
}