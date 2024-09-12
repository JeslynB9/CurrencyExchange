package CurrencyExchange.FileHandlers;

import processing.core.PApplet;
import processing.data.JSONObject;

public class JsonLoader extends PApplet {
    private JSONObject json;
    private String filePath;

    public JsonLoader(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void setup() {
        json = loadJSONObject(filePath);
        exit(); //terminate sketch when loaded
    }

    public JSONObject getJson() {
        return json;
    }

    public static JSONObject loadJson(String filePath) {
        JsonLoader jsonLoader = new JsonLoader(filePath);
        PApplet.runSketch(new String[]{""}, jsonLoader);
        
        //add delay to ensure everything loads 
        try {
            Thread.sleep(500);
        } 
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        return jsonLoader.getJson();
    }
}
