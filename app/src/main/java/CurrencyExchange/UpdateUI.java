package CurrencyExchange;

import processing.core.PApplet;
import processing.core.PImage;
import processing.data.Table;
import processing.data.TableRow;

import CurrencyExchange.Users.Dropdown;
import CurrencyExchange.FileHandlers.Database;
import CurrencyExchange.Users.CurrencyManager;

public class UpdateUI {
    PApplet parent;
    App parent2;
    PImage newratearrow;
    PImage dropdown;
    AddCurrency addCurrency;
    CurrencyManager currencyManager;
    Dropdown toDropdown;
    Flag flagManager;
    Login login;
    Database database;

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

    String selectedToCurrencyText = "AUD - AU Dollar"; // Default currency
    String selectedToCurrency = "AUD";  // Default "To" currency
    String enteredDate = "";
    String enteredNewRate = "";


    public UpdateUI(PApplet parent, Database database, App parent2) { //parent2 to store user currently logged in
        this.parent = parent;
        this.database = database;
        this.parent2 = parent2;

        flagManager = new Flag(parent);
        flagManager.loadFlag("USD");
        flagManager.loadFlag(selectedToCurrency);

        // Calculate the rectangle's top-left corner based on the center
        rectX = width / 2 - rectW / 2;
        rectY = height / 2 - rectH / 2;

        newratearrow = parent.loadImage("src/main/resources/newratearrow.png");
        newratearrow.resize(1920 / 40, 1080 / 40);

        dropdown = parent.loadImage("src/main/resources/dropdown.png");
        dropdown.resize(1920 / 80, 1080 / 80);

        addCurrency = new AddCurrency(parent, login);

        // Initialize the dropdown for "To" currency selection
        String[] countries = { "EUR - Euro", "AUD - AU Dollar", "GBP - British Pound", "JPY - JP Yen", "PHP - Philippine Peso" };
        toDropdown = new Dropdown(parent, countries, 395, 250, 200, 40);
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

        // Draw Update button after setting the fill color
        parent.noStroke();
        parent.rect(775, 425, 100, 40, cornerRadius);
        // Draw the button text
        parent.textSize(16);
        parent.fill(255);
        parent.text("Update", 797, 450);

        // Get mouse hover status
        boolean isHovering2 = isMouseOverButton(600, 425, 150, 40);

        // Change the color based on hover
        if (isHovering2) {
            System.out.println("Mouse is hovering over 'Update' button");
            parent.fill(222, 37, 176, 200);
        } else {
            parent.fill(222, 37, 176); // Default color
        }
        // Draw Add Currency button after setting the fill color
        parent.noStroke();
        parent.rect(600, 425, 150, 40, cornerRadius);
        // Draw the button text
        parent.textSize(16);
        parent.fill(255);
        parent.text("Add Currency", 622, 450);

        // Draw the static USD currency box
        parent.fill(220, 202, 216);
        parent.stroke(0);
        parent.rect(80, 250, 200, 40, cornerRadius);
        parent.fill(0);
        parent.textSize(12);
        parent.text("From", 90, 245);
        parent.textSize(16);
        flagManager.drawFlag("USD", 90, 263);
        parent.text("USD - US Dollar", 120, 275);

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
        flagManager.drawFlag(selectedToCurrency, 405, 263);

        parent.fill(0);
        parent.textSize(16);
        parent.text(selectedToCurrencyText, 435, 277);

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
        parent.text(enteredDate, 645, 275);

        parent.fill(220, 202, 216);
        parent.stroke(0);
        parent.rect(217, 350, 200, 40, cornerRadius);
        parent.fill(0);
        parent.textSize(12);
        parent.text("Old Rate", 227, 345);

        parent.textSize(16);
        parent.text(Double.toString(database.getLastExchangeRate(selectedToCurrency)), 227, 375); // need to replace to an old rate

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
        parent.text(enteredNewRate, 555, 375);

        parent.noFill();
        parent.ellipse(480, 370, 40, 40);
        parent.image(newratearrow, 457, 357);

        toDropdown.draw();
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
            performUpdate();
        }
        if (isMouseOverButton(600, 425, 150, 40)) {
            addCurrency.isAddCurrency = true;
            addCurrency.mousePressed();
        }

        toDropdown.mousePressed();

        if (!toDropdown.expanded && toDropdown.getSelectedItem() != null &&
                !selectedToCurrencyText.equals(toDropdown.getSelectedItem())) {
            selectedToCurrencyText = toDropdown.getSelectedItem();
            selectedToCurrency = selectedToCurrencyText.split(" ")[0];
            flagManager.loadFlag(selectedToCurrency);
            System.out.println("To currency updated to: " + selectedToCurrencyText);  // Debugging print
        }
    }

    public void keyPressed() {
        if (dateSelected) {
            handleDateInput();
        } else if (newRateSelected) {
            handleNewRateInput();
        }
    }

    private void handleDateInput() {
        char key = parent.key;
        if (Character.isDigit(key) || key == '-') {
            enteredDate += key;
        }
        if (key == PApplet.BACKSPACE && enteredDate.length() > 0) {
            enteredDate = enteredDate.substring(0, enteredDate.length() - 1);
        }
    }

    private void handleNewRateInput() {
        char key = parent.key;
        if ((Character.isDigit(key) || key == '.') && enteredNewRate.length() < 10) {
            enteredNewRate += key;
        }
        if (key == PApplet.BACKSPACE && enteredNewRate.length() > 0) {
            enteredNewRate = enteredNewRate.substring(0, enteredNewRate.length() - 1);
        }
    }

    private void performUpdate() {
        // Get selected "To" currency
        if (!toDropdown.expanded) {
            selectedToCurrency = toDropdown.getSelectedItem();
        }

        // Ensure all necessary values are entered
        if (!enteredDate.isEmpty() && !enteredNewRate.isEmpty()) {
            System.out.println("Updating exchange rate for " + selectedToCurrency + " on " + enteredDate + " with new rate: " + enteredNewRate);
            // You can add logic here to update the database or perform other actions
            selectedToCurrency = selectedToCurrencyText.split(" ")[0];
            database.addCountry(Integer.toString(parent2.userID), selectedToCurrency, Double.valueOf(enteredNewRate));
        } else {
            System.out.println("Please enter all required fields (date and new rate).");
        }
    }
}