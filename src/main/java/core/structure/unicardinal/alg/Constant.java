package core.structure.unicardinal.alg;

import core.structure.unicardinal.alg.directed.constant.DirectedReal;
import core.structure.unicardinal.alg.structure.Real;
import core.structure.unicardinal.alg.directed.DirectedExpression;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;
import core.structure.unicardinal.alg.symbolic.constant.*;
import core.Diagram;
import core.structure.*;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;

import java.util.*;

public abstract class Constant extends Immutable implements Expression {
    /** SECTION: Static Data ======================================================================================== */
    public static final List<InputType<?>> inputTypes = null;

    public static Real ZERO(Diagram d, Class<? extends Expression> type) {
        if (type == SymbolicExpression.class) {
            return SymbolicReal.create(d, 0);
        } else if (type == DirectedExpression.class) {
            return DirectedReal.create(d, 0);
        } else {
            return null;
        }
    }

    public static Real ONE(Diagram d, Class<? extends Expression> type) {
        if (type == SymbolicExpression.class) {
            return SymbolicReal.create(d, 1);
        } else if (type == DirectedExpression.class) {
            return DirectedReal.create(d, 1);
        } else {
            return null;
        }
    }

    public static Real NONE(Diagram d, Class<? extends Expression> type) {
        if (type == SymbolicExpression.class) {
            return SymbolicReal.create(d, -1);
        } else if (type == DirectedExpression.class) {
            return DirectedReal.create(d, -1);
        } else {
            return null;
        }
    }

    public static SymbolicInfinity INFINITY(Diagram d) {
        return SymbolicInfinity.create(d);
    }

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

    public final ArrayList<ChangeListener<? super Number>> changeListeners = new ArrayList<>();
    public final ArrayList<InvalidationListener> invalidationListeners = new ArrayList<>();

    /** SECTION: Abstract Constructor =============================================================================== */
    protected Constant(Diagram d, double value) {
        super(d);
        this.value = value;
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    @Override
    public List<InputType<?>> getInputTypes() {
        return Constant.inputTypes;
    }

    /** SUBSECTION: Unicardinal ===================================================================================== */
    @Override
    public double value() {
        return this.value;
    }

    @Override
    public void computeValue() {
    }

    @Override
    public ArrayList<ChangeListener<? super Number>> getChangeListeners() {
        return this.changeListeners;
    }

    @Override
    public ArrayList<InvalidationListener> getInvalidationListeners() {
        return this.invalidationListeners;
    }

    /** SUBSECTION: Expression ====================================================================================== */
    @Override
    public Expression expand() {
        return this;
    }

    @Override
    public int getDegree() {
        return 0;
    }

    /** SECTION: Basic Operations =================================================================================== */
    public abstract Constant add(Constant x);

    public abstract Constant sub(Constant x);

    public abstract Constant mul(Constant x);

    public abstract Constant div(Constant x);

    public abstract Constant negate();

    public abstract Constant invert();

    public abstract Constant pow(double x);

    public abstract boolean isInteger();
}
