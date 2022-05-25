package Core.AlgSystem.Operators.AddReduction;

import Core.AlgSystem.Constants.Real;
import Core.AlgSystem.Operators.*;
import Core.AlgSystem.Operators.MulReduction.Pow;
import Core.AlgSystem.UnicardinalRings.*;
import Core.EntityStructure.Entity;
import Core.EntityStructure.UnicardinalStructure.*;
import Core.Utilities.*;

import java.util.*;

public class Scale<T> extends Accumulation<T> {
    /** SECTION: Factory Methods ==================================================================================== */
    public static <T> Expression<T> create(double c, Expression<T> expr, Class<T> type) {
        return new Scale<>(c, expr, type).close();
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected Scale(double coefficient, Expression<T> expr, Class<T> type) {
        super(coefficient, expr, type);
    }

    /** SECTION: Print Format ======================================================================================= */
    public String toString() {
        return "" + this.coefficient + this.expression;
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public ArrayList<Expression<Symbolic>> symbolic() {
        if (this.TYPE == Symbolic.class) {
            return new ArrayList<>(List.of((Expression<Symbolic>) this));
        } else if (this.TYPE == DirectedAngle.class) {
            if (this.coefficient % 1 == 0) {
                int n = (int) this.coefficient;
                int k = Math.abs(n);
                Expression<Symbolic> t = this.expression.symbolic().get(0);
                ArrayList<Expression<Symbolic>> numeratorTerms = new ArrayList<>();
                ArrayList<Expression<Symbolic>> denominatorTerms = new ArrayList<>(List.of(Constant.ONE(Symbolic.class)));
                for (int i = 1; i <= k; i++) {
                    Expression<Symbolic> expr = Scale.create(Utils.binomial(k, i), Pow.create(t, i, Symbolic.class), Symbolic.class);
                    switch (i % 4) {
                        case 0:
                            denominatorTerms.add(expr);
                        case 1:
                            numeratorTerms.add(expr);
                        case 2:
                            denominatorTerms.add(Scale.create(-1, expr, Symbolic.class));
                        case 3:
                            numeratorTerms.add(Scale.create(-1, expr, Symbolic.class));
                    }
                }
                Expression<Symbolic> result = Utils.getEngine(Symbolic.class).div(
                        Add.create(numeratorTerms, Symbolic.class),
                        Add.create(denominatorTerms, Symbolic.class)
                );
                return new ArrayList<>(List.of((n > 0) ? result : Scale.create(-1, result, Symbolic.class)));
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /** SUBSECTION: Expression ====================================================================================== */
    public Expression<T> reduce() {
        if (this.reduction == null) {
            this.reduction = Scale.create(this.coefficient, this.expression.reduce(), TYPE);
        }
        return this.reduction;
    }

    public Expression<T> expand() {
        if (this.expansion == null) {
            Expression<T> expr = this.expression.expand();
            if (expr instanceof Add<T> addExpr) {
                this.expansion = Add.create(Utils.map(Utils.<Entity, Expression<T>>cast(addExpr.inputs.get(Reduction.Parameter.TERMS)),
                        arg -> Scale.create(this.coefficient, arg, TYPE)), TYPE);
            } else {
                this.expansion = Scale.create(this.coefficient, expr, TYPE);
            }
        }
        return this.expansion;
    }

    public int getDegree() {
        if (this.TYPE == Symbolic.class) {
            return this.expression.getDegree();
        } else {
            return 0;
        }
    }

    /** SUBSECTION: Multiplicity ==================================================================================== */
    protected Real<T> identity() {
        return Constant.ZERO(TYPE);
    }

    protected Constant<T> evaluateConstant(double c, Constant<T> e) {
        return e.mul(Real.create(c, TYPE));
    }
}



















