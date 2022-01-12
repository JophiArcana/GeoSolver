package Core.AlgSystem.Operators;

import Core.AlgSystem.UnicardinalRings.*;
import Core.AlgSystem.UnicardinalTypes.*;
import Core.EntityTypes.*;
import Core.Utilities.*;
import com.google.common.collect.TreeMultiset;

import java.util.*;

public class Mul<T> extends Accumulation<T> {
    /** SECTION: Static Data ======================================================================================== */
    private static Constant constant;

    /** SECTION: Instance Variables ================================================================================= */
    public TreeMap<Expression<T>, Double> terms = new TreeMap<>();

    /** SECTION: Factory Methods ==================================================================================== */
    public static <T> Expression<T> create(Iterable<Expression<T>> args, Class<T> type) {
        Expression<T> result = new Mul<>(args, type).close();
        if (Mul.constant.equalsOne()) {
            return result;
        } else {
            return Scale.create(Mul.constant, result, type);
        }
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected Mul(Iterable<Expression<T>> args, Class<T> type) {
        super(args, type);

        TreeMultiset<Entity> inputTerms = this.inputs.get(Parameter.TERMS);
        // System.out.println(args + " constructed: " + terms);
        for (Map.Entry<Expression<T>, Double> entry : new ArrayList<>(this.terms.entrySet())) {
            if (entry.getValue() == 0) {
                this.terms.remove(entry.getKey());
            } else {
                inputTerms.add(Pow.create(entry.getKey(), entry.getValue(), TYPE));
            }
        }
    }

    protected void construct(Iterable<Expression<T>> args) {
        Mul.constant = Constant.ONE(TYPE);
        // System.out.println("Constructing " + args);
        for (Expression<T> arg : args) {
            this.degree += arg.getDegree();
            if (arg instanceof Scale<T> sc) {
                Mul.constant = Mul.constant.mul(sc.coefficient);
                this.terms.put(sc.expression, this.terms.getOrDefault(sc.expression, 0.0) + 1);
            } else if (arg instanceof Mul<T> m) {
                Constant<T> current = Mul.constant;
                this.construct(Utils.cast(m.inputs.get(Parameter.TERMS)));
                Mul.constant = Mul.constant.mul(current);
            } else if (arg instanceof Pow<T> p) {
                this.terms.put(p.base, this.terms.getOrDefault(p.base, 0.0) + p.exponent);
            } else if (arg instanceof Constant<T> c) {
                Mul.constant = Mul.constant.mul(c);
            } else {
                this.terms.put(arg, this.terms.getOrDefault(arg, 0.0) + 1);
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
        return String.join("", stringTerms);
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public ArrayList<Expression<Symbolic>> symbolic() {
        if (this.TYPE == Symbolic.class) {
            return new ArrayList<>(List.of((Mul<Symbolic>) this));
        } else if (this.TYPE == DirectedAngle.class) {
            return null;
        } else {
            return null;
        }
    }

    public InputType[] getInputTypes() {
        return Mul.inputTypes;
    }

    /** SUBSECTION: Expression ====================================================================================== */
    public Expression<T> reduce() {
        if (this.reduction == null) {
            this.reduction = Mul.create(Utils.map(Utils.<Entity, Expression<T>>cast(this.inputs.get(Parameter.TERMS)), Expression::reduce), TYPE);
        }
        return this.reduction;
    }

    public Expression<T> expand() {
        if (this.expansion == null) {
            ArrayList<Expression<T>> expansions = Utils.map(this.inputs.get(Parameter.TERMS), arg -> ((Expression<T>) arg).expand());
            ArrayList<Expression<T>> singletons = new ArrayList<>();
            ArrayList<Expression<T>> expandedTerms = new ArrayList<>(List.of(Constant.ONE(TYPE)));
            for (Expression<T> expr : expansions) {
                if (expr instanceof Add<T> addExpr) {
                    ArrayList<Expression<T>> newExpandedTerms = new ArrayList<>();
                    for (Expression<T> term : Utils.<Entity, Expression<T>>cast(addExpr.inputs.get(Accumulation.Parameter.TERMS))) {
                        expandedTerms.forEach(arg -> newExpandedTerms.add(Mul.create(List.of(arg, term), TYPE)));
                    }
                    expandedTerms = newExpandedTerms;
                } else {
                    singletons.add(expr);
                }
            }
            Expression<T> singleton = Mul.create(singletons, TYPE);
            this.expansion = Add.create(Utils.map(expandedTerms, arg -> Mul.create(List.of(arg, singleton), TYPE)), TYPE);
        }
        return this.expansion;
    }

    public Expression<T> close() {
        if (this.terms.size() == 0) {
            return Constant.ONE(TYPE);
        } else if (this.terms.size() == 1) {
            return (Expression<T>) this.inputs.get(Parameter.TERMS).firstEntry().getElement();
        } else {
            return this;
        }
    }
}
