package Core.EntityStructure.UnicardinalStructure;

import Core.AlgSystem.UnicardinalRings.DirectedAngle;
import Core.AlgSystem.UnicardinalRings.Symbolic;
import Core.EntityStructure.Mutable;

import java.util.*;

public class Variable<T> extends Mutable implements Expression<T> {
    /** SECTION: Static Data ======================================================================================== */
    public static final int naturalDegreesOfFreedom = 1;

    /** SECTION: Instance Variables ================================================================================= */
    public final Class<T> TYPE;

    /** SECTION: Factory Methods ==================================================================================== */
    public static <T> Variable<T> create(String n, Class<T> type) {
        return new Variable<>(n, type);
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected Variable(String n, Class<T> type) {
        super(n);
        this.TYPE = type;
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public ArrayList<Expression<Symbolic>> symbolic() {
        if (this.TYPE == Symbolic.class) {
            return new ArrayList<>(List.of((Variable<Symbolic>) this));
        } else if (this.TYPE == DirectedAngle.class) {
            return new ArrayList<>(List.of(new Variable<>(this.name + "\u209C", Symbolic.class)));
        } else {
            return null;
        }
    }

    /** SUBSECTION: Expression ====================================================================================== */
    public Expression<T> reduce() {
        return this;
    }

    public Expression<T> expand() {
        return this;
    }

    public Expression<T> close() {
        return this;
    }

    public int signum(Variable<T> var) {
        if (this.name.equals(var.name)) {
            return 1;
        } else {
            return 0;
        }
    }

    public int getDegree() {
        if (this.TYPE == Symbolic.class) {
            if (this.name.charAt(this.name.length() - 1) == '\u209C') {
                return 0;
            } else {
                return 1;
            }
        } else {
            return 0;
        }
    }

    public int getNaturalDegreesOfFreedom() {
        return Variable.naturalDegreesOfFreedom;
    }

    public Class<T> getType() {
        return this.TYPE;
    }

}
