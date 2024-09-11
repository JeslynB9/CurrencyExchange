package CurrencyExchange;

import processing.core.PApplet;

public class Register {
    PApplet parent;
    boolean isRegisterScreenVisible = false;
    float shadowOffset = 8;
    Login Login;
    public Register(PApplet parent) {

        this.parent = parent;
    }

    public void drawRegister() {

        if (!isRegisterScreenVisible) return;

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

        // Title Text "Admin Register"
        parent.fill(0);
        parent.textSize(24);
        parent.text("Admin Register", 395, 150);

        // Username Field
        parent.stroke(84, 84, 84);
        parent.noFill();
        parent.rect(parent.width / 2 - 120, parent.height / 2 - 90, 240, 40, 5);
        parent.fill(84, 84, 84);
        parent.textSize(16);
        parent.text("Employee ID", 370, 205);

        // Username Field
        parent.stroke(84, 84, 84);
        parent.noFill();
        parent.rect(parent.width / 2 - 120, parent.height / 2 - 10, 240, 40, 5);
        parent.fill(84, 84, 84);
        parent.textSize(16);
        parent.text("Username", 370, 285);

        // Password Field
        parent.stroke(84, 84, 84);
        parent.noFill();
        parent.rect(parent.width / 2 - 120, parent.height / 2 + 70, 240, 40, 5);
        parent.fill(84, 84, 84);
        parent.textSize(16);
        parent.text("Password", 370, 365);

        // Register Button
        // Draw the button after setting the fill color
        boolean isHover = isMouseOverButton(560, 360, 100, 40);
        if (isHover) {
            parent.fill(222, 37, 176, 200);
        } else {
            parent.fill(222, 37, 176);
        }
        parent.noStroke();
        parent.rect(560, 410, 100, 40, 10);
        parent.fill(255);
        parent.textSize(16);
        parent.text("Register", 577, 435);

        boolean isHovering = isMouseOverButton(300, 435, (int)parent.textWidth("Have An Account?"), 10);
        if (isHovering) {
            parent.fill(222, 37, 176);
        } else {
            parent.fill(0);
        }

        parent.text("Have An Account?", 300, 435);

    }

    private boolean isMouseOverButton(int x, int y, int w, int h) {
        return (parent.mouseX > x && parent.mouseX < x + w &&
                parent.mouseY > y && parent.mouseY < y + h);
    }

    public void mousePressed() {
//        if (isMouseOverButton(300, 435, (int)parent.textWidth("Have An Account?"), 10)) {
//            isRegisterScreenVisible = false;
//            Login.isLoginScreenVisible = true;
//        }
    }
}
