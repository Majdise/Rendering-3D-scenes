public class Box extends Surfaces{
	private Point center; // will be used to calculate the normal

	private double edgeLength;
	private InfinitePlane[] sides; // array of planes. Each pair of plane represents a slab, the intersection of the slabs defines the box.
	private int lastIntersectionWith; // the index of the side (in the sides array) with which the last ray intersected

	Box(Point center, double edgeLength, int indx) {
		this.center = center;
		this.edgeLength = edgeLength;
		sides = new InfinitePlane[6];
		sides[0] = new InfinitePlane(1, 0, 0, center.x + edgeLength / 2, indx);
		sides[1] = new InfinitePlane(1, 0, 0, center.x - edgeLength / 2, indx);
		sides[2] = new InfinitePlane(0, 1, 0, center.y + edgeLength / 2, indx);
		sides[3] = new InfinitePlane(0, 1, 0, center.y - edgeLength / 2, indx);
		sides[4] = new InfinitePlane(0, 0, 1, center.z + edgeLength / 2, indx);
		sides[5] = new InfinitePlane(0, 0, 1, center.z - edgeLength / 2, indx);
		this.index_by_material = (indx - 1);
		myType = type.box;
	}

	public type getType() {
		return type.box;
	}
	
	/** if exists, return the intersection point  */
	public double getIntersection(Point point, Vector rayDirect) {
		double[] sideIntersections = new double[6];
		double[] slabsEntranceT = new double[3];
		double[] slabsExitMaxT = new double[3];
		double minSlabIntersection = Double.POSITIVE_INFINITY;
		for(int i = 0; i < 6; i++){
			sideIntersections[i] = sides[i].getIntersection(point, rayDirect);
			if(sideIntersections[i] < minSlabIntersection && sideIntersections[i] > 0){
				Point intersection = Point.findPoint(point, rayDirect, sideIntersections[i]);
				if(
						(((intersection.x <= center.x + (edgeLength / 2)) && (intersection.x >= center.x - (edgeLength / 2))) || (i == 0 || i == 1)) &&
						(((intersection.y <= center.y + (edgeLength / 2)) && (intersection.y >= center.y - (edgeLength / 2))) || (i == 2 || i == 3)) &&
						(((intersection.z <= center.z + (edgeLength / 2)) && (intersection.z >= center.z - (edgeLength / 2))) || (i == 4 || i == 5))
				) {
					minSlabIntersection = sideIntersections[i];
					this.lastIntersectionWith = i;
				}
			}
		}
		for(int i = 0; i < 3; i++){
			if(sideIntersections[i * 2] > sideIntersections[(i * 2) + 1]){
				slabsEntranceT[i] = sideIntersections[(i * 2) + 1];
				slabsExitMaxT[i] = sideIntersections[(i * 2)];
			}
			else {
				slabsEntranceT[i] = sideIntersections[(i * 2)];
				slabsExitMaxT[i] = sideIntersections[(i * 2) + 1];
			}
			if(slabsExitMaxT[i] <= 0)
				return -1;
		}
		double minExit = Double.POSITIVE_INFINITY; // min t-value for an exit point
		double maxEnter = Double.NEGATIVE_INFINITY; // max t-value for an entrance point
		double minEnter = Double.POSITIVE_INFINITY; // min t-value for an entrance point
		for(int i = 0; i < 3; i++){
			if(slabsEntranceT[i] > maxEnter && slabsEntranceT[i] > 0) {
				maxEnter = slabsEntranceT[i];
			}
			if(slabsExitMaxT[i] < minExit && slabsExitMaxT[i] > 0) {
				minExit = slabsExitMaxT[i];
			}
			if(slabsEntranceT[i] < minEnter && slabsEntranceT[i] > 0) {
				minEnter = slabsEntranceT[i];
			}
		}
		if(!(maxEnter > minExit)){
			return minSlabIntersection;
		}
		return -1;
	}

		
	/** create box normal */
	public Vector getNormal() {
		return sides[lastIntersectionWith].getNormal();
	}
	
	public String toString() {
		return "bo.: l=" + edgeLength + ", c=" + center;
	}
}

