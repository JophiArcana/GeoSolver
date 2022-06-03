package Core.EntityStructure.MulticardinalStructure;

import Core.EntityStructure.*;
import Core.Utilities.Utils;

import java.util.*;

public abstract class DefinedMulticardinal extends DefinedEntity implements Multicardinal {
    /** SECTION: Instance Variables ================================================================================= */
    public String name;

    /** SECTION: Abstract Constructor =============================================================================== */
    public DefinedMulticardinal(String n) {
        super();
        assert Entity.nameSet.add(n): "Name " + n + " already in use.";
        this.name = n;
    }

    /** SECTION: Print Format ======================================================================================= */
    public String toString() {
        ArrayList<String> allInputs = new ArrayList<>();
        for (InputType inputType : this.getInputTypes()) {
            for (Entity ent : this.getInputs().get(inputType)) {
                allInputs.add(ent.toString());
            }
        }
        return Utils.className(this) + "(" + String.join(", ", allInputs.toArray(new String[0])) + ")";
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Multicardinal =================================================================================== */
    public String getName() {
        return this.name;
    }
}
