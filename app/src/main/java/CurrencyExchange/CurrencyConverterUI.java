package CurrencyExchange;

import processing.core.PApplet;
import processing.core.PImage;

public class CurrencyConverterUI {
    PApplet parent;
    PImage switchImg;
    PImage dropdownFrom;
    PImage dropdownTo;
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
    public CurrencyConverterUI(PApplet parent) {
        this.parent = parent;
        // Initialize Flag manager
        flagManager = new Flag(parent);
        // Load flags for selected currencies
        flagManager.loadFlag(selectedFromCurrency);
        flagManager.loadFlag(selectedToCurrency);


        // Calculate the rectangle's top-left corner based on the center
        rectX = width / 2 - rectW / 2;
        rectY = height / 2 - rectH / 2;
        System.out.println("CurrencyConverterUI initialized");

        // Load the image
        switchImg = parent.loadImage("src/main/resources/switch.png");
        switchImg.resize(1920 / 40, 1080 / 40);

        dropdownFrom = parent.loadImage("src/main/resources/dropdown.png");
        dropdownFrom.resize(1920 / 80, 1080 / 80);

        dropdownTo = parent.loadImage("src/main/resources/dropdown.png");
        dropdownTo.resize(1920 / 80, 1080 / 80);
    }

    // Method to draw the converter UI
    public void drawConverter() {


        // Set text size using the PApplet instance
        parent.stroke(84, 84, 84);
        parent.textSize(12);
        parent.fill(0);

        // Shadow properties
        parent.fill(0, 0, 0, 50);
        parent.noStroke();
        parent.rect(rectX - shadowOffset, rectY - shadowOffset, rectW + 2 * shadowOffset, rectH + 2 * shadowOffset, cornerRadius + 5);

        // Main rectangle properties
        parent.fill(255,249,254);
        parent.noStroke();

        // Draw the main rounded rectangle
        parent.rect(rectX, rectY, rectW, rectH, cornerRadius);

        // Draw the "AMOUNT" box
        if (amountBoxSelected) {
            parent.fill(220, 202, 216);
        } else {
            parent.noFill();
        }

        parent.stroke(0);
        parent.rect(80, 250, 200, 40, cornerRadius);

        // Draw the "FROM" box
        if (fromBoxSelected) {
            parent.fill(220, 202, 216); // Highlight color
        } else {
            parent.noFill();
        }
        parent.stroke(0);
        parent.rect(350, 250, 200, 40, cornerRadius);

        // Get mouse hover status
        boolean isHoveringSwitch = isMouseOverButton(605, 255, 40, 40);

        // Change the color based on hover
        if (isHoveringSwitch) {
            System.out.println("Mouse is hovering over 'Switch' button");
            parent.fill(220, 202, 216, 191);
        } else {
            parent.noFill();
        }
        parent.ellipse(613, 270, 40, 40);
        parent.noFill();

        // Draw switch img
        parent.image(switchImg, 590, 257);

        // Draw the "TO" box
        if (toBoxSelected) {
            parent.fill(220, 202, 216);
        } else {
            parent.noFill();
        }
        parent.stroke(0);
        parent.rect(675, 250, 200, 40, cornerRadius);

        // Drop down icon
        parent.image(dropdownFrom, 525, 265);
        parent.image(dropdownTo, 850, 265);

        // Draw the flag for "TO" currency
        flagManager.drawFlag(selectedToCurrency, 685, 263);
        // Draw the flag for "FROM" currency
        flagManager.drawFlag(selectedFromCurrency, 360, 263);

        // Example text labels
        parent.fill(0);
        parent.text("From", 360, 245);
        parent.text("To", 680, 245);
        parent.text("Amount", 90, 245);

        // Draw selected currencies
        parent.textSize(16);
        parent.text(selectedToCurrencyText, 715, 277);
        parent.text(selectedFromCurrencyText, 390, 277);

        // Get mouse hover status
        boolean isHovering = isMouseOverButton(785, 350, 100, 40);

        // Change the color based on hover
        if (isHovering) {
            System.out.println("Mouse is hovering over 'Convert' button");
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
        parent.text("Convert", 795, 375);
    }

    private boolean isMouseOverButton(int x, int y, int w, int h) {
        return (parent.mouseX > x && parent.mouseX < x + w &&
                parent.mouseY > y && parent.mouseY < y + h);
    }

    // Method to handle mouse presses
    public void mousePressed() {
        // Check if the "AMOUNT" box is clicked
        if (isMouseOverButton(80, 250, 200, 40)) {
            amountBoxSelected = true;
            fromBoxSelected = false; // Deselect the other box
            toBoxSelected = false;  // Deselect the other box
        }
        // Check if the "FROM" box is clicked
        else if (isMouseOverButton(350, 250, 200, 40)) {
            amountBoxSelected = false; // Deselect the other box
            fromBoxSelected = true;
            toBoxSelected = false;  // Deselect the other box
        }
        // Check if the "TO" box is clicked
        else if (isMouseOverButton(675, 250, 200, 40)) {
            amountBoxSelected = false; // Deselect the other box
            fromBoxSelected = false;  // Deselect the other box
            toBoxSelected = true;
        }
    }

}
