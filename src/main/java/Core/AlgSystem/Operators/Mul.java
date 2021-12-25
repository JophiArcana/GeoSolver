package Core.AlgSystem.Operators;

import Core.AlgSystem.Constants.*;
import Core.AlgSystem.UnicardinalRings.*;
import Core.AlgSystem.UnicardinalTypes.*;
import Core.EntityTypes.*;
import Core.Utilities.*;
import com.google.common.collect.TreeMultiset;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;

public class Mul<T extends Expression<T>> extends DefinedExpression<T> {
    /** SECTION: Static Data ======================================================================================== */
    public enum Parameter implements InputType {
        TERMS,
        CONSTANT
    }
    public static final InputType[] inputTypes = {Parameter.TERMS, Parameter.CONSTANT};

    /** SECTION: Instance Variables ================================================================================= */
    public TreeMap<Expression<T>, Constant<T>> terms = new TreeMap<>(Utils.PRIORITY_COMPARATOR);
    public Constant<T> constant = Constant.ONE(TYPE);

    /** SECTION: Factory Methods ==================================================================================== */
    public static <T extends Expression<T>> Mul<T> create(Class<T> type) {
        return new Mul<>(type);
    }

    public static <T extends Expression<T>> Expression<T> create(Iterable<Expression<T>> args, Class<T> type) {
        ArrayList<Expression<T>> exprs = new ArrayList<>();
        for (Expression<T> arg : args) {
            if (arg.equalsZero()) {
                return Constant.ZERO(type);
            } else if (!arg.equalsOne()) {
                exprs.add(arg);
            }
        }
        return switch (exprs.size()) {
            case 0 -> Constant.ONE(type);
            case 1 -> exprs.get(0);
            default -> new Mul<>(exprs, type).close();
        };
    }

    public Entity createEntity(HashMap<InputType, ArrayList<Entity>> args) {
        ArrayList<Expression<T>> exprArgs = Utils.cast(args.get(Parameter.TERMS));
        exprArgs.add((Constant<T>) args.get(Parameter.CONSTANT).get(0));
        return Mul.create(exprArgs, TYPE);
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected Mul(Class<T> type) {
        super(type);
    }

    protected Mul(Iterable<Expression<T>> args, Class<T> type) {
        super(type);
        TreeMultiset<Entity> inputTerms = this.inputs.get(Parameter.TERMS);
        this.construct(args);
        // System.out.println(args + " constructed: " + terms);
        for (Map.Entry<Expression<T>, Constant<T>> entry : this.terms.entrySet()) {
            inputTerms.add(ENGINE.pow(entry.getKey(), entry.getValue()));
        }
        this.inputs.get(Parameter.CONSTANT).add(this.constant);
    }

    private void construct(Iterable<Expression<T>> args) {
        // System.out.println("Constructing " + args);
        for (Expression<T> arg : args) {
            Factorization<T> argFactor = arg.normalize();
            this.constant = this.constant.mul(argFactor.constant);
            for (Map.Entry<Expression<T>, Constant<T>> entry : argFactor.terms.entrySet()) {
                this.terms.put(entry.getKey(), entry.getValue().add(this.terms.getOrDefault(entry.getKey(), Constant.ZERO(TYPE))));
            }
            // System.out.println(arg + " of " + args + " constructed");
        }
        // System.out.println(args + " construction progress " + this.terms);
        ArrayList<Map.Entry<Expression<T>, Constant<T>>> entrySet = new ArrayList<>(this.terms.entrySet());
        for (Map.Entry<Expression<T>, Constant<T>> entry : entrySet) {
            if (entry.getValue().equalsZero()) {
                this.terms.remove(entry.getKey());
            }
        }
        // System.out.println(args + " construction complete");
    }

    /** SECTION: Print Format ======================================================================================= */
    public String toString() {
        ArrayList<Entity> inputTerms = new ArrayList<>(this.inputs.get(Parameter.TERMS));
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

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public ArrayList<Expression<Symbolic>> symbolic() {
        if (this.TYPE == Symbolic.class) {
            return new ArrayList<>(Collections.singletonList((Mul<Symbolic>) this));
        } else if (this.TYPE == DirectedAngle.class) {
            if (this.inputs.get(Parameter.TERMS).size() == 1 && this.constant instanceof Complex<T> cpx
                && cpx.isGaussianInteger() && cpx.im.equals(0)) {
                final AlgEngine<Symbolic> ENGINE = Utils.getEngine(Symbolic.class);
                int n = cpx.re.intValue();
                int k = Math.abs(n);
                Expression<Symbolic> expr = this.inputs.get(Parameter.TERMS).firstEntry().getElement().symbolic().get(0);
                ArrayList<Expression<Symbolic>> numeratorTerms = new ArrayList<>();
                ArrayList<Expression<Symbolic>> denominatorTerms = new ArrayList<>(Collections.singletonList(Complex.create(1, 0, Symbolic.class)));
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

    public InputType[] getInputTypes() {
        return Mul.inputTypes;
    }

    /** SUBSECTION: Expression ====================================================================================== */
    public Expression<T> close() {
        if (this.inputs.get(Parameter.TERMS).size() == 0) {
            return this.constant;
        } else if (this.constant.equalsZero()) {
            return Constant.ZERO(TYPE);
        } else if (this.constant.equalsOne() && this.inputs.get(Parameter.TERMS).size() == 1) {
            return (Expression<T>) this.inputs.get(Parameter.TERMS).firstEntry().getElement();
        } else {
            return this;
        }
    }

    public Factorization<T> normalize() {
        Constant<T> coefficient = this.constant;
        TreeMap<Expression<T>, Constant<T>> factors = new TreeMap<>(Utils.PRIORITY_COMPARATOR);
        for (Map.Entry<Expression<T>, Constant<T>> entryTerm : this.terms.entrySet()) {
            Factorization<T> exprFactor = entryTerm.getKey().normalize();
            coefficient = coefficient.mul(exprFactor.constant.pow(entryTerm.getValue()));
            for (Map.Entry<Expression<T>, Constant<T>> entry : exprFactor.terms.entrySet()) {
                Constant<T> result = factors.getOrDefault(entry.getKey(),
                        Constant.ZERO(TYPE)).add(entry.getValue().mul(entryTerm.getValue()));
                if (result.equalsZero()) {
                    factors.remove(entry.getKey());
                } else {
                    factors.put(entry.getKey(), result);
                }
            }
        }
        /**Set<Map.Entry<Expression<T>, Constant<T>>> entrySet = factors.entrySet();
        for (Map.Entry<Expression<T>, Constant<T>> entry : entrySet) {
            if (entry.getValue().equalsZero()) {
                factors.remove(entry.getKey());
            }
        }*/
        return new Factorization<>(coefficient, factors, TYPE);
    }

    public Expression<T> derivative(Univariate<T> var) {
        if (!this.variables().contains(var)) {
            return Constant.ZERO(TYPE);
        } else {
            ArrayList<Expression<T>> derivativeTerms = Utils.map(Utils.<Entity, Expression<T>>cast(this.inputs.get(Parameter.TERMS)), arg ->
                    ENGINE.mul(this, arg.logarithmicDerivative(var)));
            return ENGINE.add(derivativeTerms.toArray());
        }
    }

    @Override
    public Expression<T> logarithmicDerivative(Univariate<T> s) {
        if (!this.variables().contains(s)) {
            return Constant.ZERO(TYPE);
        } else {
            ArrayList<Expression<T>> derivativeTerms = Utils.map(Utils.<Entity, Expression<T>>cast(this.inputs.get(Parameter.TERMS)), arg -> arg.logarithmicDerivative(s));
            return ENGINE.add(derivativeTerms.toArray());
        }
    }
}
