package Core.AlgeSystem.ExpressionTypes;

import Core.EntityTypes.*;
import Core.Utilities.*;
import com.google.common.collect.TreeMultiset;

import java.util.*;

public abstract class DefinedExpression extends DefinedEntity implements Expression {
    public Expression expansion;

    public ArrayList<Expression> expression() {
        return Expression.super.expression();
    }

    public Expression expand() {
        if (this.expansion == null) {
            this.expansion = AlgeEngine.expand(this.reduction());
        }
        return this.expansion;
    }
}
