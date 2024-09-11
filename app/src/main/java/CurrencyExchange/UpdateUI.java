package CurrencyExchange;

import processing.core.PApplet;
import processing.core.PImage;
import processing.data.Table;
import processing.data.TableRow;


public class UpdateUI {
    PApplet parent;
    PImage newratearrow;
    PImage dropdown;
    AddCurrency addCurrency;
    boolean toSelected = false;
    boolean dateSelected = false;
    boolean newRateSelected = false;
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

    float shadowOffset = 8;


    public UpdateUI(PApplet parent) {
        this.parent = parent;

        // Calculate the rectangle's top-left corner based on the center
        rectX = width / 2 - rectW / 2;
        rectY = height / 2 - rectH / 2;

        newratearrow = parent.loadImage("src/main/resources/newratearrow.png");
        newratearrow.resize(1920 / 40, 1080 / 40);

        dropdown = parent.loadImage("src/main/resources/dropdown.png");
        dropdown.resize(1920 / 80, 1080 / 80);

        addCurrency = new AddCurrency(parent);

    }
    public void drawUpdate() {
        // Shadow properties
        parent.fill(0, 0, 0, 50);
        parent.noStroke();
        parent.rect(rectX - shadowOffset, rectY - shadowOffset, rectW + 2 * shadowOffset, (float)(rectH*1.3) + 2 * shadowOffset, cornerRadius + 5);

        // Main rectangle properties
        parent.fill(255,249,254);
        parent.noStroke();

        // Draw the main rounded rectangle
        parent.rect(rectX, rectY, rectW, (float)(rectH*1.3), cornerRadius);

        // Long rectangle header
        parent.fill(220, 202, 216);
        parent.noStroke();
        parent.rect(rectX, rectY+30, rectW, 30);
        parent.rect(rectX, rectY, rectW, 60, cornerRadius);

        // Get mouse hover status
        boolean isHovering = isMouseOverButton(775, 425, 100, 40);

        // Change the color based on hover
        if (isHovering) {
            System.out.println("Mouse is hovering over 'Update' button");
            parent.fill(222, 37, 176, 200);
        } else {
            parent.fill(222, 37, 176); // Default color
        }

        // Draw the button after setting the fill color
        parent.noStroke();
        parent.rect(775, 425, 100, 40, cornerRadius);

        // Draw the button text
        parent.textSize(16);
        parent.fill(255);
        parent.text("Update", 797, 450);

        // Draw the static USD currency box
        parent.fill(220, 202, 216);
        parent.stroke(0);
        parent.rect(80, 250, 200, 40, cornerRadius);
        parent.fill(0);
        parent.textSize(12);
        parent.text("From", 90, 245);

        parent.noFill();
        parent.ellipse(338, 270, 40, 40);
        parent.image(newratearrow, 315, 257);

        // Draw the "TO" box
        if (toSelected) {
            parent.fill(220, 202, 216);
        } else {
            parent.noFill();
        }
        parent.stroke(0);
        parent.rect(395, 250, 200, 40, cornerRadius);
        parent.fill(0);
        parent.textSize(12);
        parent.text("To", 405, 245);

        parent.image(dropdown, 570, 265);

        // Draw the "DATE" box
        if (dateSelected) {
            parent.fill(220, 202, 216);
        } else {
            parent.noFill();
        }
        parent.stroke(0);
        parent.rect(635, 250, 200, 40, cornerRadius);
        parent.fill(0);
        parent.textSize(12);
        parent.text("Date", 645, 245);

        parent.fill(220, 202, 216);
        parent.stroke(0);
        parent.rect(217, 350, 200, 40, cornerRadius);
        parent.fill(0);
        parent.textSize(12);
        parent.text("Old Rate", 227, 345);

        // Draw the "NEW RATE" box
        if (newRateSelected) {
            parent.fill(220, 202, 216);
        } else {
            parent.noFill();
        }
        parent.stroke(0);
        parent.rect(545, 350, 200, 40, cornerRadius);
        parent.fill(0);
        parent.textSize(12);
        parent.text("New Rate", 555, 345);

        parent.noFill();
        parent.ellipse(480, 370, 40, 40);
        parent.image(newratearrow, 457, 357);

    }

    private boolean isMouseOverButton(int x, int y, int w, int h) {
        return (parent.mouseX > x && parent.mouseX < x + w &&
                parent.mouseY > y && parent.mouseY < y + h);
    }

    public void mousePressed() {
        if (isMouseOverButton(395, 250, 200, 40)) {
            toSelected = true;
            dateSelected = false;
            newRateSelected = false;
        }

        if (isMouseOverButton(635, 250, 200, 40)) {
            toSelected = false;
            dateSelected = true;
            newRateSelected = false;
        }

        if (isMouseOverButton(545, 350, 200, 40)) {
            toSelected = false;
            dateSelected = false;
            newRateSelected = true;
        }
        if (isMouseOverButton(775, 425, 100, 40)) {
            addCurrency.isAddCurrency = true;
            addCurrency.mousePressed();
        }

//         If you click on the + on the drop down menu, change the screen to AddCurrency
//         It may look like:
//         if (isMouseOverButton(x, y, w, h)) {
//            addCurrency.drawAddCurrency();
//            addCurrency.mousePressed()
//         }

    }
}
