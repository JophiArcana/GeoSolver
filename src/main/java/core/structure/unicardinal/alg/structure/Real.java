package core.structure.unicardinal.alg.structure;

import core.Diagram;
import core.structure.equalitypivot.EqualityPivot;
import core.structure.unicardinal.Unicardinal;

public interface Real extends Unicardinal {
    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Expression ====================================================================================== */
    default EqualityPivot<? extends Unicardinal> close() {
        if (this.getEqualityPivot() == null) {
            return Diagram.retrieve(this);
        } else {
            return (EqualityPivot<? extends Unicardinal>) this.getEqualityPivot();
        }
    }
}
