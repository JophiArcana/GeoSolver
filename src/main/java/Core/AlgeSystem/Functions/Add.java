package Core.AlgeSystem.Functions;

import Core.AlgeSystem.*;
import Core.EntityTypes.*;
import Core.Utilities.*;
import com.google.common.collect.TreeMultiset;

import java.util.*;
import java.util.function.Function;

public class Add extends DefinedEntity implements Expression {
    public static final Function<HashMap<String, ArrayList<ArrayList<Expression>>>, ArrayList<Expression>> formula = args -> {
        Expression sum = AlgeEngine.add(args.get("Constant").get(0).get(0),
                AlgeEngine.add(Utils.map(args.get("Terms"), arg -> arg.get(0)).toArray()));
        return new ArrayList<>(Collections.singletonList(sum));
    };
    public static final String[] inputTypes = new String[] {"Terms", "Constant"};

    public TreeMap<Expression, Constant> terms = new TreeMap<>(Utils.PRIORITY_COMPARATOR);
    public Expression logTerm = Constant.ZERO;
    public Constant constant = Constant.ZERO;

    public Add(Expression ... args) {
        super();
        TreeMultiset<Entity> inputTerms = inputs.get("Terms");
        this.construct(args);
        for (Map.Entry<Expression, Constant> entry : terms.entrySet()) {
            inputTerms.add(AlgeEngine.mul(entry.getKey(), entry.getValue()));
        }
        if (!this.logTerm.equals(Constant.ZERO)) {
            inputTerms.add(this.logTerm);
        }
        inputs.get("Constant").add(this.constant);
    }

    public String toString() {
        ArrayList<Entity> inputTerms = new ArrayList<>(inputs.get("Terms"));
        ArrayList<String> stringTerms = new ArrayList<>();
        for (Entity ent : inputTerms) {
            stringTerms.add(ent.toString());
        }
        if (constant.equals(Constant.ZERO)) {
            return String.join(" + ", stringTerms);
        } else {
            return constant + " + " + String.join(" + ", stringTerms);
        }
    }

    private void construct(Expression ... args) {
        for (Expression arg : args) {
            if (arg instanceof Constant constArg) {
                constant = constant.add(constArg);
            } else if (arg instanceof Add addArg) {
                constant = constant.add(addArg.constant);
                this.construct(addArg.inputs.get("Terms").toArray(new Expression[0]));
            } else if (arg instanceof Mul mulArg) {
                Expression baseExpr = mulArg.baseForm();
                Constant baseConst = mulArg.constant;
                if (baseExpr instanceof Add baseAdd) {
                    for (Entity ent : baseAdd.inputs.get("Terms")) {
                        this.construct(AlgeEngine.mul(baseConst, ent));
                    }
                    constant = constant.add((Constant) AlgeEngine.mul(baseConst, baseAdd.constant));
                } else if (baseExpr instanceof Log) {
                    logTerm = AlgeEngine.log(AlgeEngine.mul(AlgeEngine.exp(logTerm), AlgeEngine.exp(mulArg)));
                } else {
                    terms.put(baseExpr, (Constant) AlgeEngine.add(mulArg.constant, terms.get(baseExpr)));
                    if (terms.get(baseExpr).equals(Constant.ZERO)) {
                        terms.remove(baseExpr);
                    }
                }
            } else if (arg instanceof Log logArg) {
                logTerm = AlgeEngine.log(AlgeEngine.mul(AlgeEngine.exp(logTerm), AlgeEngine.exp(logArg)));
            } else {
                terms.put(arg, (Constant) AlgeEngine.add(terms.get(arg), Constant.ONE));
                if (terms.get(arg).equals(Constant.ZERO)) {
                    terms.remove(arg);
                }
            }
        }
    }

    public Entity simplify() {
        if (inputs.get("Terms").size() == 0) {
            return constant;
        } else if (constant.equals(Constant.ZERO) && inputs.get("Terms").size() == 1) {
            return inputs.get("Terms").firstEntry().getElement().simplify();
        } else {
            Expression gcd = AlgeEngine.greatestCommonDivisor(this.constant,
                    AlgeEngine.greatestCommonDivisor(this.inputs.get("Terms").toArray(new Expression[0])));
            if (gcd.equals(Constant.ONE)) {
                ArrayList<Monomial> monomials = new ArrayList<>();
                for (Entity ent : this.inputs.get("Terms")) {
                    if (ent instanceof Univariate s) {
                        HashMap<Univariate, Integer> factor = new HashMap<>() {{
                            put(s, 1);
                        }};
                        monomials.add(new Monomial(Constant.ONE, factor));
                    } else if (ent instanceof Monomial m) {
                        monomials.add(m);
                    } else {
                        return this;
                    }
                }
                return new Polynomial(this.constant, monomials.toArray(new Monomial[0]));
            } else {
                ArrayList<Expression> normalizedTerms = Utils.map(new ArrayList<>(this.inputs.get("Terms")), arg ->
                        AlgeEngine.div(arg, gcd));
                return AlgeEngine.mul(gcd, AlgeEngine.add(AlgeEngine.div(this.constant, gcd),
                        AlgeEngine.add(normalizedTerms.toArray())));
            }
        }
    }

    public Factorization normalize() {
        Expression simplified = (Expression) this.simplify();
        if (simplified instanceof Add addExpr) {
            TreeMap<Expression, Expression> factors = new TreeMap<>(Utils.PRIORITY_COMPARATOR);
            if (addExpr.constant.equals(Constant.ZERO)) {
                factors.put(addExpr, Constant.ONE);
                return new Factorization(Constant.ONE, factors);
            } else {
                ArrayList<Expression> normalizedTerms = Utils.map(new ArrayList<>(addExpr.inputs.get("Terms")), arg ->
                        AlgeEngine.div(arg, addExpr.constant));
                factors.put(AlgeEngine.add(Constant.ONE, AlgeEngine.add(normalizedTerms.toArray())), Constant.ONE);
                return new Factorization(addExpr.constant, factors);
            }
        } else {
            return simplified.normalize();
        }
    }

    public Expression derivative(Univariate s) {
        ArrayList<Expression> derivativeTerms = Utils.map(new ArrayList<>(this.inputs.get("Terms")), arg ->
                ((Expression) arg).derivative(s));
        return AlgeEngine.add(derivativeTerms.toArray());
    }

    public Function<HashMap<String, ArrayList<ArrayList<Expression>>>, ArrayList<Expression>> getFormula() {
        return formula;
    }

    public String[] getInputTypes() {
        return Add.inputTypes;
    }
}
