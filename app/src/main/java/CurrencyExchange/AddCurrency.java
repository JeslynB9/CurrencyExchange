package CurrencyExchange;

import processing.core.PApplet;

public class AddCurrency {
    PApplet parent;
    boolean isAddCurrency = false;
    boolean nameSelected = false;
    boolean flagSelected = false;
    boolean symbolSelected = false;
    boolean rateSelected = false;
    float shadowOffset = 8;
    Login login;
    public AddCurrency(PApplet parent) {

        this.parent = parent;
        this.login = login;
    }

    public void drawAddCurrency() {

        if (!isAddCurrency) return;

        // Background Overlay
        parent.fill(0, 0, 0, 150);
        parent.rect(0, 0, parent.width*2, parent.height);

        // Shadow properties
        parent.fill(0, 0, 0, 50);
        parent.noStroke();
        parent.rect(parent.width / 2 - 200 - shadowOffset, parent.height / 2 - 200 - shadowOffset, 400 + 2 * shadowOffset, 400 + 2 * shadowOffset, 15);


        // White Login Box
        parent.fill(255,249,254);
        parent.stroke(200);
        parent.rect(parent.width / 2 - 200, parent.height / 2 - 200, 400, 400, 10);

        // Title Text
        parent.fill(0);
        parent.textSize(24);
        parent.text("Add Currency", 397, 140);

        // Currency Name Field
        if (nameSelected) {
            parent.fill(220, 202, 216);
        } else {
            parent.noFill();
        }
        parent.stroke(84, 84, 84);
        parent.rect(parent.width / 2 - 120, parent.height / 2 - 100, 240, 40, 5);
        parent.fill(84, 84, 84);
        parent.textSize(16);
        parent.text("Currency Name", 370, 197);

        // Flag Field
        if (flagSelected) {
            parent.fill(220, 202, 216);
        } else {
            parent.noFill();
        }
        parent.stroke(84, 84, 84);
        parent.rect(parent.width / 2 - 120, parent.height / 2 - 50, 240, 40, 5);
        parent.fill(84, 84, 84);
        parent.textSize(16);
        parent.text("Flag", 370, 247);

        // Symbol Field
        if (symbolSelected) {
            parent.fill(220, 202, 216);
        } else {
            parent.noFill();
        }
        parent.stroke(84, 84, 84);
        parent.rect(parent.width / 2 - 120, parent.height / 2, 240, 40, 5);
        parent.fill(84, 84, 84);
        parent.textSize(16);
        parent.text("Symbol", 370, 297);

        // Exchange Rate Field
        if (rateSelected) {
            parent.fill(220, 202, 216);
        } else {
            parent.noFill();
        }
        parent.stroke(84, 84, 84);
        parent.rect(parent.width / 2 - 120, parent.height / 2 + 50, 240, 40, 5);
        parent.fill(84, 84, 84);
        parent.textSize(16);
        parent.text("Exchange Rate", 370, 347);

        // Register Button
        // Draw the button after setting the fill color
        boolean isHover = isMouseOverButton(560, 410, 100, 40);
        if (isHover) {
            parent.fill(222, 37, 176, 200);
        } else {
            parent.fill(222, 37, 176);
        }
        parent.noStroke();
        parent.rect(560, 410, 100, 40, 10);
        parent.fill(255);
        parent.textSize(16);
        parent.text("Add", 595, 435);

        boolean isHovering = isMouseOverButton(300, 425, (int)parent.textWidth("Cancel?"), 10);
        if (isHovering) {
            parent.fill(222, 37, 176);
        } else {
            parent.fill(0);
        }

        parent.text("Cancel?", 300, 435);

    }

    private boolean isMouseOverButton(int x, int y, int w, int h) {
        return (parent.mouseX > x && parent.mouseX < x + w &&
                parent.mouseY > y && parent.mouseY < y + h);
    }

    public void mousePressed() {
        if (isMouseOverButton(300, 425, (int)parent.textWidth("Cancel?"), 10)) {
            System.out.println("Cancel clicked");
            isAddCurrency = false;
        }
        // When "ADD" button is pressed, change the new currency to the one that was just added, and remove the screen:
//        isAddCurrency = false;

        if (isMouseOverButton(parent.width / 2 - 120, parent.height / 2 - 100, 240, 40)) {
            nameSelected = true;
            flagSelected = false;
            symbolSelected = false;
            rateSelected = false;
        }

        if (isMouseOverButton(parent.width / 2 - 120, parent.height / 2 - 50, 240, 40)) {
            nameSelected = false;
            flagSelected = true;
            symbolSelected = false;
            rateSelected = false;
        }

        if (isMouseOverButton(parent.width / 2 - 120, parent.height / 2, 240, 40)) {
            nameSelected = false;
            flagSelected = false;
            symbolSelected = true;
            rateSelected = false;
        }

        if (isMouseOverButton(parent.width / 2 - 120, parent.height / 2 + 50, 240, 40)) {
            nameSelected = false;
            flagSelected = false;
            symbolSelected = false;
            rateSelected = true;
        }
    }
}
