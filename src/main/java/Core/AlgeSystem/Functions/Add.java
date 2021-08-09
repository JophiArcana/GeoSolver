package Core.AlgeSystem.Functions;

import Core.AlgeSystem.UnicardinalTypes.*;
import Core.EntityTypes.*;
import Core.Utilities.*;
import com.google.common.collect.TreeMultiset;

import java.util.*;
import java.util.function.Function;

public class Add<T extends Expression<T>> extends DefinedExpression<T> {
    public static final String[] inputTypes = new String[] {"Terms", "Constant"};

    public Entity create(HashMap<String, ArrayList<Entity>> args) {
        return ENGINE.add(args.get("Constant").get(0), ENGINE.add(args.get("Terms").toArray()));
    }

    public ArrayList<Unicardinal> formula(HashMap<String, ArrayList<ArrayList<Unicardinal>>> args) {
        Expression<T> sum = ENGINE.add(args.get("Constant").get(0).get(0),
                ENGINE.add(Utils.map(args.get("Terms"), arg -> arg.get(0)).toArray()));
        return new ArrayList<>(Collections.singletonList(sum));
    }

    public TreeMap<Expression<T>, Constant<T>> terms = new TreeMap<>(Utils.PRIORITY_COMPARATOR);
    public Constant<T> constant = Constant.ZERO(TYPE);

    public Add(Iterable<Expression<T>> args, Class<T> type) {
        super(type);
        TreeMultiset<Entity> inputTerms = inputs.get("Terms");
        this.construct(args);
        for (Map.Entry<Expression<T>, Constant<T>> entry : terms.entrySet()) {
            inputTerms.add(ENGINE.mul(entry.getKey(), entry.getValue()));
        }
        this.inputs.get("Constant").add(this.constant);
        // System.out.println("Add constructed: " + args);
    }

    public String toString() {
        ArrayList<Entity> inputTerms = new ArrayList<>(inputs.get("Terms"));
        ArrayList<String> stringTerms = new ArrayList<>();
        for (Entity ent : inputTerms) {
            stringTerms.add(ent.toString());
        }
        if (constant.equals(Constant.ZERO(TYPE))) {
            return String.join(" + ", stringTerms);
        } else {
            return constant + " + " + String.join(" + ", stringTerms);
        }
    }

    private void construct(Iterable<Expression<T>> args) {
        for (Expression<T> arg : args) {
            if (arg instanceof Constant<T> constArg) {
                constant = constant.add(constArg);
            } else if (arg instanceof Add<T> addArg) {
                constant = constant.add(addArg.constant);
                this.construct(Utils.cast(addArg.inputs.get("Terms")));
            } else if (arg instanceof Mul<T> mulArg) {
                Expression<T> baseExpr = mulArg.baseForm().getValue();
                Constant<T> baseConst = mulArg.baseForm().getKey();
                if (baseExpr instanceof Add<T> baseAdd) {
                    for (Entity ent : baseAdd.inputs.get("Terms")) {
                        this.construct(new ArrayList<>(Collections.singletonList(ENGINE.mul(baseConst, ent))));
                    }
                    constant = constant.add((Constant<T>) ENGINE.mul(baseConst, baseAdd.constant));
                } else {
                    terms.put(baseExpr, (Constant<T>) ENGINE.add(mulArg.constant, terms.getOrDefault(baseExpr, Constant.ZERO(TYPE))));
                    if (terms.get(baseExpr).equals(Constant.ZERO(TYPE))) {
                        terms.remove(baseExpr);
                    }
                }
            } else {
                terms.put(arg, (Constant<T>) ENGINE.add(terms.getOrDefault(arg, Constant.ZERO(TYPE)), Constant.ONE(TYPE)));
                if (terms.get(arg).equals(Constant.ZERO(TYPE))) {
                    terms.remove(arg);
                }
            }
        }
    }

    public Expression<T> reduction() {
        if (inputs.get("Terms").size() == 0) {
            return constant;
        } else if (constant.equals(Constant.ZERO(TYPE)) && inputs.get("Terms").size() == 1) {
            return (Expression<T>) inputs.get("Terms").firstEntry().getElement().simplify();
        } else {
            Expression<T> gcd = ENGINE.greatestCommonDivisor(Arrays.asList(this.constant,
                    ENGINE.greatestCommonDivisor(Utils.cast(this.inputs.get("Terms")))));
            if (gcd.equals(Constant.ONE(TYPE))) {
                ArrayList<Monomial<T>> monomials = new ArrayList<>();
                for (Entity ent : this.inputs.get("Terms")) {
                    if (ent instanceof Univariate) {
                        HashMap<Univariate<T>, Integer> factor = new HashMap<>() {{
                            put((Univariate<T>) ent, 1);
                        }};
                        monomials.add(new Monomial<>(Constant.ONE(TYPE), factor, TYPE));
                    } else if (ent instanceof Monomial) {
                        monomials.add((Monomial<T>) ent);
                    } else {
                        return this;
                    }
                }
                return new Polynomial<>(this.constant, monomials, TYPE);
            } else {
                ArrayList<Expression<T>> normalizedTerms = Utils.map(this.inputs.get("Terms"), arg ->
                        ENGINE.div(arg, gcd));
                normalizedTerms.add(ENGINE.div(this.constant, gcd));
                return new Mul<>(Arrays.asList(gcd, new Add<>(normalizedTerms, TYPE).reduction()), TYPE).reduction();
            }
        }
    }

    public Factorization<T> normalize() {
        TreeMap<Expression<T>, Constant<T>> factors = new TreeMap<>(Utils.PRIORITY_COMPARATOR);
        if (this.constant.equals(Constant.ZERO(TYPE))) {
            factors.put(this, Constant.ONE(TYPE));
            return new Factorization<>(Constant.ONE(TYPE), factors, TYPE);
        } else {
            ArrayList<Expression<T>> normalizedTerms = Utils.map(this.inputs.get("Terms"), arg ->
                    ENGINE.div(arg, this.constant));
            normalizedTerms.add(Constant.ONE(TYPE));
            factors.put(ENGINE.add(normalizedTerms.toArray()), Constant.ONE(TYPE));
            return new Factorization<>(this.constant, factors, TYPE);
        }
    }

    public Expression<T> derivative(Univariate<T> s) {
        ArrayList<Expression<T>> derivativeTerms = Utils.map(Utils.<Entity, Expression<T>>cast(this.inputs.get("Terms")),
                arg -> arg.derivative(s));
        return ENGINE.add(derivativeTerms.toArray());
    }

    public Function<HashMap<String, ArrayList<ArrayList<Unicardinal>>>, ArrayList<Unicardinal>> getFormula() {
        return this::formula;
    }

    public String[] getInputTypes() {
        return Add.inputTypes;
    }
}
