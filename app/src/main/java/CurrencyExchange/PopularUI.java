package CurrencyExchange;

import processing.core.PApplet;
import processing.data.Table;
import processing.data.TableRow;

public class PopularUI {
    PApplet parent;
    static int width = 1920 / 2;
    static int height = 1080 / 2;
    float rectW = width - 100;
    float rectH = height / 2;
    float cornerRadius = 10;
    float rectX;
    float rectY;

    // Shadow offset
    float shadowOffset = 8;

    // Table dimensions
    int rows = 4;
    int cols = 4;
    int cellWidth = 100;
    int cellHeight = 50;
    int headerHeight = 40;

    public PopularUI(PApplet parent) {
        this.parent = parent;

        // Calculate the rectangle's top-left corner based on the center
        rectX = width / 2 - rectW / 2;
        rectY = height / 2 - rectH / 2;

    }

    public void drawPopular() {
        // Shadow properties
        parent.fill(0, 0, 0, 50);
        parent.noStroke();
        parent.rect(rectX - shadowOffset, rectY - shadowOffset, rectW + 2 * shadowOffset, (float)(rectH * 1.4) + 2 * shadowOffset, cornerRadius + 5);

        // Main rectangle properties
        parent.fill(255, 249, 254);
        parent.noStroke();

        // Draw the main rounded rectangle
        parent.rect(rectX, rectY, rectW, (float)(rectH * 1.4), cornerRadius);

        // Long rectangle header
        parent.fill(220, 202, 216);
        parent.noStroke();
        parent.rect(rectX, rectY + 30, rectW, 30);
        parent.rect(rectX, rectY, rectW, 60, cornerRadius);

        parent.pushStyle();

        float textX = rectX + (rectW / 6) / 2;
        float textY = rectY + (rectH / 6) / 2;

        // Header Cells
        parent.fill(92, 16, 73);
        parent.stroke(255, 249, 254);
        parent.strokeWeight(1);
        parent.rect(123, 225, rectW/6, rectH/6);
        parent.rect(266, 225, rectW/6, rectH/6);
        parent.rect(409, 225, rectW/6, rectH/6);
        parent.rect(552, 225, rectW/6, rectH/6);
        parent.rect(695, 225, rectW/6, rectH/6);

        parent.stroke(255, 249, 254);
        parent.rect(695, 225, rectW/6+1, rectH/6);

        // Row Identifier Cells
        parent.fill(92, 16, 73);
        parent.stroke(255, 249, 254);
        parent.rect(123, 269, rectW/6, rectH/5);
        parent.rect(123, 323, rectW/6, rectH/5);
        parent.rect(123, 377, rectW/6, rectH/5);
        parent.rect(123, 431, rectW/6, rectH/5);

        parent.rect(123, 431, rectW/6, rectH/5+1);

        // Filler Cells
        parent.fill(255, 249, 254);
        parent.stroke(92, 16, 73);
        parent.rect(266, 269, rectW/6, rectH/5);
        parent.rect(266, 323, rectW/6, rectH/5);
        parent.rect(266, 377, rectW/6, rectH/5);
        parent.rect(266, 431, rectW/6, rectH/5);

        parent.rect(409, 269, rectW/6, rectH/5);
        parent.rect(409, 323, rectW/6, rectH/5);
        parent.rect(409, 377, rectW/6, rectH/5);
        parent.rect(409, 431, rectW/6, rectH/5);

        parent.rect(552, 269, rectW/6, rectH/5);
        parent.rect(552, 323, rectW/6, rectH/5);
        parent.rect(552, 377, rectW/6, rectH/5);
        parent.rect(552, 431, rectW/6, rectH/5);

        parent.rect(695, 269, rectW/6, rectH/5);
        parent.rect(695, 323, rectW/6, rectH/5);
        parent.rect(695, 377, rectW/6, rectH/5);
        parent.rect(695, 431, rectW/6, rectH/5);


        parent.popStyle();
    }

    private boolean isMouseOverButton(int x, int y, int w, int h) {
        return (parent.mouseX > x && parent.mouseX < x + w &&
                parent.mouseY > y && parent.mouseY < y + h);
    }

    public void mousePressed() {
        // Handle mouse pressed events here
    }
}
