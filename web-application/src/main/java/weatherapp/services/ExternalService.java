/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weatherapp.services;

import com.google.gson.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

/**
 * External API Provider: serves internal API to respond to the requests to DarkSky.
 * 
 * @author Filipe Pires
 */
public class ExternalService {
   
    /**
     * Template used for HTTP requests to the DarkSky API
     */
    @Autowired
    private RestTemplate restTemplate;
    
    /**
     * Sends requests to the external API according to the supplied path.
     * 
     * @param path String holding the url path for the HTTP request to the external API
     * @param headers HTTP headers to be used in the request
     * @return HTTP response of the external API containing the weather forecasts for the intended time and location
     */
    public JsonArray getWeatherForecast(String path, HttpHeaders headers) {
        // preparing http request
        HttpEntity<String> entity = new HttpEntity<>(headers);
        // retrieving data from the external api
        ResponseEntity<String> response = restTemplate.exchange(path, HttpMethod.GET, entity, String.class);
        String darkSky = response.getBody();
        JsonObject darkSkyJSON = new JsonParser().parse(darkSky).getAsJsonObject();
        JsonArray requestedData = new JsonArray();
        if(darkSkyJSON.has("error")) {
            requestedData.add(darkSkyJSON);
            return requestedData;
        }
        requestedData = darkSkyJSON.getAsJsonObject("daily").getAsJsonArray("data");
        return requestedData;
    }
}
