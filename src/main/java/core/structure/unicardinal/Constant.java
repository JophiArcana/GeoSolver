package core.structure.unicardinal;

import core.structure.equalitypivot.EqualityPivot;
import core.structure.*;
import core.structure.unicardinal.alg.symbolic.constant.SymbolicInfinity;
import javafx.beans.property.SimpleDoubleProperty;

import java.util.*;

public abstract class Constant extends Immutable implements Unicardinal {
    /** SECTION: Static Data ======================================================================================== */
    public static final List<InputType<?>> inputTypes = null;

    public static int compare(Constant c1, Constant c2) {
        if (c1 instanceof SymbolicInfinity inf1 && c2 instanceof SymbolicInfinity inf2) {
            if (inf1.degree != inf2.degree) {
                return Double.compare(inf1.degree, inf2.degree);
            } else {
                return Double.compare(inf1.coefficient, inf2.coefficient);
            }
        } else if (c1 instanceof SymbolicInfinity) {
            return 1;
        } else if (c2 instanceof SymbolicInfinity) {
            return -1;
        } else {
            return Double.compare(c1.value, c2.value);
        }
    }

    /** SECTION: Instance Variables ================================================================================= */
    public final double value;
    public final HashSet<EqualityPivot<? extends Unicardinal>> computationalDependencies = new HashSet<>();

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

    public HashSet<EqualityPivot<? extends Unicardinal>> computationalDependencies() {
        return this.computationalDependencies;
    }

    /** SUBSECTION: Expression ====================================================================================== */
    public EqualityPivot<? extends Unicardinal> expand() {
        return (EqualityPivot<? extends Unicardinal>) this.equalityPivot;
    }

    public int getDegree() {
        return 0;
    }
}



