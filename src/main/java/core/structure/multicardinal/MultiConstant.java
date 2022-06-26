package core.structure.multicardinal;

import core.structure.*;
import javafx.scene.Node;

public abstract class MultiConstant extends Immutable implements Multicardinal {
    /** SECTION: Instance Variables ================================================================================= */
    public String name;
    public Node node;
    public boolean anonymous = true;

    /** SECTION: Abstract Constructor =============================================================================== */
    protected MultiConstant(String n) {
        super();
        this.name = n;
    }

    protected MultiConstant(String n, boolean anon) {
        this(n);
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
