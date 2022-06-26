package core.structure.unicardinal.alg.symbolic.constant;

import core.structure.unicardinal.alg.structure.Real;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;
import core.Diagram;
import core.structure.*;
import core.structure.unicardinal.alg.*;
import core.util.Utils;

public class SymbolicInfinity extends Constant implements SymbolicExpression {
    /** SECTION: Static Data ======================================================================================== */
    public static final double INFINITY_VALUE = Math.sqrt(Double.MAX_VALUE);

    /** SECTION: Instance Variables ================================================================================= */
    public final double coefficient, degree;

    /** SECTION: Factory Methods ==================================================================================== */
    public static SymbolicInfinity create(Diagram d) {
        return new SymbolicInfinity(d, 1, 1);
    }

    public static Constant create(Diagram d, double coefficient, double degree) {
        return (Constant) new SymbolicInfinity(d, coefficient, degree).close();
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected SymbolicInfinity(Diagram d, double coefficient, double degree) {
        super(d, coefficient * Math.pow(SymbolicInfinity.INFINITY_VALUE, degree));
        this.coefficient = coefficient;
        this.degree = degree;
    }

    /** SECTION: Print Format ======================================================================================= */
    public String toString() {
        return "Infinity(" + this.coefficient + " * \u221E ** " + this.degree + ")";
    }

    /** SECTION: Implementation ===================================================================================== */


    /** SUBSECTION: Entity ========================================================================================== */
    public Entity simplify() {
        if (this.degree == 0 || this.coefficient == 0) {
            return new SymbolicReal(this.diagram, this.coefficient);
        } else {
            return this;
        }
    }

    /** SUBSECTION: Expression ====================================================================================== */
    public Expression close() {
        if (this.degree == 0 || this.coefficient == 0) {
            return new SymbolicReal(this.diagram, this.coefficient);
        } else {
            return this;
        }
    }

    /** SECTION: Basic Operations =================================================================================== */
    public Constant add(Constant x) {
        if (x instanceof SymbolicInfinity inf) {
            if (inf.degree > this.degree) {
                return inf;
            } else if (inf.degree == this.degree) {
                return SymbolicInfinity.create(this.diagram, this.coefficient + inf.coefficient, this.degree);
            } else {
                return this;
            }
        } else {
            if (this.degree > 0) {
                return this;
            } else {
                return x;
            }
        }
    }

    public Constant sub(Constant x) {
        if (x instanceof SymbolicInfinity inf) {
            if (inf.degree > this.degree) {
                return inf.negate();
            } else if (inf.degree == this.degree) {
                return SymbolicInfinity.create(this.diagram, this.coefficient - inf.coefficient, this.degree);
            } else {
                return this;
            }
        } else {
            if (this.degree > 0) {
                return this;
            } else {
                return x.negate();
            }
        }
    }

    public Constant mul(Constant x) {
        if (x instanceof Real re) {
            return SymbolicInfinity.create(this.diagram, this.coefficient * re.value, this.degree);
        } else if (x instanceof SymbolicInfinity inf) {
            return SymbolicInfinity.create(this.diagram, this.coefficient * inf.coefficient, this.degree + inf.degree);
        } else {
            return null;
        }
    }

    public Constant div(Constant x) {
        if (x instanceof Real re) {
            if (re.value == 0) {
                return SymbolicInfinity.create(this.diagram, this.coefficient, this.degree + 1);
            } else {
                return SymbolicInfinity.create(this.diagram, this.coefficient / re.value, this.degree);
            }
        } else if (x instanceof SymbolicInfinity inf) {
            return SymbolicInfinity.create(this.diagram, this.coefficient / inf.coefficient, this.degree - inf.degree);
        } else {
            return null;
        }
    }

    public Constant negate() {
        return new SymbolicInfinity(this.diagram, -this.coefficient, this.degree);
    }

    public Constant invert() {
        return new SymbolicInfinity(this.diagram, 1 / this.coefficient, -this.degree);
    }

    public Constant pow(double x) {
        return SymbolicInfinity.create(this.diagram, Math.pow(this.coefficient, x), this.degree * x);
    }

    public Constant gcd(Constant c) {
        if (c instanceof SymbolicInfinity inf) {
            return SymbolicInfinity.create(this.diagram, Utils.gcd(this.coefficient, inf.coefficient), Math.min(this.degree, inf.degree));
        } else if (c instanceof Real) {
            return c;
        } else {
            return null;
        }
    }

    public boolean isInteger() {
        return false;
    }
}
