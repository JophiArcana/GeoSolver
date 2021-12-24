package Core.AlgSystem.UnicardinalTypes;

import Core.EntityTypes.*;
import Core.Utilities.*;

public abstract class DefinedExpression<T extends Expression<T>> extends DefinedEntity implements Expression<T> {
    /** SECTION: Instance Variables ================================================================================= */
    public final Class<T> TYPE;
    public final AlgEngine<T> ENGINE;
    public Expression<T> reduction, expansion;

    /** SECTION: Abstract Constructor =============================================================================== */
    public DefinedExpression(Class<T> type) {
        super();
        this.TYPE = type;
        this.ENGINE = Utils.getEngine(TYPE);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Expression ====================================================================================== */
    public Expression<T> reduce() {
        if (this.reduction == null) {
            this.reduction = ENGINE.reduce(this);
        }
        return this.reduction;
    }

    public Expression<T> expand() {
        if (this.expansion == null) {
            this.expansion = ENGINE.expand(this);
        }
        return this.expansion;
    }

    public Class<T> getType() {
        return this.TYPE;
    }

    public AlgEngine<T> getEngine() {
        return this.ENGINE;
    }
}
