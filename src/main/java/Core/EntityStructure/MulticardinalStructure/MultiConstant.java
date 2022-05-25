package Core.EntityStructure.MulticardinalStructure;

import Core.EntityStructure.*;

public abstract class MultiConstant extends Immutable implements Multicardinal {
    /** SECTION: Instance Variables ================================================================================= */
    public String name;

    /** SECTION: Abstract Constructor =============================================================================== */
    public MultiConstant(String n) {
        super();
        assert Entity.nameSet.add(n): "Name " + n + " already used";
        this.name = n;
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Multicardinal =================================================================================== */
    public String getName() {
        return this.name;
    }
}
