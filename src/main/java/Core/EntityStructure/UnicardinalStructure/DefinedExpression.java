package Core.EntityStructure.UnicardinalStructure;

import Core.EntityStructure.*;
import Core.Utilities.*;

public abstract class DefinedExpression<T> extends DefinedEntity implements Expression<T> {
    /** SECTION: Instance Variables ================================================================================= */
    public final Class<T> TYPE;
    public Expression<T> reduction, expansion;

    /** SECTION: Abstract Constructor =============================================================================== */
    public DefinedExpression(Class<T> type) {
        super();
        this.TYPE = type;
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Expression ====================================================================================== */
    public boolean equals(Entity ent) {
        if (ent instanceof Expression) {
            return Utils.getEngine(this.getType()).sub(this, (Expression<T>) ent).expressionSimplify().equalsZero();
        } else {
            return false;
        }
    }

    public Class<T> getType() {
        return this.TYPE;
    }

}
