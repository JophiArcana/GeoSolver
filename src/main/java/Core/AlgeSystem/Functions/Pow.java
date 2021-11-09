package Core.AlgeSystem.Functions;

import Core.AlgeSystem.UnicardinalRings.*;
import Core.AlgeSystem.UnicardinalTypes.*;
import Core.EntityTypes.Entity;
import Core.Utilities.*;

import java.util.*;

public class Pow<T extends Expression<T>> extends DefinedExpression<T> {
    public static final String[] inputTypes = {"Base", "Exponent"};

    public Entity create(HashMap<String, ArrayList<Entity>> args) {
        return ENGINE.pow(args.get("Base").get(0), args.get("Exponent").get(0));
    }

    public Expression<T> base;
    public Constant<T> exponent;

    public Pow(Expression<T> base, Constant<T> exponent, Class<T> type) {
        super(type);
        if (base instanceof Pow) {
            this.base = ((Pow<T>) base).base;
            this.exponent = ((Pow<T>) base).exponent.mul(exponent);
        } else {
            this.base = base;
            this.exponent = exponent;
        }
        this.inputs.get("Base").add(this.base);
        this.inputs.get("Exponent").add(this.exponent);
        // System.out.println(base + " and " + exponent + " Pow constructed");
    }

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

    public ArrayList<Expression<Symbolic>> symbolic() {
        if (this.TYPE == Symbolic.class) {
            return new ArrayList<>(Collections.singletonList((Pow<Symbolic>) this));
        } else if (this.TYPE == DirectedAngle.class) {
            return null;
        } else {
            return null;
        }
    }

    public Expression<T> close() {
        if (this.exponent.equals(Constant.ONE(TYPE)) || this.base.equals(Constant.ZERO(TYPE)) || this.base.equals(Constant.ONE(TYPE))) {
            return this.base;
        } else if (this.exponent.equals(Constant.ZERO(TYPE))) {
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

    public String[] getInputTypes() {
        return Pow.inputTypes;
    }
}

