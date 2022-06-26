package core.structure.unicardinal.alg;

import core.Diagram;
import core.structure.*;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;

import java.util.ArrayList;

public abstract class DefinedExpression extends DefinedEntity implements Expression {
    /** SECTION: Instance Variables ================================================================================= */
    public Expression expansion;
    public double value;

    public final ArrayList<ChangeListener<? super Number>> changeListeners = new ArrayList<>();
    public final ArrayList<InvalidationListener> invalidationListeners = new ArrayList<>();

    /** SECTION: Abstract Constructor =============================================================================== */
    protected DefinedExpression(Diagram d) {
        super(d);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Unicardinal ===================================================================================== */
    @Override
    public double value() {
        return this.value;
    }

    @Override
    public ArrayList<ChangeListener<? super Number>> getChangeListeners() {
        return this.changeListeners;
    }

    @Override
    public ArrayList<InvalidationListener> getInvalidationListeners() {
        return this.invalidationListeners;
    }
}
