package core.structure.unicardinal.alg;

import core.structure.*;
import core.structure.unicardinal.Unicardinal;
import javafx.beans.property.SimpleDoubleProperty;

import java.util.HashSet;

public abstract class DefinedExpression extends DefinedEntity implements Expression {
    /** SECTION: Instance Variables ================================================================================= */
    public Expression expansion;
    public SimpleDoubleProperty value = new SimpleDoubleProperty();
    public HashSet<Unicardinal> reverseComputationalDependencies = new HashSet<>();

    /** SECTION: Abstract Constructor =============================================================================== */
    protected DefinedExpression() {
        super();
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Unicardinal ===================================================================================== */
    public SimpleDoubleProperty valueProperty() {
        return this.value;
    }

    public HashSet<Unicardinal> reverseComputationalDependencies() {
        return this.reverseComputationalDependencies;
    }
}
