import org.lwjgl.opengl.Display;

public class Test {

    public static void main(String[] args) {
        DisplayManager.createDisplay();

        while (!Display.isCloseRequested()) {

            // game logic
            // render geometry
            DisplayManager.updateDisplay();
        }

        DisplayManager.closeDisplay();
    }

}
