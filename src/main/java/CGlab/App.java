package CGlab;

import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {

    String version = "0.02";

    public static void main(String[] args) {

        Renderer mainRenderer = new Renderer(args);
        mainRenderer.clear();
        //mainRenderer.drawPoint(100, 100);
        try {
            // 1
            //mainRenderer.drawLine(100,100,150,400);
            // 2
            //mainRenderer.drawLine(100,100,150,450);
            // 3
            //mainRenderer.drawLine(200,150,0,200);
            // 4
            //mainRenderer.drawLine(100,100,120,400);
            // 5
            //mainRenderer.drawLine(100,100,120,400);
            // 6
            //mainRenderer.drawLine(100,100,120,400);
            // 7
            //mainRenderer.drawLine(100,100,120,400);
            // 8
            //mainRenderer.drawLine(100,100,120,400);

            //mainRenderer.drawTriangle(new Vec2f(50, 50), new Vec2f(230, 100), new Vec2f(170, 300));

            Model model = new Model();
            RandomColorRenderer jelenRender = new RandomColorRenderer("render.png", 640, 480);
            model.readOBJ("deer.obj");
            jelenRender.render(model);
            jelenRender.save();

            //mainRenderer.save();
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getVersion() {
	return this.version;
    }
}
