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
     *      id: int 
     *      user: String
     *      pass: String
     * @ret:    
     *      true if successful, else false
     */
    public boolean addUser(int id, String user, String pass) {
        if (user != null && pass != null && !user.isEmpty() && !pass.isEmpty()) {
            if (fileObj.hasKey(String.valueOf(id))) { //check if id exists
                return false; 
            } 

            JSONObject newUser = new JSONObject();
            newUser.put("username", user);
            newUser.put("password", pass);
            fileObj.put(String.valueOf(id), newUser);
            saveJsonFile();
            return true;
        }
        return false;
    }

    /* 
     * Deletes an existing user from the JSON object
     * @params: 
     *      user: String 
     * @ret: 
     *      true if user exists and deleted, else false
     */
    public boolean deleteUser(int id) {
        if (fileObj.hasKey(String.valueOf(id))) {
            fileObj.remove(String.valueOf(id));
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
    public boolean updatePassword(int id, String oldPass, String newPass) {
        if (fileObj.hasKey(String.valueOf(id))) {
            JSONObject userObj = fileObj.getJSONObject(String.valueOf(id));
            if (userObj.getString("password").equals(oldPass)) {
                if (newPass != null && !newPass.trim().isEmpty()) {
                    userObj.put("password", newPass);
                    saveJsonFile();
                    return true;
                }
            }
        }
        return false;
    }

    /*
     * Update the admin username 
     * @params: 
     *      id : int
     *      userNew : String
     * @ret: 
     *      true if updated successfully, else false 
     */
    public boolean updateUsername(int id, String userNew) {
        if (userNew != null && !userNew.trim().isEmpty() && fileObj.hasKey(String.valueOf(id))) {
            JSONObject userObj = fileObj.getJSONObject(String.valueOf(id));
            userObj.put("username", userNew);
            saveJsonFile();
            return true;
        }
        return false;
    }


    /*
     * Checks if entered username and password match 
     * @params: 
     *      id : int 
     *      user : String
     *      pass : String 
     * @ret: 
     *      true if correct user and pass, else false
     */
    public boolean checkLogin(int id, String username, String password) {
        if (fileObj.hasKey(String.valueOf(id))) {
            JSONObject userObj = fileObj.getJSONObject(String.valueOf(id));
            return userObj.getString("username").equals(username) && userObj.getString("password").equals(password);
        }
        return false;
    }

    /*
     * Checks if entered id and password match 
     * @params: 
     *      id : int 
     *      pass : String 
     * @ret: 
     *      true if correct user and pass, else false
     */
    public boolean checkLogin(int id, String password) {
        if (fileObj.hasKey(String.valueOf(id))) {
            JSONObject userObj = fileObj.getJSONObject(String.valueOf(id));
            return userObj.getString("password").equals(password);
        }
        return false;
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