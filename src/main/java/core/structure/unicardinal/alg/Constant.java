package core.structure.unicardinal.alg;

import core.structure.unicardinal.Unicardinal;
import core.structure.unicardinal.alg.symbolic.constant.*;
import core.structure.*;
import javafx.beans.property.SimpleDoubleProperty;

import java.util.*;

public abstract class Constant extends Immutable implements Expression {
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

    /** SECTION: Abstract Constructor =============================================================================== */
    protected Constant(double value) {
        super();
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
    public SimpleDoubleProperty valueProperty() {
        return new SimpleDoubleProperty(this.value);
    }

    @Override
    public void computeValue() {
    }

    @Override
    public HashSet<Unicardinal> reverseDependencies() {
        return null;
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
}
