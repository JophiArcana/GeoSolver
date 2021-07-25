package Core.AlgeSystem.UnicardinalTypes;

import Core.EntityTypes.*;
import Core.Utilities.*;

import java.util.*;

public abstract class DefinedExpression<T extends Expression<T>> extends DefinedEntity implements Expression<T> {
    public final Class<T> TYPE;
    public final AlgeEngine<T> ENGINE;
    public Expression<T> expansion;

    public DefinedExpression(Class<T> type) {
        super();
        this.TYPE = type;
        this.ENGINE = Utils.getEngine(TYPE);
    }

    public ArrayList<Unicardinal> expression() {
        return Expression.super.expression();
    }

    public Expression<T> expand() {
        if (this.expansion == null) {
            this.expansion = ENGINE.expand(this.reduction());
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
