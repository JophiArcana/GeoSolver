package Core.AlgeSystem.Functions;

import Core.AlgeSystem.ExpressionTypes.*;
import Core.Utilities.*;

import java.util.*;
import java.util.function.Function;

public class Pow extends DefinedExpression {
    public static final Function<HashMap<String, ArrayList<ArrayList<Expression>>>, ArrayList<Expression>> formula = args ->
            new ArrayList<>(Collections.singletonList(AlgeEngine.pow(args.get("Base").get(0).get(0),
                    args.get("Exponent").get(0).get(0))));
    public static final String[] inputTypes = new String[] {"Base", "Exponent"};

    public Expression base, exponent;

    public Pow(Expression base, Expression exponent) {
        super();
        if (base instanceof Pow) {
            this.base = ((Pow) base).base;
            this.exponent = AlgeEngine.mul(((Pow) base).exponent, exponent);
        } else if (base instanceof Constant && exponent instanceof Log logExp) {
            this.base = logExp.input;
            this.exponent = ((Constant) base).log();
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

    public Expression reduction() {
        if (this.exponent.equals(Constant.ONE)) {
            return this.base;
        } else if (this.exponent.equals(Constant.ZERO)) {
            return Constant.ONE;
        } else if (this.base.equals(Constant.ZERO) || this.base.equals(Constant.ONE)) {
            return this.base;
        } else if (this.base instanceof Mul mulBase) {
            ArrayList<Expression> powTerms = Utils.map(mulBase.inputs.get("Terms"), arg ->
                    AlgeEngine.pow(arg, this.exponent));
            return AlgeEngine.mul(AlgeEngine.pow(mulBase.constant, this.exponent),
                    AlgeEngine.mul(powTerms.toArray()));
        } else if (this.base instanceof Constant baseConst) {
            if (this.exponent instanceof Constant expConst) {
                return baseConst.pow(expConst);
            } else if (this.exponent instanceof Log expLog) {
                return AlgeEngine.pow(expLog.input, AlgeEngine.log(baseConst));
            } else if (this.exponent instanceof Mul expMul && expMul.baseForm() instanceof Log baseLog) {
                return AlgeEngine.pow(baseLog.input, AlgeEngine.mul(expMul.constant, AlgeEngine.log(baseConst)));
            } else if (this.exponent instanceof Add expAdd
                    && (!expAdd.constant.equals(Constant.ZERO) || !expAdd.logTerm.equals(Constant.ZERO))) {
                Expression expBase = AlgeEngine.sub(expAdd, AlgeEngine.add(expAdd.constant, expAdd.logTerm));
                return AlgeEngine.mul(AlgeEngine.pow(baseConst, expAdd.constant), AlgeEngine.pow(baseConst, expAdd.logTerm),
                        AlgeEngine.pow(baseConst, expBase));
            } else {
                return this;
            }
        } else {
            return this;
        }
    }

    public Factorization normalize() {
        TreeMap<Expression, Expression> factors = new TreeMap<>(Utils.PRIORITY_COMPARATOR);
        factors.put(this.base, this.exponent);
        return new Factorization(Constant.ONE, factors);
    }

    public Expression derivative(Univariate s) {
        if (!this.variables().contains(s)) {
            return Constant.ZERO;
        } else {
            return AlgeEngine.mul(this, AlgeEngine.add(AlgeEngine.mul(this.exponent.derivative(s), AlgeEngine.log(this.base)),
                    AlgeEngine.mul(this.exponent, this.base.derivative(s), AlgeEngine.pow(this.base, Constant.NONE))));
        }
    }

    public Function<HashMap<String, ArrayList<ArrayList<Expression>>>, ArrayList<Expression>> getFormula() {
        return Pow.formula;
    }

    public String[] getInputTypes() {
        return Pow.inputTypes;
    }
}
