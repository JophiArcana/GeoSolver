package Core.AlgeSystem.Functions;

import Core.AlgeSystem.UnicardinalTypes.*;
import Core.EntityTypes.Entity;
import Core.Utilities.*;

import java.util.*;
import java.util.function.Function;

public class Log<T extends Expression<T>> extends DefinedExpression<T> {
    public static final String[] inputTypes = new String[] {"Input"};

    public Entity create(HashMap<String, ArrayList<Entity>> args) {
        return ENGINE.log(args.get("Input").get(0));
    }

    public ArrayList<Unicardinal> formula(HashMap<String, ArrayList<ArrayList<Unicardinal>>> args) {
        return new ArrayList<>(Collections.singletonList(ENGINE.log(args.get("Input").get(0).get(0))));
    }

    public Expression<T> input;

    public Log(Expression<T> expr, Class<T> type) {
        super(type);
        this.input = expr;
        this.inputs.get("Input").add(this.input);
    }

    public String toString() {
        return "ln(" + this.input + ")";
    }

    public Expression<T> reduction() {
        if (this.input instanceof Constant) {
            return ((Constant<T>) input).log();
        } else if (this.input instanceof Pow<T> powInput) {
            return ENGINE.mul(powInput.exponent, ENGINE.log(powInput.base));
        } else if (this.input instanceof Mul<T> mulInput && !mulInput.constant.equals(Constant.ONE(TYPE))) {
            return ENGINE.add(ENGINE.log(mulInput.constant), ENGINE.log(ENGINE.div(mulInput, mulInput.constant)));
        } else {
            return this;
        }
    }

    public Factorization<T> normalize() {
        TreeMap<Expression<T>, Expression<T>> factors = new TreeMap<>(Utils.PRIORITY_COMPARATOR);
        factors.put(this, Constant.ONE(TYPE));
        return new Factorization<>(Constant.ONE(TYPE), factors, TYPE);
    }

    public Expression<T> derivative(Univariate<T> s) {
        return ENGINE.div(this.input.derivative(s), this.input);
    }

    public Function<HashMap<String, ArrayList<ArrayList<Unicardinal>>>, ArrayList<Unicardinal>> getFormula() {
        return this::formula;
    }

    public String[] getInputTypes() {
        return Log.inputTypes;
    }
}


