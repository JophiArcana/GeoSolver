package Core.AlgeSystem.UnicardinalTypes;

import Core.EntityTypes.*;
import Core.Utilities.*;

public abstract class DefinedExpression<T extends Expression<T>> extends DefinedEntity implements Expression<T> {
    public final Class<T> TYPE;
    public final AlgeEngine<T> ENGINE;
    public Expression<T> reduction, expansion;

    public DefinedExpression(Class<T> type) {
        super();
        this.TYPE = type;
        this.ENGINE = Utils.getEngine(TYPE);
    }

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

    public AlgeEngine<T> getEngine() {
        return this.ENGINE;
    }
}
