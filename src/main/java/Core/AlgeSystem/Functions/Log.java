package Core.AlgeSystem.Functions;

import Core.AlgeSystem.ExpressionTypes.*;
import Core.Utilities.*;

import java.util.*;
import java.util.function.Function;

public class Log extends DefinedExpression {
    public static final Function<HashMap<String, ArrayList<ArrayList<Expression>>>, ArrayList<Expression>> formula = args ->
            new ArrayList<>(Collections.singletonList(AlgeEngine.log(args.get("Input").get(0).get(0))));
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

    public Expression reduction() {
        if (this.input instanceof Constant) {
            return ((Constant) input).log();
        } else if (this.input instanceof Pow powInput) {
            return AlgeEngine.mul(powInput.exponent, AlgeEngine.log(powInput.base));
        } else if (this.input instanceof Mul mulInput && !mulInput.constant.equals(Constant.ONE)) {
            return AlgeEngine.add(AlgeEngine.log(mulInput.constant), AlgeEngine.log(AlgeEngine.div(mulInput, mulInput.constant)));
        } else {
            return this;
        }
    }

    public Factorization normalize() {
        TreeMap<Expression, Expression> factors = new TreeMap<>(Utils.PRIORITY_COMPARATOR);
        factors.put(this, Constant.ONE);
        return new Factorization(Constant.ONE, factors);
    }

    public Expression derivative(Univariate s) {
        return AlgeEngine.div(this.input.derivative(s), this.input);
    }

    public Function<HashMap<String, ArrayList<ArrayList<Expression>>>, ArrayList<Expression>> getFormula() {
        return Log.formula;
    }

    public String[] getInputTypes() {
        return Log.inputTypes;
    }
}


