package core.structure.unicardinal;

import core.structure.*;
import core.structure.equalitypivot.EqualityPivot;
import javafx.beans.property.SimpleDoubleProperty;

import java.util.HashSet;

public abstract class DefinedUnicardinal extends DefinedEntity implements Unicardinal {
    /** SECTION: Instance Variables ================================================================================= */
    public EqualityPivot<? extends Unicardinal> expansion;
    public final SimpleDoubleProperty value = new SimpleDoubleProperty();

    public final HashSet<EqualityPivot<? extends Unicardinal>> computationalDependencies = new HashSet<>();

    /** SECTION: Abstract Constructor =============================================================================== */
    protected DefinedUnicardinal() {
        super();
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Unicardinal ===================================================================================== */
    public SimpleDoubleProperty valueProperty() {
        return this.value;
    }

    public HashSet<EqualityPivot<? extends Unicardinal>> computationalDependencies() {
        return this.computationalDependencies;
    }
}
