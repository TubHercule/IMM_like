package renderer.core.light;

import renderer.algebra.Vector;

public class PointLight extends Light {

    /**
     * The first coordinate.
     */
    private double x;

    /**
     * The second coordinate.
     */
    private double y;

    /**
     * The third coordinate.
     */
    private double z;

    /**
     * Adds a new point light source of intensity @id at position (x, y, z)
     * to the environment.
     *
     * @param x  the x coordinate of the light source
     * @param y  the y coordinate of the light source
     * @param z  the z coordinate of the light source
     * @param id the intensity of the light source
     */
    public PointLight(double x, double y, double z, double id) {
        super(id);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public double getContribution(Vector position, Vector normal, double[] color,
                                  Vector cameraPosition, double ka, double kd, double ks, double s) {
        // vector from point to camera center
        Vector e = cameraPosition.subtract(position).normalize();

        // vector from point to light
        Vector l = getPositionAsVector().subtract(position).normalize();

        // half-vector between e and l
        Vector h = e.add(l).normalize();

        // diffuse contribution
        // Scalar product of normalized vectors is their cosine
        double I_diffuse = intensity * ka * normal.dot(l);

        // specular contribution
        double I_specular = intensity * ks * Math.pow(normal.dot(h), s);
        return I_diffuse + I_specular;
    }

    private Vector getPositionAsVector() {
        return new Vector(x, y, z);
    }

}
