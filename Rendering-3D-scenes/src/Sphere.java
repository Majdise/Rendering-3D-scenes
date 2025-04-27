
public class Sphere extends Surfaces {
	private Point center, intersectionPoint; // will be used to calculate the normal
	private double radius;

	public Sphere(Point center, double radius, int indx) {
		this.center = center;
		this.radius = radius;
		this.index_by_material = (indx - 1);
		myType = type.sphere;
	}
	
	public type getType() {
		return type.sphere;
	}

	/** if exists, return the intersection point  */
	public double getIntersection(Point point, Vector rayDirect) {
		Vector L = new Vector(point, center);
		double tca = Vector.dotProduct(L, rayDirect);
		if (tca < 0)
			return (-1);
		double r = (Vector.dotProduct(L, L) - (tca * tca));
		if (radius * radius<r )
			return (-1);
		return (tca - Math.sqrt(radius * radius - r));
	}
	
	void setIntersectionPoint(Point intersectionPoint) {
		this.intersectionPoint = intersectionPoint;
	}
	
	/** create sphere normal */
	public Vector getNormal() {
		Vector normal = new Vector(center,intersectionPoint);
		normal.normalise();
		return normal;
	}

	public String toString() {
		return "Sp.: r=" + radius + ", c=" + center;
	}
}
