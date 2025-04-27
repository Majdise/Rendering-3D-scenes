
public class Light {
	Point position;
	Color color;
	double specularIntensity;
	double shadowIntensity;
	double Lightradius;

	public Light(Point place, double[] RGB, double specularIn, double shadowIn, double lightrad) {
		this.position = place;
		this.color = new Color(RGB);
		this.Lightradius = lightrad;
		this.specularIntensity = specularIn;
		this.shadowIntensity = shadowIn;
	}
}
