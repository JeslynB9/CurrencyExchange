package CurrencyExchange;

import processing.core.PApplet;
import processing.core.PImage;
import CurrencyExchange.Users.CurrencyManager;
import CurrencyExchange.Users.Dropdown;

import java.util.HashMap;
import java.util.Map;

public class CurrencyConverterUI {
    PApplet parent;
    PImage switchImg;
    PImage dropdownFrom;
    PImage dropdownTo;
    Flag flagManager;
    static int width = 1920 / 2;
    static int height = 1080 / 2;
    float rectW = width-100;
    float rectH = (float) height /2;
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

    String selectedFromCurrencyText = "USD - US Dollar"; // Default currency
    String selectedToCurrencyText = "AUD - AU Dollar";   // Default currency
    String selectedFromCurrency = "USD";
    String selectedToCurrency = "AUD";

    String enteredAmount = "";  // Amount input
    String conversionResult = "";
    String conversionRateText = "";

    CurrencyManager currencyManager;
    Dropdown fromDropdown;
    Dropdown toDropdown;
    boolean isSwitched = false;

    private Map<String, String> currencySymbols;

    // Constructor receives the PApplet instance
    public CurrencyConverterUI(PApplet parent, CurrencyManager currencyManager) {
        this.parent = parent;
        this.currencyManager = currencyManager;

        // Initialize Flag manager
        flagManager = new Flag(parent);
        // Load flags for selected currencies
        flagManager.loadFlag(selectedFromCurrency);
        flagManager.loadFlag(selectedToCurrency);

        updateConversionRate();

        // Calculate the rectangle's top-left corner based on the center
        rectX = (float) width / 2 - rectW / 2;
        rectY = (float) height / 2 - rectH / 2;

        // Load the image
        switchImg = parent.loadImage("src/main/resources/switch.png");
        switchImg.resize(1920 / 40, 1080 / 40);

        dropdownFrom = parent.loadImage("src/main/resources/dropdown.png");
        dropdownFrom.resize(1920 / 80, 1080 / 80);

        dropdownTo = parent.loadImage("src/main/resources/dropdown.png");
        dropdownTo.resize(1920 / 80, 1080 / 80);

        // List of country currencies for dropdowns
        String[] countries = { "USD - US Dollar", "EUR - Euro", "AUD - AU Dollar", "GBP - British Pound", "JPY - JP Yen" };
        fromDropdown = new Dropdown(parent, countries, 350, 250, 200, 40);
        toDropdown = new Dropdown(parent, countries, 675, 250, 200, 40);

        // Initialize the currency symbol map
        currencySymbols = new HashMap<>();
        currencySymbols.put("USD", "$");
        currencySymbols.put("EUR", "€");
        currencySymbols.put("AUD", "A$");
        currencySymbols.put("GBP", "£");
        currencySymbols.put("JPY", "¥");
    }

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
            // System.out.println("Mouse is hovering over 'Convert' button");
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


        if (currencySymbols != null) {
            // Get the currency symbol for the selected "From" currency
            String currencySymbol = currencySymbols.getOrDefault(selectedFromCurrency, "");
            // Display the entered amount
            parent.fill(0);
            parent.textSize(16);
            parent.text(currencySymbol + " " + enteredAmount, 90, 275);  // Currency symbol and amount
        } else {
            // Handle the case where currencySymbols is null
            System.out.println("currencySymbols map is null");
        }

        // Display Conversion result
        parent.text(conversionResult, 500, 380);

        // Display conversion rate
        parent.fill(0);
        parent.textSize(12);
        parent.text(conversionRateText, 90, 380);

        fromDropdown.draw();
        toDropdown.draw();
        parent.textSize(16);
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
            fromBoxSelected = false;
            toBoxSelected = false;
        }
        // Check if the "FROM" box is clicked
        else if (isMouseOverButton(350, 250, 200, 40)) {
            amountBoxSelected = false;
            fromBoxSelected = true;
            toBoxSelected = false;
        }

        // Check if the "TO" box is clicked
        else if (isMouseOverButton(675, 250, 200, 40)) {
            amountBoxSelected = false;
            fromBoxSelected = false;
            toBoxSelected = true;
        }
        // Check if the "Convert" Button is clicked
        else if (isMouseOverButton(775, 350, 100, 40)) {
            performConversion();
            System.out.println("convert button");
            currencyManager.convertCurrency(centerX, selectedFromCurrency, selectedToCurrency);
        }

        else if (isMouseOverButton(590, 257, 40, 40)) {  // Coordinates for the switch button
            switchCurrencies();
            System.out.println("Switch button clicked");
        }

        if (!isSwitched) {
            fromDropdown.mousePressed();
            toDropdown.mousePressed();
        }

        // Update dropdowns after the switch is done
        if (!isSwitched) {
            // Check for "From" dropdown and update
            if (!fromDropdown.expanded && fromDropdown.getSelectedItem() != null &&
                    !selectedFromCurrencyText.equals(fromDropdown.getSelectedItem())) {
                selectedFromCurrencyText = fromDropdown.getSelectedItem();
                selectedFromCurrency = selectedFromCurrencyText.split(" ")[0];
                updateConversionRate();
                System.out.println("From currency updated to: " + selectedFromCurrencyText);  // Debugging print
            }

            // Check for "To" dropdown and update
            if (!toDropdown.expanded && toDropdown.getSelectedItem() != null &&
                    !selectedToCurrencyText.equals(toDropdown.getSelectedItem())) {
                selectedToCurrencyText = toDropdown.getSelectedItem();
                selectedToCurrency = selectedToCurrencyText.split(" ")[0];
                updateConversionRate();
                System.out.println("To currency updated to: " + selectedToCurrencyText);  // Debugging print
            }
        }

        // Reset isSwitched
        isSwitched = false;
    }

    public void keyPressed() {
        if (amountBoxSelected) {
            if ((parent.key >= '0' && parent.key <= '9') || parent.key == '.') {
                enteredAmount += parent.key;
            }

            // Handle backspace
            if (parent.key == PApplet.BACKSPACE && enteredAmount.length() > 0) {
                enteredAmount = enteredAmount.substring(0, enteredAmount.length() - 1);
            }
        }
    }

    public void switchCurrencies() {
        // Swap the "From" and "To" currencies
        String tempCurrency = selectedFromCurrency;
        selectedFromCurrency = selectedToCurrency;
        selectedToCurrency = tempCurrency;

        // Swap the "From" and "To" currency text labels
        String tempCurrencyText = selectedFromCurrencyText;
        selectedFromCurrencyText = selectedToCurrencyText;
        selectedToCurrencyText = tempCurrencyText;

        // Update the flags after the swap
        flagManager.loadFlag(selectedFromCurrency);
        flagManager.loadFlag(selectedToCurrency);

        isSwitched = true;

        System.out.println("Currencies switched: " + selectedFromCurrency + " to " + selectedToCurrency);

        // Update the conversion rate after the swap
        updateConversionRate();
    }

    public void updateConversionRate() {
        try {
            // Fetch the conversion rates from the database via CurrencyManager
            double fromRate = currencyManager.getLastExchangeRate(selectedFromCurrency);
            double toRate = currencyManager.getLastExchangeRate(selectedToCurrency);

            System.out.println("From Rate (" + selectedFromCurrency + "): " + fromRate);
            System.out.println("To Rate (" + selectedToCurrency + "): " + toRate);

            // Calculate the conversion rate
            double conversionRate;

            if (fromRate != 1) {
                conversionRate = toRate/fromRate;
            } else {
                conversionRate = toRate;
            }

            // Update the conversion rate text for display
            conversionRateText = String.format("$ 1 %s = %.3f %s", selectedFromCurrency, toRate / fromRate, selectedToCurrency);

        } catch (Exception e) {
            // If there's an issue (e.g., missing exchange rate), clear the conversion rate text
            conversionRateText = "Conversion rate unavailable";
        }
    }

    // Perform the conversion using backend logic
    public void performConversion() {
        try {
            // Ensure that an amount has been entered
            if (enteredAmount.isEmpty()) {
                conversionResult = "Please enter an amount.";
                return;
            }

            // Capture and validate the entered amount
            double amount = Double.parseDouble(enteredAmount);

            // Ensure valid currencies
            if (selectedFromCurrency == null || selectedToCurrency == null) {
                conversionResult = "Please select valid currencies.";
                return;
            }

            // Get the exchange rates and perform the conversion
            double fromRate = currencyManager.getLastExchangeRate(selectedFromCurrency);
            double toRate = currencyManager.getLastExchangeRate(selectedToCurrency);

            if (fromRate == 0 || toRate == 0) {
                conversionResult = "Conversion rate unavailable!";
                return;
            }

            // Perform the conversion
            double convertedAmount = currencyManager.convertCurrency(amount, selectedFromCurrency, selectedToCurrency);

            // Update the conversion result to display
            conversionResult = String.format("%.2f %s = %.2f %s", amount, selectedFromCurrency, convertedAmount, selectedToCurrency);

        } catch (NumberFormatException e) {
            // Handle invalid input for the amount
            conversionResult = "Invalid amount entered!";
        } catch (Exception e) {
            // Handle any other unexpected errors
            conversionResult = "Error occurred during conversion!";
        }
    }
}
