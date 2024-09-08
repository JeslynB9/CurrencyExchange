package CurrencyExchange;

import java.util.HashMap;
import processing.core.PApplet;
import processing.core.PImage;


public class Flag {
    private final HashMap<String, PImage> flagMap;
    private final PApplet pApplet;  // Reference to PApplet for image loading

    public Flag(PApplet parent) {
        this.pApplet = parent;
        flagMap = new HashMap<>();
    }

    // Load flag
    public void loadFlag(String countryCode) {
        if (!flagMap.containsKey(countryCode)) {
            // Load the image only if it's not already loaded
            PImage flag = pApplet.loadImage("src/main/resources/flags/" + countryCode.toUpperCase() + ".png");
            if (flag != null) {
                flag.resize(200/10, 150/10);
                flagMap.put(countryCode, flag);

            }
        }
    }

    // Get the loaded flag image
    public PImage getFlag(String countryCode) {
        return flagMap.get(countryCode);
    }

    // Draw the flag
    public void drawFlag(String countryCode, int x, int y) {
        PImage flag = getFlag(countryCode);
        if (flag != null) {
            pApplet.image(flag, x, y);
        }
    }
}
