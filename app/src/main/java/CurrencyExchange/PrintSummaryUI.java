package CurrencyExchange;

import processing.core.PApplet;
import processing.core.PImage;

import CurrencyExchange.Users.Dropdown;
import CurrencyExchange.Users.CurrencyManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.concurrent.ExecutorService;

public class PrintSummaryUI {
    PApplet parent;
    PImage switchImg;
    PImage dropdownFirst;
    PImage dropdownSecond;
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
    boolean startDateBox = false;
    boolean endDateBox = false;
    String selectedFirstCurrencyText = "USD - US Dollar"; // Default currency
    String selectedSecondCurrencyText = "EUR - Euros";   // Default currency
    String selectedFirstCurrency = "USD";
    String selectedSecondCurrency = "EUR";

    String enteredStartDate = ""; // User input for start date
    String enteredEndDate = "";

    private ExecutorService executor;
    private boolean isPdfGenerating = false;
    private boolean isPdfGenerated = false;

    private File generatedPDFFile;
    private boolean pdfGenerated = false;

    // Constructor receives the PApplet instance
    public PrintSummaryUI(PApplet parent, CurrencyManager currencyManager, ExecutorService executor) {
        this.parent = parent;
        this.currencyManager = currencyManager;
        this.executor = executor;
        flagManager = new Flag(parent);

        // Load flags for selected currencies
        flagManager.loadFlag(selectedFirstCurrency);
        flagManager.loadFlag(selectedSecondCurrency);

        // Calculate the rectangle's top-left corner based on the center
        rectX = width / 2 - rectW / 2;
        rectY = height / 2 - rectH / 2;
//        System.out.println("PrintSummaryUI initialized");

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

        String[] countries = { "USD - US Dollar", "EUR - Euro", "AUD - AU Dollar", "GBP - British Pound", "JPY - JP Yen" };
        firstDropdown = new Dropdown(parent, countries, 80, 250, 200, 40);
        secondDropdown = new Dropdown(parent, countries, 325, 250, 200, 40);
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

        if (startDateBox) {
            parent.fill(220, 202, 216);
        } else {
            parent.noFill();
        }
        parent.stroke(0);
        parent.rect(80, 325, 200, 40, cornerRadius);
        parent.fill(0);
        parent.textSize(12);
        parent.text("Start Date", 90, 320);
        parent.textSize(16);
        parent.text(enteredStartDate, 90, 350);

        if (endDateBox) {
            parent.fill(220, 202, 216);
        } else {
            parent.noFill();
        }
        parent.stroke(0);
        parent.rect(325, 325, 200, 40, cornerRadius);
        parent.fill(0);
        parent.textSize(12);
        parent.text("End Date", 330, 320);
        parent.textSize(16);
        parent.text(enteredEndDate, 330, 350);


        // Draw the flag for 1st currency
        flagManager.drawFlag(selectedFirstCurrency, 90, 263);
        // Draw the flag for 2nd currency
        flagManager.drawFlag(selectedSecondCurrency, 335, 263);

        // Draw selected currencies
        parent.textSize(16);
        parent.text(selectedFirstCurrencyText, 120, 277);
        parent.text(selectedSecondCurrencyText, 365, 277);



        // Add "Open PDF" button if a PDF has been generated
//        if (pdfGenerated && generatedPDFFile != null && generatedPDFFile.exists()) {
//            if (drawButton("Open PDF", 775, 300, 100, 40)) {
//                currencyManager.openPDFFile(generatedPDFFile);
//            }
//        }

        if (pdfGenerated && generatedPDFFile != null && generatedPDFFile.exists()) {
            if (drawButton("Open PDF", 775, 300, 100, 40)) {
                try {
                    currencyManager.openPDFFile(generatedPDFFile);
                } catch (IOException e) {
                    System.err.println("Error opening PDF file: " + e.getMessage());
                    // You might want to show an error message to the user here
                    // For example, if you're using Processing, you could do:
                    // fill(255, 0, 0);
                    // text("Error opening PDF: " + e.getMessage(), 100, 400);
                } catch (UnsupportedOperationException e) {
                    System.err.println("Cannot open PDF: " + e.getMessage());
                    // Show an error message to the user
                    // fill(255, 0, 0);
                    // text("Cannot open PDF: " + e.getMessage(), 100, 400);
                }
            }
        }

        firstDropdown.draw();
        secondDropdown.draw();
        parent.textSize(16);
//        drawButton("Open PDF", 775, 300, 100, 40); // testing
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
            startDateBox = false;
            endDateBox = false;
//            secondDropdown.expanded = false;

        }
        else if (isMouseOverButton(325, 250, 200, 40)) {
            firstCurrencyBox = false;
            secondCurrencyBox = true;
            startDateBox = false;
            endDateBox = false;
//            firstDropdown.expanded = false;
        }
        else if (isMouseOverButton(80, 325, 200, 40)) {
            firstCurrencyBox = false;
            secondCurrencyBox = false;
            startDateBox = true;
            endDateBox = false;
        }
        else if (isMouseOverButton(325, 325, 200, 40)) {
            firstCurrencyBox = false;
            secondCurrencyBox = false;
            startDateBox = false;
            endDateBox = true;
        }
        else if (isMouseOverButton(775, 350, 100, 40)) {
            generateSummary();
            if (pdfGenerated && isMouseOverButton(775, 400, 100, 40)) {
                try {
                    currencyManager.openPDFFile(generatedPDFFile);
                } catch (IOException e) {
                    System.err.println("Error opening PDF file: " + e.getMessage());
                    // Display error message to the user
                    // For example:
                    // errorMessage = "Error opening PDF: " + e.getMessage();
                    // errorMessageTimer = frameRate * 5; // Display for 5 seconds
                } catch (UnsupportedOperationException e) {
                    System.err.println("Cannot open PDF: " + e.getMessage());
                    // Display error message to the user
                    // For example:
                    // errorMessage = "Cannot open PDF: " + e.getMessage();
                    // errorMessageTimer = frameRate * 5; // Display for 5 seconds
                }
            }
        }

        // Handle dropdown interactions
        firstDropdown.mousePressed();
        secondDropdown.mousePressed();

        if (!firstDropdown.expanded && firstDropdown.getSelectedItem() != null &&
                !selectedFirstCurrencyText.equals(firstDropdown.getSelectedItem())) {
            selectedFirstCurrencyText = firstDropdown.getSelectedItem();
            selectedFirstCurrency = selectedFirstCurrencyText.split(" ")[0];
//            System.out.println("First currency updated to: " + selectedFirstCurrencyText);  // Debugging print
        }

        // Check for "To" dropdown and update
        if (!secondDropdown.expanded && secondDropdown.getSelectedItem() != null &&
                !selectedSecondCurrency.equals(secondDropdown.getSelectedItem())) {
            selectedSecondCurrencyText = secondDropdown.getSelectedItem();
            selectedSecondCurrency = selectedSecondCurrencyText.split(" ")[0];
//            System.out.println("Second currency updated to: " + selectedSecondCurrencyText);  // Debugging print
        }
    }

    public void keyPressed () {
        if (startDateBox) {
            handleDateInput(true);
        } else if (endDateBox) {
            handleDateInput(false);
        }
    }

    private void handleDateInput(boolean isStartDate) {
        char key = parent.key;
        if (Character.isDigit(key) || key == '-') {
            if (isStartDate) {
                enteredStartDate += key;
            } else {
                enteredEndDate += key;
            }
        }
        if (key == PApplet.BACKSPACE) {
            if (isStartDate && enteredStartDate.length() > 0) {
                enteredStartDate = enteredStartDate.substring(0, enteredStartDate.length() - 1);
            } else if (!isStartDate && enteredEndDate.length() > 0) {
                enteredEndDate = enteredEndDate.substring(0, enteredEndDate.length() - 1);
            }
        }
    }

    private void generateSummary() {
        System.out.println("Attempting to generate summary...");

        if (selectedFirstCurrency == null || selectedSecondCurrency == null) {
            System.out.println("Error: Currencies not selected");
            return;
        }

        if (enteredStartDate.isEmpty() || enteredEndDate.isEmpty()) {
            System.out.println("Error: Date range not entered");
            return;
        }

        try {
            String testFilePath = "app/src/main/java/CurrencyExchange/Users/PDFSummary/test.txt";
            File testFile = new File(testFilePath);
            FileWriter writer = new FileWriter(testFile);
            writer.write("Test file writing");
            writer.close();
            System.out.println("Test file created at: " + testFile.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Error creating test file: " + e.getMessage());
            e.printStackTrace();
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDate = LocalDate.parse(enteredStartDate, formatter);
            LocalDate endDate = LocalDate.parse(enteredEndDate, formatter);

            if (endDate.isBefore(startDate)) {
                System.out.println("Error: End date is before start date");
                return;
            }

            isPdfGenerating = true;
            isPdfGenerated = false;

            executor.submit(() -> {
                try {
                    currencyManager.generateExchangeRateSummaryPDF(selectedFirstCurrency, selectedSecondCurrency, startDate, endDate);
                    isPdfGenerated = true;
                } catch (Exception e) {
                    System.out.println("Error generating PDF summary: " + e.getMessage());
                    e.printStackTrace();
                } finally {
                    isPdfGenerating = false;
                }
            });

        } catch (DateTimeParseException e) {
            System.out.println("Error parsing dates: " + e.getMessage());
        }

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDate = LocalDate.parse(enteredStartDate, formatter);
            LocalDate endDate = LocalDate.parse(enteredEndDate, formatter);

            String pdfPath = currencyManager.generateExchangeRateSummaryPDF(selectedFirstCurrency, selectedSecondCurrency, startDate, endDate);
            if (pdfPath != null) {
                generatedPDFFile = new File(pdfPath);
                pdfGenerated = true;
                System.out.println("PDF summary generated for: " + selectedFirstCurrency + " to " + selectedSecondCurrency);
            } else {
                System.out.println("Failed to generate PDF summary.");
                pdfGenerated = false;
            }
        } catch (Exception e) {
            System.out.println("Error generating PDF summary: " + e.getMessage());
            e.printStackTrace();
            pdfGenerated = false;
        }
    }

    private boolean drawButton(String label, int x, int y, int w, int h) {
        parent.noStroke();
        parent.fill(222, 37, 176);
        parent.rect(x, y, w, h, 10);
        parent.fill(255);
        parent.text(label, 790, 325);
        return parent.mousePressed && parent.mouseX > x && parent.mouseX < x + w && parent.mouseY > y && parent.mouseY < y + h;
    }

}


