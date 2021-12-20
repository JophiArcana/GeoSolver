package Core.GeoSystem.Points.PointTypes;

import Core.AlgSystem.UnicardinalRings.Symbolic;
import Core.AlgSystem.UnicardinalTypes.Expression;
import Core.GeoSystem.MulticardinalTypes.DefinedMulticardinal;

import java.util.ArrayList;

public abstract class DefinedPoint extends DefinedMulticardinal implements Point {
    public ArrayList<Expression<Symbolic>> symbolic;

    public DefinedPoint(String n) {
        super(n);
    }

    public ArrayList<Expression<Symbolic>> symbolic() {
        if (this.symbolic == null) {
            this.symbolic = this.computeSymbolic();
        }
        return this.symbolic;
    }

    protected abstract ArrayList<Expression<Symbolic>> computeSymbolic();
}
