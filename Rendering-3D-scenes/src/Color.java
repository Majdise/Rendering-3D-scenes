
public class Color {
	double R;
	double G;
	double B;
	
	Color(double[] RGB) {
		R = RGB[0] ;
		G = RGB[1] ;
		B = RGB[2] ;
	}
	
	/** scale RGB with another RGB of a different color*/
	void multiply(Color color) {
		R *= color.R;
		G *= color.G;
		B *= color.B;
	}
	
	/** scale by t */
	void scale(double t) {
		R *= t;
		G *= t;
		B *= t;
	}

	/**add to the original RGB the multiplication of (transparency value with RGB value )*/

	void updateColor(Color col, double transparency) {
		R += (transparency*col.R) ;
		G +=  (transparency*col.G);
		B +=  (transparency*col.B);
	}
	
	double[] getValues() {
		return (new double[] { R, G, B });
	}
	
	void normalise() {
		R = Math.min(1, R);
		G = Math.min(1, G);
		B = Math.min(1, B);
	}

	public String toString() {
		return "C(" + R + ", " + G + ", " + B + ")";
	}
}
