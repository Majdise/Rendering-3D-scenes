
public class Camera {
	Point position;
	private Point lookAtPoint;
	private double screenDistance;
	double screenWidth, screenHeight;
	private double imageWidth, imageHeight;
	private Vector cameraDirection, upVector;
	Vector xAxis, yAxis;// this is the the screen's normalized axes
	boolean fishEye = false;//given default value
	double fishEyeCoeff = 0.5;//given default value
	
	public Camera(Point position, Point lookAtPoint, Vector upVector, double screenDistance, double screenWidth,
			int width, int height, boolean fishEye, double fishEyeCoeff) {
		this.position = position;
		this.lookAtPoint = lookAtPoint;
		this.upVector = upVector;
		this.screenDistance = screenDistance;
		this.screenWidth = screenWidth;
		this.imageWidth = (double) width;
		this.imageHeight = (double) height;
		this.screenHeight = screenWidth * (imageHeight / imageWidth);
		this.fishEye = fishEye;
		this.fishEyeCoeff = fishEyeCoeff;
		translateAxises();
	}

	/* calculates the ray direction after refracting it through a fish-eye lens*/
	public Point fishEyeRefraction(Point p){
		/* return original ray direction if no fish eye has been configured */
		if(!this.fishEye){
			return p;
		}
		// find the point at the center of the image screen
		Point screenCenter = Point.findPoint(position, cameraDirection, -screenDistance);
		// a vector going from the center of the image screen to the intersection between the ray and the screen (p)
		Vector centerToXip = new Vector(screenCenter, p);
		// the angle theta between the camera direction and the ray
		double theta = Math.atan(centerToXip.length / screenDistance);
		// set r according to fisheye guidelines.
		double r;
		if(fishEyeCoeff > 0){
			r = (screenDistance / fishEyeCoeff) * Math.tan(fishEyeCoeff * theta);
		}
		else if(fishEyeCoeff < 0){
			r = (screenDistance / fishEyeCoeff) * Math.sin(fishEyeCoeff * theta);
		}
		else{
			r = theta * screenDistance;
		}

		centerToXip.normalise();
		p = Point.findPoint(screenCenter, centerToXip, r);
		return p;
	}

	/**
	 * calculate the camera's position and translate it using the newly defined x
	 * and y axes
	 */
	private void translateAxises() {
		cameraDirection = new Vector(position, lookAtPoint);
		cameraDirection.normalise();
		xAxis = Vector.crossProduct(upVector, cameraDirection);
		yAxis = Vector.crossProduct(cameraDirection, xAxis);
	}

	/** finding the screen's origin */
	Point findLeftLowerPoint() {
		Point p0 = Point.findPoint(position, cameraDirection, screenDistance);
		p0 = Point.findPoint(p0, yAxis, -screenHeight / 2);
		p0 = Point.findPoint(p0, xAxis, -screenWidth / 2);
		return p0;
	}
}
