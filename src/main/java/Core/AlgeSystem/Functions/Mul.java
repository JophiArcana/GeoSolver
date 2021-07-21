package Core.AlgeSystem.Functions;

import Core.AlgeSystem.Constants.*;
import Core.AlgeSystem.UnicardinalTypes.*;
import Core.EntityTypes.*;
import Core.Utilities.*;
import com.google.common.collect.TreeMultiset;

import java.util.*;
import java.util.function.Function;

public class Mul<T extends Expression<T>> extends DefinedExpression<T> {
    public static final String[] inputTypes = new String[] {"Terms", "Constant"};

    public ArrayList<Unicardinal> formula(HashMap<String, ArrayList<ArrayList<Unicardinal>>> args) {
        Expression<T> product = ENGINE.mul(args.get("Constant").get(0).get(0),
                ENGINE.mul(Utils.map(args.get("Terms"), arg -> arg.get(0)).toArray()));
        return new ArrayList<>(Collections.singletonList(product));
    }

    public TreeMap<Expression<T>, Expression<T>> terms = new TreeMap<>(Utils.PRIORITY_COMPARATOR);
    public Constant<T> constant = Constant.ONE(TYPE);

    public Mul(Iterable<Expression<T>> args, Class<T> type) {
        super(type);
        TreeMultiset<Entity> inputTerms = this.inputs.get("Terms");
        this.construct(args);
        for (Map.Entry<Expression<T>, Expression<T>> entry : this.terms.entrySet()) {
            inputTerms.add(ENGINE.pow(entry.getKey(), entry.getValue()));
        }
        this.inputs.get("Constant").add(this.constant);
    }

    public String toString() {
        ArrayList<Entity> inputTerms = new ArrayList<>(this.inputs.get("Terms"));
        ArrayList<String> stringTerms = new ArrayList<>();
        for (Entity ent : inputTerms) {
            if (Utils.CLOSED_FORM.contains(ent.getClass())) {
                stringTerms.add(ent.toString());
            } else {
                stringTerms.add("(" + ent + ")");
            }
        }
        if (this.constant.equals(Constant.ONE(TYPE))) {
            return String.join("", stringTerms);
        } else if (this.constant.equals(Constant.NONE(TYPE))) {
            return "-" + String.join("", stringTerms);
        } else {
            return this.constant + String.join("", stringTerms);
        }
    }

    private void construct(Iterable<Expression<T>> args) {
        for (Expression<T> arg : args) {
            Factorization<T> argFactor = arg.normalize();
            constant = constant.mul(argFactor.constant);
            for (Map.Entry<Expression<T>, Expression<T>> entry : argFactor.terms.entrySet()) {
                terms.put(entry.getKey(), ENGINE.add(entry.getValue(), terms.getOrDefault(entry.getKey(), Constant.ZERO(TYPE))));
            }
        }
        ArrayList<Map.Entry<Expression<T>, Expression<T>>> entrySet = new ArrayList<>(terms.entrySet());
        for (Map.Entry<Expression<T>, Expression<T>> entry : entrySet) {
            if (entry.getValue().equals(Constant.ZERO(TYPE))) {
                terms.remove(entry.getKey());
            }
        }
    }

    public Expression<T> reduction() {
        if (inputs.get("Terms").size() == 0) {
            return constant;
        } else if (constant.equals(Constant.ZERO(TYPE))) {
            return Constant.ZERO(TYPE);
        } else if (constant.equals(Constant.ONE(TYPE)) && inputs.get("Terms").size() == 1) {
            return (Expression<T>) inputs.get("Terms").firstEntry().getElement().simplify();
        } else {
            if (!ENGINE.imaginary(this.constant).equals(Constant.ZERO(TYPE))) {
                return this;
            } else {
                TreeMap<Univariate<T>, Integer> factors = new TreeMap<>(Utils.PRIORITY_COMPARATOR);
                for (Map.Entry<Expression<T>, Expression<T>> entry : this.terms.entrySet()) {
                    if (!(entry.getKey() instanceof Univariate)) {
                        return this;
                    } else if (!(entry.getValue() instanceof Complex<T> cpxExp && cpxExp.integer())) {
                        return this;
                    }
                    factors.put((Univariate<T>) entry.getKey(), ((Complex<T>) entry.getValue()).re.intValue());
                }
                return new Monomial<>(this.constant, factors, TYPE);
            }
        }
    }

    public Factorization<T> normalize() {
        Constant<T> coefficient = this.constant;
        TreeMap<Expression<T>, Expression<T>> factors = new TreeMap<>(Utils.PRIORITY_COMPARATOR);
        for (Entity ent : this.inputs.get("Terms")) {
            Factorization<T> entFactor = ((Expression<T>) ent).normalize();
            coefficient = (Constant<T>) ENGINE.mul(coefficient, entFactor.constant);
            for (Map.Entry<Expression<T>, Expression<T>> entry : entFactor.terms.entrySet()) {
                factors.put(entry.getKey(), ENGINE.add(factors.get(entry.getKey()), entry.getValue()));
                if (factors.get(entry.getKey()).equals(Constant.ZERO(TYPE))) {
                    factors.remove(entry.getKey());
                }
            }
        }
        return new Factorization<>(coefficient, factors, TYPE);
    }

    public Expression<T> derivative(Univariate<T> s) {
        if (!this.variables().contains(s)) {
            return Constant.ZERO(TYPE);
        } else {
            ArrayList<Expression<T>> derivativeTerms = Utils.map(Utils.<Entity, Expression<T>>cast(this.inputs.get("Terms")), arg ->
                    ENGINE.mul(this, arg.logarithmicDerivative(s)));
            return ENGINE.add(derivativeTerms.toArray());
        }
    }

    public Function<HashMap<String, ArrayList<ArrayList<Unicardinal>>>, ArrayList<Unicardinal>> getFormula() {
        return this::formula;
    }

    public String[] getInputTypes() {
        return Mul.inputTypes;
    }

    public Expression<T> baseForm() {
        return new Mul<T>(Utils.cast(this.inputs.get("Terms")), TYPE).reduction();
    }
}
