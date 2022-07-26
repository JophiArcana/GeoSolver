package core.structure.unicardinal;

import core.Propositions.equalitypivot.unicardinal.UnicardinalPivot;
import core.structure.*;
import javafx.beans.property.SimpleDoubleProperty;

import java.util.HashSet;

public abstract class DefinedUnicardinal extends DefinedEntity implements Unicardinal {
    /** SECTION: Instance Variables ================================================================================= */
    public UnicardinalPivot<?> expansion;
    public final SimpleDoubleProperty value = new SimpleDoubleProperty();

    public HashSet<UnicardinalPivot<?>> computationalDependencies = new HashSet<>();

    /** SECTION: Abstract Constructor =============================================================================== */
    protected DefinedUnicardinal() {
        super();
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Unicardinal ===================================================================================== */
    public SimpleDoubleProperty valueProperty() {
        return this.value;
    }

    public HashSet<UnicardinalPivot<?>> computationalDependencies() {
        return this.computationalDependencies;
    }

    public void deleteComputationalDependencies() {
        this.computationalDependencies = null;
    }
}
