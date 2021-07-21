package Core.AlgeSystem.Functions;

import Core.AlgeSystem.UnicardinalTypes.*;
import Core.Utilities.*;

import java.util.*;
import java.util.function.Function;

public class Pow<T extends Expression<T>> extends DefinedExpression<T> {
    public static final String[] inputTypes = new String[] {"Base", "Exponent"};

    public ArrayList<Unicardinal> formula(HashMap<String, ArrayList<ArrayList<Unicardinal>>> args) {
        return new ArrayList<>(Collections.singletonList(ENGINE.pow(args.get("Base").get(0).get(0), args.get("Exponent").get(0).get(0))));
    }

    public Expression<T> base, exponent;

    public Pow(Expression<T> base, Expression<T> exponent, Class<T> type) {
        super(type);
        if (base instanceof Pow) {
            this.base = ((Pow<T>) base).base;
            this.exponent = ENGINE.mul(((Pow<T>) base).exponent, exponent);
        } else if (base instanceof Constant && exponent instanceof Log<T> logExp) {
            this.base = logExp.input;
            this.exponent = ((Constant<T>) base).log();
        } else {
            this.base = base;
            this.exponent = exponent;
        }
        this.inputs.get("Base").add(this.base);
        this.inputs.get("Exponent").add(this.exponent);
    }

    public String toString() {
        String baseString = this.base.toString();
        if (!Utils.CLOSED_FORM.contains(this.base.getClass())) {
            baseString = "(" + baseString + ")";
        }
        return baseString + " ** " + this.exponent;
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
            if (this.exponent instanceof Constant<T> expConst) {
                return baseConst.pow(expConst);
            } else if (this.exponent instanceof Log<T> expLog) {
                return ENGINE.pow(expLog.input, ENGINE.log(baseConst));
            } else if (this.exponent instanceof Mul<T> expMul && expMul.baseForm() instanceof Log<T> baseLog) {
                return ENGINE.pow(baseLog.input, ENGINE.mul(expMul.constant, ENGINE.log(baseConst)));
            } else if (this.exponent instanceof Add<T> expAdd
                    && (!expAdd.constant.equals(Constant.ZERO(TYPE)) || !expAdd.logTerm.equals(Constant.ZERO(TYPE)))) {
                Expression<T> expBase = ENGINE.sub(expAdd, ENGINE.add(expAdd.constant, expAdd.logTerm));
                return ENGINE.mul(ENGINE.pow(baseConst, expAdd.constant), ENGINE.pow(baseConst, expAdd.logTerm),
                        ENGINE.pow(baseConst, expBase));
            } else {
                return this;
            }
        } else {
            return this;
        }
    }

    public Factorization<T> normalize() {
        TreeMap<Expression<T>, Expression<T>> factors = new TreeMap<>(Utils.PRIORITY_COMPARATOR);
        factors.put(this.base, this.exponent);
        return new Factorization<>(Constant.ONE(TYPE), factors, TYPE);
    }

    public Expression<T> derivative(Univariate<T> s) {
        if (!this.variables().contains(s)) {
            return Constant.ZERO(TYPE);
        } else {
            return ENGINE.mul(this, ENGINE.add(ENGINE.mul(this.exponent.derivative(s), ENGINE.log(this.base)),
                    ENGINE.mul(this.exponent, this.base.derivative(s), ENGINE.pow(this.base, Constant.NONE(TYPE)))));
        }
    }

    public Function<HashMap<String, ArrayList<ArrayList<Unicardinal>>>, ArrayList<Unicardinal>> getFormula() {
        return this::formula;
    }

    public String[] getInputTypes() {
        return Pow.inputTypes;
    }
}
