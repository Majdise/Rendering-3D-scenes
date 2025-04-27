public class Vector {
	double x = 0.0, y = 0.0, z = 0.0;
	double length;

	Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		length = Math.sqrt(x * x + y * y + z * z);
	}

	/** create a vector using two points */
	Vector(Point startPoint, Point endPoint) {
		this.x = (endPoint.x - startPoint.x);
		this.y = (endPoint.y - startPoint.y);
		this.z = (endPoint.z - startPoint.z);
		length = Math.sqrt(x * x + y * y + z * z);
	}

	/** copy by constructor */
	Vector(Vector v) {
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
		this.length = v.length;
	}
	
	/** adds two vectors */
	static Vector add(Vector v1, Vector v2) {
		return new Vector(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
	}
	
	/** regular vector scaler */
	static Vector scaleMult(Vector v, double t) {
		return new Vector(v.x * t, v.y * t, v.z * t);
	}

	/** dot Product */
	static double dotProduct(Vector v1, Vector v2) {
		return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
	}
	
	/** dot product using a point and a vector */
	static double pointMulVector(Point p, Vector v) {
		return (p.x * v.x + p.y * v.y + p.z * v.z);
	}
	
	/** cross product */
	static Vector crossProduct(Vector v1, Vector v2) {
		Vector v3 = new Vector(0, 0, 0);
		v3.x = (v1.y * v2.z - v1.z * v2.y);
		v3.y = (v1.z * v2.x - v1.x * v2.z);
		v3.z = (v1.x * v2.y - v1.y * v2.x);
		v3.normalise();
		return v3;
	}

	/** normalizing */
	void normalise() {
		length=Math.sqrt(x * x + y * y + z * z);
		if (length != 0) {
			this.x = (this.x/this.length);
			this.y = (this.y/this.length);
			this.z = (this.z/this.length);
			this.length = 1;
		}
	}

	@Override
	public String toString() {
		return "V(" + x + ", " + y + ", " + z + ")";
	}
}
