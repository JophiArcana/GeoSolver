package Core.AlgSystem.Operators.AddReduction;

import Core.AlgSystem.Constants.Real;
import Core.AlgSystem.Operators.MulReduction.Mul;
import Core.AlgSystem.Operators.MulReduction.Pow;
import Core.AlgSystem.Operators.Reduction;
import Core.AlgSystem.UnicardinalRings.*;
import Core.EntityStructure.*;
import Core.EntityStructure.UnicardinalStructure.Constant;
import Core.EntityStructure.UnicardinalStructure.Expression;
import Core.Utilities.*;
import com.google.common.collect.TreeMultiset;

import java.util.*;

public class Add<T> extends Reduction<T> {
    /** SECTION: Factory Methods ==================================================================================== */
    public static <T> Expression<T> create(Iterable<Expression<T>> args, Class<T> type) {
        return new Add<T>(args, type).close();
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected Add(Iterable<Expression<T>> args, Class<T> type) {
        super(args, type);

        TreeMultiset<Entity> inputTerms = this.inputs.get(Parameter.TERMS);
        for (Map.Entry<Expression<T>, Double> entry : new ArrayList<>(this.terms.entrySet())) {
            if (entry.getValue() == 0) {
                this.terms.remove(entry.getKey());
            } else {
                inputTerms.add(Scale.create(entry.getValue(), entry.getKey(), TYPE));
            }
        }
    }

    protected void construct(Iterable<Expression<T>> args) {
        ArrayList<Expression<T>> validArgs = new ArrayList<>();
        args.forEach(arg -> {
            if (!arg.equalsZero()) {
                validArgs.add(arg);
            }
        });
        if (validArgs.size() == 0) {
            return;
        }
        this.degree = validArgs.get(0).getDegree();
        boolean directed = TYPE != Symbolic.class;
        for (Expression<T> arg : validArgs) {
            assert directed || arg.getDegree() == this.degree: args + ": Degree mismatch: deg(" + arg + ") \u2260 " + this.degree + ", deg(" + validArgs.get(0) + ")";
            if (arg instanceof Add<T> addExpr) {
                this.construct(Utils.cast(addExpr.inputs.get(Parameter.TERMS)));
            } else if (arg instanceof Scale<T> sc) {
                this.terms.put(sc.expression, this.terms.getOrDefault(sc.expression, 0.0) + sc.coefficient);
            } else if (arg instanceof Real<T> re) {
                this.terms.put(Constant.ONE(TYPE), this.terms.getOrDefault(Constant.ONE(TYPE), 0.0) + re.value);
            } else {
                this.terms.put(arg, this.terms.getOrDefault(arg, 0.0) + 1);
            }
        }
    }

    /** SECTION: Print Format ======================================================================================= */
    public String toString() {
        return String.join(" + ", Utils.map(inputs.get(Parameter.TERMS), Object::toString));
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public ArrayList<Expression<Symbolic>> symbolic() {
        if (this.TYPE == Symbolic.class) {
            return new ArrayList<>(List.of((Expression<Symbolic>) this));
        } else if (this.TYPE == DirectedAngle.class) {
            ArrayList<Expression<T>> terms = Utils.cast(this.inputs.get(Parameter.TERMS));

            ArrayList<ArrayList<HashSet<Expression<T>>>> subsets = Utils.sortedSubsets(terms);
            ArrayList<Expression<Symbolic>> numeratorTerms = new ArrayList<>();
            ArrayList<Expression<Symbolic>> denominatorTerms = new ArrayList<>(List.of(Constant.ONE(Symbolic.class)));
            for (int i = 1; i < subsets.size(); i++) {
                ArrayList<Expression<Symbolic>> symbolics = Utils.map(subsets.get(i),
                        subset -> Mul.create(Utils.map(subset, arg -> arg.symbolic().get(0)), Symbolic.class));
                Expression<Symbolic> symmetricSum = Add.create(symbolics, Symbolic.class);
                switch (i % 4) {
                    case 0:
                        denominatorTerms.add(symmetricSum);
                    case 1:
                        numeratorTerms.add(symmetricSum);
                    case 2:
                        denominatorTerms.add(Scale.create(-1, symmetricSum, Symbolic.class));
                    case 3:
                        numeratorTerms.add(Scale.create(-1, symmetricSum, Symbolic.class));
                }
            }
            return new ArrayList<>(List.of(Utils.getEngine(Symbolic.class).div(
                    Add.create(numeratorTerms, Symbolic.class),
                    Add.create(denominatorTerms, Symbolic.class)
            )));
        } else {
            return null;
        }
    }

    /** SUBSECTION: Expression ====================================================================================== */
    public Expression<T> reduce() {
        if (this.reduction == null) {
            ArrayList<Expression<T>> exprs = Utils.map(Utils.<Entity, Expression<T>>cast(this.inputs.get(Parameter.TERMS)), Expression::reduce);
            Expression<T> gcd = Utils.getEngine(TYPE).greatestCommonDivisor(exprs);
            if (gcd.equalsOne()) {
                this.reduction = this;
            } else {
                Expression<T> gcdInverse = Pow.create(gcd, -1, TYPE);
                ArrayList<Expression<T>> normalizedTerms = Utils.map(exprs, arg -> Mul.create(List.of(gcdInverse, arg), TYPE));

                /**
                 if (normalizedTerms.size() <= 16) {
                 GCDGraph<T> reducedGraph = ENGINE.GCDReduction(normalizedTerms);
                 normalizedTerms = Utils.setParse(reducedGraph.elements, reducedGraph.binaryRepresentation);
                 }
                 */

                this.reduction = Mul.create(List.of(gcd, Add.create(normalizedTerms, TYPE)), TYPE);
            }
        }
        return this.reduction;
    }

    public Expression<T> expand() {
        if (this.expansion == null) {
            this.expansion = Add.create(Utils.map(Utils.<Entity, Expression<T>>cast(this.inputs.get(Parameter.TERMS)), Expression::expand), TYPE);
        }
        return this.expansion;
    }

    public Expression<T> close() {
        if (this.terms.size() == 0) {
            return Constant.ZERO(TYPE);
        } else if (this.terms.size() == 1) {
            return (Expression<T>) this.inputs.get(Parameter.TERMS).firstEntry().getElement();
        } else {
            return this;
        }
    }
}
