package Core.GeoSystem.MulticardinalTypes;

import Core.EntityTypes.*;
import javafx.util.Pair;

public abstract class Multivariate extends Mutable implements Multicardinal {
    /** SECTION: Abstract Constructor =============================================================================== */
    public Multivariate(String n) {
        super(n);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Multicardinal =================================================================================== */
    public String getName() {
        return this.name;
    }
}
