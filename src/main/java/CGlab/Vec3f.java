package CGlab;

public class Vec3f {
    public float x;
    public float y;
    public float z;

    public Vec3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3f() {

    }

    @Override
    public String toString() {
        return x + " " + y + " " + z;
    }
}
