package CurrencyExchange.Users;

import processing.core.PApplet;

public class Dropdown {
    PApplet parent;
    String[] countryList;
    float x, y, w, h;
    public boolean expanded = false;
    int selectedIndex = 0;
    boolean clickedOutside = true;

    public Dropdown(PApplet parent, String[] countryList, float x, float y, float w, float h) {
        this.parent = parent;
        this.countryList = countryList;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    // Draw the dropdown
    public void draw() {
        parent.fill(255, 249, 254);  // Same fill color as the boxes
//        parent.stroke(0);

        // If expanded, draw the list of items
        if (expanded) {
            for (int i = 0; i < countryList.length; i++) {
                float itemY = y + (i + 1) * h;
                parent.fill(255, 249, 254);
                parent.stroke(0);
                parent.rect(x, itemY, w, h, 10);  // Draw each item with rounded corners
                parent.fill(0);
                parent.text(countryList[i], x + 10, itemY + h / 2 + 5);  // Center-align the text vertically
                parent.noStroke();
            }
        }
    }

    // Handle mouse clicks for selection
    public void mousePressed() {
        if (isMouseOver(x, y, w, h)) {
            expanded = !expanded;
        } else if (expanded) {
            clickedOutside = true;
            // Check if an item is clicked when expanded
            for (int i = 0; i < countryList.length; i++) {
                float itemY = y + (i + 1) * h;
                if (isMouseOver(x, itemY, w, h)) {
                    selectedIndex = i;  // Select the clicked item
                    expanded = false;
                    clickedOutside = false;
                    break;
                }
            }
            if (clickedOutside) {
                expanded = false;
            }
        } else {
            expanded = false;  // Close the dropdown if clicked outside
        }
    }

    // Utility method to check if the mouse is over a given area
    boolean isMouseOver(float x, float y, float w, float h) {
        return parent.mouseX > x && parent.mouseX < x + w &&
                parent.mouseY > y && parent.mouseY < y + h;
    }

    // Get the currently selected item
    public String getSelectedItem() {
        return countryList[selectedIndex];
    }
}

