package CurrencyExchange;

import processing.core.PApplet;
import processing.core.PImage;

public class Login {
    PApplet parent;
    boolean isLoginScreenVisible = false;
    boolean usernameSelected = false;
    boolean passwordSelected = false;
    Register Register;
    PImage exitButton;
    float shadowOffset = 8;
    public Login(PApplet parent) {

        this.parent = parent;
        Register = new Register(parent, this);
        System.out.println("Register initialized");
        exitButton = parent.loadImage("src/main/resources/exit.png");
        exitButton.resize(1920 / 40, 1080 / 40);
    }

    public void drawLogin() {

        if (!isLoginScreenVisible) return;

        // Background Overlay
        parent.fill(0, 0, 0, 150);
        parent.rect(0, 0, parent.width*2, parent.height);

        // Shadow properties
        parent.fill(0, 0, 0, 50);
        parent.noStroke();
        parent.rect(parent.width / 2 - 200 - shadowOffset, parent.height / 2 - 150 - shadowOffset, 400 + 2 * shadowOffset, 300 + 2 * shadowOffset, 15);


        // White Login Box
        parent.fill(255,249,254);
        parent.stroke(200);
        parent.rect(parent.width / 2 - 200, parent.height / 2 - 150, 400, 300, 10);

        // Title Text "Admin Login"
        parent.fill(0);
        parent.textSize(24);
        parent.text("Admin Login", 410, 175);

        if (usernameSelected) {
            parent.fill(220, 202, 216);
        } else {
            parent.noFill();
        }
        // Username Field
        parent.stroke(84, 84, 84);
        parent.rect(parent.width / 2 - 120, parent.height / 2 - 60, 240, 40, 5);
        parent.fill(84, 84, 84);
        parent.textSize(16);
        parent.text("Username", 370, 235);

        // Password Field
        if (passwordSelected) {
            parent.fill(220, 202, 216);
        } else {
            parent.noFill();
        }
        parent.stroke(84, 84, 84);
        parent.rect(parent.width / 2 - 120, parent.height / 2 + 20, 240, 40, 5);
        parent.fill(84, 84, 84);
        parent.textSize(16);
        parent.text("Password", 370, 315);

        // Login Button
        // Draw the button after setting the fill color
        boolean isHover = isMouseOverButton(560, 360, 100, 40);
        if (isHover) {
            parent.fill(222, 37, 176, 200);
        } else {
            parent.fill(222, 37, 176);
        }
        parent.noStroke();
        parent.rect(560, 360, 100, 40, 10);
        parent.fill(255);
        parent.textSize(16);
        parent.text("Login", 590, 385);

        boolean isHovering = isMouseOverButton(300, 375, (int)parent.textWidth("New User?"), 10);
        if (isHovering) {
            parent.fill(222, 37, 176);
        } else {
            parent.fill(0);
        }

        parent.text("New User?", 300, 385);

        boolean isHoveringExit = isMouseOverButton(295, 135, 35, 35);

        if (isHoveringExit) {
            System.out.println("Hovering exit button");
            parent.fill(220, 202, 216, 191);
        } else {
            parent.noFill();
        }
        parent.stroke(84, 84, 84);
        parent.ellipse(314, 153, 35, 35);
        parent.image(exitButton, 290, 140);


    }

    private boolean isMouseOverButton(int x, int y, int w, int h) {
        return (parent.mouseX > x && parent.mouseX < x + w &&
                parent.mouseY > y && parent.mouseY < y + h);
    }

    public void mousePressed() {
        System.out.println("Mouse Pressed at: " + parent.mouseX + ", " + parent.mouseY);
        if (isMouseOverButton(300, 375, (int)parent.textWidth("New User?"), 10)) {
            System.out.println("Switching to Register screen");
            Register.isRegisterScreenVisible = true;
            isLoginScreenVisible = false;
//            if (!isLoginScreenVisible) {
//                System.out.println("Login screen removed");
//            }
            Register.mousePressed();
        }

        if (isMouseOverButton(295, 135, 35, 35)) {
            System.out.println("Login Screen exited");
            isLoginScreenVisible = false;
        }

        if (isMouseOverButton(parent.width / 2 - 120, parent.height / 2 - 60, 240, 40)) {
            usernameSelected = true;
            passwordSelected = false;
        }

        if (isMouseOverButton(parent.width / 2 - 120, parent.height / 2 + 20, 240, 40)) {
            usernameSelected = false;
            passwordSelected = true;
        }
    }
}
