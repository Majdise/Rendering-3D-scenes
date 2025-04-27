public abstract class Surfaces {
	type myType;
	int index_by_material;

	public enum type {
		box, sphere, infinitePlane
	}

	public abstract type getType();

	public abstract double getIntersection(Point p, Vector v);

	public abstract String toString();
	
	public abstract Vector getNormal();
}
