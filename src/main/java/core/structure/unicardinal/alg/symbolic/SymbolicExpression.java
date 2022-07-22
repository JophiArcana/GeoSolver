package core.structure.unicardinal.alg.symbolic;

import core.structure.equalitypivot.EqualityPivot;
import core.structure.equalitypivot.LockedEqualityPivot;
import core.structure.unicardinal.alg.symbolic.constant.SymbolicReal;
import core.structure.unicardinal.Unicardinal;
import core.structure.unicardinal.alg.symbolic.operator.SymbolicAdd;
import core.structure.unicardinal.alg.symbolic.operator.SymbolicScale;

import java.util.*;

public interface SymbolicExpression extends Unicardinal {
    /** SECTION: Implementation ===================================================================================== */
    default List<EqualityPivot<SymbolicExpression>> symbolic() {
        return List.of((EqualityPivot<SymbolicExpression>) this.getEqualityPivot());
    }

    /** SUBSECTION: Expression ====================================================================================== */
    default LockedEqualityPivot<SymbolicExpression, SymbolicReal> createReal(double value) {
        return SymbolicReal.create(value);
    }

    default EqualityPivot<SymbolicExpression> createAdd(Collection<? extends EqualityPivot<? extends Unicardinal>> args) {
        return SymbolicAdd.create((Collection<EqualityPivot<SymbolicExpression>>) args);
    }

    default EqualityPivot<SymbolicExpression> createScale(double coefficient, EqualityPivot<? extends Unicardinal> expr) {
        return SymbolicScale.create(coefficient, (EqualityPivot<SymbolicExpression>) expr);
    }
}
