package core.structure.unicardinal.alg;

import core.Diagram;
import core.structure.Mutable;
import core.structure.unicardinal.Unicardinal;
import javafx.beans.property.SimpleDoubleProperty;

import java.util.HashSet;

public abstract class Variable extends Mutable implements Expression {
    /** SECTION: Static Data ======================================================================================== */
    public static final int naturalDegreesOfFreedom = 1;

    /** SECTION: Instance Variables ================================================================================= */
    public SimpleDoubleProperty value;
    public HashSet<Unicardinal> reverseComputationalDependencies = new HashSet<>();

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

    public HashSet<Unicardinal> reverseComputationalDependencies() {
        return this.reverseComputationalDependencies;
    }

    /** SUBSECTION: Expression ====================================================================================== */
    public Expression expand() {
        return this;
    }

    public Expression close() {
        return Diagram.retrieve(this);
    }

    public int getNaturalDegreesOfFreedom() {
        return Variable.naturalDegreesOfFreedom;
    }
}
