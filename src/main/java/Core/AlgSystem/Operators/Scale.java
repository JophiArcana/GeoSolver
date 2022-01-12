package Core.AlgSystem.Operators;

import Core.AlgSystem.Constants.*;
import Core.AlgSystem.UnicardinalRings.DirectedAngle;
import Core.AlgSystem.UnicardinalRings.Symbolic;
import Core.AlgSystem.UnicardinalTypes.*;
import Core.EntityTypes.Entity;
import Core.Utilities.*;

import java.util.*;

public class Scale<T> extends DefinedExpression<T> {
    /** SECTION: Static Data ======================================================================================== */
    public enum Parameter implements InputType {
        COEFFICIENT,
        EXPRESSION
    }
    public static final InputType[] inputTypes = {Parameter.COEFFICIENT, Parameter.EXPRESSION};

    /** SECTION: Instance Variables ================================================================================= */
    public Constant<T> coefficient;
    public Expression<T> expression;

    /** SECTION: Factory Methods ==================================================================================== */
    public static <T> Expression<T> create(Constant<T> c, Expression<T> expr, Class<T> type) {
        return new Scale<>(c, expr, type).close();
    }

    public static <T> Expression<T> create(double c, Expression<T> expr, Class<T> type) {
        return new Scale<>(Complex.create(c, 0, type), expr, type).close();
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected Scale(Constant<T> c, Expression<T> expr, Class<T> type) {
        super(type);
        if (expr instanceof Constant<T> constExpr) {
            this.coefficient = Constant.ONE(TYPE);
            this.expression = c.mul(constExpr);
        } else if (expr instanceof Scale<T> scaleExpr) {
            this.coefficient = c.mul(scaleExpr.coefficient);
            this.expression = scaleExpr.expression;
        } else {
            this.coefficient = c;
            this.expression = expr;
        }
        this.inputs.get(Parameter.COEFFICIENT).add(this.coefficient);
        this.inputs.get(Parameter.EXPRESSION).add(this.expression);
    }

    /** SECTION: Print Format ======================================================================================= */
    public String toString() {
        return this.coefficient.toString() + this.expression.toString();
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public ArrayList<Expression<Symbolic>> symbolic() {
        if (this.TYPE == Symbolic.class) {
            return new ArrayList<>(List.of((Expression<Symbolic>) this));
        } else if (this.TYPE == DirectedAngle.class) {
            if (this.coefficient instanceof Complex<T> cpx && cpx.isGaussianInteger() && cpx.im.equals(0)) {
                int n = cpx.re.intValue();
                int k = Math.abs(n);
                Expression<Symbolic> t = this.expression.symbolic().get(0);
                ArrayList<Expression<Symbolic>> numeratorTerms = new ArrayList<>();
                ArrayList<Expression<Symbolic>> denominatorTerms = new ArrayList<>(List.of(Complex.ONE(Symbolic.class)));
                for (int i = 1; i <= k; i++) {
                    Expression<Symbolic> expr = Scale.create(Utils.binomial(k, i), Pow.create(t, i, Symbolic.class), Symbolic.class);
                    switch (i % 4) {
                        case 0:
                            denominatorTerms.add(expr);
                        case 1:
                            numeratorTerms.add(expr);
                        case 2:
                            denominatorTerms.add(Scale.create(Constant.NONE(Symbolic.class), expr, Symbolic.class));
                        case 3:
                            numeratorTerms.add(Scale.create(Constant.NONE(Symbolic.class), expr, Symbolic.class));
                    }
                }
                Expression<Symbolic> result = Utils.getEngine(Symbolic.class).div(
                        Add.create(numeratorTerms, Symbolic.class),
                        Add.create(denominatorTerms, Symbolic.class)
                );
                return new ArrayList<>(List.of((n > 0) ? result : Scale.create(Constant.NONE(Symbolic.class), result, Symbolic.class)));
            } else if (this.coefficient instanceof Infinity<T> inf) {
                if (Utils.getGrowthComparator(Symbolic.class).compare((Expression<Symbolic>) inf.expression, Constant.ONE(Symbolic.class)) < 0) {
                    return new ArrayList<>(List.of(Constant.ZERO(Symbolic.class)));
                }
            }
            return null;
        } else {
            return null;
        }
    }

    public InputType[] getInputTypes() {
        return Scale.inputTypes;
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
                this.expansion = Add.create(Utils.map(Utils.<Entity, Expression<T>>cast(addExpr.inputs.get(Accumulation.Parameter.TERMS)),
                        arg -> Scale.create(this.coefficient, arg, TYPE)), TYPE);
            } else {
                this.expansion = Scale.create(this.coefficient, expr, TYPE);
            }
        }
        return this.expansion;
    }

    public Expression<T> close() {
        if (this.coefficient.equalsOne()) {
            return this.expression;
        } else if (this.coefficient.equalsZero()) {
            return this.coefficient;
        } else {
            return this;
        }
    }

    public int getDegree() {
        return this.expression.getDegree();
    }
}



















