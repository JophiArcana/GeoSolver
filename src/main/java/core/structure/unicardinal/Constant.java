package core.structure.unicardinal;

import core.Diagram;
import core.Propositions.equalitypivot.unicardinal.*;
import core.structure.*;
import javafx.beans.property.SimpleDoubleProperty;

import java.util.*;

public abstract class Constant extends Immutable implements Unicardinal {
    /** SECTION: Static Data ======================================================================================== */
    public static final List<InputType<?>> inputTypes = null;

    /** SECTION: Instance Variables ================================================================================= */
    public final double value;
    public HashSet<UnicardinalPivot<?>> computationalDependencies = new HashSet<>();

    /** SECTION: Abstract Constructor =============================================================================== */
    protected Constant(double value) {
        super();
        this.value = value;
    }

    /** SECTION: Print Format ======================================================================================= */
    public String toString() {
        return "" + this.value;
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public List<InputType<?>> getInputTypes() {
        return Constant.inputTypes;
    }

    /** SUBSECTION: Unicardinal ===================================================================================== */
    public SimpleDoubleProperty valueProperty() {
        return new SimpleDoubleProperty(this.value);
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
    public LockedUnicardinalPivot<?, ? extends Constant> expand() {
        return (LockedUnicardinalPivot<?, ? extends Constant>) this.equalityPivot;
    }

    public LockedUnicardinalPivot<?, ? extends Constant> close() {
        return (LockedUnicardinalPivot<?, ? extends Constant>) Diagram.retrieve(this);
    }

    public int getDegree() {
        return 0;
    }
}



