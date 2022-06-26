package core.structure.multicardinal;

import core.Diagram;
import core.structure.*;
import javafx.scene.Node;

public abstract class MultiVariable extends Mutable implements Multicardinal {
    /** SECTION: Instance Variables ================================================================================= */
    public Node node;
    public boolean anonymous = true;

    /** SECTION: Abstract Constructor =============================================================================== */
    protected MultiVariable(Diagram d, String n) {
        super(d, n);
    }

    protected MultiVariable(Diagram d, String n, boolean anon) {
        super(d, n);
        this.anonymous = anon;
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
