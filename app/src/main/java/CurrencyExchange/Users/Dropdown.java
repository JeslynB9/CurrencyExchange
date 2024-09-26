package CurrencyExchange.Users;

import processing.core.PApplet;

public class Dropdown {
    PApplet parent;
    String[] countryList;
    float x, y, w, h;
    public boolean expanded = false;
    int selectedIndex = 0;
    boolean clickedOutside = true;

    int visibleItems = 4;
    int scrollOffset = 0;

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

        // If expanded, draw the list of items
        if (expanded && countryList != null) {
            int maxOffset = countryList.length - visibleItems; // Max scroll position
            if (scrollOffset > maxOffset) scrollOffset = maxOffset;
            if (scrollOffset < 0) scrollOffset = 0;

            for (int i = 0; i < visibleItems && i + scrollOffset < countryList.length; i++) {
                float itemY = y + (i + 1) * h;
                parent.fill(255, 249, 254);
                parent.stroke(0);
                parent.rect(x, itemY, w, h, 10);
                parent.fill(0);
                parent.text(countryList[i + scrollOffset], x + 10, itemY + h / 2 + 5);  // Display the item
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
            for (int i = 0; i < visibleItems && i + scrollOffset < countryList.length; i++) {
                float itemY = y + (i + 1) * h;
                if (isMouseOver(x, itemY, w, h)) {
                    selectedIndex = i + scrollOffset;
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

    boolean isMouseOver(float x, float y, float w, float h) {
        return parent.mouseX > x && parent.mouseX < x + w &&
                parent.mouseY > y && parent.mouseY < y + h;
    }

    // Handle mouse scrolling
    public void mouseWheel(int wheelCount) {
        if (expanded) {
            scrollOffset -= wheelCount;  // Scroll up or down based on wheel movement
        }
    }

    // Get the currently selected item
    public String getSelectedItem() {
        return countryList[selectedIndex];
    }

    // Set the specific item from the dropdown
    public void setSelectedItem(String item) {
        for (int i = 0; i < countryList.length; ++i) {
            if (countryList[i].equals(item)) {
                selectedIndex = i;
                break;
            }
        }

        expanded = false;
    }
}

