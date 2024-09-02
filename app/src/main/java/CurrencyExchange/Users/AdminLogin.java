package CurrencyExchange.Users;

import java.io.*;
import processing.data.*;

public class AdminLogin {
    String path; 
    JSONObject fileObj;

    public AdminLogin(JSONObject fileObj, String path) {
        this.path = path;
        this.fileObj = fileObj;
    }

    /*
     * Add a new user to the JSON file
     * @params:
     *      user: String
     *      pass: String
     * @ret:    
     *      true if successful, else false
     */
    public boolean addUser(String user, String pass) {
        if (user != null && pass != null && !user.isEmpty() && !pass.isEmpty()) {
            fileObj.setString(user, pass);
            saveJsonFile();
            return true;
        }
        return false;
    }

    /* 
     * Deletes an existing user from the json 
     * @params: 
     *      user: String 
     * @ret: 
     *      true if user exists and deleted, else false
     */
    public boolean deleteUser(String user) {
        if (fileObj.hasKey(user)) {
            fileObj.remove(user);
            saveJsonFile();
            return true;
        }
        return false;
    }


    /*
     * Update an existing users password
     * @params:
     *      username: String
     *      oldPass: String
     *      newPass: String
     * @ret: 
     *      true if successful, else false if unsuccessful or user does not exist 
     */
    public boolean updatePassword(String username, String oldPass, String newPass) {
        if (checkLogin(username, oldPass)) {
            if (fileObj.hasKey(username) && newPass != null && !newPass.trim().isEmpty()) {
                fileObj.setString(username, newPass);
                saveJsonFile();
                return true;
            }
        }
        return false; //user does not exist 
    }

    /*
     * Update the admin username 
     * @params: 
     *      userOld : String
     *      userNew : String
     * @ret: 
     *      true if updated successfully, else false 
     */
    public boolean updateUser(String userOld, String userNew) {
        if (userNew != null && fileObj.hasKey(userOld) && !userNew.trim().isEmpty()) {
            String password = fileObj.getString(userOld);
            fileObj.remove(userOld);
            fileObj.setString(userNew, password);
            saveJsonFile();
            return true;
        }
        return false;
    }


    /*
     * Checks if entered username and password match 
     * @params: 
     *      user : String
     *      pass : String 
     * @ret: 
     *      true if correct user and pass, else false
     */
    public boolean checkLogin(String username, String password) {
        return fileObj.hasKey(username) && fileObj.getString(username).equals(password);
    }

    /*
     * To save changes made to JSON file 
     */
    private void saveJsonFile() {
        try (FileWriter file = new FileWriter(path)) {
            file.write(fileObj.toString());
            file.flush();
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
