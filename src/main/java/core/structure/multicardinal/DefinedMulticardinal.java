package core.structure.multicardinal;

import core.Diagram;
import core.structure.*;
import core.util.Utils;
import javafx.scene.Node;

import java.util.*;

public abstract class DefinedMulticardinal extends DefinedEntity implements Multicardinal {
    /** SECTION: Instance Variables ================================================================================= */
    public String name;
    public Node node;
    public boolean anonymous = true;

    /** SECTION: Abstract Constructor =============================================================================== */
    protected DefinedMulticardinal(Diagram d, String n) {
        super(d);
        assert Entity.nameSet.add(n): "Name " + n + " already in use.";
        this.name = n;
    }

    protected DefinedMulticardinal(Diagram d, String n, boolean anon) {
        this(d, n);
        this.anonymous = anon;
    }

    /** SECTION: Print Format ======================================================================================= */
    public String toString() {
        ArrayList<String> allInputs = new ArrayList<>();
        for (InputType<?> inputType : this.getInputTypes()) {
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

    public Node getNode() {
        return this.node;
    }

    public boolean isAnonymous() {
        return this.anonymous;
    }
}
