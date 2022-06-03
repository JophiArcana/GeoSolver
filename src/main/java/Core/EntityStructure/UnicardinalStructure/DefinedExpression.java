package Core.EntityStructure.UnicardinalStructure;

import Core.Diagram;
import Core.EntityStructure.*;

public abstract class DefinedExpression<T> extends DefinedEntity implements Expression<T> {
    /** SECTION: Instance Variables ================================================================================= */
    public final Class<T> TYPE;
    public Expression<T> reduction, expansion;

    /** SECTION: Abstract Constructor =============================================================================== */
    public DefinedExpression(Diagram d, Class<T> type) {
        super(d);
        this.TYPE = type;
    }

    /** SECTION: Implementation ===================================================================================== */
    public Class<T> getType() {
        return this.TYPE;
    }
}
