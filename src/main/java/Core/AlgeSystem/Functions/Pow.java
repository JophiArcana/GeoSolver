package Core.AlgeSystem.Functions;

import Core.AlgeSystem.UnicardinalTypes.*;
import Core.EntityTypes.Entity;
import Core.Utilities.*;

import java.util.*;
import java.util.function.Function;

public class Pow<T extends Expression<T>> extends DefinedExpression<T> {
    public static final String[] inputTypes = new String[] {"Base", "Exponent"};

    public Entity create(HashMap<String, ArrayList<Entity>> args) {
        return ENGINE.pow(args.get("Base").get(0), args.get("Exponent").get(0));
    }

    public ArrayList<Unicardinal> formula(HashMap<String, ArrayList<ArrayList<Unicardinal>>> args) {
        return new ArrayList<>(Collections.singletonList(ENGINE.pow(args.get("Base").get(0).get(0), args.get("Exponent").get(0).get(0))));
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

    public Expression<T> reduction() {
        if (this.exponent.equals(Constant.ONE(TYPE))) {
            return this.base;
        } else if (this.exponent.equals(Constant.ZERO(TYPE))) {
            return Constant.ONE(TYPE);
        } else if (this.base.equals(Constant.ZERO(TYPE)) || this.base.equals(Constant.ONE(TYPE))) {
            return this.base;
        } else if (this.base instanceof Mul<T> mulBase) {
            ArrayList<Expression<T>> powTerms = Utils.map(mulBase.inputs.get("Terms"), arg ->
                    ENGINE.pow(arg, this.exponent));
            return ENGINE.mul(ENGINE.pow(mulBase.constant, this.exponent),
                    ENGINE.mul(powTerms.toArray()));
        } else if (this.base instanceof Constant<T> baseConst) {
            return baseConst.pow(this.exponent);
        } else {
            return this;
        }
    }

    public Factorization<T> normalize() {
        TreeMap<Expression<T>, Constant<T>> factors = new TreeMap<>(Utils.PRIORITY_COMPARATOR);
        factors.put(this.base, this.exponent);
        return new Factorization<>(Constant.ONE(TYPE), factors, TYPE);
    }

    public Expression<T> derivative(Univariate<T> s) {
        if (!this.variables().contains(s)) {
            return Constant.ZERO(TYPE);
        } else {
            return ENGINE.mul(this.exponent, ENGINE.pow(this.base, ENGINE.sub(this.exponent, 1)), this.base.derivative(s));
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

    public Function<HashMap<String, ArrayList<ArrayList<Unicardinal>>>, ArrayList<Unicardinal>> getFormula() {
        return this::formula;
    }

    public String[] getInputTypes() {
        return Pow.inputTypes;
    }
}
