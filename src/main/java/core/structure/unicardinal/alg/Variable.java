package core.structure.unicardinal.alg;

import core.structure.Mutable;
import javafx.beans.InvalidationListener;
import javafx.beans.value.*;

import java.util.*;

public abstract class Variable extends Mutable implements Expression {
    /** SECTION: Static Data ======================================================================================== */
    public static final int naturalDegreesOfFreedom = 1;

    /** SECTION: Instance Variables ================================================================================= */
    public double value;

    public final ArrayList<ChangeListener<? super Number>> changeListeners = new ArrayList<>();
    public final ArrayList<InvalidationListener> invalidationListeners = new ArrayList<>();

    /** SECTION: Protected Constructors ============================================================================= */
    protected Variable(String n, double value) {
        super(n);
        this.value = value;
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Unicardinal ===================================================================================== */
    public double value() {
        return this.value;
    }

    public void computeValue() {
    }

    public ArrayList<ChangeListener<? super Number>> getChangeListeners() {
        return this.changeListeners;
    }

    public ArrayList<InvalidationListener> getInvalidationListeners() {
        return this.invalidationListeners;
    }

    /** SUBSECTION: Expression ====================================================================================== */
    public Expression expand() {
        return this;
    }

    public Expression close() {
        return this;
    }

    public int getNaturalDegreesOfFreedom() {
        return Variable.naturalDegreesOfFreedom;
    }
}
