package core.structure.unicardinal.alg.structure;

import core.structure.unicardinal.alg.symbolic.constant.SymbolicInfinity;
import core.Diagram;
import core.structure.unicardinal.alg.Constant;
import core.structure.unicardinal.alg.Expression;
import core.util.*;

public abstract class Real extends Constant {
    /** SECTION: Protected Constructors ============================================================================= */
    protected Real(Diagram d, double value) {
        super(d, value);
        assert !Double.isNaN(this.value): this.value;
    }

    /** SECTION: Print Format ======================================================================================= */
    public String toString() {
        return "" + this.value;
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Immutable ======================================================================================= */
    public boolean equalsZero() {
        return this.value == 0;
    }

    public boolean equalsOne() {
        return this.value == 1;
    }

    /** SUBSECTION: Expression ====================================================================================== */
    public Expression close() {
        return this;
    }

    /** SECTION: Basic Operations =================================================================================== */
    public Constant add(Constant c) {
        if (c instanceof Real re) {
            return this.createReal(this.value + re.value);
        } else if (c instanceof SymbolicInfinity inf) {
            if (inf.degree > 0) {
                return inf;
            } else {
                return this;
            }
        } else {
            return null;
        }
    }

    public Constant sub(Constant c) {
        if (c instanceof Real re) {
            return this.createReal(this.value - re.value);
        } else if (c instanceof SymbolicInfinity inf) {
            if (inf.degree > 0) {
                return inf.negate();
            } else {
                return this;
            }
        } else {
            return null;
        }
    }

    public Constant mul(Constant c) {
        if (c instanceof Real re) {
            return this.createReal(this.value * re.value);
        } else if (c instanceof SymbolicInfinity inf) {
            return SymbolicInfinity.create(this.diagram, this.value * inf.coefficient, inf.degree);
        } else {
            return this;
        }
    }

    public Constant div(Constant c) {
        if (c instanceof Real re) {
            if (re.value == 0) {
                return SymbolicInfinity.create(this.diagram, this.value, 1);
            } else {
                return this.createReal(this.value / re.value);
            }
        } else if (c instanceof SymbolicInfinity inf) {
            return SymbolicInfinity.create(this.diagram, this.value / inf.coefficient, -inf.degree);
        } else {
            return null;
        }
    }

    public Constant negate() {
        return this.createReal(-this.value);
    }

    public Constant invert() {
        if (this.value == 0) {
            return SymbolicInfinity.create(this.diagram);
        } else {
            return this.createReal(1 / this.value);
        }
    }

    public Constant pow(double x) {
        if (this.value == 0) {
            if (x > 0) {
                return this;
            } else {
                return SymbolicInfinity.create(this.diagram, 1, -x);
            }
        } else {
            return this.createReal(Math.pow(this.value, x));
        }
    }

    public Constant gcd(Constant c) {
        if (c instanceof Real re) {
            return this.createReal(Utils.gcd(this.value, re.value));
        } else if (c instanceof SymbolicInfinity) {
            return this;
        } else {
            return null;
        }
    }

    public boolean isInteger() {
        return this.value % 1 == 0;
    }
}
