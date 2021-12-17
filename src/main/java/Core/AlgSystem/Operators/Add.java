package Core.AlgSystem.Operators;

import Core.AlgSystem.UnicardinalRings.*;
import Core.AlgSystem.UnicardinalTypes.*;
import Core.EntityTypes.*;
import Core.Utilities.*;
import com.google.common.collect.TreeMultiset;

import java.util.*;

public class Add<T extends Expression<T>> extends DefinedExpression<T> {
    public enum Parameter implements InputType {
        TERMS,
        CONSTANT
    }
    public static final InputType[] inputTypes = {Parameter.TERMS, Parameter.CONSTANT};

    public Entity create(HashMap<InputType, ArrayList<Entity>> args) {
        return ENGINE.add(args.get(Parameter.CONSTANT).get(0), ENGINE.add(args.get(Parameter.TERMS).toArray()));
    }

    public TreeMap<Expression<T>, Constant<T>> terms = new TreeMap<>(Utils.PRIORITY_COMPARATOR);
    public Constant<T> constant = Constant.ZERO(TYPE);

    public Add(Class<T> type) {
        super(type);
    }

    public Add(Iterable<Expression<T>> args, Class<T> type) {
        super(type);
        TreeMultiset<Entity> inputTerms = inputs.get(Parameter.TERMS);
        this.construct(args);
        for (Map.Entry<Expression<T>, Constant<T>> entry : terms.entrySet()) {
            inputTerms.add(ENGINE.mul(entry.getKey(), entry.getValue()));
        }
        this.inputs.get(Parameter.CONSTANT).add(this.constant);
        // System.out.println("Add constructed: " + args);
    }

    public String toString() {
        ArrayList<Entity> inputTerms = new ArrayList<>(inputs.get(Parameter.TERMS));
        ArrayList<String> stringTerms = new ArrayList<>();
        for (Entity ent : inputTerms) {
            stringTerms.add(ent.toString());
        }
        if (constant.equalsZero()) {
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
                this.construct(Utils.cast(addArg.inputs.get(Parameter.TERMS)));
            } else if (arg instanceof Mul<T> mulArg) {
                Expression<T> baseExpr = mulArg.baseForm().getValue();
                Constant<T> baseConst = mulArg.baseForm().getKey();
                if (baseExpr instanceof Add<T> baseAdd) {
                    for (Entity ent : baseAdd.inputs.get(Parameter.TERMS)) {
                        this.construct(new ArrayList<>(Collections.singletonList(ENGINE.mul(baseConst, ent))));
                    }
                    constant = constant.add((Constant<T>) ENGINE.mul(baseConst, baseAdd.constant));
                } else {
                    terms.put(baseExpr, (Constant<T>) ENGINE.add(mulArg.constant, terms.getOrDefault(baseExpr, Constant.ZERO(TYPE))));
                    if (terms.get(baseExpr).equalsZero()) {
                        terms.remove(baseExpr);
                    }
                }
            } else {
                terms.put(arg, (Constant<T>) ENGINE.add(terms.getOrDefault(arg, Constant.ZERO(TYPE)), Constant.ONE(TYPE)));
                if (terms.get(arg).equalsZero()) {
                    terms.remove(arg);
                }
            }
        }
    }

    public ArrayList<Expression<Symbolic>> symbolic() {
        if (this.TYPE == Symbolic.class) {
            return new ArrayList<>(Collections.singletonList((Add<Symbolic>) this));
        } else if (this.TYPE == DirectedAngle.class){
            final AlgeEngine<Symbolic> ENGINE = Utils.getEngine(Symbolic.class);
            ArrayList<Expression<T>> terms = new ArrayList<>(Collections.singletonList(this.constant));
            terms.addAll(Utils.cast(this.inputs.get(Parameter.TERMS)));
            ArrayList<ArrayList<HashSet<Expression<T>>>> subsets = Utils.sortedSubsets(terms);
            ArrayList<Expression<Symbolic>> numeratorTerms = new ArrayList<>();
            ArrayList<Expression<Symbolic>> denominatorTerms = new ArrayList<>(Collections.singletonList(Constant.ONE(Symbolic.class)));
            for (int i = 1; i < subsets.size(); i++) {
                ArrayList<Expression<Symbolic>> symbolics = new ArrayList<>();
                for (HashSet<Expression<T>> subset : subsets.get(i)) {
                    symbolics.add(ENGINE.mul(Utils.map(subset, arg -> arg.symbolic().get(0)).toArray()));
                }
                Expression<Symbolic> symmetricSum = ENGINE.add(symbolics.toArray());
                switch (i % 4) {
                    case 0:
                        denominatorTerms.add(symmetricSum);
                    case 1:
                        numeratorTerms.add(symmetricSum);
                    case 2:
                        denominatorTerms.add(ENGINE.negate(symmetricSum));
                    case 3:
                        numeratorTerms.add(ENGINE.negate(symmetricSum));
                }
            }
            return new ArrayList<>(Collections.singletonList(ENGINE.div(ENGINE.add(numeratorTerms.toArray()),
                    ENGINE.add(denominatorTerms.toArray()))));
        } else {
            return null;
        }
    }

    public Expression<T> close() {
        if (this.inputs.get(Parameter.TERMS).size() == 0) {
            return this.constant;
        } else if (this.constant.equalsZero() && this.inputs.get(Parameter.TERMS).size() == 1) {
            return (Expression<T>) this.inputs.get(Parameter.TERMS).firstEntry().getElement();
        } else {
            return this;
        }
    }

    public Factorization<T> normalize() {
        TreeMap<Expression<T>, Constant<T>> factors = new TreeMap<>(Utils.PRIORITY_COMPARATOR);
        if (this.constant.equalsZero()) {
            factors.put(this, Constant.ONE(TYPE));
            return new Factorization<>(Constant.ONE(TYPE), factors, TYPE);
        } else {
            ArrayList<Expression<T>> normalizedTerms = Utils.map(this.inputs.get(Parameter.TERMS), arg ->
                    ENGINE.div(arg, this.constant));
            normalizedTerms.add(Constant.ONE(TYPE));
            factors.put(ENGINE.add(normalizedTerms.toArray()), Constant.ONE(TYPE));
            return new Factorization<>(this.constant, factors, TYPE);
        }
    }

    public Expression<T> derivative(Univariate<T> var) {
        ArrayList<Expression<T>> derivativeTerms = Utils.map(Utils.<Entity, Expression<T>>cast(this.inputs.get(Parameter.TERMS)),
                arg -> arg.derivative(var));
        return ENGINE.add(derivativeTerms.toArray());
    }

    public InputType[] getInputTypes() {
        return Add.inputTypes;
    }
}
