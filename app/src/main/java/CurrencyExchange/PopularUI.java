package CurrencyExchange;

import processing.core.PApplet;
import processing.data.Table;
import processing.data.TableRow;
import CurrencyExchange.Users.CurrencyManager;
import java.util.HashMap;
import java.util.Map;

public class PopularUI {
    PApplet parent;
    CurrencyManager currencyManager;

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
    private Map<String, Double> previousRates;

    public PopularUI(PApplet parent, CurrencyManager currencyManager) {
        this.parent = parent;
        this.currencyManager = currencyManager;

        // Calculate the rectangle's top-left corner based on the center
        rectX = width / 2 - rectW / 2;
        rectY = height / 2 - rectH / 2;
        previousRates = new HashMap<>();
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

        // Get the popular currencies from the currency manager
        String[] popularCurrencies = currencyManager.popularCurrencies.toArray(new String[0]);

        parent.pushStyle();

        float textX = rectX + (rectW / 6) / 2;
        float textY = rectY + (rectH / 6) / 2;

        // Header Cells
        parent.fill(92, 16, 73);
        parent.stroke(255, 249, 254);
        parent.strokeWeight(1);

        // Draw top-left corner cell (this was missing)
        parent.fill(92, 16, 73); // Dark fill color
        parent.stroke(255, 249, 254); // Stroke color
        parent.rect(123, 225, rectW / 6, rectH / 6); // Position the top-left cell

//        parent.rect(123, 225, rectW/6, rectH/6);
//        parent.rect(266, 225, rectW/6, rectH/6);
//        parent.rect(409, 225, rectW/6, rectH/6);
//        parent.rect(552, 225, rectW/6, rectH/6);
//        parent.rect(695, 225, rectW/6, rectH/6);
//
//        parent.stroke(255, 249, 254);
//        parent.rect(695, 225, rectW/6+1, rectH/6);
//
//        // Row Identifier Cells
//        parent.fill(92, 16, 73);
//        parent.stroke(255, 249, 254);
//        parent.rect(123, 269, rectW/6, rectH/5);
//        parent.rect(123, 323, rectW/6, rectH/5);
//        parent.rect(123, 377, rectW/6, rectH/5);
//        parent.rect(123, 431, rectW/6, rectH/5);
//
//        parent.rect(123, 431, rectW/6, rectH/5+1);
//
//        // Filler Cells
//        parent.fill(255, 249, 254);
//        parent.stroke(92, 16, 73);
//        parent.rect(266, 269, rectW/6, rectH/5);
//        parent.rect(266, 323, rectW/6, rectH/5);
//        parent.rect(266, 377, rectW/6, rectH/5);
//        parent.rect(266, 431, rectW/6, rectH/5);
//
//        parent.rect(409, 269, rectW/6, rectH/5);
//        parent.rect(409, 323, rectW/6, rectH/5);
//        parent.rect(409, 377, rectW/6, rectH/5);
//        parent.rect(409, 431, rectW/6, rectH/5);
//
//        parent.rect(552, 269, rectW/6, rectH/5);
//        parent.rect(552, 323, rectW/6, rectH/5);
//        parent.rect(552, 377, rectW/6, rectH/5);
//        parent.rect(552, 431, rectW/6, rectH/5);
//
//        parent.rect(695, 269, rectW/6, rectH/5);
//        parent.rect(695, 323, rectW/6, rectH/5);
//        parent.rect(695, 377, rectW/6, rectH/5);
//        parent.rect(695, 431, rectW/6, rectH/5);
//
//        parent.popStyle();
//
//        // Draw column headers (currency names)
//        parent.fill(92, 16, 73);
//        parent.stroke(255, 249, 254);
//        for (int i = 0; i < popularCurrencies.length; i++) {
//            float headerX = 123 + (i + 1) * rectW / 6;
//            parent.rect(headerX, 225, rectW / 6, rectH / 6); // Draw header cell
//            parent.fill(255); // Set text color
//            parent.textSize(14);
//            parent.textAlign(parent.CENTER, parent.CENTER);
//            parent.text(popularCurrencies[i], headerX + 10, 245); // Draw currency name
//        }
//
//        // Draw row headers (currency names)
//        for (int i = 0; i < popularCurrencies.length; i++) {
//            float rowY = 269 + i * (rectH / 5);
//            parent.fill(92, 16, 73);
//            parent.rect(123, rowY, rectW / 6, rectH / 5); // Draw row identifier
//            parent.fill(255);
//            parent.textSize(14);
//            parent.textAlign(parent.CENTER, parent.CENTER);
//            parent.text(popularCurrencies[i], 133, rowY + cellHeight / 2); // Draw currency name
//        }
//
//        // Draw exchange rates between currencies
//        for (int row = 0; row < rows; row++) {
//            for (int col = 0; col < cols; col++) {
//                float cellX = 123 + (col + 1) * rectW / 6;
//                float cellY = 269 + row * (rectH / 5);
//
//                parent.fill(255, 249, 254);
//                parent.stroke(92, 16, 73);
//                parent.rect(cellX, cellY, rectW / 6, rectH / 5); // Draw cell
//
//                if (row != col) { // No conversion if it's the same currency
//                    double rate = currencyManager.convertCurrency(1, popularCurrencies[row], popularCurrencies[col]);
//                    parent.fill(0);
//                    parent.textSize(14);
//                    parent.text(String.format("%.2f", rate), cellX + 10, cellY + cellHeight / 2); // Draw exchange rate
//                } else {
//                    parent.fill(0);
//                    parent.textSize(14);
//                    parent.text("-", cellX + 10, cellY + cellHeight / 2); // Draw dash for same currency
//                }
//            }
//        }





        // Draw column headers (currency names)
        for (int i = 0; i < popularCurrencies.length; i++) {
            float headerX = 123 + (i + 1) * rectW / 6; // Correct position for each column header
            parent.fill(92, 16, 73);
            parent.rect(headerX, 225, rectW / 6, rectH / 6); // Draw header cell
            parent.fill(255); // Set text color
            parent.textSize(14);
            parent.textAlign(parent.CENTER, parent.CENTER); // Center the text
            parent.text(popularCurrencies[i], headerX + rectW / 12, 245); // Center text in the cell
        }

        // Draw row headers (currency names)
        for (int i = 0; i < popularCurrencies.length; i++) {
            float rowY = 269 + i * (rectH / 5);
            parent.fill(92, 16, 73);
            parent.rect(123, rowY, rectW / 6, rectH / 5); // Draw row identifier
            parent.fill(255);
            parent.textSize(14);
            parent.textAlign(parent.CENTER, parent.CENTER); // Center the text
            parent.text(popularCurrencies[i], 123 + rectW / 12, rowY + cellHeight / 2); // Adjust row text position
        }

        // Draw exchange rates between currencies
//        for (int row = 0; row < rows; row++) {
//            for (int col = 0; col < cols; col++) {
//                float cellX = 123 + (col + 1) * rectW / 6;
//                float cellY = 269 + row * (rectH / 5);
//
//                parent.fill(255, 249, 254);
//                parent.stroke(92, 16, 73);
//                parent.rect(cellX, cellY, rectW / 6, rectH / 5); // Draw cell
//
//                if (row != col) { // No conversion if it's the same currency
//                    double rate = currencyManager.convertCurrency(1, popularCurrencies[row], popularCurrencies[col]);
//                    parent.fill(0);
//                    parent.textSize(14);
//                    parent.textAlign(parent.CENTER, parent.CENTER); // Center the text
//                    parent.text(String.format("%.2f", rate), cellX + rectW / 12, cellY + cellHeight / 2); // Draw exchange rate
//                } else {
//                    parent.fill(0);
//                    parent.textSize(14);
//                    parent.textAlign(parent.CENTER, parent.CENTER); // Center the text
//                    parent.text("-", cellX + rectW / 12, cellY + cellHeight / 2); // Draw dash for same currency
//                }
//            }
//        }


        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                float cellX = 123 + (col + 1) * rectW / 6;
                float cellY = 269 + row * (rectH / 5);

                parent.fill(255, 249, 254);
                parent.stroke(92, 16, 73);
                parent.rect(cellX, cellY, rectW / 6, rectH / 5);

                if (row != col) {
                    String fromCurrency = popularCurrencies[row];
                    String toCurrency = popularCurrencies[col];

                    // Get the current rate
                    double currentRate = currencyManager.convertCurrency(1, fromCurrency, toCurrency);

                    // Get the last recorded rate for the target currency
                    double lastRate = currencyManager.getLastExchangeRate(toCurrency);

                    parent.textSize(14);
                    parent.textAlign(parent.CENTER, parent.CENTER);

                    String arrow = "same"; // Default arrow (no change)
                    int arrowColor = parent.color(0, 0, 255); // Green for increase


                    if (currentRate > lastRate) {
                        arrow = "increase"; // Up arrow
                        arrowColor = parent.color(0, 255, 0); // Green for increase
                    } else if (currentRate < lastRate) {
                        arrow = "decrease"; // Down arrow
                        arrowColor = parent.color(255, 0, 0); // Red for decrease
                    }

                    // Draw the rate
                    parent.fill(0);
                    parent.text(String.format("%.2f", currentRate), cellX + rectW / 12, cellY + cellHeight / 2 - 10);

                    // Draw the arrow
                    parent.fill(arrowColor);
                    parent.text(arrow, cellX + rectW / 12, cellY + cellHeight / 2 + 10);

                    // Debug output
                    System.out.println("Rate for " + fromCurrency + " to " + toCurrency +
                            ": Current = " + currentRate +
                            ", Last = " + lastRate +
                            ", Arrow = " + arrow);

                } else {
                    parent.fill(0);
                    parent.textSize(14);
                    parent.textAlign(parent.CENTER, parent.CENTER);
                    parent.text("-", cellX + rectW / 12, cellY + cellHeight / 2);
                }
            }
        }





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
