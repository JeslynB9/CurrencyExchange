package CurrencyExchange;

import processing.core.PApplet;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddCurrency {
    PApplet parent;
    boolean isAddCurrency = false;
    boolean nameSelected = false;
    boolean flagSelected = false;
    boolean symbolSelected = false;
    boolean rateSelected = false;
    float shadowOffset = 8;
    Login login;

    String enteredCurrencyName = "";
    String enteredFlag = "";
    String enteredSymbol = "";
    String enteredRate = "";

    public AddCurrency(PApplet parent, Login login) {
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

        parent.fill(0);
        parent.textSize(16);
        if (enteredCurrencyName.isEmpty()) {
            parent.fill(84, 84, 84);
            parent.text("Currency Name", 370, 197);
        }
        parent.text(enteredCurrencyName, 370, 197);


        // Flag Field
        if (flagSelected) {
            parent.fill(220, 202, 216);
        } else {
            parent.noFill();
        }
        parent.stroke(84, 84, 84);
        parent.rect(parent.width / 2 - 120, parent.height / 2 - 50, 240, 40, 5);

        parent.fill(0);
        parent.textSize(16);
        if (enteredFlag.isEmpty()) {
            parent.fill(84, 84, 84);
            parent.text("Flag", 370, 247);
        }
        parent.text(enteredFlag, 370, 197);


        // Symbol Field
        if (symbolSelected) {
            parent.fill(220, 202, 216);
        } else {
            parent.noFill();
        }
        parent.stroke(84, 84, 84);
        parent.rect(parent.width / 2 - 120, parent.height / 2, 240, 40, 5);

        parent.fill(0);
        parent.textSize(16);
        if (enteredSymbol.isEmpty()) {
            parent.fill(84, 84, 84);
            parent.text("Symbol", 370, 297);
        }
        parent.text(enteredSymbol, 370, 297);


        // Exchange Rate Field
        if (rateSelected) {
            parent.fill(220, 202, 216);
        } else {
            parent.noFill();
        }
        parent.stroke(84, 84, 84);
        parent.rect(parent.width / 2 - 120, parent.height / 2 + 50, 240, 40, 5);

        parent.fill(0);
        parent.textSize(16);
        if (enteredRate.isEmpty()) {
            parent.fill(84, 84, 84);
            parent.text("Exchange Rate", 370, 347);
        }
        parent.fill(0);
        parent.textSize(16);
        parent.text(enteredRate, 370, 347);

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

        if (isMouseOverButton(560, 410, 100, 40)) {
            isAddCurrency = true;
            if (enteredCurrencyName.isEmpty() || enteredFlag.isEmpty() || enteredSymbol.isEmpty() || enteredRate.isEmpty()) {
                System.out.println("All fields are required!");
                return; // Prevent adding if any field is empty
            }

//            // Store the new currency (you will need to define how to store it, e.g., in a database or array)
//            System.out.println("Adding new currency: " + enteredCurrencyName);
//            System.out.println("Flag: " + enteredFlag);
//            System.out.println("Symbol: " + enteredSymbol);
//            System.out.println("Rate: " + enteredRate);
//
//            // Reset the fields after adding the currency
//            enteredCurrencyName = "";
//            enteredFlag = "";
//            enteredSymbol = "";
//            enteredRate = "";
//
//            isAddCurrency = false; // Close the "Add Currency" screen
        }

        // When "ADD" button is pressed, change the new currency to the one that was just added, and remove the screen:
//      //isAddCurrency = false;

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

    public void keyPressed() {
        System.out.println("Key Pressed: " + parent.key);
        char key = parent.key;

        // Handling input for Currency Name
        if (nameSelected) {
            if (Character.isLetterOrDigit(key) || key == ' ') {
                enteredCurrencyName += key;
            } else if (key == PApplet.BACKSPACE && enteredCurrencyName.length() > 0) {
                enteredCurrencyName = enteredCurrencyName.substring(0, enteredCurrencyName.length() - 1);
            }
            System.out.println("Entered Currency Name: " + enteredCurrencyName);  // Debugging
        }

        // Handling input for Flag
        else if (flagSelected) {
            if (Character.isLetterOrDigit(key) || key == ' ') {
                enteredFlag += key;
            } else if (key == PApplet.BACKSPACE && enteredFlag.length() > 0) {
                enteredFlag = enteredFlag.substring(0, enteredFlag.length() - 1);
            }
            System.out.println("Entered Flag: " + enteredFlag);  // Debugging
        }

        // Handling input for Symbol
        else if (symbolSelected) {
            if (Character.isLetterOrDigit(key)) {
                enteredSymbol += key;
            } else if (key == PApplet.BACKSPACE && enteredSymbol.length() > 0) {
                enteredSymbol = enteredSymbol.substring(0, enteredSymbol.length() - 1);
            }
            System.out.println("Entered Symbol: " + enteredSymbol);  // Debugging
        }

        // Handling input for Exchange Rate
        else if (rateSelected) {
            if (Character.isDigit(key) || key == '.') {
                enteredRate += key;
            } else if (key == PApplet.BACKSPACE && enteredRate.length() > 0) {
                enteredRate = enteredRate.substring(0, enteredRate.length() - 1);
            }
        }
        System.out.println("Entered Rate: " + enteredRate);  // Debugging
    }

//    public void addCurrencyToDatabase(String currencyName, String flag, String symbol, double exchangeRate) {
//        // SQL or database insert logic goes here
//        String query = "INSERT INTO currencies (name, flag, symbol, rate) VALUES (?, ?, ?, ?)";
//        try (PreparedStatement stmt = connection.prepareStatement(query)) {
//            stmt.setString(1, currencyName);
//            stmt.setString(2, flag);
//            stmt.setString(3, symbol);
//            stmt.setDouble(4, exchangeRate);
//            stmt.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

}
