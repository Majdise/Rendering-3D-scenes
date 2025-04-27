
import java.awt.Transparency;
import java.awt.color.*;
import java.awt.image.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 *  Main class for ray tracing exercise.
 */
public class RayTracer {
	private int imageWidth;
	private int imageHeight;
	
	private int maxRecursionNumber, root_shadows_rays;
	private double[] background_RGB = new double[3];
	private Camera myCamera;
	private Random random = new Random();
	private List<Material> list_of_mat;
	private List<Surfaces> list_of_surfaces;
	private List<Light> list_of_lgt;
	private double epsilon = 0.000002174192738;

	
	/**
	 * Runs the ray tracer. Takes scene file, output image file and image size as input.
	 */
	public static void main(String[] args) {

		try {

			RayTracer tracer = new RayTracer();

                        // Default values:
			tracer.imageWidth = 500;
			tracer.imageHeight = 500;

			if (args.length < 2)
				throw new RayTracerException("Not enough arguments provided. Please specify an input scene file and an output image file for rendering.");

			String sceneFileName = args[0];
			String outputFileName = args[1];

			if (args.length > 3)
			{
				tracer.imageWidth = Integer.parseInt(args[2]);
				tracer.imageHeight = Integer.parseInt(args[3]);
			}


			// Parse scene file:
			tracer.parseScene(sceneFileName);

			// Render scene:
			tracer.renderScene(outputFileName);

//		} catch (IOException e) {
//			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}


	}

	/**
	 * Parses the scene file and creates the scene. Change this function so it generates the required objects.
	 */
	private void parseScene(String sceneFileName) throws IOException, RayTracerException
	{
		FileReader fr = new FileReader(sceneFileName);

		BufferedReader r = new BufferedReader(fr);
		String line = null;
		int lineNum = 0;
		
		
		list_of_mat = new ArrayList<>();
		list_of_surfaces = new ArrayList<>();
		list_of_lgt = new ArrayList<>();

		System.out.println("Started parsing scene file " + sceneFileName);



		while ((line = r.readLine()) != null)
		{
			line = line.trim();
			++lineNum;

			if (!line.isEmpty() && (line.charAt(0) != '#'))
			{
				String code = line.substring(0, 3).toLowerCase();
				// Split according to white space characters:
				String[] params = line.substring(3).trim().toLowerCase().split("\\s+");

				switch (code) {
					case "cam":
						if (params.length != 13) {
							System.out.println("cam input file error");
							r.close();
							return;
						}
						Point position = new Point(Double.parseDouble(params[0]), Double.parseDouble(params[1]),
								Double.parseDouble(params[2]));
						Point lookAtPoint = new Point(Double.parseDouble(params[3]), Double.parseDouble(params[4]),
								Double.parseDouble(params[5]));
						Vector upVector = new Vector(Double.parseDouble(params[6]), Double.parseDouble(params[7]),
								Double.parseDouble(params[8]));
						double screenDistance = Double.parseDouble(params[9]);
						double screenWidth = Double.parseDouble(params[10]);

						//regarding fisheye variables
						boolean fishy = Boolean.parseBoolean(params[11]);
						double fishyCoef = Double.parseDouble(params[12]);

						myCamera = new Camera(position, lookAtPoint, upVector, screenDistance, screenWidth, imageWidth,
								imageHeight, fishy, fishyCoef);
						System.out.println(String.format("Parsed camera parameters (line %d)", lineNum));
						break;
					case "set":
						if (params.length != 5) {
							System.out.println("general settings input file error");
							r.close();
							return;
						}
						background_RGB[0] = Double.parseDouble(params[0]);
						background_RGB[1] = Double.parseDouble(params[1]);
						background_RGB[2] = Double.parseDouble(params[2]);
						root_shadows_rays = Integer.parseInt(params[3]);
						maxRecursionNumber = Integer.parseInt(params[4]);
						System.out.println(String.format("Parsed general settings (line %d)", lineNum));
						break;
					case "mtl":
						if (params.length != 11) {
							System.out.println("material input file error");
							r.close();
							return;
						}
						double[] diffuseColor = {Double.parseDouble(params[0]), Double.parseDouble(params[1]),
								Double.parseDouble(params[2])};
						double[] specularColor = {Double.parseDouble(params[3]), Double.parseDouble(params[4]),
								Double.parseDouble(params[5])};
						double[] reflectionColor = {Double.parseDouble(params[6]), Double.parseDouble(params[7]),
								Double.parseDouble(params[8])};
						double phongCoeff = Double.parseDouble(params[9]);
						double transparencyValue = Double.parseDouble(params[10]);
						list_of_mat.add(new Material(diffuseColor, specularColor, reflectionColor, phongCoeff, transparencyValue));


						System.out.println(String.format("Parsed material (line %d)", lineNum));
						break;
					case "sph": {
						if (params.length != 5) {
							System.out.println("sphere input file error");
							r.close();
							return;
						}
						// Add code here to parse sphere parameters

						Point center = new Point(Double.parseDouble(params[0]), Double.parseDouble(params[1]),
								Double.parseDouble(params[2]));
						double rad = Double.parseDouble(params[3]);
						int mat_index = Integer.parseInt(params[4]);
						list_of_surfaces.add(new Sphere(center, rad, mat_index));
						System.out.println(String.format("Parsed sphere (line %d)", lineNum));
						break;
					}
					case "pln": {
						if (params.length != 5) {
							System.out.println("plane input file error");
							r.close();
							return;
						}
						double a, b, c, offset;
						a = Double.parseDouble(params[0]);
						b = Double.parseDouble(params[1]);
						c = Double.parseDouble(params[2]);
						offset = Double.parseDouble(params[3]);
						int matIndex = Integer.parseInt(params[4]);
						list_of_surfaces.add(new InfinitePlane(a, b, c, offset, matIndex));
						System.out.println(String.format("Parsed plane (line %d)", lineNum));
						break;
					}
					case "box": {
						if (params.length != 5) {
							System.out.println("box input file error");
							r.close();
							return;
						}
						double a, b, c, edgeLength;
						a = Double.parseDouble(params[0]);
						b = Double.parseDouble(params[1]);
						c = Double.parseDouble(params[2]);
						edgeLength = Double.parseDouble(params[3]);
						int mat_index = Integer.parseInt(params[4]);
						list_of_surfaces.add(new Box(new Point(a, b, c), edgeLength, mat_index));
						System.out.println(String.format("Parsed box (line %d)", lineNum));
						break;
					}
					case "lgt":
						if (params.length != 9) {
							System.out.println("light input file error");
							r.close();
							return;
						}
						double specIntensity, shadowIndensity, lightRadius;
						Point pos = new Point(Double.parseDouble(params[0]), Double.parseDouble(params[1]),
								Double.parseDouble(params[2]));
						double[] light_color = {Double.parseDouble(params[3]), Double.parseDouble(params[4]),
								Double.parseDouble(params[5])};
						specIntensity = Double.parseDouble(params[6]);
						shadowIndensity = Double.parseDouble(params[7]);
						lightRadius = Double.parseDouble(params[8]);
						list_of_lgt.add(new Light(pos, light_color, specIntensity, shadowIndensity, lightRadius));
						System.out.println(String.format("Parsed light (line %d)", lineNum));
						break;
					default:
						System.out.println(String.format("ERROR: Did not recognize object: %s (line %d)", code, lineNum));
						break;
				}
			}
		}

                // It is recommended that you check here that the scene is valid,
                // for example camera settings and all necessary materials were defined.
		r.close();

		System.out.println("Finished parsing scene file " + sceneFileName);

	}

	/**
	 * Renders the loaded scene and saves it to the specified file location.
	 */
	private void renderScene(String outputFileName)
	{
		long startTime = System.currentTimeMillis();

		// Create a byte array to hold the pixel data:
		byte[] rgbData = new byte[this.imageWidth * this.imageHeight * 3];


                // Put your ray tracing code here!
                //
                // Write pixel color values in RGB format to rgbData:
                // Pixel [x, y] red component is in rgbData[(y * this.imageWidth + x) * 3]
                //            green component is in rgbData[(y * this.imageWidth + x) * 3 + 1]
                //             blue component is in rgbData[(y * this.imageWidth + x) * 3 + 2]
                //
                // Each of the red, green and blue components should be a byte, i.e. 0-255

		
		Point p0 = myCamera.findLeftLowerPoint();
		Vector ray;
		Point p;
		System.out.println("rendering...");
		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				p = Point.findPoint(p0, myCamera.xAxis, j * myCamera.screenWidth / imageWidth);
				p = Point.findPoint(p, myCamera.yAxis, i * myCamera.screenHeight / imageHeight);
				p = myCamera.fishEyeRefraction(p);

				double[] color_of_p = { 0, 0, 0 };
				ray = new Vector(myCamera.position, p);
				ray.normalise();
				double[] theColor = intersectionColor(myCamera.position, ray, 0);
				for (int k = 0; k < 3; k++)
					color_of_p[k] += theColor[k];

				/*
				 * according to the way we iterate over the indexes, we matched them in the
				 * saved array (RGBData)
				 */
				for (int k = 0; k < 3; k++)
					rgbData[((imageHeight - i - 1) * this.imageWidth + (j)) * 3 + k] = (byte) Math
							.round(255 * color_of_p[k]);
			}
			if (((double) 100 * i / imageHeight) % 10 == 0)
				System.out.println(" " + Math.round(100 * (double) i / imageHeight) + "%");
		}
		System.out.println(100+"%");
		
		

		long endTime = System.currentTimeMillis();
		long renderTime = endTime - startTime;

		System.out.println("Finished rendering scene in " + renderTime + " milliseconds.");

                // This is already implemented, and should work without adding any code.
		saveImage(this.imageWidth, rgbData, outputFileName);

		System.out.println("Saved file " + outputFileName);

	}


	
	/** return the closest surface that the ray intersects (slides) */
	private Surfaces findIntersection(Point p0, Vector rayDirection) {
		double min_t = Double.POSITIVE_INFINITY;
		Surfaces min_primitive = null;

		for (Surfaces shape : list_of_surfaces) {
			double t = shape.getIntersection(p0, rayDirection);
			if (t == -1) // no intersection found
				continue;
			if (t < min_t && t > 0) {
				min_t = t;
				min_primitive = shape;
			}
		}
		return min_primitive;
	}

	private double[] intersectionColor(Point position, Vector ray, int recNum) {
		if (recNum == maxRecursionNumber)
			return new double[] { 0, 0, 0 };

		Surfaces intersectionSurface = findIntersection(position, ray);
		// if there is no intersection then return the back ground color
		if (intersectionSurface == null)
			return background_RGB;

		Color outputColor = new Color(new double[] { 0, 0, 0 });
		double t = intersectionSurface.getIntersection(position, ray);
		Point intersectionPoint = Point.findPoint(position, ray, t);
		Material surfaceMat = new Material(list_of_mat.get(intersectionSurface.index_by_material));
		double transparency = surfaceMat.transparency;
		Color transparancyColor = new Color(new double[] { 0, 0, 0 }),
				reflectionColor = new Color(new double[] { 0, 0, 0 });
		Point reflectedPoint;
		// if the surface is transparent, then create another transparency ray
		if (transparency != 0) {
			reflectedPoint = Point.findPoint(intersectionPoint, ray, epsilon);
			transparancyColor = new Color(intersectionColor(reflectedPoint, ray, recNum + 1));
		}
		// create a reflection ray
		double[] reflectionMat = surfaceMat.reflectionColor.getValues();
		if (reflectionMat[0] != 0 || reflectionMat[1] != 0 || reflectionMat[2] != 0) {
			if (intersectionSurface.getType() == Surfaces.type.sphere)
				// sphere has to update the intersection point in order to get the right normal
				((Sphere) intersectionSurface).setIntersectionPoint(intersectionPoint);
			Vector normal = intersectionSurface.getNormal();
			Vector reflectionRay = Vector.scaleMult(normal, -2 * Vector.dotProduct(normal, ray));
			reflectionRay = Vector.add(ray, reflectionRay);
			reflectedPoint = Point.findPoint(intersectionPoint, reflectionRay, epsilon);
			reflectionColor = new Color(intersectionColor(reflectedPoint, reflectionRay, recNum + 1));
			reflectionColor.multiply(surfaceMat.reflectionColor);
		}
		// calculate the shadow and illuminate the outputColor
		for (Light light : list_of_lgt) {
			Color temporaryColor = getColor(intersectionPoint, intersectionSurface, ray, light);
			if (light.shadowIntensity != 0 && (temporaryColor.R != 0 || temporaryColor.G != 0 || temporaryColor.B != 0))
				temporaryColor.scale(
						1 - light.shadowIntensity + (light.shadowIntensity * softShadow(intersectionPoint, light)));
			outputColor.updateColor(temporaryColor, 1);
		}

		outputColor.normalise();
		outputColor.scale(1 - transparency);
		outputColor.updateColor(transparancyColor, transparency);
		outputColor.updateColor(reflectionColor, 1);
		outputColor.normalise();
		return outputColor.getValues();
	}

	/** calculate soft shadow using the light's position and direction */
	private double softShadow(Point p, Light light) {
		Vector lightDirection = new Vector(p, light.position);
		Vector firstRay = Vector.crossProduct(lightDirection, myCamera.yAxis);
		Vector secondRay = Vector.crossProduct(lightDirection, firstRay);
		Point fixedLightPosition = Point.findPoint(light.position, firstRay, -light.Lightradius / 2);
		fixedLightPosition = Point.findPoint(fixedLightPosition, secondRay, -light.Lightradius / 2);

		Vector tmpLightDir;
		double counter = 0, rand1, rand2;
		for (int i = 0; i < root_shadows_rays; i++) {
			tmpLightDir = new Vector(fixedLightPosition.x, fixedLightPosition.y, fixedLightPosition.z);
			for (int j = 0; j < root_shadows_rays; j++) {
				rand1 = random.nextDouble();
				rand2 = random.nextDouble();
				tmpLightDir = Vector.add(tmpLightDir,
						Vector.scaleMult(firstRay, rand1 * (light.Lightradius / root_shadows_rays)));
				tmpLightDir = Vector.add(tmpLightDir,
						Vector.scaleMult(secondRay, rand2 * (light.Lightradius / root_shadows_rays)));
				counter += getShadowNum(p, tmpLightDir);
				tmpLightDir = Vector.add(tmpLightDir,
						Vector.scaleMult(firstRay, -rand1 * (light.Lightradius / root_shadows_rays)));
				tmpLightDir = Vector.add(tmpLightDir,
						Vector.scaleMult(secondRay, (1 - rand2) * (light.Lightradius / root_shadows_rays)));
			}
			fixedLightPosition = Point.findPoint(fixedLightPosition, firstRay, light.Lightradius / root_shadows_rays);
		}
		return counter / (root_shadows_rays * root_shadows_rays);
	}

	private double getShadowNum(Point pt, Vector light) {
		Point lightPoint = new Point(light.x, light.y, light.z);
		Vector rayDirection = new Vector(lightPoint, pt);
		double length = rayDirection.length;
		rayDirection.normalise();
		double lightNum = 1, t;
		for (Surfaces currentSurface : list_of_surfaces) {
			t = currentSurface.getIntersection(lightPoint, rayDirection);
			if (t < length - epsilon && t > epsilon) {
				double transparancy = (list_of_mat.get(currentSurface.index_by_material)).transparency;
				if (transparancy == 0)
					return 0;
				lightNum *= transparancy;
			}
		}
		return lightNum;
	}

	private Color getColor(Point pt, Surfaces sur, Vector visionDir, Light light) {

		Material mat = new Material(list_of_mat.get(sur.index_by_material));
		Color diffuseColor = new Color(new double[] { mat.diffuseColor.R, mat.diffuseColor.G, mat.diffuseColor.B });
		Color specularColor = new Color(new double[] { mat.specularColor.R, mat.specularColor.G, mat.specularColor.B });
		// update the points
		Vector lightDirection = new Vector(pt, new Point(light.position.x, light.position.y, light.position.z));
		lightDirection.normalise();
		Vector normal;

		if (sur.getType() == Surfaces.type.sphere)
			((Sphere) sur).setIntersectionPoint(pt);

		normal = sur.getNormal();
		if (Vector.dotProduct(visionDir, normal) > 0)
			normal = Vector.scaleMult(normal, -1);
		double theta = Vector.dotProduct(lightDirection, normal);
		if (theta <= 0) {
			diffuseColor.scale(0);
			return new Color(new double[] { 0, 0, 0 });
		}
		diffuseColor.scale(theta);

		if (specularColor.R != 0 || specularColor.G != 0 || specularColor.B != 0) {
			Vector reflect = new Vector(normal);
			reflect = Vector.scaleMult(reflect, 2 * Vector.dotProduct(lightDirection, normal));
			reflect = Vector.add(reflect, Vector.scaleMult(lightDirection, -1));
			theta = Vector.dotProduct(visionDir, reflect);
			if (theta < 0) {
				theta = Math.pow(theta, mat.PhongSpecularityCoefficient);
				specularColor.scale(theta);
				diffuseColor.updateColor(specularColor, light.specularIntensity);
			}
		}
		diffuseColor.normalise();
		diffuseColor.multiply(light.color);
		return diffuseColor;
	}

	


	//////////////////////// FUNCTIONS TO SAVE IMAGES IN PNG FORMAT //////////////////////////////////////////

	/*
	 * Saves RGB data as an image in png format to the specified location.
	 */
	private static void saveImage(int width, byte[] rgbData, String fileName)
	{
		try {

			BufferedImage image = bytes2RGB(width, rgbData);
			ImageIO.write(image, "png", new File(fileName));

		} catch (IOException e) {
			System.out.println("ERROR SAVING FILE: " + e.getMessage());
		}

	}

	/*
	 * Producing a BufferedImage that can be saved as png from a byte array of RGB values.
	 */
	private static BufferedImage bytes2RGB(int width, byte[] buffer) {
	    int height = buffer.length / width / 3;
	    ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
	    ColorModel cm = new ComponentColorModel(cs, false, false,
	            Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
	    SampleModel sm = cm.createCompatibleSampleModel(width, height);
	    DataBufferByte db = new DataBufferByte(buffer, width * height);
	    WritableRaster raster = Raster.createWritableRaster(sm, db, null);
		return new BufferedImage(cm, raster, false, null);
	}

	public static class RayTracerException extends Exception {
		private static final long serialVersionUID = 1L;

		RayTracerException(String msg) {  super(msg); }
	}


}
