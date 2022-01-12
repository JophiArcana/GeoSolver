package Core.AlgSystem.UnicardinalTypes;

import Core.AlgSystem.UnicardinalRings.DirectedAngle;
import Core.AlgSystem.UnicardinalRings.Symbolic;
import Core.EntityTypes.Mutable;

import java.util.*;

public class Univariate<T> extends Mutable implements Expression<T> {
    /** SECTION: Static Data ======================================================================================== */
    public static final int naturalDegreesOfFreedom = 1;

    /** SECTION: Instance Variables ================================================================================= */
    public final Class<T> TYPE;

    /** SECTION: Factory Methods ==================================================================================== */
    public static <T> Univariate<T> create(String n, Class<T> type) {
        return new Univariate<>(n, type);
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected Univariate(String n, Class<T> type) {
        super(n);
        this.TYPE = type;
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public ArrayList<Expression<Symbolic>> symbolic() {
        if (this.TYPE == Symbolic.class) {
            return new ArrayList<>(List.of((Univariate<Symbolic>) this));
        } else if (this.TYPE == DirectedAngle.class) {
            return new ArrayList<>(List.of(new Univariate<>(this.name + "\u209C", Symbolic.class)));
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

    public int signum(Univariate<T> var) {
        if (this.name.equals(var.name)) {
            return 1;
        } else {
            return 0;
        }
    }

    public int getDegree() {
        if (this.name.charAt(this.name.length() - 1) == '\u209C') {
            return 0;
        } else {
            return 1;
        }
    }

    public int getNaturalDegreesOfFreedom() {
        return Univariate.naturalDegreesOfFreedom;
    }

    public Class<T> getType() {
        return this.TYPE;
    }

}
