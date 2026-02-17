                                           package renderer.core.camera;

import renderer.algebra.Matrix;
import renderer.algebra.SizeMismatchException;
import renderer.algebra.Vector;


/**
 * The Transformation class represents a transformation in 3D space.
 * author: cdehais
 */
public class Transformation {

    /**
     * The world to camera matrix.
     */
    private Matrix worldToCamera;
    /**
     * The 3x4 projection matrix.
     */
    private Matrix projection;
    /**
     * The 3x3 calibration matrix.
     */
    private Matrix calibration;

    /**
     * Creates a new Transformation object.
     */
    public Transformation() {
        final int w2cDim = 4;
        worldToCamera = Matrix.createIdentity("W2C", w2cDim);
        final int projRows = 3;
        final int projCols = 4;
        projection = new Matrix("P", projRows, projCols);
        final int calibDim = 3;
        calibration = Matrix.createIdentity("K", calibDim);
    }

    /**
     * Sets the lookAt transformation.
     * @param eye a 3D vector representing the eye position
     * @param lookAtPoint a 3D vector representing the point to look at
     * @param up a 3D vector representing the up direction
     */
    public void setLookAt(final Vector eye, final Vector lookAtPoint, final Vector up) {
        try {
            // compute rotation
            // TODO
            Vector z = lookAtPoint.subtract(eye).normalize();
            Vector x = up.cross(z).normalize();
            Vector y = z.cross(x).normalize();

            this.worldToCamera.set(1,1,x.getX());
            this.worldToCamera.set(1,2,x.getY());
            this.worldToCamera.set(1,3,x.getZ());
            this.worldToCamera.set(2,1,y.getX());
            this.worldToCamera.set(2,2,y.getY());
            this.worldToCamera.set(2,3,y.getZ());
            this.worldToCamera.set(3,1,-z.getX());
            this.worldToCamera.set(3,2,-z.getY());
            this.worldToCamera.set(3,3,-z.getZ());

            
            // compute translation
            // TODO
            this.worldToCamera.set(1,4,-x.dot(eye));
            this.worldToCamera.set(2,4,-y.dot(eye));
            this.worldToCamera.set(3,4,z.dot(eye));

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Modelview matrix:\n" + worldToCamera);
    }

    /**
     * Sets the projection matrix.
     */
    public void setProjection() {
        // TODO
        Matrix P = new Matrix("P", 3, 4);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                if (i == j) {
                    P.set(i, j, 1);
                } else {
                    P.set(i, j, 0);
                }
            }
            
        }
        this.projection = P;

        System.out.println("Projection matrix:\n" + projection);
    }

    /**
     * Sets the calibration matrix.
     * @param focal the focal length
     * @param width the width of the image
     * @param height the height of the image
     */
    public void setCalibration(double focal, double width, double height) {

        // TODO
        double cx = width / 2.0;
        double cy = height / 2.0;

        calibration.set(0,0,focal);
        calibration.set(0,1,0);
        calibration.set(0,2,cx);

        calibration.set(1,0,0);
        calibration.set(1,1,focal);
        calibration.set(1,2,cy);

        calibration.set(2,0,0);
        calibration.set(2,1,0);
        calibration.set(2,2,1);

        System.out.println("Calibration matrix:\n" + calibration);
    }

    /**
     * Projects the given 3 dimensional point onto the screen.
     * The resulting Vector as its (x,y) coordinates in pixel, and its z coordinate
     * is the depth of the point in the camera coordinate system.
     * @param p a 3d vector representing a point
     * @return the projected point as a 3d vector, with (x,y) the pixel
     * coordinates and z the depth
     * @throws SizeMismatchException if the size of the input vector is not 3
     */
    public Vector projectPoint(Vector p) throws SizeMismatchException {
        // TODO
        Vector ps = new Vector(3);
        Vector p2 = new Vector(4);
        p2.set(0, p.getX());
        p2.set(1, p.getY());
        p2.set(2, p.getZ());
        p2.set(3, 1.0);

        ps = worldToCamera.multiply(p2);
        ps = projection.multiply(ps);
        ps = calibration.multiply(ps);

        return ps;
    }

    /**
     * Transform a vector from world to camera coordinates.
     * @param v the vector to transform
     * @return the transformed vector
     * @throws SizeMismatchException if the size of the input vector is not 3
     */
    public Vector transformVector(final Vector v) {
        // Doing nothing special here because there is no scaling
        final Matrix m = worldToCamera.getSubMatrix(0, 0, 3, 3);
        return m.multiply(v);
    }

}
