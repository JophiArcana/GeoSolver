package Core.GeoSystem.MulticardinalTypes;

import Core.EntityTypes.*;

public abstract class Multiconstant extends Immutable implements Multicardinal {
    /** SECTION: Instance Variables ================================================================================= */
    public String name;

    /** SECTION: Abstract Constructor =============================================================================== */
    public Multiconstant(String n) {
        super();
        this.name = n;
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Multicardinal =================================================================================== */
    public String getName() {
        return this.name;
    }
}
