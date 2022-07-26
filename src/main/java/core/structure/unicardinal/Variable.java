package core.structure.unicardinal;

import core.Diagram;
import core.Propositions.equalitypivot.unicardinal.*;
import core.structure.Mutable;
import javafx.beans.property.SimpleDoubleProperty;

import java.util.HashSet;

public abstract class Variable extends Mutable implements Unicardinal {
    /** SECTION: Static Data ======================================================================================== */
    public static final int naturalDegreesOfFreedom = 1;

    /** SECTION: Instance Variables ================================================================================= */
    public final SimpleDoubleProperty value;
    public HashSet<UnicardinalPivot<?>> computationalDependencies = new HashSet<>();

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

    public HashSet<UnicardinalPivot<?>> computationalDependencies() {
        return this.computationalDependencies;
    }

    public void deleteComputationalDependencies() {
        this.computationalDependencies = null;
    }

    /** SUBSECTION: Expression ====================================================================================== */
    public LockedUnicardinalPivot<?, ? extends Variable> expand() {
        return (LockedUnicardinalPivot<?, ? extends Variable>) this.equalityPivot;
    }

    public LockedUnicardinalPivot<?, ? extends Variable> close() {
        return (LockedUnicardinalPivot<?, ? extends Variable>) Diagram.retrieve(this);
    }

    public int getNaturalDegreesOfFreedom() {
        return Variable.naturalDegreesOfFreedom;
    }
}
