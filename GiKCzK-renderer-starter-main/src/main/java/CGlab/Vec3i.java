package CGlab;

public class Vec3i {
    public int x;
    public int y;
    public int z;

    public Vec3i(int x, int y, int z) {
        this.x=x;
        this.y=y;
        this.z=z;
    }

    public int get(int i){
        if(i == 0) return this.x;
        if(i == 1) return this.y;
        if(i == 2) return this.z;
        else return 0;
    }

    @Override
    public String toString() {
        return x + " " + y + " " + z;
    }
}