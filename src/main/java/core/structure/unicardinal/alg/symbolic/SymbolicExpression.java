package core.structure.unicardinal.alg.symbolic;

import core.Propositions.equalitypivot.EqualityPivot;
import core.Propositions.equalitypivot.unicardinal.LockedUnicardinalPivot;
import core.Propositions.equalitypivot.unicardinal.UnicardinalPivot;
import core.structure.unicardinal.Unicardinal;
import core.structure.unicardinal.alg.symbolic.operator.SymbolicAdd;
import core.structure.unicardinal.alg.symbolic.operator.SymbolicScale;

import java.util.*;

public interface SymbolicExpression extends Unicardinal {
    /** SECTION: Implementation ===================================================================================== */
    default List<UnicardinalPivot<SymbolicExpression>> symbolic() {
        return List.of((UnicardinalPivot<SymbolicExpression>) this.getEqualityPivot());
    }

    /** SUBSECTION: Expression ====================================================================================== */
    default LockedUnicardinalPivot<SymbolicExpression, SymbolicConstant> createConstant(double value) {
        return SymbolicConstant.create(value);
    }

    default UnicardinalPivot<SymbolicExpression> createAdd(Collection<? extends UnicardinalPivot<?>> args) {
        return SymbolicAdd.create((Collection<UnicardinalPivot<SymbolicExpression>>) args);
    }

    default UnicardinalPivot<SymbolicExpression> createScale(double coefficient, UnicardinalPivot<?> expr) {
        return SymbolicScale.create(coefficient, (UnicardinalPivot<SymbolicExpression>) expr);
    }
}
