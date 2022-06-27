package core.structure.multicardinal.geo.triangle;

import core.structure.multicardinal.*;
import core.structure.multicardinal.geo.circle.structure.DefinedCircle;
import core.structure.multicardinal.geo.point.structure.*;
import core.structure.unicardinal.alg.symbolic.SymbolicExpression;
import core.structure.unicardinal.alg.symbolic.operator.*;
import core.util.Utils;

import java.util.*;

public class Triangle extends DefinedMulticardinal implements Triangulate {
    /** SECTION: Instance Variables ================================================================================= */
    public Point A, B, C;
    public SymbolicExpression[] vectorBC, vectorCA, vectorAB;
    public SymbolicExpression doubleSignedArea, perimeter, BC, CA, AB;
    public SymbolicExpression[] vectorSumABC;

    public Circumcenter CIRCUMCENTER;
    public Circumcircle CIRCUMCIRCLE;

    public Incenter INCENTER;
    public Incircle INCIRCLE;

    public Orthocenter ORTHOCENTER;
    public Centroid CENTROID;

    /** SECTION: Factory Methods ==================================================================================== */
    public static Triangle create(Point A, Point B, Point C) {
        return new Triangle(A, B, C, true);
    }

    public static Triangle create(Point A, Point B, Point C, boolean anon) {
        return new Triangle(A, B, C, anon);
    }

    /** SECTION: Protected Constructors ============================================================================= */
    protected Triangle(Point A, Point B, Point C, boolean anon) {
        super('\u25B3' + A.getName() + B.getName() + C.getName(), anon);
        this.A = A;
        this.B = B;
        this.C = C;
        this.getInputs(Triangle.VERTICES).addAll(List.of(this.A, this.B, this.C));
    }

    /** SECTION: Triangle Centers =================================================================================== */
    /** SUBSECTION: TriangleCenter ==================================================================================== */
    public abstract class TriangleCenter extends DefinedPoint implements Triangulate {
        protected TriangleCenter(String n, boolean anon) {
            super(n, anon);
            this.inputs = Triangle.this.inputs;
        }
    }

    /** SUBSECTION: Circumcenter ==================================================================================== */
    public class Circumcenter extends TriangleCenter {
        public SymbolicExpression inverseQuadSignedArea;

        protected Circumcenter(String n, boolean anon) {
            super(n, anon);
            this.inverseQuadSignedArea = SymbolicPow.create(SymbolicScale.create(2, Triangle.this.doubleSignedArea()), -1);
            List<SymbolicExpression> n2Vector = Utils.map(List.of(
                    Triangle.this.A.symbolic(),
                    Triangle.this.B.symbolic(),
                    Triangle.this.C.symbolic()
            ), arg -> Utils.ENGINE.norm2(arg.get(0), arg.get(1)));
            SymbolicExpression xNum = SymbolicAdd.create(
                    SymbolicMul.create(n2Vector.get(0), Triangle.this.vectorBC()[1]),
                    SymbolicMul.create(n2Vector.get(1), Triangle.this.vectorCA()[1]),
                    SymbolicMul.create(n2Vector.get(2), Triangle.this.vectorAB()[1])
            );
            SymbolicExpression yNum = SymbolicScale.create(-1, SymbolicAdd.create(
                    SymbolicMul.create(n2Vector.get(0), Triangle.this.vectorBC()[0]),
                    SymbolicMul.create(n2Vector.get(1), Triangle.this.vectorCA()[0]),
                    SymbolicMul.create(n2Vector.get(2), Triangle.this.vectorAB()[0])
            ));
            this.symbolic = new ArrayList<>(List.of(
                    SymbolicMul.create(xNum, this.inverseQuadSignedArea),
                    SymbolicMul.create(yNum, this.inverseQuadSignedArea)
            ));
            if (!this.anonymous) {
                this.setNode();
            }
        }
    }

    public class Circumcircle extends DefinedCircle implements Triangulate {
        protected Circumcircle(String n, boolean anon) {
            super(n, anon);
            this.inputs = Triangle.this.inputs;

            this.center = Triangle.this.circumcenter('\u2D59' + n, anon);
            this.radius = SymbolicMul.create(
                    Triangle.this.BC(),
                    Triangle.this.CA(),
                    Triangle.this.AB(),
                    SymbolicAbs.create(((Circumcenter) this.center).inverseQuadSignedArea)
            );
            this.center.symbolic.add(this.radius);
            if (!this.anonymous) {
                this.setNode();
            }
        }
    }

    public Circumcenter circumcenter(String n, boolean anon) {
        if (this.CIRCUMCENTER == null) {
            this.CIRCUMCENTER = this.new Circumcenter(n, anon);
        } else if (this.CIRCUMCENTER.anonymous && !anon) {
            this.CIRCUMCENTER.name = n;
            this.CIRCUMCENTER.anonymous = false;
            this.CIRCUMCENTER.setNode();
        }
        return this.CIRCUMCENTER;
    }

    public Circumcircle circumcircle(String n, boolean anon) {
        this.circumcenter(n, anon);
        if (this.CIRCUMCIRCLE == null) {
            this.CIRCUMCIRCLE = this.new Circumcircle(n, anon);
        } else if (this.CIRCUMCIRCLE.anonymous && !anon) {
            this.CIRCUMCIRCLE.name = n;
            this.CIRCUMCIRCLE.anonymous = false;
            this.CIRCUMCIRCLE.setNode();
        }
        return this.CIRCUMCIRCLE;
    }

    /** SUBSECTION: Incenter ======================================================================================== */
    public class Incenter extends TriangleCenter {
        public SymbolicExpression inversePerimeter;

        protected Incenter(String n, boolean anon) {
            super(n, anon);
            this.inversePerimeter = SymbolicPow.create(Triangle.this.perimeter(), -1);
            List<SymbolicExpression> A_symbolic = Triangle.this.A.symbolic();
            List<SymbolicExpression> B_symbolic = Triangle.this.B.symbolic();
            List<SymbolicExpression> C_symbolic = Triangle.this.C.symbolic();
            SymbolicExpression xNum = SymbolicAdd.create(
                    SymbolicMul.create(Triangle.this.BC(), A_symbolic.get(0)),
                    SymbolicMul.create(Triangle.this.CA(), B_symbolic.get(0)),
                    SymbolicMul.create(Triangle.this.AB(), C_symbolic.get(0))
            );
            SymbolicExpression yNum = SymbolicAdd.create(
                    SymbolicMul.create(Triangle.this.BC(), A_symbolic.get(1)),
                    SymbolicMul.create(Triangle.this.CA(), B_symbolic.get(1)),
                    SymbolicMul.create(Triangle.this.AB(), C_symbolic.get(1))
            );
            this.symbolic = new ArrayList<>(List.of(
                    SymbolicMul.create(xNum, this.inversePerimeter),
                    SymbolicMul.create(yNum, this.inversePerimeter)
            ));
            if (!this.anonymous) {
                this.setNode();
            }
        }
    }

    public class Incircle extends DefinedCircle implements Triangulate {
        protected Incircle(String n, boolean anon) {
            super(n, anon);
            this.inputs = Triangle.this.inputs;

            this.center = Triangle.this.incenter('\u2D59' + n, anon);
            this.radius = SymbolicMul.create(
                    Triangle.this.doubleSignedArea(),
                    SymbolicAbs.create(((Incenter) this.center).inversePerimeter)
            );
            this.center.symbolic.add(this.radius);
            if (!this.anonymous) {
                this.setNode();
            }
        }
    }

    public Incenter incenter(String n, boolean anon) {
        if (this.INCENTER == null) {
            this.INCENTER = this.new Incenter(n, anon);
        } else if (this.INCENTER.anonymous && !anon) {
            this.INCENTER.name = n;
            this.INCENTER.anonymous = false;
            this.INCENTER.setNode();
        }
        return this.INCENTER;
    }

    public Incircle incircle(String n, boolean anon) {
        if (this.INCIRCLE == null) {
            this.INCIRCLE = this.new Incircle(n, anon);
        } else if (this.INCIRCLE.anonymous && !anon) {
            this.INCIRCLE.name = n;
            this.INCIRCLE.anonymous = false;
            this.INCIRCLE.setNode();
        }
        return this.INCIRCLE;
    }

    /** SUBSECTION: Orthocenter ===================================================================================== */
    public class Orthocenter extends TriangleCenter {
        protected Orthocenter(String n, boolean anon) {
            super(n, anon);
            List<SymbolicExpression> circumcenter = Triangle.this.circumcenter(Utils.randomHash(), true).symbolic;
            this.symbolic = List.of(
                    Utils.ENGINE.sub(Triangle.this.vectorSumABC()[0], SymbolicScale.create(2, circumcenter.get(0))),
                    Utils.ENGINE.sub(Triangle.this.vectorSumABC()[1], SymbolicScale.create(2, circumcenter.get(1)))
            );
            if (!this.anonymous) {
                this.setNode();
            }
        }
    }

    public Orthocenter orthocenter(String n, boolean anon) {
        if (this.ORTHOCENTER == null) {
            this.ORTHOCENTER = this.new Orthocenter(n, anon);
        } else if (this.ORTHOCENTER.anonymous && !anon) {
            this.ORTHOCENTER.name = n;
            this.ORTHOCENTER.anonymous = false;
            this.ORTHOCENTER.setNode();
        }
        return this.ORTHOCENTER;
    }

    /** SUBSECTION: Centroid ======================================================================================== */
    public class Centroid extends TriangleCenter {
        protected Centroid(String n, boolean anon) {
            super(n, anon);
            this.symbolic = List.of(
                    SymbolicScale.create(1.0 / 3, Triangle.this.vectorSumABC()[0]),
                    SymbolicScale.create(1.0 / 3, Triangle.this.vectorSumABC()[1])
            );
            if (!this.anonymous) {
                this.setNode();
            }
        }
    }

    public Centroid centroid(String n, boolean anon) {
        if (this.CENTROID == null) {
            this.CENTROID = this.new Centroid(n, anon);
        } else if (this.CENTROID.anonymous && !anon) {
            this.CENTROID.name = n;
            this.CENTROID.anonymous = false;
            this.CENTROID.setNode();
        }
        return this.CENTROID;
    }

    /** SECTION: Triangle Data ====================================================================================== */
    public SymbolicExpression doubleSignedArea() {
        if (this.doubleSignedArea == null) {
            List<SymbolicExpression> A_symbolic = this.A.symbolic();
            List<SymbolicExpression> B_symbolic = this.B.symbolic();
            List<SymbolicExpression> C_symbolic = this.C.symbolic();
            this.doubleSignedArea = SymbolicAdd.create(
                    SymbolicMul.create(A_symbolic.get(0), Utils.ENGINE.sub(B_symbolic.get(1), C_symbolic.get(1))),
                    SymbolicMul.create(B_symbolic.get(0), Utils.ENGINE.sub(C_symbolic.get(1), A_symbolic.get(1))),
                    SymbolicMul.create(C_symbolic.get(0), Utils.ENGINE.sub(A_symbolic.get(1), B_symbolic.get(1)))
            );
        }
        return this.doubleSignedArea;
    }

    public SymbolicExpression perimeter() {
        if (this.perimeter == null) {
            this.perimeter = SymbolicAdd.create(this.BC(), this.CA(), this.AB());
        }
        return this.perimeter;
    }

    public SymbolicExpression[] vectorBC() {
        if (this.vectorBC == null) {
            List<SymbolicExpression> B_symbolic = this.B.symbolic();
            List<SymbolicExpression> C_symbolic = this.C.symbolic();
            this.vectorBC = new SymbolicExpression[] {
                    Utils.ENGINE.sub(B_symbolic.get(0), C_symbolic.get(0)),
                    Utils.ENGINE.sub(B_symbolic.get(1), C_symbolic.get(1)),
            };
        }
        return this.vectorBC;
    }

    public SymbolicExpression[] vectorCA() {
        if (this.vectorCA == null) {
            List<SymbolicExpression> C_symbolic = this.C.symbolic();
            List<SymbolicExpression> A_symbolic = this.A.symbolic();
            this.vectorCA = new SymbolicExpression[] {
                    Utils.ENGINE.sub(C_symbolic.get(0), A_symbolic.get(0)),
                    Utils.ENGINE.sub(C_symbolic.get(1), A_symbolic.get(1)),
            };
        }
        return this.vectorCA;
    }

    public SymbolicExpression[] vectorAB() {
        if (this.vectorAB == null) {
            List<SymbolicExpression> A_symbolic = this.A.symbolic();
            List<SymbolicExpression> B_symbolic = this.B.symbolic();
            this.vectorAB = new SymbolicExpression[] {
                    Utils.ENGINE.sub(A_symbolic.get(0), B_symbolic.get(0)),
                    Utils.ENGINE.sub(A_symbolic.get(1), B_symbolic.get(1)),
            };
        }
        return this.vectorAB;
    }

    public SymbolicExpression BC() {
        if (this.BC == null) {
            this.BC = Utils.ENGINE.norm(this.vectorBC()[0], this.vectorBC()[1]);
        }
        return this.BC;
    }

    public SymbolicExpression CA() {
        if (this.CA == null) {
            this.CA = Utils.ENGINE.norm(this.vectorCA()[0], this.vectorCA()[1]);
        }
        return this.CA;
    }

    public SymbolicExpression AB() {
        if (this.AB == null) {
            this.AB = Utils.ENGINE.norm(this.vectorAB()[0], this.vectorAB()[1]);
        }
        return this.AB;
    }

    public SymbolicExpression[] vectorSumABC() {
        if (this.vectorSumABC == null) {
            this.vectorSumABC = new SymbolicExpression[] {
                    SymbolicAdd.create(
                            this.A.symbolic().get(0),
                            this.B.symbolic().get(0),
                            this.C.symbolic().get(0)
                    ),
                    SymbolicAdd.create(
                            this.A.symbolic().get(1),
                            this.B.symbolic().get(1),
                            this.C.symbolic().get(1)
                    ),
            };
        }
        return this.vectorSumABC;
    }

    /** SECTION: Implementation ===================================================================================== */
    /** SUBSECTION: Entity ========================================================================================== */
    public List<SymbolicExpression> symbolic() {
        return List.of(
                this.A.symbolic().get(0), this.A.symbolic().get(1),
                this.B.symbolic().get(0), this.B.symbolic().get(1),
                this.C.symbolic().get(0), this.C.symbolic().get(1)
        );
    }
}
