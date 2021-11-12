package Core.AlgeSystem.Operators;

import Core.AlgeSystem.Constants.*;
import Core.AlgeSystem.UnicardinalRings.*;
import Core.AlgeSystem.UnicardinalTypes.*;
import Core.EntityTypes.*;
import Core.Utilities.*;
import com.google.common.collect.TreeMultiset;

import java.util.*;

public class Mul<T extends Expression<T>> extends DefinedExpression<T> {
    public static final String[] inputTypes = {"Terms", "Constant"};

    public Entity create(HashMap<String, ArrayList<Entity>> args) {
        return ENGINE.mul(args.get("Constant").get(0), ENGINE.mul(args.get("Terms").toArray()));
    }

    public TreeMap<Expression<T>, Constant<T>> terms = new TreeMap<>(Utils.PRIORITY_COMPARATOR);
    public Constant<T> constant = Constant.ONE(TYPE);

    public Mul(Class<T> type) {
        super(type);
    }

    public Mul(Iterable<Expression<T>> args, Class<T> type) {
        super(type);
        TreeMultiset<Entity> inputTerms = this.inputs.get("Terms");
        this.construct(args);
        // System.out.println(args + " constructed: " + terms);
        for (Map.Entry<Expression<T>, Constant<T>> entry : this.terms.entrySet()) {
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
        if (this.constant.equalsOne()) {
            return String.join("", stringTerms);
        } else if (this.constant.equals(Constant.NONE(TYPE))) {
            return "-" + String.join("", stringTerms);
        } else {
            return this.constant + String.join("", stringTerms);
        }
    }

    private void construct(Iterable<Expression<T>> args) {
        // System.out.println("Constructing " + args);
        for (Expression<T> arg : args) {
            Factorization<T> argFactor = arg.normalize();
            constant = constant.mul(argFactor.constant);
            for (Map.Entry<Expression<T>, Constant<T>> entry : argFactor.terms.entrySet()) {
                terms.put(entry.getKey(), entry.getValue().add(terms.getOrDefault(entry.getKey(), Constant.ZERO(TYPE))));
            }
            // System.out.println(arg + " of " + args + " constructed");
        }
        // System.out.println(args + " construction progress " + this.terms);
        ArrayList<Map.Entry<Expression<T>, Constant<T>>> entrySet = new ArrayList<>(terms.entrySet());
        for (Map.Entry<Expression<T>, Constant<T>> entry : entrySet) {
            if (entry.getValue().equalsZero()) {
                terms.remove(entry.getKey());
            }
        }
        // System.out.println(args + " construction complete");
    }

    public ArrayList<Expression<Symbolic>> symbolic() {
        if (this.TYPE == Symbolic.class) {
            return new ArrayList<>(Collections.singletonList((Mul<Symbolic>) this));
        } else if (this.TYPE == DirectedAngle.class) {
            if (this.inputs.get("Terms").size() == 1 && this.constant instanceof Complex<T> cpx
                && cpx.gaussianInteger() && cpx.im.equals(0)) {
                final AlgeEngine<Symbolic> ENGINE = Utils.getEngine(Symbolic.class);
                int n = cpx.re.intValue();
                int k = Math.abs(n);
                Expression<Symbolic> expr = this.inputs.get("Terms").firstEntry().getElement().symbolic().get(0);
                ArrayList<Expression<Symbolic>> numeratorTerms = new ArrayList<>();
                ArrayList<Expression<Symbolic>> denominatorTerms = new ArrayList<>(Collections.singletonList(Constant.ONE(Symbolic.class)));
                for (int i = 1; i <= k; i++) {
                    Expression<Symbolic> symbolic = ENGINE.mul(Utils.binomial(k, i), ENGINE.pow(expr, i));
                    switch (i % 4) {
                        case 0:
                            denominatorTerms.add(symbolic);
                        case 1:
                            numeratorTerms.add(symbolic);
                        case 2:
                            denominatorTerms.add(ENGINE.negate(symbolic));
                        case 3:
                            numeratorTerms.add(ENGINE.negate(symbolic));
                    }
                }
                Expression<Symbolic> sum = ENGINE.div(ENGINE.add(numeratorTerms.toArray()),
                        ENGINE.add(denominatorTerms.toArray()));
                return new ArrayList<>(Collections.singletonList((n > 0) ? sum : ENGINE.negate(sum)));
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public Expression<T> close() {
        if (this.inputs.get("Terms").size() == 0) {
            return this.constant;
        } else if (this.constant.equalsZero()) {
            return Constant.ZERO(TYPE);
        } else if (this.constant.equalsOne() && this.inputs.get("Terms").size() == 1) {
            return (Expression<T>) this.inputs.get("Terms").firstEntry().getElement();
        } else {
            return this;
        }
    }

    public Factorization<T> normalize() {
        Constant<T> coefficient = this.constant;
        TreeMap<Expression<T>, Constant<T>> factors = new TreeMap<>(Utils.PRIORITY_COMPARATOR);
        for (Entity ent : this.inputs.get("Terms")) {
            Factorization<T> entFactor = ((Expression<T>) ent).normalize();
            coefficient = coefficient.mul(entFactor.constant);
            for (Map.Entry<Expression<T>, Constant<T>> entry : entFactor.terms.entrySet()) {
                factors.put(entry.getKey(), factors.getOrDefault(entry.getKey(), Constant.ZERO(TYPE)).add(entry.getValue()));
                if (factors.get(entry.getKey()).equalsZero()) {
                    factors.remove(entry.getKey());
                }
            }
        }
        return new Factorization<>(coefficient, factors, TYPE);
    }

    public Expression<T> derivative(Univariate<T> var) {
        if (!this.variables().contains(var)) {
            return Constant.ZERO(TYPE);
        } else {
            ArrayList<Expression<T>> derivativeTerms = Utils.map(Utils.<Entity, Expression<T>>cast(this.inputs.get("Terms")), arg ->
                    ENGINE.mul(this, arg.logarithmicDerivative(var)));
            return ENGINE.add(derivativeTerms.toArray());
        }
    }

    @Override
    public Expression<T> logarithmicDerivative(Univariate<T> s) {
        if (!this.variables().contains(s)) {
            return Constant.ZERO(TYPE);
        } else {
            ArrayList<Expression<T>> derivativeTerms = Utils.map(Utils.<Entity, Expression<T>>cast(this.inputs.get("Terms")), arg -> arg.logarithmicDerivative(s));
            return ENGINE.add(derivativeTerms.toArray());
        }
    }

    public String[] getInputTypes() {
        return Mul.inputTypes;
    }
}
