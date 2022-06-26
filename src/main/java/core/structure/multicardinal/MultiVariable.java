package core.structure.multicardinal;

import core.structure.*;
import javafx.scene.Node;

public abstract class MultiVariable extends Mutable implements Multicardinal {
    /** SECTION: Instance Variables ================================================================================= */
    public Node node;
    public boolean anonymous = true;

    /** SECTION: Abstract Constructor =============================================================================== */
    protected MultiVariable(String n) {
        super(n);
    }

    protected MultiVariable(String n, boolean anon) {
        super(n);
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
