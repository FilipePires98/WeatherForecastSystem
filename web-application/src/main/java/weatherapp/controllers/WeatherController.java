package weatherapp.controllers;

import weatherapp.services.WeatherService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Paths related to the API weather requests.
 * 
 * @author Filipe Pires
 */
@RestController
@RequestMapping("/weather")
public class WeatherController {
    
    // Attributes
    
    /**
     * Service supported by the web application, providing:
     * - weather predictions;
     * - memory cache for past searches.
     */
    @Autowired
    private WeatherService weatherService;
    
    // Methods
    
    /*
    Paths:  "http://localhost:8080/weather/now/40.6405,-8.6538"
            "http://localhost:8080/weather/recent/40.6405,-8.6538/3"
            "http://localhost:8080/weather/period/40.6405,-8.6538/2019-04-28,2019-04-30"
            "http://localhost:8080/weather/cached"
    DarkSky:"https://api.darksky.net/forecast/fcaa01e2ece206bf84f403198e2d85a5/40.6405,-8.6538"
            "https://api.darksky.net/forecast/fcaa01e2ece206bf84f403198e2d85a5/40.6405,-8.6538,1556652090"
    */
    
    /**
     * Retrieves today's weather prediction.
     * 
     * @param latitude coordinate of latitude of the target location
     * @param longitude coordinate of longitude of the target location
     * @return response from the weather service in JSON format 
     * with the cached predictions (if successful) or the error message (if not)
     */
    @GetMapping("/now/{latitude},{longitude}")
    public String getWeatherNow(@PathVariable("latitude") Double latitude, @PathVariable("longitude") Double longitude) {
        // using the weather service
        return weatherService.get(latitude + "," + longitude, "now", new Long[0]).toString();
    }
    
    /**
     * Retrieves weather prediction for a specific number of days starting from today.
     * 
     * @param latitude coordinate of latitude of the target location
     * @param longitude coordinate of longitude of the target location
     * @param days Period of time (in days) of the desired weather prediction starting from today (passed in the http path) with a maximum of 7 days
     * @return response from the weather service in JSON format 
     * with the cached predictions (if successful) or the error message (if not)
     */
    @GetMapping("/recent/{latitude},{longitude}/{days}")
    public String getWeatherRecent(@PathVariable("latitude") double latitude, @PathVariable("longitude") double longitude, @PathVariable("days") int days) {
        // validating path variables
        if(days < 1){ days = 1; }
        if(days > 7){ days = 7; }
        // using the weather service
        return weatherService.get(latitude + "," + longitude, "recent", new Long[]{Long.valueOf(days)}).toString();
    }
    
    /**
     * Retrieves weather prediction for a specific time period;
     * The dates passed as path variables must follow the format "yyyy-MM-dd".
     * 
     * @param latitude coordinate of latitude of the target location
     * @param longitude coordinate of longitude of the target location
     * @param start Starting date of the desired weather prediction
     * @param end Ending date of the desired weather prediction with a maximum of 7 days ahead of today
     * @return response from the weather service in JSON format 
     * with the cached predictions (if successful) or the error message (if not)
     */
    @GetMapping("/period/{latitude},{longitude}/{start},{end}")
    public String getWeatherPeriod(@PathVariable("latitude") double latitude, @PathVariable("longitude") double longitude, @PathVariable("start") String start, @PathVariable("end") String end) {
        // validating path variables
        String formatString = "yyyy-MM-dd";
        Long currentTime = System.currentTimeMillis() / 1000;
        Long maxTime = currentTime + 6*86400;
        Long startingTime = this.getDateFromString(start, formatString);
        Long endingTime = this.getDateFromString(end, formatString);
        if(startingTime == -1L || endingTime == -1L) { 
            return "[{\"error\":\"-1\", \"message\":\"Unable to parse date\"}]"; 
        }
        if(startingTime > maxTime || startingTime > endingTime) {
            return "[{\"error\":\"-2\", \"message\":\"Invalid dates\"}]"; 
        }
        if(endingTime > maxTime) {
            endingTime = maxTime;
        }
        // using the weather service
        return weatherService.get(latitude + "," + longitude, "period", new Long[]{startingTime, endingTime}).toString();
    }
    
    /**
     * Retrieves the entire cache of previous weather predictions.
     * 
     * @return response from the weather service in JSON format 
     * with the cached predictions (if successful) or the error message (if not)
     */
    @GetMapping("/cached")
    public String getCache() {
        return this.weatherService.getAll(true).toString();
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
            Logger.getLogger(WeatherController.class.getName()).log(Level.SEVERE, null, ex);
            return -1L;
        }
        return date.getTime() / 1000;
    }

    /**
     * Setter method used to create mocks of the weather service.
     * @param weatherService mock object
     */
    public void setWeatherService(WeatherService weatherService) {
        this.weatherService = weatherService;
    }
    
}
