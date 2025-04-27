
public class InfinitePlane extends Surfaces {
	private double a, b, c, d;
	
	InfinitePlane(double a, double b, double c, double offset, int indx) {
		Vector normal = new Vector(a, b, c) ;
		normal.normalise();
		this.a = normal.x;
		this.b = normal.y;
		this.c = normal.z;
		this.d = offset ;
		this.index_by_material = (indx - 1);
		myType = type.infinitePlane;
	}

	@Override
	public type getType() {
		return type.infinitePlane;
	}

	@Override
	public double getIntersection(Point point, Vector v) {
		Vector normal = getNormal();
		return (-(Vector.pointMulVector(point, normal) - this.d) / (Vector.dotProduct(v, normal)));
	}
	
	public Vector getNormal() {
		return new Vector(a, b, c);
	}
	
	@Override
	public String toString() {
		return "IP(a=" + a + ", b=" + b + ", c=" + c + ", d=" + d + ")";
	}

}
