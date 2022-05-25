package Core.EntityStructure.MulticardinalStructure;

import Core.EntityStructure.*;

public abstract class MultiVariable extends Mutable implements Multicardinal {
    /** SECTION: Abstract Constructor =============================================================================== */
    public MultiVariable(String n) {
        super(n);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Multicardinal =================================================================================== */
    public String getName() {
        return this.name;
    }
}
