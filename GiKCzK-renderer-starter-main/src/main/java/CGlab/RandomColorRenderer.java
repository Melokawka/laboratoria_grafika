package CGlab;
import java.util.concurrent.ThreadLocalRandom;
public class RandomColorRenderer extends Renderer {
    public RandomColorRenderer() {
        super();
    }
    public RandomColorRenderer(String filename) {
        super(filename);
    }
    public RandomColorRenderer(String filename, int w, int h) {
        super(filename, w, h);
    }
    void render(Model model) {
        float A_z=0, B_z=0, C_z=0;
        for (Vec3i face : model.getFaceList()) {
            Vec2i[] screen_coords = new Vec2i[3];
            for (int j=0; j<3; j++) {
                Vec3f world_coord = model.getVertex(face.get(j));
                if(j==0) A_z=world_coord.z;
                if(j==1) B_z=world_coord.z;
                if(j==2) C_z=world_coord.z;
                screen_coords[j] = new Vec2i((int)((world_coord.x + 1.0) * this.getRender().getWidth() / 2.0),
                        (int)((world_coord.y + 1.0) * getRender().getHeight() / 2.0) - this.getRender().getHeight() / 2);
            }
            int randColor = ThreadLocalRandom.current().nextInt(0, 0x00ffffff) | 0xff000000;
            drawTriangle(screen_coords[0].transform(), screen_coords[1].transform(), screen_coords[2].transform(), A_z, B_z, C_z);
        }
    }
}