package weatherapp.services;

import weatherapp.cache.LocalCache;
import com.google.gson.*;
import java.text.*;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

/**
 * Weather Forecast Provider: serves controller to respond to the API requests.
 * 
 * @author Filipe Pires
 */
@Service
public class WeatherService {
    
    // Attributes
    
    /**
     * Used to interact with the memory cache
     */
    @Autowired
    private LocalCache<String, JsonObject> localCache;
    
    /**
     * Used to send requests to the external API DarkSky
     */
    @Autowired
    private ExternalService darkSkyService;
    
    /**
     * Internal private key used for the HTTP requests to the external API DarkSky
     */
    private String darkSkyKey = "fcaa01e2ece206bf84f403198e2d85a5";
    
    // Methods

    /**
     * Inserts new entry on the memory cache.
     * 
     * @param key identifier of the new entry
     * @param value data to be cached
     */
    private void store(String key, JsonObject value) {
        this.localCache.put(key, value);
    }
    
    /**
     * Internal method used to communicate with the external API.
     * 
     * @param path String holding the url path for the HTTP request to the external API
     * @param headers HTTP headers to be used in the request
     * @return HTTP response of the external API containing the weather forecasts for the intended time and location
     */
    private JsonArray httpRequest(String path, HttpHeaders headers) {
        return this.darkSkyService.getWeatherForecast(path, headers);
    }
    
    /**
     * Provides weather predictions for 3 types of requests:
     * - current day's forecast;
     * - next days' forecasts (with a maximum time distance of 7 days);
     * - daily forecasts of a given period of time (including past, present and future).
     * 
     * @param coords String containing the coordinates of the location of the desired forecast, separated by ','
     * @param type type of the desired forecast ('now', 'recent' or 'period')
     * @param options parameters used in some types of requests
     * @return array of JSON objects containing the weather forecasts for the intended time and location
     */
    public JsonArray get(String coords, String type, Long[] options) {
        // checking cache
        JsonArray cache = this.getAll(false); // [{"coords":[{day1},{day2}]},{"coords":[{day1}]},...]
        JsonObject entryObj;
        Object cachedObject;
        String key = coords + "," + type;
        if(type.equals("recent")) {
            key += "," + options[0];
        } else if(type.equals("period")) {
            key += "," + options[0] + "," + options[1];
        }
        for (JsonElement entry: cache) {
            entryObj = entry.getAsJsonObject();
            cachedObject = entryObj.get(key);
            if(cachedObject != null) {
                return (this.localCache.get(key).getAsJsonObject()).get(key).getAsJsonArray();
            }
        }
        // preparing request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        // preparing request 
        String path = "https://api.darksky.net/forecast/" + this.darkSkyKey + "/" + coords;
        JsonArray httpResponse;
        JsonArray requestedData = new JsonArray();
        Long currentTime = System.currentTimeMillis() / 1000;
        // treating the response
        if(type.equals("recent")) {
            path = path + "?exclude=currently,minutely,hourly,alerts,flags";
            httpResponse = this.httpRequest(path, headers);
            Long days = options[0];
            for(int i=0; i<days; i++) {
                if(httpResponse.size()==i){ break; }
                requestedData.add(httpResponse.get(i));
            }
        } else if(type.equals("period")) {
            Long startingTime = options[0];
            Long endingTime = options[1];
            String current_path;
            while(startingTime <= endingTime) {
                current_path = path + "," + startingTime
                     + "?exclude=currently,minutely,hourly,alerts,flags";
                httpResponse = this.httpRequest(current_path, headers);
                requestedData.add(httpResponse.get(0));
                startingTime = startingTime + 86400;
            }
        } else { // type.equals("now")
            path = path + "," + currentTime
                 + "?exclude=currently,minutely,hourly,alerts,flags";
            httpResponse = this.httpRequest(path, headers);
            requestedData.add(httpResponse.get(0));
        }
        // storing data in cache
        JsonObject cacheObject = new JsonObject();
        cacheObject.add(key, requestedData);
        this.store(key, cacheObject);
        return requestedData;
    }
    
    /**
     * Retrieved all entries from the memory cache.
     * 
     * @param updateLastAccessed tells the method whether it should update the last time the object was accessed or not
     * @return array of JSON objects containing the cached data
     */
    public JsonArray getAll(Boolean updateLastAccessed) {
        Gson gson = new GsonBuilder().create();
        Object[] cache = (Object[]) this.localCache.getAll(updateLastAccessed);
        JsonArray all = new JsonArray();
        for(Object o: cache) {
            all.add((JsonObject) gson.toJsonTree(o));
        }
        return all;
    }
    
    /**
     * Internal auxiliary method used to convert dates in String with a given format
     * into dates in milliseconds.
     * 
     * @param dateString date string to be converted
     * @param formatString format in which the date string is written
     * @return Long value holding the specified date in milliseconds
     */
    private Long getDateFromString(String dateString, String formatString) {
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        Date date;
        try {
            date = format.parse(dateString);
        } catch (ParseException ex) {
            return -1L;
        }
        return date.getTime() / 1000;
    }
}
