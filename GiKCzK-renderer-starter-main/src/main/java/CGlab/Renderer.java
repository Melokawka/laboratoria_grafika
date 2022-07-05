package CGlab;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import CGlab.Vec3f;

public class Renderer {

    public enum LineAlgo { NAIVE, DDA, BRESENHAM, BRESENHAM_INT; }

    public static int kolorflat;

    private BufferedImage render;
    public final int h=480;
    public final int w=640;

    private String filename;
    private LineAlgo lineAlgo = LineAlgo.NAIVE;
    private String pickAlg;

    public BufferedImage getRender() {
        return render;
    }

    public Renderer() {}

    public Renderer(String[] parameters) {
        render = new BufferedImage(Integer.parseInt(parameters[1]), Integer.parseInt(parameters[2]), BufferedImage.TYPE_INT_ARGB);
        filename = parameters[0];
        pickAlg = parameters[3];
    }
    public Renderer(String filename) {
        render = new BufferedImage(640, 480, BufferedImage.TYPE_INT_ARGB);
        filename = filename;
    }
    public Renderer(String filename, int width, int height) {
        render = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        this.filename = filename;
    }

    public void drawPoint(int x, int y) {
        int white = 255 | (255 << 8) | (255 << 16) | (255 << 24);
        render.setRGB(x, y, kolorflat);
    }

    public void drawLine(int x0, int y0, int x1, int y1) {
        if(pickAlg.equals("LINE_NAIVE")) drawLineNaive(x0, y0, x1, y1);
        if(pickAlg.equals("LINE_DDA")) drawLineDDA(x0, y0, x1, y1);
        if(pickAlg.equals("LINE_BRESENHAM")) drawLineBresenham(x0, y0, x1, y1);
        if(pickAlg.equals("LINE_BRESENHAM_INT")) drawLineBresenhamInt(x0, y0, x1, y1);
    }

    public void drawLineNaive(int x0, int y0, int x1, int y1) {
        // y = (y1 - y2)/(x1 - x2)x + (y1 - (y1 - y2)/(x1 - x2)x1)
        float a = (float)(y0 - y1)/(x0 - x1);
        System.err.println(a);

        // jezeli x0 wieksze to renderuj linie od x1
        // nie dziala dobrze dla a > 1 // fixed
        if (x0 > x1) {
            int pom = x0;
            x0 = x1;
            x1 = pom;

            pom = y0;
            y0 = y1;
            y1 = pom;
        }
        if(a <= 1) {
            for (int i = 0; i <= (x1 - x0); i++) {
                float kierunkowy = i * a;
                drawPoint(x0 + i, y0 + Math.round(kierunkowy));
            }
        }
        else {
            for (int i = 0; i <= (x1 - x0); i++) {
                float kierunkowy = i * a;
                drawPoint(x0 + i, y0 + Math.round(kierunkowy));
                // jezeli a > 1 czyli gdy piksele przeskakuja o wiecej niz 1, wypelnij luki
                int wypelnij = (int) a - 1;
                int iter = 1;
                while(wypelnij > 0) {
                    drawPoint(x0 + i, y0 + Math.round(kierunkowy) + iter);
                    iter++;
                    wypelnij--;
                }
            }
        }
    }

    public void drawLineDDA(int x0, int y0, int x1, int y1) {
        // TODO: zaimplementuj
    }

    public void drawLineBresenham(int x0, int y0, int x1, int y1) {
        // jezeli x0 > x1 to je zamien
        if(x0 > x1) {
            int temp = x0;
            x0 = x1;
            x1 = temp;

            temp = y0;
            y0 = y1;
            y1 = temp;
        }

        int dx = x1-x0;
        int dy = y1-y0;
        float derr = Math.abs(dy/(float)(dx));
        float err = 0;

        int y = y0;

        for (int x=x0; x<=x1; x++) {
            drawPoint(x, y);
            err += derr;
            if (err > 0.5) {
                y += (y1 > y0 ? 1 : -1);
                err -= 1.;
            }
        } // Oktanty: 8, 3,
    }

    public void drawLineBresenhamInt(int x0, int y0, int x1, int y1) {
        // TODO: zaimplementuj
    }

    public Vec3f barycentric(Vec2f A, Vec2f B, Vec2f C, Vec2f P) {
        Vec3f v1 = new Vec3f(A.x - B.x, A.x - C.x, P.x - A.x);  // wspolrzedne x wektorow
        Vec3f v2 = new Vec3f(A.y - B.y, A.y - C.y, P.y - A.y);  // wspolrzedne y wektorow

        Vec3f cross = crosser(v1, v2);                                  // iloczyn wektorowy v1 i v2. Wskazówka: zaimplementuj do tego oddzielną metodę

        kolorflat = FlatShadingRenderer.gradecolor(cross);

        Vec2f uv = new Vec2f(cross.x/cross.z, cross.y/cross.z);   // wektor postaci: cross.x / cross.z, cross.y / cross.z

        Vec3f barycentric = new Vec3f(uv.x, uv.y, 1 - uv.x - uv.y);  // współrzędne barycentryczne, uv.x, uv.y, 1- uv.x - uv.y

        return barycentric;
    }

    public static Vec3f crosser(Vec3f v1, Vec3f v2) {
        Vec3f cross = new Vec3f();

        cross.x = (v1.y * v2.z) - (v1.z * v2.y);

        cross.y = (v1.z * v2.x) - (v1.x * v2.z);

        cross.z = (v1.x * v2.y) - (v1.y * v2.x);

        return cross;
    }

    public void drawTriangle(Vec2f A, Vec2f B, Vec2f C, float A_z, float B_z, float C_z) {
        float[][] z_buffer = new float[w][h];
        // minimalny x sposrod wierzcholkow
        float minX = Math.min(A.x, B.x);
        minX = Math.min(minX, C.x);
        float maxX = Math.max(A.x, B.x);
        maxX = Math.max(maxX, C.x);

        // minimalny y
        float minY = Math.min(A.y, B.y);
        minY = Math.min(minY, C.y);
        float maxY = Math.max(A.y, B.y);
        maxY = Math.max(maxY, C.y);

        for (int x = (int) minX; x < (int) maxX; x++) {
            for (int y = (int) minY; y < (int) maxY; y++) {

                //System.out.println(barycentric(A, B, C, new Vec2f(x, y)));
                float z_depth = (A_z+B_z+C_z) / 3;
                // wspolrzedne barycentryczne dla kazdego punktu i czy kazdy piksel miesci sie w trojkacie
                if (barycentric(A, B, C, new Vec2f(x, y)).x >= 0 && barycentric(A, B, C, new Vec2f(x, y)).x <= 1) {
                    if (barycentric(A, B, C, new Vec2f(x, y)).y >= 0 && barycentric(A, B, C, new Vec2f(x, y)).y <= 1) {
                        if (barycentric(A, B, C, new Vec2f(x, y)).z >= 0 && barycentric(A, B, C, new Vec2f(x, y)).z <= 1) {
                            if (z_depth > z_buffer[x][y])
                            {
                                z_depth=z_buffer[x][y];
                                drawPoint(x, y);
                            }
                        }
                    }
                }
            }
        }
    }

    /*public void drawTriangle(Vec2f A, Vec2f B, Vec2f C, int randColor) {
        // minimalny x sposrod wierzcholkow
        float minX = Math.min(A.x, B.x);
        minX = Math.min(minX, C.x);
        float maxX = Math.max(A.x, B.x);
        maxX = Math.max(maxX, C.x);

        // minimalny y
        float minY = Math.min(A.y, B.y);
        minY = Math.min(minY, C.y);
        float maxY = Math.max(A.y, B.y);
        maxY = Math.max(maxY, C.y);

        for (int x = (int) minX; x < (int) maxX; x++) {
            for (int y = (int) minY; y < (int) maxY; y++) {

                //System.out.println(barycentric(A, B, C, new Vec2f(x, y)));
                // wspolrzedne barycentryczne dla kazdego punktu i czy kazdy piksel miesci sie w trojkacie
                if (barycentric(A, B, C, new Vec2f(x, y)).x >= 0 && barycentric(A, B, C, new Vec2f(x, y)).x <= 1) {
                    if (barycentric(A, B, C, new Vec2f(x, y)).y >= 0 && barycentric(A, B, C, new Vec2f(x, y)).y <= 1) {
                        if (barycentric(A, B, C, new Vec2f(x, y)).z >= 0 && barycentric(A, B, C, new Vec2f(x, y)).z <= 1) {
                            drawPoint(x, y, randColor);
                        }
                    }
                }
            }
        }
    }*/



    public void save() throws IOException {
        File outputfile = new File(filename);
        render = Renderer.verticalFlip(render);
        ImageIO.write(render, "png", outputfile);
    }

    public void clear() {
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int black = 0 | (0 << 8) | (0 << 16) | (255 << 24);
                render.setRGB(x, y, black);
            }
        }
    }

    public static BufferedImage verticalFlip(BufferedImage img) {
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage flippedImage = new BufferedImage(w, h, img.getColorModel().getTransparency());
        Graphics2D g = flippedImage.createGraphics();
        g.drawImage(img, 0, 0, w, h, 0, h, w, 0, null);
        g.dispose();
        return flippedImage;
    }
}