package CurrencyExchange;

import processing.core.PApplet;
import processing.core.PImage;

public class PrintSummaryUI {
    PApplet parent;
    PImage switchImg;
    PImage dropdownFrom;
    PImage dropdownTo;
    PImage unSelectedConvert;
    PImage unSelectedPopular;
    Flag flagManager;
    static int width = 1920 / 2;
    static int height = 1080 / 2;
    float rectW = width-100;
    float rectH = height/2;
    float cornerRadius = 10;
    float rectX;
    float rectY;

    // Canvas center
    int centerX = width/2;
    int centerY = height/2;

    // Shadow offset
    float shadowOffsetX = 10;
    float shadowOffsetY = 10;

    // Draw the shadow all around (slightly larger than the rectangle)
    float shadowOffset = 8;

    boolean amountBoxSelected = false;
    boolean fromBoxSelected = false;
    boolean toBoxSelected = false;
    String selectedFromCurrencyText = "USD - Us Dollar"; // Default currency
    String selectedToCurrencyText = "EUR - Euros";   // Default currency
    String selectedFromCurrency = "USD";
    String selectedToCurrency = "EUR";

    // Constructor receives the PApplet instance
    public PrintSummaryUI(PApplet parent) {
        this.parent = parent;
        // Initialize Flag manager
        flagManager = new Flag(parent);
        // Load flags for selected currencies
        flagManager.loadFlag(selectedFromCurrency);
        flagManager.loadFlag(selectedToCurrency);

        // Calculate the rectangle's top-left corner based on the center
        rectX = width / 2 - rectW / 2;
        rectY = height / 2 - rectH / 2;
        System.out.println("PrintSummaryUI initialized");

        // Load the image
        switchImg = parent.loadImage("src/main/resources/switch.png");
        switchImg.resize(1920 / 40, 1080 / 40);

        dropdownFrom = parent.loadImage("src/main/resources/dropdown.png");
        dropdownFrom.resize(1920 / 80, 1080 / 80);

        dropdownTo = parent.loadImage("src/main/resources/dropdown.png");
        dropdownTo.resize(1920 / 80, 1080 / 80);

        unSelectedConvert = parent.loadImage("src/main/resources/convert-not-selected.png");
        unSelectedConvert.resize(1920 / 40, 1080 / 40);

        unSelectedPopular = parent.loadImage("src/main/resources/popular-not-selected.png");
        unSelectedPopular.resize(1920 / 40, 1080 / 40);
    }

    // Method to draw the converter UI
    public void drawPrintSummaryUI() {

        // Shadow properties
        parent.fill(0, 0, 0, 50);
        parent.noStroke();
        parent.rect(rectX - shadowOffset, rectY - shadowOffset, rectW + 2 * shadowOffset, rectH + 2 * shadowOffset, cornerRadius + 5);

        // Long rectangle header
        parent.fill(220, 202, 216);
        parent.rect(rectX, rectY+30, rectW, 30);
        parent.rect(rectX, rectY, rectW, 60, cornerRadius);

        // Main rectangle properties
        parent.fill(255, 249, 254);
        parent.noStroke();

        // Draw the main rounded rectangle
        parent.rect(rectX, rectY, rectW, rectH, cornerRadius);

    }

    // Method to handle mouse presses
    public void mousePressed() {

    }
}
