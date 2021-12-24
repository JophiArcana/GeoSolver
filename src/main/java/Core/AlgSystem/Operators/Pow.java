package Core.AlgSystem.Operators;

import Core.AlgSystem.UnicardinalRings.*;
import Core.AlgSystem.UnicardinalTypes.*;
import Core.EntityTypes.Entity;
import Core.Utilities.*;

import java.util.*;

public class Pow<T extends Expression<T>> extends DefinedExpression<T> {
    /** SECTION: Static Data ======================================================================================== */
    public enum Parameter implements InputType {
        BASE,
        EXPONENT
    }
    public static final InputType[] inputTypes = {Parameter.BASE, Parameter.EXPONENT};

    /** SECTION: Instance Variables ================================================================================= */
    public Expression<T> base;
    public Constant<T> exponent;

    /** SECTION: Factory Methods ==================================================================================== */
    public static <T extends Expression<T>> Expression<T> create(Expression<T> base, Constant<T> exponent, Class<T> type) {
        if (base instanceof Constant<T> baseConst) {
            return baseConst.pow(exponent);
        } else {
            return new Pow<>(base, exponent, type).close();
        }
    }

    public Entity createEntity(HashMap<InputType, ArrayList<Entity>> args) {
        Expression<T> base = (Expression<T>) args.get(Parameter.BASE).get(0);
        Constant<T> exponent = (Constant<T>) args.get(Parameter.EXPONENT).get(0);
        return Pow.create(base, exponent, TYPE);
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected Pow(Expression<T> base, Constant<T> exponent, Class<T> type) {
        super(type);
        if (base instanceof Pow) {
            this.base = ((Pow<T>) base).base;
            this.exponent = ((Pow<T>) base).exponent.mul(exponent);
        } else {
            this.base = base;
            this.exponent = exponent;
        }
        this.inputs.get(Parameter.BASE).add(this.base);
        this.inputs.get(Parameter.EXPONENT).add(this.exponent);
        // System.out.println(base + " and " + exponent + " Pow constructed");
    }

    /** SECTION: Print Format ======================================================================================= */
    public String toString() {
        String baseString = this.base.toString();
        String exponentString = this.exponent.toString();
        if (!Utils.CLOSED_FORM.contains(this.base.getClass())) {
            baseString = "(" + baseString + ")";
        }
        if (!Utils.CLOSED_FORM.contains(this.exponent.getClass())) {
            exponentString = "(" + exponentString + ")";
        }
        return baseString + " ** " + exponentString;
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public ArrayList<Expression<Symbolic>> symbolic() {
        if (this.TYPE == Symbolic.class) {
            return new ArrayList<>(Collections.singletonList((Pow<Symbolic>) this));
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
    public Expression<T> close() {
        if (this.exponent.equalsOne() || this.base.equalsZero() || this.base.equalsOne()) {
            return this.base;
        } else if (this.exponent.equalsZero()) {
            return Constant.ONE(TYPE);
        } else if (this.base instanceof Constant<T> baseConst) {
            return baseConst.pow(this.exponent);
        } else {
            return this;
        }
    }

    public Factorization<T> normalize() {
        Factorization<T> baseFactorization = this.base.normalize();
        baseFactorization.constant = baseFactorization.constant.pow(this.exponent);
        for (Map.Entry<Expression<T>, Constant<T>> entry : baseFactorization.terms.entrySet()) {
            entry.setValue(entry.getValue().mul(this.exponent));
        }
        return baseFactorization;
    }

    public Expression<T> derivative(Univariate<T> var) {
        if (!this.variables().contains(var)) {
            return Constant.ZERO(TYPE);
        } else {
            return ENGINE.mul(this.exponent, ENGINE.pow(this.base, ENGINE.sub(this.exponent, 1)), this.base.derivative(var));
        }
    }

    @Override
    public Expression<T> logarithmicDerivative(Univariate<T> s) {
        if (!this.variables().contains(s)) {
            return Constant.ZERO(TYPE);
        } else {
            return ENGINE.mul(this.exponent, this.base.logarithmicDerivative(s));
        }
    }
}

