
# RayTracer

A basic yet powerful **ray tracing engine** for rendering 3D scenes with realistic lighting, reflections, transparency, and soft shadows.  
Built from scratch to explore computer graphics concepts like ray-surface intersections, shading models, and camera modeling — including a **fisheye lens** effect for wide-angle artistic shots.

---

## Features

- **Surface Types**:
  - Spheres
  - Infinite Planes
  - Axis-Aligned Boxes (Cubes)

- **Material Properties**:
  - Diffuse and Specular colors
  - Phong Shading (adjustable shininess)
  - Reflection colors
  - Transparency (full, partial, or opaque surfaces)

- **Lighting System**:
  - Point lights with adjustable color and intensity
  - Soft shadow support via multiple randomized shadow rays
  - Configurable specular and shadow intensity per light

- **Camera System**:
  - Standard pinhole camera model
  - Fisheye lens simulation for dramatic wide-angle effects
  - Customizable position, look-at point, up vector, screen distance, and screen width

- **Rendering Options**:
  - Recursive ray tracing with adjustable depth
  - Reflection and refraction
  - Background color blending when rays miss all surfaces

---

## How to Run

### Requirements

- Python 3 (using only standard libraries + `numpy`)  
  or
- Java 8+ (for the `.jar` version)

### Running the Renderer

**Python version**:
python RayTracer.py <scene_file> <output_image> [image_width] [image_height]

**Java version**:
java -jar RayTrace.jar <scene_file> <output_image> [image_width] [image_height]

**Parameters**:
- `<scene_file>`: Path to a `.txt` file describing the scene.
- `<output_image>`: Path to save the resulting `.png` image.
- `[image_width]` and `[image_height]`: (Optional) Output resolution. Defaults to 500x500 if omitted.

---

## Scene File Format

Scenes are defined in simple text files. Each line defines an object using a 3-letter code, followed by its parameters.

| Code | Description | Parameters |
|:----:|:------------|:-----------|
| cam | Camera | Position (x, y, z), Look-at (x, y, z), Up vector (x, y, z), Screen distance, Screen width, Fisheye flag (true/false), Fisheye K-value (optional) |
| set | Global Settings | Background color (r, g, b), Shadow ray root number (N), Max recursion depth |
| mtl | Material | Diffuse color (r, g, b), Specular color (r, g, b), Reflection color (r, g, b), Phong coefficient, Transparency |
| sph | Sphere | Center position (x, y, z), Radius, Material index |
| pln | Plane | Normal (x, y, z), Offset, Material index |
| box | Box | Center position (x, y, z), Edge length, Material index |
| lgt | Light | Position (x, y, z), Color (r, g, b), Specular intensity, Shadow intensity, Light radius |

- Comments start with `#`.
- Empty lines are ignored.

---

## Highlights and Technical Details

- **Soft Shadows**: Simulated by casting multiple rays from a light source area, with randomized sampling to avoid artifacts.
- **Recursive Ray Tracing**: Supports reflections and transparent materials, with a configurable recursion limit.
- **Fisheye Camera**: Warps the field of view for dramatic lens effects using a custom distortion formula.
- **Vector Math Library**: Custom built for handling all the necessary operations like dot products, cross products, and component-wise multiplications (especially useful for color calculations).

---

## Example Command

python RayTracer.py scenes/simple_scene.txt renders/output.png 800 800
```

or

java -jar RayTrace.jar scenes/simple_scene.txt renders/output.png

---

## Future Ideas / Possible Improvements

- Add support for additional surfaces (e.g., cylinders, triangles).
- Implement BSP trees or acceleration structures for faster intersection checks.
- Add textures and bump mapping.
- Full global illumination (path tracing).
- GUI scene builder.

---

## Why I Built This

This project deepens  understanding of how light interacts with surfaces, how cameras project 3D scenes into 2D images, and to explore techniques like soft shadows and reflection in a hands-on way.  

---

## References

- [Fisheye Projection - Research Paper](https://hal.inria.fr/hal-02463429/document)
- [Panotools Wiki: Fisheye Projections](https://wiki.panotools.org/Fisheye_Projection)
