package weatherapp.services;

import weatherapp.cache.LocalCache;
import java.util.Arrays;
import com.google.gson.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.quality.Strictness;
import org.mockito.junit.jupiter.*;
import org.springframework.http.*;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class used to ensure the correct functioning of the WeatherService.
 * 
 * @author Filipe Pires
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class WeatherServiceTest {
    
    @TestConfiguration
    static class WeatherServiceTestContextConfiguration {
        @Bean
        public WeatherService weatherService() {
            return new WeatherService();
        }
    }
    
    /**
     * Instance of the target class to be tested.
     */
    @Autowired
    @InjectMocks
    WeatherService weatherService;
    
    /**
     * Mock object used to abstract the tests to the WeatherService from the implementation of the ExternalService.
     */
    @Mock
    ExternalService externalService;
    
    /**
     * Mock object used to abstract the tests to the WeatherService from the implementation of the LocalCache.
     */
    @Mock
    LocalCache localCache;
    
    /**
     * Header object passed to the ExternalService's mock object.
     */
    private HttpHeaders headers;
    /**
     * Private key needed for all requests made to the DarkSky API, inserted in every path.
     */
    private final String darkSkyKey = "fcaa01e2ece206bf84f403198e2d85a5";
    /**
     * Coordinates to be inserted on the test path.
     */
    private String coords = "40.6405,-8.6538";
    
    /*
    @BeforeEach
    public void setUp() {
        
    }
    
    @AfterEach
    public void tearDown() {
    }
    */
    
    /**
     * Test of get method, of class WeatherService.
     */
    @Test
    public void testGetNow() {
        System.out.println("get now");
        // arrange
        headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        String type = "now";
        Long[] options = new Long[0];
        Long currentTime = System.currentTimeMillis() / 1000;
        String path = "https://api.darksky.net/forecast/" + darkSkyKey + "/" + coords + "," + currentTime + "?exclude=currently,minutely,hourly,alerts,flags";
        
        String expResultStr = "{\"time\":1556406000,\"summary\":\"Foggy in the morning.\",\"icon\":\"fog\",\"sunriseTime\":1556429923,\"sunsetTime\":1556479673,\"moonPhase\":0.8,\"precipIntensity\":0,\"precipIntensityMax\":0,\"precipProbability\":0,\"temperatureHigh\":68.85,\"temperatureHighTime\":1556460000,\"temperatureLow\":52.26,\"temperatureLowTime\":1556506800,\"apparentTemperatureHigh\":68.85,\"apparentTemperatureHighTime\":1556460000,\"apparentTemperatureLow\":52.26,\"apparentTemperatureLowTime\":1556506800,\"dewPoint\":52.92,\"humidity\":0.85,\"pressure\":1021.43,\"windSpeed\":5.18,\"windGust\":12.34,\"windGustTime\":1556470800,\"windBearing\":355,\"cloudCover\":0.3,\"uvIndex\":8,\"uvIndexTime\":1556456400,\"visibility\":5.4,\"ozone\":309.01,\"temperatureMin\":46.48,\"temperatureMinTime\":1556427600,\"temperatureMax\":68.85,\"temperatureMaxTime\":1556460000,\"apparentTemperatureMin\":44.92,\"apparentTemperatureMinTime\":1556427600,\"apparentTemperatureMax\":68.85,\"apparentTemperatureMaxTime\":1556460000}";
        JsonObject expResultObj = new JsonParser().parse(expResultStr).getAsJsonObject();
        JsonArray expResult = new JsonArray();
        expResult.add(expResultObj);
        Mockito.when(externalService.getWeatherForecast(path, headers)).thenReturn(expResult);
        Mockito.when(localCache.getAll(false)).thenReturn(new Object[0]);
        //Mockito.when(localCache.get(coords + "," + type)).thenReturn(expResult);
        // act
        JsonArray result = weatherService.get(coords, type, options);
        //assert
        assertThat(result.size()).isEqualTo(expResult.size());
        assertThat(result.get(0).getAsJsonObject().keySet()).isEqualTo(expResult.get(0).getAsJsonObject().keySet());
    }
    
    /**
     * Test of get method, of class WeatherService.
     */
    @Test
    public void testGetRecent() {
        System.out.println("get recent");
        // arrange
        headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        String type = "recent";
        Long[] options = new Long[]{Long.valueOf(2)};
        String path = "https://api.darksky.net/forecast/" + darkSkyKey + "/" + coords + "?exclude=currently,minutely,hourly,alerts,flags";
        String expResultStr = "{\"time\":1556406000,\"summary\":\"Foggy in the morning.\",\"icon\":\"fog\",\"sunriseTime\":1556429923,\"sunsetTime\":1556479673,\"moonPhase\":0.8,\"precipIntensity\":0,\"precipIntensityMax\":0,\"precipProbability\":0,\"temperatureHigh\":68.85,\"temperatureHighTime\":1556460000,\"temperatureLow\":52.26,\"temperatureLowTime\":1556506800,\"apparentTemperatureHigh\":68.85,\"apparentTemperatureHighTime\":1556460000,\"apparentTemperatureLow\":52.26,\"apparentTemperatureLowTime\":1556506800,\"dewPoint\":52.92,\"humidity\":0.85,\"pressure\":1021.43,\"windSpeed\":5.18,\"windGust\":12.34,\"windGustTime\":1556470800,\"windBearing\":355,\"cloudCover\":0.3,\"uvIndex\":8,\"uvIndexTime\":1556456400,\"visibility\":5.4,\"ozone\":309.01,\"temperatureMin\":46.48,\"temperatureMinTime\":1556427600,\"temperatureMax\":68.85,\"temperatureMaxTime\":1556460000,\"apparentTemperatureMin\":44.92,\"apparentTemperatureMinTime\":1556427600,\"apparentTemperatureMax\":68.85,\"apparentTemperatureMaxTime\":1556460000}";
        JsonObject expResultObj = new JsonParser().parse(expResultStr).getAsJsonObject();
        JsonArray expResult = new JsonArray();
        expResult.add(expResultObj);expResult.add(expResultObj);
        Mockito.when(externalService.getWeatherForecast(path, headers)).thenReturn(expResult);
        Mockito.when(localCache.getAll(false)).thenReturn(new Object[0]);
        //Mockito.when(localCache.get(coords + "," + type)).thenReturn(expResult);
        // act
        JsonArray result = weatherService.get(coords, type, options);
        //assert
        assertThat(result.size()).isEqualTo(expResult.size());
        assertThat(result.get(0).getAsJsonObject().keySet()).isEqualTo(expResult.get(0).getAsJsonObject().keySet());
    }

    /**
     * Test of get method, of class WeatherService.
     */
    @Test
    public void testGetPeriod() {
        System.out.println("get period");
        // arrange
        headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        String type = "period";
        Long[] options = new Long[]{1556406000L, 1556406000L};
        String path = "https://api.darksky.net/forecast/" + darkSkyKey + "/" + coords + "," + options[0] + "?exclude=currently,minutely,hourly,alerts,flags";
        
        String expResultStr = "{\"time\":1556406000,\"summary\":\"Foggy in the morning.\",\"icon\":\"fog\",\"sunriseTime\":1556429923,\"sunsetTime\":1556479673,\"moonPhase\":0.8,\"precipIntensity\":0,\"precipIntensityMax\":0,\"precipProbability\":0,\"temperatureHigh\":68.85,\"temperatureHighTime\":1556460000,\"temperatureLow\":52.26,\"temperatureLowTime\":1556506800,\"apparentTemperatureHigh\":68.85,\"apparentTemperatureHighTime\":1556460000,\"apparentTemperatureLow\":52.26,\"apparentTemperatureLowTime\":1556506800,\"dewPoint\":52.92,\"humidity\":0.85,\"pressure\":1021.43,\"windSpeed\":5.18,\"windGust\":12.34,\"windGustTime\":1556470800,\"windBearing\":355,\"cloudCover\":0.3,\"uvIndex\":8,\"uvIndexTime\":1556456400,\"visibility\":5.4,\"ozone\":309.01,\"temperatureMin\":46.48,\"temperatureMinTime\":1556427600,\"temperatureMax\":68.85,\"temperatureMaxTime\":1556460000,\"apparentTemperatureMin\":44.92,\"apparentTemperatureMinTime\":1556427600,\"apparentTemperatureMax\":68.85,\"apparentTemperatureMaxTime\":1556460000}";
        JsonObject expResultObj = new JsonParser().parse(expResultStr).getAsJsonObject();
        JsonArray expResult = new JsonArray();
        expResult.add(expResultObj);
        Mockito.when(externalService.getWeatherForecast(path, headers)).thenReturn(expResult);
        Mockito.when(localCache.getAll(false)).thenReturn(new Object[0]);
        //Mockito.when(localCache.get(coords + "," + type + "," + options[0] + "," + options[1])).thenReturn(expResult);
        // act
        JsonArray result = weatherService.get(coords, type, options);
        //assert
        assertThat(result.size()).isEqualTo(expResult.size());
        assertThat(result.get(0).getAsJsonObject().keySet()).isEqualTo(expResult.get(0).getAsJsonObject().keySet());
    }
    
    /**
     * Test of getAll method, of class WeatherService.
     */
    @Test
    public void testGetAllEmpty() {
        System.out.println("getAll empty");
        // arrange
        Mockito.when(localCache.getAll(false)).thenReturn(new Object[0]);
        Gson gson = new GsonBuilder().create();
        // act
        Object[] cache = (Object[]) this.localCache.getAll(false);
        JsonArray all = new JsonArray();
        for(Object o: cache) {
            all.add((JsonObject) gson.toJsonTree(o));
        }
        // assert
        assertThat(all.size()).isEqualTo(0);
    }
    
    /**
     * Test of getAll method, of class WeatherService.
     */
    @Test
    public void testGetAllNonEmpty() {
        System.out.println("getAll non-empty");
        // arrange
        //  prepare JsonObject to be cached as value
        String expResValueStr = "{\"time\":1556406000,\"summary\":\"Foggy in the morning.\",\"icon\":\"fog\",\"sunriseTime\":1556429923,\"sunsetTime\":1556479673,\"moonPhase\":0.8,\"precipIntensity\":0,\"precipIntensityMax\":0,\"precipProbability\":0,\"temperatureHigh\":68.85,\"temperatureHighTime\":1556460000,\"temperatureLow\":52.26,\"temperatureLowTime\":1556506800,\"apparentTemperatureHigh\":68.85,\"apparentTemperatureHighTime\":1556460000,\"apparentTemperatureLow\":52.26,\"apparentTemperatureLowTime\":1556506800,\"dewPoint\":52.92,\"humidity\":0.85,\"pressure\":1021.43,\"windSpeed\":5.18,\"windGust\":12.34,\"windGustTime\":1556470800,\"windBearing\":355,\"cloudCover\":0.3,\"uvIndex\":8,\"uvIndexTime\":1556456400,\"visibility\":5.4,\"ozone\":309.01,\"temperatureMin\":46.48,\"temperatureMinTime\":1556427600,\"temperatureMax\":68.85,\"temperatureMaxTime\":1556460000,\"apparentTemperatureMin\":44.92,\"apparentTemperatureMinTime\":1556427600,\"apparentTemperatureMax\":68.85,\"apparentTemperatureMaxTime\":1556460000}";
        JsonObject expResValueObj = new JsonParser().parse(expResValueStr).getAsJsonObject();
        JsonArray expResValue = new JsonArray();
        expResValue.add(expResValueObj);
        //  prepare String to be cached as key
        String expResKey = coords + "," + "now";
        //  wrap key + value in CachedObject
        JsonObject expResult = new JsonObject();
        expResult.add(expResKey, expResValue);
        //  insert cachedObject in expected result array (Object[] for request, JsonArray for reply verification)
        Object[] expResultArray = new Object[1];
        JsonArray expResultJsonArray = new JsonArray();
        expResultArray[0] = expResult;
        expResultJsonArray.add(expResult);
        //  prepare mock object
        Mockito.when(localCache.getAll(false)).thenReturn(expResultArray);
        Gson gson = new GsonBuilder().create();
        // act
        Object[] cache = (Object[]) this.localCache.getAll(false);
        JsonArray all = new JsonArray();
        for(Object o: cache) {
            all.add((JsonObject) gson.toJsonTree(o));
        }
        // assert
        assertThat(all).isEqualTo(expResultJsonArray);
    }
    
}
