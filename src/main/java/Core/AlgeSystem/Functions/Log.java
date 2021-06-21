package Core.AlgeSystem.Functions;

import Core.AlgeSystem.*;
import Core.EntityTypes.*;
import Core.Utilities.*;

import java.util.*;
import java.util.function.Function;

public class Log extends DefinedEntity implements Expression {
    public static final Function<HashMap<String, ArrayList<ArrayList<Expression>>>, ArrayList<Expression>> formula = args ->
            new ArrayList<>(Collections.singletonList(ASEngine.log(args.get("Input").get(0).get(0))));
    public static final String[] inputTypes = new String[] {"Input"};

    public Expression input;

    public Log(Expression expr) {
        super();
        this.input = expr;
        this.inputs.get("Input").add(this.input);
    }

    public String toString() {
        return "ln(" + this.input + ")";
    }

    public Entity simplify() {
        if (this.input instanceof Constant) {
            return ((Constant) input).log();
        } else if (this.input instanceof Pow powInput) {
            return ASEngine.mul(powInput.exponent, ASEngine.log(powInput.base));
        } else if (this.input instanceof Mul mulInput && !mulInput.constant.equals(Constant.ONE)) {
            return ASEngine.add(ASEngine.log(mulInput.constant), ASEngine.log(ASEngine.div(mulInput, mulInput.constant)));
        } else {
            return this;
        }
    }

    public Factorization normalize() {
        Expression simplified = (Expression) this.simplify();
        if (simplified instanceof Log logExpr) {
            TreeMap<Expression, Expression> factors = new TreeMap<>(Utils.PRIORITY_COMPARATOR);
            factors.put(logExpr, Constant.ONE);
            return new Factorization(Constant.ONE, factors);
        } else {
            return simplified.normalize();
        }
    }

    public Expression derivative(Symbol s) {
        return ASEngine.div(this.input.derivative(s), this.input);
    }

    public Function<HashMap<String, ArrayList<ArrayList<Expression>>>, ArrayList<Expression>> getFormula() {
        return formula;
    }

    public String[] getInputTypes() {
        return Log.inputTypes;
    }
}