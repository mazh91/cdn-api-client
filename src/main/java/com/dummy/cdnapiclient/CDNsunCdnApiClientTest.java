package com.dummy.cdnapiclient;

import org.json.JSONObject;

public class CDNsunCdnApiClientTest {
    public static void main(String[] args) {

        CDNsunCdnApiClient client;
        JSONObject response = null;

        String username   = "u8809648490718";
        String password   = "6BfDyH8F2ZNW";
        int id = 91;

        JSONObject options;

        options = new JSONObject();
        options.put("username", username);
        options.put("password", password);

        client = new CDNsunCdnApiClient(options);

        // TEST 1
//        options.put("url", "cdns");

        // TEST 2
        options = new JSONObject();
        options.put("url", "cdns/" + id + "/reports");
        JSONObject dataArray = new JSONObject();
        dataArray.put("type", "GB");
        dataArray.put("period", "4h");
        options.put("data", dataArray);

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
