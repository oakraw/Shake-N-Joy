package com.oakraw.shakenjoy;

import java.util.HashMap;
import java.util.Map;


/**
 * store data that use for request an api on each category
 */
public class Data {
    public static final Map<String, String> myMap;
    static
    {
        myMap = new HashMap<String, String>();
        //myMap.put("Food", "4bf58dd8d48988d1c4941735");
        myMap.put("Food", "food");
        //myMap.put("NightLife", "4d4b7105d754a06376d81259");
        myMap.put("NightLife", "drinks");
        //myMap.put("Coffee", "52e81612bcbc57f1066b7a0c,4bf58dd8d48988d16d941735,4bf58dd8d48988d1e0931735,4bf58dd8d48988d1dc931735");
        myMap.put("Coffee", "coffee");
        myMap.put("Dessert", "4bf58dd8d48988d1bc941735,4bf58dd8d48988d1d0941735,4bf58dd8d48988d148941735,4bf58dd8d48988d1c9941735,4bf58dd8d48988d112941735,52e81612bcbc57f1066b7a0a,512e7cae91d4cbb4e5efe0af");
        //myMap.put("Shopping", "4d4b7105d754a06378d81259");
        myMap.put("Shopping", "shops");
        //myMap.put("Outdoors", "4d4b7105d754a06377d81259");
        myMap.put("Sights", "sights");
    }
}
