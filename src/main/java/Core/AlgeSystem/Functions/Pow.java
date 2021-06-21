package Core.AlgeSystem.Functions;

import Core.AlgeSystem.*;
import Core.EntityTypes.*;
import Core.Utilities.*;

import java.util.*;
import java.util.function.Function;

public class Pow extends DefinedEntity implements Expression {
    public static final Function<HashMap<String, ArrayList<ArrayList<Expression>>>, ArrayList<Expression>> formula = args ->
            new ArrayList<>(Collections.singletonList(ASEngine.pow(args.get("Base").get(0).get(0),
                    args.get("Exponent").get(0).get(0))));
    public static final String[] inputTypes = new String[] {"Base", "Exponent"};

    public Expression base;
    public Expression exponent;

    public Pow(Expression base, Expression exponent) {
        super();
        if (base instanceof Pow) {
            this.base = ((Pow) base).base;
            this.exponent = ASEngine.mul(((Pow) base).exponent, exponent);
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

    public Entity simplify() {
        if (this.exponent.equals(Constant.ONE)) {
            return this.base.simplify();
        } else if (this.exponent.equals(Constant.ZERO)) {
            return Constant.ONE;
        } else if (this.base.equals(Constant.ZERO) || this.base.equals(Constant.ONE)) {
            return this.base;
        } else if (this.base instanceof Mul mulBase) {
            ArrayList<Expression> powTerms = Utils.map(new ArrayList<>(mulBase.inputs.get("Terms")), arg ->
                    ASEngine.pow(arg, this.exponent));
            return ASEngine.mul(ASEngine.pow(mulBase.constant, this.exponent),
                    ASEngine.mul(powTerms.toArray()));
        } else if (this.base instanceof Constant baseConst) {
            if (this.exponent instanceof Constant expConst) {
                return baseConst.pow(expConst);
            } else if (this.exponent instanceof Log expLog) {
                return ASEngine.pow(expLog.input, ASEngine.log(baseConst));
            } else if (this.exponent instanceof Mul expMul && expMul.baseForm() instanceof Log baseLog) {
                return ASEngine.pow(baseLog.input, ASEngine.mul(expMul.constant, ASEngine.log(baseConst)));
            } else if (this.exponent instanceof Add expAdd
                    && (!expAdd.constant.equals(Constant.ZERO) || !expAdd.logTerm.equals(Constant.ZERO))) {
                Expression expBase = ASEngine.sub(expAdd, ASEngine.add(expAdd.constant, expAdd.logTerm));
                return ASEngine.mul(ASEngine.pow(baseConst, expAdd.constant), ASEngine.pow(baseConst, expAdd.logTerm),
                        ASEngine.pow(baseConst, expBase));
            } else {
                return this;
            }
        } else {
            return this;
        }
    }

    public Factorization normalize() {
        Expression simplified = (Expression) this.simplify();
        if (simplified instanceof Pow powExpr) {
            TreeMap<Expression, Expression> factors = new TreeMap<>(Utils.PRIORITY_COMPARATOR);
            factors.put(powExpr.base, powExpr.exponent);
            return new Factorization(Constant.ONE, factors);
        } else {
            return simplified.normalize();
        }
    }

    public Expression derivative(Symbol s) {
        if (!this.variables().contains(s)) {
            return Constant.ZERO;
        } else {
            return ASEngine.mul(this, ASEngine.add(ASEngine.mul(this.exponent.derivative(s), ASEngine.log(this.base)),
                    ASEngine.mul(this.exponent, this.base.derivative(s), ASEngine.pow(this.base, Constant.NONE))));
        }
    }

    public Function<HashMap<String, ArrayList<ArrayList<Expression>>>, ArrayList<Expression>> getFormula() {
        return Pow.formula;
    }

    public String[] getInputTypes() {
        return Pow.inputTypes;
    }
}
