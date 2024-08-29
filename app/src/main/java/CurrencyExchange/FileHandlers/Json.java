package CurrencyExchange.FileHandlers;

import java.io.*;
import java.util.*;
import java.nio.file.*;
import processing.data.*;
import processing.core.*;


//class imports 
import CurrencyExchange.*;

public class Json {
    private JSONObject fileObj;
    private String filePath;

    public Json(JSONObject fileObj, String filePath) {
        this.fileObj = fileObj;
        this.filePath = filePath;
    }

    /*
     * To add a country to the json file 
     * @params: 
     *      country: String
     *      flagFilePath : String 
     *      symbol : String       
     */
    public void addCountry(String country, String flagFilePath, String symbol) {
        //check if country is valid string 
        if (country != null && country.matches("[a-zA-Z]+")) {
            JSONObject flagObject = fileObj.getJSONObject("flag");
            flagObject.setString(country, flagFilePath);
    
            JSONObject symbolObject = fileObj.getJSONObject("symbol");
            symbolObject.setString(country, symbol);
            saveJsonFile();
        }
        return;
    }

    /*
     * To update an already existing country flag 
     * @params: 
     *      country: String
     *      flagFilePath : String 
     */
    public void updateFlag(String country, String flagFilePath) {
        JSONObject flagObject = fileObj.getJSONObject("flag");
        if (flagObject.hasKey(country)) {
            flagObject.setString(country, flagFilePath);
            saveJsonFile();
        } 
        else {
            addCountry(country, flagFilePath, "NULL"); 
        }
    }

    /*
     * To update an already existing country symbol flag
     * @params: 
     *      country: String
     *      symbol : String   
     */
    public void updateSymbol(String country, String symbol) {
        JSONObject symbolObject = fileObj.getJSONObject("symbol");
        if (symbolObject.hasKey(country)) {
            symbolObject.setString(country, symbol);
            saveJsonFile();
        } 
        else {
            addCountry(country, "NULL", symbol);
        }
    }

    /*
     * To save changes made to JSON file 
     */
    private void saveJsonFile() {
        try (FileWriter file = new FileWriter(filePath)) {
            file.write(fileObj.toString());
            file.flush();
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /*
     * Get the file path of a countrys flag 
     * @params: 
     *      country: String 
     */
    public String getFlag(String country) {
        JSONObject flagObject = fileObj.getJSONObject("flag");

        if (flagObject.hasKey(country)) {
            return flagObject.getString(country);
        } 
        else {
            return null;
        }
    }

    /*
     * Get the symbol for a countrys currency  
     * @params: 
     *      country: String
     */
    public String getSymbol(String country) {
        JSONObject symbolObject = fileObj.getJSONObject("symbol");

        if (symbolObject.hasKey(country)) {
            return symbolObject.getString(country);
        } 
        else {
            return null;
        }
    }
}
