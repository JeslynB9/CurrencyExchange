package processing.core;

import java.awt.Desktop;
import java.awt.Image;
import java.awt.Taskbar;
import java.awt.desktop.QuitEvent;
import java.awt.desktop.QuitHandler;
import java.awt.desktop.QuitResponse;

public class ThinkDifferent {

    // True if user has tried to quit once. Prevents us from cancelling the quit
    // call if the sketch is held up for some reason, like an exception that's
    // managed to put the sketch in a bad state.
    static boolean attemptedQuit;

    public static void init(final PApplet sketch) {
        Desktop desktop = Desktop.getDesktop();
        desktop.setQuitHandler(
                new QuitHandler() {
                    @Override
                    public void handleQuitRequestWith(QuitEvent e, QuitResponse response) {
                        sketch.exit();
                        if (PApplet.uncaughtThrowable == null && !attemptedQuit) {
                            response.cancelQuit();
                            attemptedQuit = true;
                        } else {
                            response.performQuit();
                        }
                    }
                });
    }

    public static void cleanup() {
        Desktop.getDesktop().setQuitHandler(null);
    }

    // Called via reflection from PSurfaceAWT and others
    public static void setIconImage(Image image) {
        Taskbar.getTaskbar().setIconImage(image);
    }
}