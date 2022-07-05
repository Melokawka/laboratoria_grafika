package CGlab;

public class FlatShadingRenderer {
    public static Vec3f light = new Vec3f(1f, 5f, 8f);

    public static double CosFunc(Vec3f trinorm) {
        float dotproduct = trinorm.x*light.x + trinorm.y*light.y + trinorm.z*light.z;

        return dotproduct/(Math.sqrt(Math.pow(trinorm.x, 2)+Math.pow(trinorm.y, 2)+Math.pow(trinorm.z, 2))*Math.sqrt(Math.pow(light.x, 2)+Math.pow(light.y, 2)+Math.pow(light.z, 2)));
    }

    public static int gradecolor(Vec3f trinorm) {

        return 255 | (255 << 8) | (255 << 16) | ((int)(255*CosFunc(trinorm)) << 24);
    }

}
