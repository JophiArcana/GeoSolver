package Core.AlgSystem.Operators;

import Core.AlgSystem.UnicardinalRings.*;
import Core.AlgSystem.UnicardinalTypes.*;
import Core.EntityTypes.Entity;
import Core.Utilities.*;

import java.util.*;

public class Pow<T> extends DefinedExpression<T> {
    /** SECTION: Static Data ======================================================================================== */
    public enum Parameter implements InputType {
        BASE,
        EXPONENT
    }
    public static final InputType[] inputTypes = {Parameter.BASE, Parameter.EXPONENT};

    /** SECTION: Instance Variables ================================================================================= */
    public Expression<T> base;
    public double exponent;

    /** SECTION: Factory Methods ==================================================================================== */
    public static <T> Expression<T> create(Expression<T> base, Complex<T> exponent, Class<T> type) {
        return new Pow<>(base, exponent.re, type).close();
    }

    public static <T> Expression<T> create(Expression<T> base, double exponent, Class<T> type) {
        return new Pow<>(base, exponent, type).close();
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected Pow(Expression<T> base, double exponent, Class<T> type) {
        super(type);
        if (base instanceof Pow<T> powExpr) {
            this.base = powExpr.base;
            this.exponent = powExpr.exponent * exponent;
        } else if (base instanceof Constant<T> constExpr) {
            this.base = constExpr.pow(exponent);
            this.exponent = 1;
        } else {
            this.base = base;
            this.exponent = exponent;
        }
        this.inputs.get(Parameter.BASE).add(this.base);
        this.inputs.get(Parameter.EXPONENT).add(Complex.create(this.exponent, 0, TYPE));
    }

    /** SECTION: Print Format ======================================================================================= */
    public String toString() {
        String baseString = this.base.toString();
        if (!Utils.CLOSED_FORM.contains(this.base.getClass())) {
            baseString = "(" + baseString + ")";
        }
        if (this.exponent % 1 == 0) {
            return baseString + " ** " + (int) this.exponent;
        } else {
            return baseString + " ** " + this.exponent;
        }
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public ArrayList<Expression<Symbolic>> symbolic() {
        if (this.TYPE == Symbolic.class) {
            return new ArrayList<>(List.of((Pow<Symbolic>) this));
        } else if (this.TYPE == DirectedAngle.class) {
            return null;
        } else {
            return null;
        }
    }

    public InputType[] getInputTypes() {
        return Pow.inputTypes;
    }

    /** SUBSECTION: Expression ====================================================================================== */
    public Expression<T> reduce() {
        if (this.reduction == null) {
            this.reduction = Pow.create(this.base.reduce(), this.exponent, TYPE);
        }
        return this.reduction;
    }

    public Expression<T> expand() {
        if (this.expansion == null) {
            if (this.base instanceof Mul<T> mulExpr) {
                this.expansion = Mul.create(Utils.map(Utils.<Entity, Expression<T>>cast(mulExpr.inputs.get(Mul.Parameter.TERMS)),
                        arg -> Pow.create(arg, this.exponent, TYPE)), TYPE).expand();
            } else if (this.base instanceof Scale<T> scaleExpr) {
                this.expansion = Scale.create(scaleExpr.coefficient.pow(this.exponent),
                        Pow.create(scaleExpr.expression, this.exponent, TYPE).expand(), TYPE);
            } else if (this.exponent >= 1 && this.base instanceof Add<T> addExpr) {
                ArrayList<Expression<T>> expandedTerms = this.expandHelper(Utils.cast(addExpr.inputs.get(Accumulation.Parameter.TERMS)), (int) this.exponent);
                this.expansion = Add.create(expandedTerms, TYPE);
                if (this.exponent % 1 != 0) {
                    this.expansion = Mul.create(List.of(Pow.create(this.base, this.exponent % 1, TYPE), this.expansion), TYPE);
                }
            } else {
                this.expansion = this;
            }
        }
        return this.expansion;
    }

    private ArrayList<Expression<T>> expandHelper(ArrayList<Expression<T>> terms, int n) {
        if (n == 1) {
            return terms;
        } else {
            ArrayList<Expression<T>> sqrt = expandHelper(terms, n / 2);
            ArrayList<Expression<T>> result = new ArrayList<>();
            for (int i = 0; i < sqrt.size(); i++) {
                for (int j = 0; j < i; j++) {
                    result.add(Scale.create(2, Mul.create(List.of(sqrt.get(i), sqrt.get(j)), TYPE), TYPE));
                }
                result.add(Pow.create(sqrt.get(i), 2, TYPE));
            }
            if (n % 2 == 0) {
                return result;
            } else {
                ArrayList<Expression<T>> newResult = new ArrayList<>();
                for (Expression<T> term : terms) {
                    result.forEach(arg -> newResult.add(Mul.create(List.of(term, arg), TYPE)));
                }
                return newResult;
            }
        }
    }

    public Expression<T> close() {
        if (this.exponent == 1 || this.base.equalsZero() || this.base.equalsOne()) {
            return this.base;
        } else if (this.exponent == 0) {
            return Constant.ONE(TYPE);
        } else {
            return this;
        }
    }

    public int getDegree() {
        if (this.TYPE == Symbolic.class) {
            return (int) (this.exponent * this.base.getDegree());
        } else {
            return 0;
        }
    }
}

