package CurrencyExchange.Users;

import CurrencyExchange.Flag;
import processing.core.PApplet;
import processing.core.PImage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class AdminPopularUI {
    PApplet parent;
    PImage switchImg;
    PImage dropdown;
    PImage unSelectedConvert;
    PImage unSelectedPopular;
    Flag flagManager;
    CurrencyManager currencyManager;

    static int width = 1920 / 2;
    static int height = 1080 / 2;
    float rectW = width-100;
    float rectH = height/2;
    float cornerRadius = 10;
    float rectX;
    float rectY;

    Dropdown firstDropdown;
    Dropdown secondDropdown;
    Dropdown thirdDropdown;
    Dropdown fourthDropdown;

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
    boolean thirdCurrencyBox = false;
    boolean fourthCurrencyBox = false;

    String selectedFirstCurrencyText = "USD - US Dollar"; // Default currency
    String selectedSecondCurrencyText = "EUR - Euros";   // Default currency
    String selectedThirdCurrencyText = "AUD - AU Dollar";
    String selectedFourthCurrencyText = "JPY - JP Yen";

    String selectedFirstCurrency = "USD";
    String selectedSecondCurrency = "EUR";
    String selectedThirdCurrency = "AUD";
    String selectedFourthCurrency = "JPY";

    public AdminPopularUI(PApplet parent, CurrencyManager currencyManager) {
        this.parent = parent;
        this.currencyManager = currencyManager;
        flagManager = new Flag(parent);

        flagManager.loadFlag(selectedFirstCurrency);
        flagManager.loadFlag(selectedSecondCurrency);
        flagManager.loadFlag(selectedThirdCurrency);
        flagManager.loadFlag(selectedFourthCurrency);

        // Calculate the rectangle's top-left corner based on the center
        rectX = width / 2 - rectW / 2;
        rectY = height / 2 - rectH / 2;

        // Load the image
        switchImg = parent.loadImage("src/main/resources/switch.png");
        switchImg.resize(1920 / 40, 1080 / 40);

        dropdown = parent.loadImage("src/main/resources/dropdown.png");
        dropdown.resize(1920 / 80, 1080 / 80);

        unSelectedConvert = parent.loadImage("src/main/resources/convert-not-selected.png");
        unSelectedConvert.resize(1920 / 40, 1080 / 40);

        unSelectedPopular = parent.loadImage("src/main/resources/popular-not-selected.png");
        unSelectedPopular.resize(1920 / 40, 1080 / 40);

        String[] countries = { "USD - US Dollar", "EUR - Euro", "AUD - AU Dollar", "GBP - British Pound", "JPY - JP Yen" };
        firstDropdown = new Dropdown(parent, countries, 80, 250, 200, 40);
        secondDropdown = new Dropdown(parent, countries, 325, 250, 200, 40);
        thirdDropdown = new Dropdown(parent, countries, 80, 325, 200, 40);
        fourthDropdown = new Dropdown(parent, countries, 325, 325, 200, 40);
    }

    public void drawAdminPopularUI() {

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
            System.out.println("Mouse is hovering over 'Update' button");
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
        parent.text("Update", 800, 375);

        // Draw the 1st currency box
        if (firstCurrencyBox) {
            parent.fill(220, 202, 216);
        } else {
            parent.noFill();
        }
        parent.stroke(0);
        parent.rect(80, 250, 200, 40, cornerRadius);
        parent.image(dropdown, 250, 265);

        // Draw the 2nd currency box
        if (secondCurrencyBox) {
            parent.fill(220, 202, 216);
        } else {
            parent.noFill();
        }
        parent.stroke(0);
        parent.rect(325, 250, 200, 40, cornerRadius);
        parent.image(dropdown, 495, 265);

        // Draw the 3rd currency box
        if (thirdCurrencyBox) {
            parent.fill(220, 202, 216);
        } else {
            parent.noFill();
        }
        parent.stroke(0);
        parent.rect(80, 325, 200, 40, cornerRadius);
        parent.image(dropdown, 250, 340);

        // Draw the 4th currency box
        if (fourthCurrencyBox) {
            parent.fill(220, 202, 216);
        } else {
            parent.noFill();
        }
        parent.stroke(0);
        parent.rect(325, 325, 200, 40, cornerRadius);
        parent.image(dropdown, 495, 340);

        parent.textSize(12);
        parent.fill(0);
        parent.text("Currencies", 90, 245);

        // Draw the flags for currencies
        flagManager.drawFlag(selectedFirstCurrency, 90, 263);
        flagManager.drawFlag(selectedSecondCurrency, 335, 263);
        flagManager.drawFlag(selectedThirdCurrency, 90, 338);
        flagManager.drawFlag(selectedFourthCurrency, 335, 338);

        // Draw selected currencies
        parent.textSize(16);
        parent.text(selectedFirstCurrencyText, 120, 277);
        parent.text(selectedSecondCurrencyText, 365, 277);
        parent.text(selectedThirdCurrencyText, 120, 352);
        parent.text(selectedFourthCurrencyText, 365, 352);

        firstDropdown.draw();
        secondDropdown.draw();
        thirdDropdown.draw();
        fourthDropdown.draw();
        parent.textSize(16);
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
            thirdCurrencyBox = false;
            fourthCurrencyBox = false;
        }
        else if (isMouseOverButton(325, 250, 200, 40)) {
            firstCurrencyBox = false;
            secondCurrencyBox = true;
            thirdCurrencyBox = false;
            fourthCurrencyBox = false;
        }
        else if (isMouseOverButton(80, 325, 200, 40)) {
            firstCurrencyBox = false;
            secondCurrencyBox = false;
            thirdCurrencyBox = true;
            fourthCurrencyBox = false;
        }
        else if (isMouseOverButton(325, 325, 200, 40)) {
            firstCurrencyBox = false;
            secondCurrencyBox = false;
            thirdCurrencyBox = false;
            fourthCurrencyBox = true;
        }
        else if (isMouseOverButton(775, 350, 100, 40)) {
            List<String> selectedCurrencies = Arrays.asList(
                    selectedFirstCurrency, selectedSecondCurrency,
                    selectedThirdCurrency, selectedFourthCurrency
            );
            try {
                currencyManager.setPopularCurrencies(selectedCurrencies);
                System.out.println("Popular currencies updated successfully.");
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        // Handle dropdown interactions
        firstDropdown.mousePressed();
        secondDropdown.mousePressed();
        thirdDropdown.mousePressed();
        fourthDropdown.mousePressed();

        if (!firstDropdown.expanded && firstDropdown.getSelectedItem() != null &&
                !selectedFirstCurrencyText.equals(firstDropdown.getSelectedItem())) {
            selectedFirstCurrencyText = firstDropdown.getSelectedItem();
            selectedFirstCurrency = selectedFirstCurrencyText.split(" ")[0];
        }

        if (!secondDropdown.expanded && secondDropdown.getSelectedItem() != null &&
                !selectedSecondCurrency.equals(secondDropdown.getSelectedItem())) {
            selectedSecondCurrencyText = secondDropdown.getSelectedItem();
            selectedSecondCurrency = selectedSecondCurrencyText.split(" ")[0];
        }

        if (!thirdDropdown.expanded && thirdDropdown.getSelectedItem() != null &&
                !selectedThirdCurrency.equals(thirdDropdown.getSelectedItem())) {
            selectedThirdCurrencyText = thirdDropdown.getSelectedItem();
            selectedThirdCurrency = selectedThirdCurrencyText.split(" ")[0];
        }

        if (!fourthDropdown.expanded && fourthDropdown.getSelectedItem() != null &&
                !selectedFourthCurrency.equals(fourthDropdown.getSelectedItem())) {
            selectedFourthCurrencyText = fourthDropdown.getSelectedItem();
            selectedFourthCurrency = selectedFourthCurrencyText.split(" ")[0];
        }
    }
}



