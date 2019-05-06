package weatherapp.controllers;

import weatherapp.services.WeatherService;
import com.google.gson.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;



/**
 * Test class used to ensure the correct functioning of the WeatherController.
 * 
 * @author Filipe Pires
 */
@ExtendWith(MockitoExtension.class)
@WebMvcTest(value=WeatherController.class)
public class WeatherControllerTest {
    
    @Autowired
    private MockMvc mvc;
    
    /**
     * Mock object used to abstract the tests to the WeatherController from the implementation of the WeatherService.
     */
    @MockBean
    WeatherService weatherService;
    
    /**
     * Coordinates to be inserted on the test path.
     */
    private Double latitude, longitude;
    
    /**
     * Method called every time a method annotated with @Test is executed, before its execution.
     */
    @BeforeEach
    public void setUp() {
        latitude = 40.6405;
        longitude = -8.6538;
    }
    
    /**
     * Method called every time a method annotated with @Test is executed, after its execution.
     */
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of getWeatherNow method, of class WeatherController.
     */
    @Test
    public void testGetWeatherNowSuccess() throws Exception {
        System.out.println("getWeatherNowSuccess");
        // arrange
        String expResultStr = "{\"time\":1556838000,\"summary\":\"Breezy starting in the afternoon, continuing until evening.\",\"icon\":\"wind\",\"sunriseTime\":1556861541,\"sunsetTime\":1556911989,\"moonPhase\":0.95,\"precipIntensity\":0,\"precipIntensityMax\":0,\"precipProbability\":0,\"temperatureHigh\":72.37,\"temperatureHighTime\":1556877600,\"temperatureLow\":54.23,\"temperatureLowTime\":1556949600,\"apparentTemperatureHigh\":72.37,\"apparentTemperatureHighTime\":1556877600,\"apparentTemperatureLow\":54.23,\"apparentTemperatureLowTime\":1556949600,\"dewPoint\":50.99,\"humidity\":0.72,\"pressure\":1011.42,\"windSpeed\":9.6,\"windGust\":33.66,\"windGustTime\":1556899200,\"windBearing\":4,\"cloudCover\":0.08,\"uvIndex\":7,\"uvIndexTime\":1556884800,\"visibility\":7.08,\"ozone\":346.98,\"temperatureMin\":47.13,\"temperatureMinTime\":1556863200,\"temperatureMax\":72.37,\"temperatureMaxTime\":1556877600,\"apparentTemperatureMin\":44.09,\"apparentTemperatureMinTime\":1556863200,\"apparentTemperatureMax\":72.37,\"apparentTemperatureMaxTime\":1556877600}";
        JsonObject expResultObj = new JsonParser().parse(expResultStr).getAsJsonObject();
        JsonArray expResult = new JsonArray();
        expResult.add(expResultObj);
        Mockito.when(weatherService.get(latitude + "," + longitude, "now", new Long[0])).thenReturn(expResult);
        // act and assert
        mvc.perform(get("/weather/now/" + latitude + "," + longitude))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0]", hasKey(expResultObj.keySet().toArray()[0])));
    }

    /**
     * Test of getWeatherRecent method, of class WeatherController.
     */
    @Test
    public void testGetWeatherRecent() throws Exception {
        System.out.println("getWeatherRecent");
        // arrange
        int days = 1;
        String expResultStr = "{\"time\":1556838000,\"summary\":\"Breezy starting in the afternoon, continuing until evening.\",\"icon\":\"wind\",\"sunriseTime\":1556861541,\"sunsetTime\":1556911989,\"moonPhase\":0.95,\"precipIntensity\":0,\"precipIntensityMax\":0,\"precipProbability\":0,\"temperatureHigh\":72.37,\"temperatureHighTime\":1556877600,\"temperatureLow\":54.23,\"temperatureLowTime\":1556949600,\"apparentTemperatureHigh\":72.37,\"apparentTemperatureHighTime\":1556877600,\"apparentTemperatureLow\":54.23,\"apparentTemperatureLowTime\":1556949600,\"dewPoint\":50.99,\"humidity\":0.72,\"pressure\":1011.42,\"windSpeed\":9.6,\"windGust\":33.66,\"windGustTime\":1556899200,\"windBearing\":4,\"cloudCover\":0.08,\"uvIndex\":7,\"uvIndexTime\":1556884800,\"visibility\":7.08,\"ozone\":346.98,\"temperatureMin\":47.13,\"temperatureMinTime\":1556863200,\"temperatureMax\":72.37,\"temperatureMaxTime\":1556877600,\"apparentTemperatureMin\":44.09,\"apparentTemperatureMinTime\":1556863200,\"apparentTemperatureMax\":72.37,\"apparentTemperatureMaxTime\":1556877600}";
        JsonObject expResultObj = new JsonParser().parse(expResultStr).getAsJsonObject();
        JsonArray expResult = new JsonArray();
        expResult.add(expResultObj);
        Mockito.when(weatherService.get(latitude + "," + longitude, "recent", new Long[]{Long.valueOf(days)})).thenReturn(expResult);
        // act and assert
        mvc.perform(get("/weather/recent/" + latitude + "," + longitude + "/" + days))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0]", hasKey(expResultObj.keySet().toArray()[0])));
    }
    
    /**
     * Test of getWeatherRecent method, of class WeatherController.
     */
    @Test
    public void testGetWeatherRecentBadInput() throws Exception {
        System.out.println("getWeatherRecent BadInput");
        // arrange
        String expResultStr = "{\"time\":1556838000,\"summary\":\"Breezy starting in the afternoon, continuing until evening.\",\"icon\":\"wind\",\"sunriseTime\":1556861541,\"sunsetTime\":1556911989,\"moonPhase\":0.95,\"precipIntensity\":0,\"precipIntensityMax\":0,\"precipProbability\":0,\"temperatureHigh\":72.37,\"temperatureHighTime\":1556877600,\"temperatureLow\":54.23,\"temperatureLowTime\":1556949600,\"apparentTemperatureHigh\":72.37,\"apparentTemperatureHighTime\":1556877600,\"apparentTemperatureLow\":54.23,\"apparentTemperatureLowTime\":1556949600,\"dewPoint\":50.99,\"humidity\":0.72,\"pressure\":1011.42,\"windSpeed\":9.6,\"windGust\":33.66,\"windGustTime\":1556899200,\"windBearing\":4,\"cloudCover\":0.08,\"uvIndex\":7,\"uvIndexTime\":1556884800,\"visibility\":7.08,\"ozone\":346.98,\"temperatureMin\":47.13,\"temperatureMinTime\":1556863200,\"temperatureMax\":72.37,\"temperatureMaxTime\":1556877600,\"apparentTemperatureMin\":44.09,\"apparentTemperatureMinTime\":1556863200,\"apparentTemperatureMax\":72.37,\"apparentTemperatureMaxTime\":1556877600}";
        JsonObject expResultObj = new JsonParser().parse(expResultStr).getAsJsonObject();
        JsonArray expResult1 = new JsonArray();
        JsonArray expResult7 = new JsonArray();
        expResult1.add(expResultObj);
        Mockito.when(weatherService.get(latitude + "," + longitude, "recent", new Long[]{Long.valueOf(1)})).thenReturn(expResult1);
        expResult7.add(expResultObj); expResult7.add(expResultObj); expResult7.add(expResultObj); expResult7.add(expResultObj); expResult7.add(expResultObj); expResult7.add(expResultObj); expResult7.add(expResultObj);
        Mockito.when(weatherService.get(latitude + "," + longitude, "recent", new Long[]{Long.valueOf(7)})).thenReturn(expResult7);
        // act and assert
        mvc.perform(get("/weather/recent/" + latitude + "," + longitude + "/" + -1))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0]", hasKey(expResultObj.keySet().toArray()[0])));
        mvc.perform(get("/weather/recent/" + latitude + "," + longitude + "/" + 8))
                .andExpect(jsonPath("$", hasSize(7)))
                .andExpect(jsonPath("$[0]", hasKey(expResultObj.keySet().toArray()[0])));
    }

    /**
     * Test of getWeatherPeriod method, of class WeatherController.
     */
    @Test
    public void testGetWeatherPeriod() throws Exception {
        System.out.println("getWeatherPeriod");
        // arrange
        Long[] timemillis = new Long[]{1556406000L, 1556406000L};
        String[] timedate = new String[]{"2019-04-28","2019-04-28"};
        String expResultStr = "{\"time\":1556406000,\"summary\":\"Foggy in the morning.\",\"icon\":\"fog\",\"sunriseTime\":1556429923,\"sunsetTime\":1556479673,\"moonPhase\":0.8,\"precipIntensity\":0,\"precipIntensityMax\":0,\"precipProbability\":0,\"temperatureHigh\":68.85,\"temperatureHighTime\":1556460000,\"temperatureLow\":52.26,\"temperatureLowTime\":1556506800,\"apparentTemperatureHigh\":68.85,\"apparentTemperatureHighTime\":1556460000,\"apparentTemperatureLow\":52.26,\"apparentTemperatureLowTime\":1556506800,\"dewPoint\":52.92,\"humidity\":0.85,\"pressure\":1021.43,\"windSpeed\":5.18,\"windGust\":12.34,\"windGustTime\":1556470800,\"windBearing\":355,\"cloudCover\":0.3,\"uvIndex\":8,\"uvIndexTime\":1556456400,\"visibility\":5.4,\"ozone\":309.01,\"temperatureMin\":46.48,\"temperatureMinTime\":1556427600,\"temperatureMax\":68.85,\"temperatureMaxTime\":1556460000,\"apparentTemperatureMin\":44.92,\"apparentTemperatureMinTime\":1556427600,\"apparentTemperatureMax\":68.85,\"apparentTemperatureMaxTime\":1556460000}";
        JsonObject expResultObj = new JsonParser().parse(expResultStr).getAsJsonObject();
        JsonArray expResult = new JsonArray();
        expResult.add(expResultObj);
        Mockito.when(weatherService.get(latitude + "," + longitude, "period", timemillis)).thenReturn(expResult);
        // act and assert
        mvc.perform(get("/weather/period/" + latitude + "," + longitude + "/" + timedate[0] + "," + timedate[1]))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0]", hasKey(expResultObj.keySet().toArray()[0])));
    }
    
    /**
     * Test of getWeatherPeriod method, of class WeatherController.
     */
    @Test
    public void testGetWeatherPeriodBadInput() throws Exception {
        System.out.println("getWeatherPeriod BadInput");
        // arrange
        Long[] timemillis = new Long[]{1556406000L, 1556406000L};
        String[] timedate = new String[]{"2019-04-28","2019-04-27"};
        String expResultStr = "{\"error\":\"-2\", \"message\":\"Invalid dates\"}"; 
        JsonObject expResultObj = new JsonParser().parse(expResultStr).getAsJsonObject();
        JsonArray expResult = new JsonArray();
        expResult.add(expResultObj);
        Mockito.when(weatherService.get(latitude + "," + longitude, "period", timemillis)).thenReturn(expResult);
        Mockito.when(weatherService.get(latitude + "," + longitude, "period", new Long[]{-1L, 1556406000L})).thenReturn(expResult);
        Mockito.when(weatherService.get(latitude + "," + longitude, "period", new Long[]{1556406000L, -1L})).thenReturn(expResult);
        // act and assert
        mvc.perform(get("/weather/period/" + latitude + "," + longitude + "/" + timedate[0] + "," + timedate[1]))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0]", hasKey(expResultObj.keySet().toArray()[0])));
        mvc.perform(get("/weather/period/" + latitude + "," + longitude + "/" + "bad-date" + "," + timedate[1]))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].error", is("-1")));
        mvc.perform(get("/weather/period/" + latitude + "," + longitude + "/" + timedate[0] + "," + "bad-date"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].error", is("-1")));
    }

    /**
     * Test of getCache method, of class WeatherController.
     */
    @Test
    public void testGetCache() throws Exception {
        System.out.println("getCache");
        // arrange
        Long[] timemillis = new Long[]{1556406000L, 1556406000L};
        String[] timedate = new String[]{"2019-04-28","2019-04-28"};
        String expResultStr = "{\"time\":1556406000,\"summary\":\"Foggy in the morning.\",\"icon\":\"fog\",\"sunriseTime\":1556429923,\"sunsetTime\":1556479673,\"moonPhase\":0.8,\"precipIntensity\":0,\"precipIntensityMax\":0,\"precipProbability\":0,\"temperatureHigh\":68.85,\"temperatureHighTime\":1556460000,\"temperatureLow\":52.26,\"temperatureLowTime\":1556506800,\"apparentTemperatureHigh\":68.85,\"apparentTemperatureHighTime\":1556460000,\"apparentTemperatureLow\":52.26,\"apparentTemperatureLowTime\":1556506800,\"dewPoint\":52.92,\"humidity\":0.85,\"pressure\":1021.43,\"windSpeed\":5.18,\"windGust\":12.34,\"windGustTime\":1556470800,\"windBearing\":355,\"cloudCover\":0.3,\"uvIndex\":8,\"uvIndexTime\":1556456400,\"visibility\":5.4,\"ozone\":309.01,\"temperatureMin\":46.48,\"temperatureMinTime\":1556427600,\"temperatureMax\":68.85,\"temperatureMaxTime\":1556460000,\"apparentTemperatureMin\":44.92,\"apparentTemperatureMinTime\":1556427600,\"apparentTemperatureMax\":68.85,\"apparentTemperatureMaxTime\":1556460000}";
        JsonObject expResultObj = new JsonParser().parse(expResultStr).getAsJsonObject();
        JsonArray expResult = new JsonArray();
        expResult.add(expResultObj);
        Mockito.when(weatherService.getAll(true)).thenReturn(expResult);
        // act and assert
        mvc.perform(get("/weather/cached/"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0]", hasKey(expResultObj.keySet().toArray()[0])));
    }
    
}
