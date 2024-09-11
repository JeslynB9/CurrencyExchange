package CurrencyExchange;

import processing.core.PApplet;
import processing.core.PImage;

public class PrintSummaryUI {
    PApplet parent;
    PImage switchImg;
    PImage dropdownFirst;
    PImage dropdownSecond;
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

    boolean firstCurrencyBox = false;
    boolean secondCurrencyBox = false;
    boolean startDate = false;
    boolean endDate = false;
    String selectedFirstCurrencyText = "USD - Us Dollar"; // Default currency
    String selectedSecondCurrencyText = "EUR - Euros";   // Default currency
    String selectedFirstCurrency = "USD";
    String selectedSecondCurrency = "EUR";

    // Constructor receives the PApplet instance
    public PrintSummaryUI(PApplet parent) {
        this.parent = parent;
        // Initialize Flag manager
        flagManager = new Flag(parent);
        // Load flags for selected currencies
        flagManager.loadFlag(selectedFirstCurrency);
        flagManager.loadFlag(selectedSecondCurrency);

        // Calculate the rectangle's top-left corner based on the center
        rectX = width / 2 - rectW / 2;
        rectY = height / 2 - rectH / 2;
        System.out.println("PrintSummaryUI initialized");

        // Load the image
        switchImg = parent.loadImage("src/main/resources/switch.png");
        switchImg.resize(1920 / 40, 1080 / 40);

        dropdownFirst = parent.loadImage("src/main/resources/dropdown.png");
        dropdownFirst.resize(1920 / 80, 1080 / 80);

        dropdownSecond = parent.loadImage("src/main/resources/dropdown.png");
        dropdownSecond.resize(1920 / 80, 1080 / 80);

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

        // Main rectangle properties
        parent.fill(255,249,254);
        parent.noStroke();

        // Draw the main rounded rectangle
        parent.rect(rectX, rectY, rectW, rectH, cornerRadius);

        // Long rectangle header
        parent.fill(220, 202, 216);
        parent.noStroke();
        parent.rect(rectX, rectY+30, rectW, 30);
        parent.rect(rectX, rectY, rectW, 60, cornerRadius);

        // Get mouse hover status
        boolean isHovering = isMouseOverButton(775, 350, 100, 40);

        // Change the color based on hover
        if (isHovering) {
            System.out.println("Mouse is hovering over 'Print' button");
            parent.fill(222, 37, 176, 200);
        } else {
            parent.fill(222, 37, 176); // Default color
        }

        // Draw the button after setting the fill color
        parent.noStroke();
        parent.rect(775, 350, 100, 40, cornerRadius);

        // Draw the button text
        parent.textSize(16);
        parent.fill(255);
        parent.text("Print", 807, 375);

        // Draw the 1st currency box
        if (firstCurrencyBox) {
            parent.fill(220, 202, 216);
        } else {
            parent.noFill();
        }

        parent.stroke(0);
        parent.rect(80, 250, 200, 40, cornerRadius);

        parent.image(dropdownFirst, 250, 265);

        // Draw the 2nd currency box
        if (secondCurrencyBox) {
            parent.fill(220, 202, 216);
        } else {
            parent.noFill();
        }

        parent.stroke(0);
        parent.rect(325, 250, 200, 40, cornerRadius);

        parent.image(dropdownSecond, 495, 265);

        parent.textSize(12);
        parent.fill(0);
        parent.text("Currencies", 90, 245);

        if (startDate) {
            parent.fill(220, 202, 216);
        } else {
            parent.noFill();
        }

        parent.stroke(0);
        parent.rect(80, 325, 200, 40, cornerRadius);
        parent.fill(0);
        parent.text("Start Date", 90, 320);

        if (endDate) {
            parent.fill(220, 202, 216);
        } else {
            parent.noFill();
        }

        parent.stroke(0);
        parent.rect(325, 325, 200, 40, cornerRadius);
        parent.fill(0);
        parent.text("End Date", 330, 320);

        // Draw the flag for 1st currency
        flagManager.drawFlag(selectedFirstCurrency, 90, 263);
        // Draw the flag for 2nd currency
        flagManager.drawFlag(selectedSecondCurrency, 335, 263);

        // Draw selected currencies
        parent.textSize(16);
        parent.text(selectedFirstCurrencyText, 120, 277);
        parent.text(selectedSecondCurrencyText, 365, 277);

    }

    private boolean isMouseOverButton(int x, int y, int w, int h) {
        return (parent.mouseX > x && parent.mouseX < x + w &&
                parent.mouseY > y && parent.mouseY < y + h);
    }

    // Method to handle mouse presses
    public void mousePressed() {
        // Check if the 1st currency box is clicked
        if (isMouseOverButton(80, 250, 200, 40)) {
            firstCurrencyBox = true;
            secondCurrencyBox = false;
            startDate = false;
            endDate = false;
        }
        if (isMouseOverButton(325, 250, 200, 40)) {
            firstCurrencyBox = false;
            secondCurrencyBox = true;
            startDate = false;
            endDate = false;
        }

        if (isMouseOverButton(80, 325, 200, 40)) {
            firstCurrencyBox = false;
            secondCurrencyBox = false;
            startDate = true;
            endDate = false;
        }
        if (isMouseOverButton(325, 325, 200, 40)) {
            firstCurrencyBox = false;
            secondCurrencyBox = false;
            startDate = false;
            endDate = true;
        }

    }
}
