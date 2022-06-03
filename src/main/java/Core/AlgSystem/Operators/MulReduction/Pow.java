package Core.AlgSystem.Operators.MulReduction;

import Core.AlgSystem.Constants.Real;
import Core.AlgSystem.Operators.*;
import Core.AlgSystem.Operators.AddReduction.*;
import Core.AlgSystem.UnicardinalRings.*;
import Core.Diagram;
import Core.EntityStructure.UnicardinalStructure.*;
import Core.Utilities.*;
import com.google.common.collect.TreeMultiset;

import java.util.*;

public class Pow<T> extends Accumulation<T> {
    /** SECTION: Factory Methods ==================================================================================== */
    public static <T> Expression<T> create(Diagram d, Expression<T> base, double exponent, Class<T> type) {
        return new Pow<>(d, base, exponent, type).close();
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected Pow(Diagram d, Expression<T> base, double exponent, Class<T> type) {
        super(d, exponent, base, type);
    }

    /** SECTION: Print Format ======================================================================================= */
    public String toString() {
        String baseString = this.expression.toString();
        if (!Utils.CLOSED_FORM.contains(this.expression.getClass())) {
            baseString = "(" + baseString + ")";
        }
        if (this.coefficient % 1 == 0) {
            return baseString + " ** " + (int) this.coefficient;
        } else {
            return baseString + " ** " + this.coefficient;
        }
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public ArrayList<Expression<Symbolic>> symbolic() {
        if (this.TYPE == Symbolic.class) {
            return new ArrayList<>(List.of((Pow<Symbolic>) this));
        } else if (this.TYPE == DirectedAngle.class) {
            return null;
        } else {
            return null;
        }
    }

    /** SUBSECTION: Expression ====================================================================================== */
    public Expression<T> reduce() {
        if (this.reduction == null) {
            this.reduction = Pow.create(this.diagram, this.expression.reduce(), this.coefficient, TYPE);
        }
        return this.reduction;
    }

    public Expression<T> expand() {
        if (this.expansion == null) {
            if (this.expression instanceof Mul<T> mulExpr) {
                this.expansion = Mul.create(this.diagram, Utils.map((TreeMultiset<Expression<T>>) mulExpr.inputs.get(Mul.Parameter.TERMS),
                        arg -> Pow.create(this.diagram, arg, this.coefficient, TYPE)), TYPE).expand();
            } else if (this.expression instanceof Scale<T> scaleExpr) {
                this.expansion = Scale.create(Math.pow(scaleExpr.coefficient, this.coefficient),
                        Pow.create(this.diagram, scaleExpr.expression, this.coefficient, TYPE).expand(), TYPE);
            } else if (this.coefficient >= 1 && this.expression instanceof Add<T> addExpr) {
                ArrayList<Expression<T>> expandedTerms = this.expandHelper(Utils.cast(addExpr.inputs.get(Reduction.Parameter.TERMS)), (int) this.coefficient);
                this.expansion = Add.create(expandedTerms, TYPE);
                if (this.coefficient % 1 != 0) {
                    this.expansion = Mul.create(this.diagram, List.of(Pow.create(this.diagram, this.expression, this.coefficient % 1, TYPE), this.expansion), TYPE);
                }
            } else {
                this.expansion = this;
            }
        }
        return this.expansion;
    }

    private ArrayList<Expression<T>> expandHelper(ArrayList<Expression<T>> terms, int n) {
        if (n == 1) {
            return terms;
        } else {
            ArrayList<Expression<T>> sqrt = expandHelper(terms, n / 2);
            ArrayList<Expression<T>> result = new ArrayList<>();
            for (int i = 0; i < sqrt.size(); i++) {
                for (int j = 0; j < i; j++) {
                    result.add(Scale.create(2, Mul.create(List.of(sqrt.get(i), sqrt.get(j)), TYPE), TYPE));
                }
                result.add(Pow.create(sqrt.get(i), 2, TYPE));
            }
            if (n % 2 == 0) {
                return result;
            } else {
                ArrayList<Expression<T>> newResult = new ArrayList<>();
                for (Expression<T> term : terms) {
                    result.forEach(arg -> newResult.add(Mul.create(List.of(term, arg), TYPE)));
                }
                return newResult;
            }
        }
    }

    public int getDegree() {
        if (this.TYPE == Symbolic.class) {
            return (int) (this.coefficient * this.expression.getDegree());
        } else {
            return 0;
        }
    }

    /** SUBSECTION: Multiplicity ==================================================================================== */
    protected Real<T> identity() {
        return Constant.ONE(TYPE);
    }

    protected Constant<T> evaluateConstant(double c, Constant<T> e) {
        return e.pow(c);
    }
}

