package CGlab;

public class Vec2i {
    public int x;
    public int y;

    public Vec2i(int x, int y) {
        this.x=x;
        this.y=y;
    }

    public Vec2f transform(){
        return new Vec2f(this.x, this.y);
    }


    @Override
    public String toString() {
        return x + " " + y;
    }
}