package com.dummy.cdnapiclient;

import javax.xml.ws.http.HTTPException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.json.*;

class CDNsunCdnApiClient
{
    private String username;
    private String password;
    private static final int REQ_TIMEOUT_MS = 60_000;
   
	public CDNsunCdnApiClient(JSONObject options){
		String username = options.getString("username");
		String password = options.getString("password");

		if(options == null)
			throw new IllegalArgumentException("options is null");
		if(username == null)
			throw new IllegalArgumentException("username is null");
        if(password == null)
			throw new IllegalArgumentException("password is null");
		
		this.username = username;
		this.password = password;
	}

	public JSONObject get(JSONObject options) throws Exception{
		if(options == null)
			throw new IllegalArgumentException("options is null");
		options.put("method", "GET");			
		return request(options);
	}

	/*
	Process client request
	 */
	private JSONObject request(JSONObject options) throws  Exception{
		String urlString = options.getString("url");
		String method = options.getString("method");

		if(options == null)
			throw new IllegalArgumentException("options is null");
		if(urlString == null)
			throw new IllegalArgumentException("url is null");
        if(method == null)
			throw new IllegalArgumentException("method is null");

		method = method.toUpperCase();
		switch (method){
			// TODO: case "POST", ...

			case "GET":
				JSONObject data = null;
				if(options.has("data")) {
                    data = options.getJSONObject("data");
                    List<NameValuePair> nvpData = mapToNvpList(data.toMap());
                    urlString = urlString + "?" + URLEncodedUtils.format(nvpData, '&', StandardCharsets.UTF_8);
                }
				break;
			default:
				throw new IllegalArgumentException("Unsupported method: " + method);
		}

        // API endpoint
        String base_url = "https://cdnsun.com/api/";
		if(urlString.length() < base_url.length())
            options.put("url", base_url + urlString);
		else if (urlString.substring(0, base_url.length()).equals(base_url) == false)
            options.put("url", base_url + urlString);

        urlString = options.getString("url");
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // set header values
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Content-Type", "application/json");

        // authentication
        String authEncoded = Base64.getEncoder().encodeToString(new String(username+":"+password).getBytes()); //StandardCharsets.UTF_8
        connection.setRequestProperty("Authorization", "Basic " + authEncoded);

        // other options
		connection.setConnectTimeout(REQ_TIMEOUT_MS);

		// make request
		int responseCode = connection.getResponseCode();

		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null)
			response.append(inputLine);
		in.close();

		if(responseCode != HttpURLConnection.HTTP_OK || response.length() == 0)
			throw new HTTPException(responseCode);

		JSONObject responseObject = new JSONObject(response.toString());

		return responseObject;
	}

	/*
	Helper function for converting a map to a name-value pair
	 */
    private List<NameValuePair> mapToNvpList(Map<String,Object> pairs) {
        List<NameValuePair> nvpList = new ArrayList<>(pairs.size());
        for (Map.Entry<String, Object> entry : pairs.entrySet())
            nvpList.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
        return  nvpList;
    }

}

