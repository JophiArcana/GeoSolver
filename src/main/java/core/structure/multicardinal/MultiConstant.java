package core.structure.multicardinal;

import core.structure.*;
import javafx.scene.Node;

public abstract class MultiConstant extends Immutable implements Multicardinal {
    /** SECTION: Instance Variables ================================================================================= */
    public String name;
    public Node node;
    public boolean anonymous;

    /** SECTION: Abstract Constructor =============================================================================== */
    protected MultiConstant(String n, boolean anon) {
        super();
        this.name = n;
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
