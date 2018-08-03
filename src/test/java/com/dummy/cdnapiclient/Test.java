package com.dummy.cdnapiclient;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String[] args) {

        CDNsunCdnApiClient client;
        JSONObject response = null;

        String username   = "u8809648490718";
        String password   = "6BfDyH8F2ZNW";
        int id = 91;

        JSONObject options = new JSONObject();
        options.put("username", username);
        options.put("password", password);

        client = new CDNsunCdnApiClient(options);

        options.put("url", "cdns");

        /*
        //options = new HashMap<>();
        JSONObject opt = new JSONObject();
        opt.put("url", "cdns" + id + "/reports");
        Map<String, String> dataArrayMap = new HashMap<>();
        dataArrayMap.put("type", "GB");
        dataArrayMap.put("period", "4h");
        opt.put("data", dataArrayMap);
        */


        try {
            response = client.get(options);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }

        if(response != null)
            System.out.println(response.toString());

    }
}
