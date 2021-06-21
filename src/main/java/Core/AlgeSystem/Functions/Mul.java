package Core.AlgeSystem.Functions;

import Core.AlgeSystem.*;
import Core.EntityTypes.*;
import Core.Utilities.*;
import com.google.common.collect.TreeMultiset;

import java.util.*;
import java.util.function.Function;

public class Mul extends DefinedEntity implements Expression {
    public static final Function<HashMap<String, ArrayList<ArrayList<Expression>>>, ArrayList<Expression>> formula = args -> {
        ArrayList<ArrayList<Expression>> argTerms = args.get("Terms");
        Expression[] terms = new Expression[argTerms.size() + 1];
        for (int i = 0; i < argTerms.size(); i++) {
            terms[i] = argTerms.get(i).get(0);
        }
        terms[argTerms.size()] = args.get("Constant").get(0).get(0);
        return new ArrayList<>(Collections.singletonList(ASEngine.mul((Object[]) terms)));
    };
    public static final String[] inputTypes = new String[] {"Terms", "Constant"};

    public TreeMap<Expression, Expression> terms = new TreeMap<>(Utils.PRIORITY_COMPARATOR);
    public Constant constant = Constant.ONE;

    public Mul(Expression ... args) {
        super();
        TreeMultiset<Entity> inputTerms = this.inputs.get("Terms");
        this.construct(args);
        for (Map.Entry<Expression, Expression> entry : this.terms.entrySet()) {
            inputTerms.add(ASEngine.pow(entry.getKey(), entry.getValue()));
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
        for (Expression arg : args) {
            Factorization argFactor = ((Expression) arg.simplify()).normalize();
            constant = constant.mul(argFactor.constant);
            for (Map.Entry<Expression, Expression> entry : argFactor.terms.entrySet()) {
                terms.put(entry.getKey(), ASEngine.add(entry.getValue(), terms.get(entry.getKey())));
            }
        }
        ArrayList<Map.Entry<Expression, Expression>> entrySet = new ArrayList<>(terms.entrySet());
        for (Map.Entry<Expression, Expression> entry : entrySet) {
            if (entry.getValue().equals(Constant.ZERO)) {
                terms.remove(entry.getKey());
            }
        }
    }

    public Entity simplify() {
        if (inputs.get("Terms").size() == 0) {
            return constant;
        } else if (constant.equals(Constant.ZERO)) {
            return Constant.ZERO;
        } else if (constant.equals(Constant.ONE) && inputs.get("Terms").size() == 1) {
            return inputs.get("Terms").firstEntry().getElement().simplify();
        } else {
            if (!ASEngine.imaginary(this.constant).equals(Constant.ZERO)) {
                return this;
            } else {
                TreeMap<Symbol, Integer> factors = new TreeMap<>(Utils.PRIORITY_COMPARATOR);
                for (Map.Entry<Expression, Expression> entry : this.terms.entrySet()) {
                    if (!(entry.getKey() instanceof Symbol)) {
                        return this;
                    } else if (!(entry.getValue() instanceof Complex cpxExp && cpxExp.integer())) {
                        return this;
                    }
                    factors.put((Symbol) entry.getKey(), ((Complex) entry.getValue()).re.intValue());
                }
                return new Monomial(this.constant, factors);
            }
        }
    }

    public Factorization normalize() {
        Expression simplified = (Expression) this.simplify();
        if (simplified instanceof Mul mulExpr) {
            Constant coefficient = mulExpr.constant;
            TreeMap<Expression, Expression> factors = new TreeMap<>(Utils.PRIORITY_COMPARATOR);
            for (Entity ent : mulExpr.inputs.get("Terms")) {
                Factorization entFactor = ((Expression) ent).normalize();
                coefficient = (Constant) ASEngine.mul(coefficient, entFactor.constant);
                for (Map.Entry<Expression, Expression> entry : entFactor.terms.entrySet()) {
                    factors.put(entry.getKey(), ASEngine.add(factors.get(entry.getKey()), entry.getValue()));
                    if (factors.get(entry.getKey()).equals(Constant.ZERO)) {
                        factors.remove(entry.getKey());
                    }
                }
            }
            return new Factorization(coefficient, factors);
        } else {
            return simplified.normalize();
        }
    }

    public Expression derivative(Symbol s) {
        if (!this.variables().contains(s)) {
            return Constant.ZERO;
        } else {
            ArrayList<Expression> derivativeTerms = Utils.map(new ArrayList<>(this.inputs.get("Terms")), arg ->
                    ASEngine.mul(this, ((Expression) arg).logarithmicDerivative(s)));
            return ASEngine.add(derivativeTerms.toArray());
        }
    }

    public Function<HashMap<String, ArrayList<ArrayList<Expression>>>, ArrayList<Expression>> getFormula() {
        return Mul.formula;
    }

    public String[] getInputTypes() {
        return Mul.inputTypes;
    }

    public Expression baseForm() {
        return (Expression) (new Mul(this, this.constant.inverse())).simplify();
    }
}
