package core.structure.multicardinal;

import core.Diagram;
import core.structure.*;
import javafx.scene.Node;

public abstract class MultiConstant extends Immutable implements Multicardinal {
    /** SECTION: Instance Variables ================================================================================= */
    public String name;
    public Node node;
    public boolean anonymous = true;

    /** SECTION: Abstract Constructor =============================================================================== */
    protected MultiConstant(Diagram d, String n) {
        super(d);
        assert Entity.nameSet.add(n): "Name " + n + " already in use.";
        this.name = n;
    }

    protected MultiConstant(Diagram d, String n, boolean anon) {
        this(d, n);
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
