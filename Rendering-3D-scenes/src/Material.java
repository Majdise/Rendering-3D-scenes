
public class Material {
	Color diffuseColor;
	Color specularColor;
	Color reflectionColor;

	double transparency;
	double PhongSpecularityCoefficient;

	public Material(double[] diffuseCol, double[] specularCol, double[] reflectionCol, double phongCoef, double transValue) {
		diffuseColor = new Color(diffuseCol);
		specularColor = new Color(specularCol);
		reflectionColor = new Color(reflectionCol);
		transparency = transValue;
		PhongSpecularityCoefficient = phongCoef;
	}
	
	/** copy */
	public Material(Material mat) {
		diffuseColor=mat.diffuseColor;
		specularColor=mat.specularColor;
		reflectionColor=mat.reflectionColor;
		transparency=mat.transparency;
		PhongSpecularityCoefficient=mat.PhongSpecularityCoefficient;
	}
}
