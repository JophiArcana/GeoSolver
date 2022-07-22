package core.structure.unicardinal;

import core.Diagram;
import core.structure.Mutable;
import core.structure.equalitypivot.EqualityPivot;
import javafx.beans.property.SimpleDoubleProperty;

import java.util.HashSet;

public abstract class Variable extends Mutable implements Unicardinal {
    /** SECTION: Static Data ======================================================================================== */
    public static final int naturalDegreesOfFreedom = 1;

    /** SECTION: Instance Variables ================================================================================= */
    public final SimpleDoubleProperty value;
    public final HashSet<EqualityPivot<? extends Unicardinal>> computationalDependencies = new HashSet<>();

    /** SECTION: Protected Constructors ============================================================================= */
    protected Variable(String n, double value) {
        super(n);
        this.value = new SimpleDoubleProperty(value);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Unicardinal ===================================================================================== */
    public SimpleDoubleProperty valueProperty() {
        return this.value;
    }

    public void computeValue() {
    }

    public HashSet<EqualityPivot<? extends Unicardinal>> computationalDependencies() {
        return this.computationalDependencies;
    }

    /** SUBSECTION: Expression ====================================================================================== */
    public EqualityPivot<? extends Unicardinal> expand() {
        return Diagram.retrieve(this);
    }

    public EqualityPivot<? extends Unicardinal> close() {
        return Diagram.retrieve(this);
    }

    public int getNaturalDegreesOfFreedom() {
        return Variable.naturalDegreesOfFreedom;
    }
}
