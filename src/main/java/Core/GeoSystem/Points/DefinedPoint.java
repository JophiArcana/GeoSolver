package Core.GeoSystem.Points;

import Core.EntityTypes.*;
import Core.Utilities.Utils;

import java.util.*;

public abstract class DefinedPoint extends DefinedEntity implements Point {
    public String toString() {
        ArrayList<String> allInputs = new ArrayList<>(0);
        for (String inputType : this.getInputTypes()) {
            for (Entity ent : this.getInputs().get(inputType)) {
                allInputs.add(ent.toString());
            }
        }
        return Utils.className(this) + "(" + String.join(", ", allInputs.toArray(new String[0])) + ")";
    }
}
