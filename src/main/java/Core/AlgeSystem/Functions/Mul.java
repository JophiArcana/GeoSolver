package Core.AlgeSystem.Functions;

import Core.AlgeSystem.Constants.*;
import Core.AlgeSystem.ExpressionTypes.*;
import Core.EntityTypes.*;
import Core.Utilities.*;
import com.google.common.collect.TreeMultiset;

import java.util.*;
import java.util.function.Function;

public class Mul extends DefinedExpression {
    public static final Function<HashMap<String, ArrayList<ArrayList<Expression>>>, ArrayList<Expression>> formula = args -> {
        Expression product = AlgeEngine.mul(args.get("Constant").get(0).get(0),
                AlgeEngine.mul(Utils.map(args.get("Terms"), arg -> arg.get(0)).toArray()));
        return new ArrayList<>(Collections.singletonList(product));
    };
    public static final String[] inputTypes = new String[] {"Terms", "Constant"};

    public TreeMap<Expression, Expression> terms = new TreeMap<>(Utils.PRIORITY_COMPARATOR);
    public Constant constant = Constant.ONE;

    public Mul(Expression ... args) {
        super();
        TreeMultiset<Entity> inputTerms = this.inputs.get("Terms");
        this.construct(args);
        for (Map.Entry<Expression, Expression> entry : this.terms.entrySet()) {
            inputTerms.add(AlgeEngine.pow(entry.getKey(), entry.getValue()));
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
        if (this.constant.equals(Constant.ONE)) {
            return String.join("", stringTerms);
        } else if (this.constant.equals(Constant.NONE)) {
            return "-" + String.join("", stringTerms);
        } else {
            return this.constant + String.join("", stringTerms);
        }
    }

    private void construct(Expression ... args) {
        // ystem.out.println("Mul args: " + Arrays.toString(args));
        for (Expression arg : args) {
            Factorization argFactor = arg.normalize();
            // System.out.println("Arg " + arg + " normalized: " + argFactor);
            constant = constant.mul(argFactor.constant);
            for (Map.Entry<Expression, Expression> entry : argFactor.terms.entrySet()) {
                terms.put(entry.getKey(), AlgeEngine.add(entry.getValue(), terms.getOrDefault(entry.getKey(), Constant.ZERO)));
            }
        }
        ArrayList<Map.Entry<Expression, Expression>> entrySet = new ArrayList<>(terms.entrySet());
        for (Map.Entry<Expression, Expression> entry : entrySet) {
            if (entry.getValue().equals(Constant.ZERO)) {
                terms.remove(entry.getKey());
            }
        }
    }

    public Expression reduction() {
        if (inputs.get("Terms").size() == 0) {
            return constant;
        } else if (constant.equals(Constant.ZERO)) {
            return Constant.ZERO;
        } else if (constant.equals(Constant.ONE) && inputs.get("Terms").size() == 1) {
            return (Expression) inputs.get("Terms").firstEntry().getElement().simplify();
        } else {
            if (!AlgeEngine.imaginary(this.constant).equals(Constant.ZERO)) {
                return this;
            } else {
                TreeMap<Univariate, Integer> factors = new TreeMap<>(Utils.PRIORITY_COMPARATOR);
                for (Map.Entry<Expression, Expression> entry : this.terms.entrySet()) {
                    if (!(entry.getKey() instanceof Univariate)) {
                        return this;
                    } else if (!(entry.getValue() instanceof Complex cpxExp && cpxExp.integer())) {
                        return this;
                    }
                    factors.put((Univariate) entry.getKey(), ((Complex) entry.getValue()).re.intValue());
                }
                return new Monomial(this.constant, factors);
            }
        }
    }

    public Factorization normalize() {
        Constant coefficient = this.constant;
        TreeMap<Expression, Expression> factors = new TreeMap<>(Utils.PRIORITY_COMPARATOR);
        for (Entity ent : this.inputs.get("Terms")) {
            Factorization entFactor = ((Expression) ent).normalize();
            coefficient = (Constant) AlgeEngine.mul(coefficient, entFactor.constant);
            for (Map.Entry<Expression, Expression> entry : entFactor.terms.entrySet()) {
                factors.put(entry.getKey(), AlgeEngine.add(factors.get(entry.getKey()), entry.getValue()));
                if (factors.get(entry.getKey()).equals(Constant.ZERO)) {
                    factors.remove(entry.getKey());
                }
            }
        }
        return new Factorization(coefficient, factors);
    }

    public Expression derivative(Univariate s) {
        if (!this.variables().contains(s)) {
            return Constant.ZERO;
        } else {
            ArrayList<Expression> derivativeTerms = Utils.map(this.inputs.get("Terms"), arg ->
                    AlgeEngine.mul(this, ((Expression) arg).logarithmicDerivative(s)));
            return AlgeEngine.add(derivativeTerms.toArray());
        }
    }

    public Function<HashMap<String, ArrayList<ArrayList<Expression>>>, ArrayList<Expression>> getFormula() {
        return Mul.formula;
    }

    public String[] getInputTypes() {
        return Mul.inputTypes;
    }

    public Expression baseForm() {
        return (new Mul(this.inputs.get("Terms").toArray(new Expression[0]))).reduction();
    }
}
