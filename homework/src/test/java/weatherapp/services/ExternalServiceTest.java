package weatherapp.services;

import java.util.Arrays;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.*;
import org.springframework.http.*;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Test class used to ensure the correct functioning of the ExternalService.
 * 
 * @author Filipe Pires
 */
@SpringBootTest
public class ExternalServiceTest {
    
    /**
     * Instance of the target class to be tested.
     */
    @Autowired
    ExternalService instance;
    
    /**
     * Path of the requests made to the DarkSky API.
     */
    private String path;
    /**
     * Header object passed in the requests made to the DarkSky API.
     */
    private HttpHeaders headers;
    
    /**
     * Method called every time a method annotated with @Test is executed, before its execution.
     */
    @BeforeEach
    public void setUp() {
        headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
    }
    
    /**
     * Method called every time a method annotated with @Test is executed, after its execution.
     */
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of getWeatherForecast method, of class ExternalService.
     */
    @Test
    public void testGetWeatherForecastPastPeriod() {
        System.out.println("getWeatherForecast Period");
        // arrange 
        path = "https://api.darksky.net/forecast/fcaa01e2ece206bf84f403198e2d85a5/40.6405,-8.6538,1555714800?exclude=currently,minutely,hourly,alerts,flags";
        String expResultStr ="{\"latitude\":40.6405,\"longitude\":-8.6538,\"timezone\":\"Europe/Lisbon\",\"daily\":{\"data\":[{\"time\":1555714800,\"summary\":\"Partly cloudy until afternoon.\",\"icon\":\"partly-cloudy-day\",\"sunriseTime\":1555739391,\"sunsetTime\":1555787966,\"moonPhase\":0.54,\"precipIntensity\":0,\"precipIntensityMax\":0.0002,\"precipIntensityMaxTime\":1555729200,\"precipProbability\":0,\"temperatureHigh\":79.53,\"temperatureHighTime\":1555761600,\"temperatureLow\":53.73,\"temperatureLowTime\":1555808400,\"apparentTemperatureHigh\":79.53,\"apparentTemperatureHighTime\":1555761600,\"apparentTemperatureLow\":53.73,\"apparentTemperatureLowTime\":1555808400,\"dewPoint\":52.22,\"humidity\":0.64,\"pressure\":1016.62,\"windSpeed\":7.74,\"windGust\":28.77,\"windGustTime\":1555768800,\"windBearing\":9,\"cloudCover\":0.18,\"uvIndex\":6,\"uvIndexTime\":1555761600,\"visibility\":6.22,\"ozone\":358.78,\"temperatureMin\":56.73,\"temperatureMinTime\":1555732800,\"temperatureMax\":79.53,\"temperatureMaxTime\":1555761600,\"apparentTemperatureMin\":56.73,\"apparentTemperatureMinTime\":1555732800,\"apparentTemperatureMax\":79.53,\"apparentTemperatureMaxTime\":1555761600}]},\"offset\":1}";
        JsonObject expResultObj = new JsonParser().parse(expResultStr).getAsJsonObject();
        JsonArray expResult = expResultObj.getAsJsonObject("daily").getAsJsonArray("data");
        // act
        JsonArray result = instance.getWeatherForecast(path, headers);
        // assert
        assertThat(result.get(0).getAsJsonObject().keySet()).isEqualTo(expResult.get(0).getAsJsonObject().keySet());
    }
    
    /**
     * Test of getWeatherForecast method, of class ExternalService.
     */
    @Test
    public void testGetWeatherForecastNow() {
        System.out.println("getWeatherForecast Now");
        // arrange 
        path = "https://api.darksky.net/forecast/fcaa01e2ece206bf84f403198e2d85a5/40.6405,-8.6538?exclude=currently,minutely,hourly,alerts,flags";
        String expResultStr = "{\"latitude\":40.6405,\"longitude\":-8.6538,\"timezone\":\"Europe/Lisbon\",\"daily\":{\"summary\":\"Light rain tomorrow through Thursday, with high temperatures bottoming out at 61°F on Wednesday.\",\"icon\":\"rain\",\"data\":[{\"time\":1557097200,\"summary\":\"Mostly cloudy throughout the day.\",\"icon\":\"partly-cloudy-day\",\"sunriseTime\":1557120526,\"sunsetTime\":1557171377,\"moonPhase\":0.05,\"precipIntensity\":0.0004,\"precipIntensityMax\":0.0016,\"precipIntensityMaxTime\":1557111600,\"precipProbability\":0.03,\"precipType\":\"rain\",\"temperatureHigh\":65.53,\"temperatureHighTime\":1557154800,\"temperatureLow\":54.93,\"temperatureLowTime\":1557187200,\"apparentTemperatureHigh\":65.53,\"apparentTemperatureHighTime\":1557154800,\"apparentTemperatureLow\":54.93,\"apparentTemperatureLowTime\":1557187200,\"dewPoint\":51.24,\"humidity\":0.74,\"pressure\":1019.26,\"windSpeed\":3.08,\"windGust\":9.34,\"windGustTime\":1557158400,\"windBearing\":240,\"cloudCover\":0.58,\"uvIndex\":6,\"uvIndexTime\":1557147600,\"visibility\":7.98,\"ozone\":352.97,\"temperatureMin\":55.51,\"temperatureMinTime\":1557180000,\"temperatureMax\":65.53,\"temperatureMaxTime\":1557154800,\"apparentTemperatureMin\":55.51,\"apparentTemperatureMinTime\":1557180000,\"apparentTemperatureMax\":65.53,\"apparentTemperatureMaxTime\":1557154800},{\"time\":1557183600,\"summary\":\"Overcast throughout the day.\",\"icon\":\"cloudy\",\"sunriseTime\":1557206858,\"sunsetTime\":1557257839,\"moonPhase\":0.08,\"precipIntensity\":0.007,\"precipIntensityMax\":0.0296,\"precipIntensityMaxTime\":1557241200,\"precipProbability\":0.61,\"precipType\":\"rain\",\"temperatureHigh\":62.71,\"temperatureHighTime\":1557244800,\"temperatureLow\":58.04,\"temperatureLowTime\":1557284400,\"apparentTemperatureHigh\":62.81,\"apparentTemperatureHighTime\":1557244800,\"apparentTemperatureLow\":58.04,\"apparentTemperatureLowTime\":1557284400,\"dewPoint\":56.05,\"humidity\":0.89,\"pressure\":1017.04,\"windSpeed\":9.11,\"windGust\":25.87,\"windGustTime\":1557241200,\"windBearing\":193,\"cloudCover\":0.92,\"uvIndex\":4,\"uvIndexTime\":1557226800,\"visibility\":8.71,\"ozone\":348.67,\"temperatureMin\":54.93,\"temperatureMinTime\":1557187200,\"temperatureMax\":62.71,\"temperatureMaxTime\":1557244800,\"apparentTemperatureMin\":54.93,\"apparentTemperatureMinTime\":1557187200,\"apparentTemperatureMax\":62.81,\"apparentTemperatureMaxTime\":1557244800},{\"time\":1557270000,\"summary\":\"Rain until afternoon and breezy in the morning.\",\"icon\":\"rain\",\"sunriseTime\":1557293190,\"sunsetTime\":1557344301,\"moonPhase\":0.12,\"precipIntensity\":0.0332,\"precipIntensityMax\":0.0945,\"precipIntensityMaxTime\":1557306000,\"precipProbability\":0.95,\"precipType\":\"rain\",\"temperatureHigh\":60.5,\"temperatureHighTime\":1557309600,\"temperatureLow\":54.82,\"temperatureLowTime\":1557367200,\"apparentTemperatureHigh\":60.5,\"apparentTemperatureHighTime\":1557309600,\"apparentTemperatureLow\":54.82,\"apparentTemperatureLowTime\":1557367200,\"dewPoint\":55.24,\"humidity\":0.88,\"pressure\":1014.88,\"windSpeed\":9.72,\"windGust\":28.04,\"windGustTime\":1557280800,\"windBearing\":231,\"cloudCover\":0.96,\"uvIndex\":4,\"uvIndexTime\":1557313200,\"visibility\":7.82,\"ozone\":347.8,\"temperatureMin\":56.01,\"temperatureMinTime\":1557352800,\"temperatureMax\":60.5,\"temperatureMaxTime\":1557309600,\"apparentTemperatureMin\":56.01,\"apparentTemperatureMinTime\":1557352800,\"apparentTemperatureMax\":60.5,\"apparentTemperatureMaxTime\":1557309600},{\"time\":1557356400,\"summary\":\"Light rain in the afternoon.\",\"icon\":\"rain\",\"sunriseTime\":1557379524,\"sunsetTime\":1557430762,\"moonPhase\":0.16,\"precipIntensity\":0.0118,\"precipIntensityMax\":0.0355,\"precipIntensityMaxTime\":1557414000,\"precipProbability\":0.93,\"precipType\":\"rain\",\"temperatureHigh\":62.67,\"temperatureHighTime\":1557424800,\"temperatureLow\":60.83,\"temperatureLowTime\":1557450000,\"apparentTemperatureHigh\":62.87,\"apparentTemperatureHighTime\":1557424800,\"apparentTemperatureLow\":61.09,\"apparentTemperatureLowTime\":1557450000,\"dewPoint\":55.22,\"humidity\":0.88,\"pressure\":1014.94,\"windSpeed\":7.69,\"windGust\":28.9,\"windGustTime\":1557424800,\"windBearing\":193,\"cloudCover\":0.95,\"uvIndex\":4,\"uvIndexTime\":1557399600,\"visibility\":7.13,\"ozone\":328.69,\"temperatureMin\":54.82,\"temperatureMinTime\":1557367200,\"temperatureMax\":63.21,\"temperatureMaxTime\":1557428400,\"apparentTemperatureMin\":54.82,\"apparentTemperatureMinTime\":1557367200,\"apparentTemperatureMax\":63.37,\"apparentTemperatureMaxTime\":1557428400},{\"time\":1557442800,\"summary\":\"Mostly cloudy throughout the day.\",\"icon\":\"partly-cloudy-day\",\"sunriseTime\":1557465860,\"sunsetTime\":1557517224,\"moonPhase\":0.19,\"precipIntensity\":0.0101,\"precipIntensityMax\":0.0162,\"precipIntensityMaxTime\":1557446400,\"precipProbability\":0.93,\"precipType\":\"rain\",\"temperatureHigh\":64.5,\"temperatureHighTime\":1557507600,\"temperatureLow\":55.32,\"temperatureLowTime\":1557550800,\"apparentTemperatureHigh\":64.51,\"apparentTemperatureHighTime\":1557507600,\"apparentTemperatureLow\":55.32,\"apparentTemperatureLowTime\":1557550800,\"dewPoint\":59.01,\"humidity\":0.9,\"pressure\":1015.41,\"windSpeed\":9.95,\"windGust\":26.61,\"windGustTime\":1557442800,\"windBearing\":223,\"cloudCover\":0.95,\"uvIndex\":4,\"uvIndexTime\":1557486000,\"visibility\":8.43,\"ozone\":318.53,\"temperatureMin\":59.32,\"temperatureMinTime\":1557525600,\"temperatureMax\":64.5,\"temperatureMaxTime\":1557507600,\"apparentTemperatureMin\":59.32,\"apparentTemperatureMinTime\":1557525600,\"apparentTemperatureMax\":64.51,\"apparentTemperatureMaxTime\":1557507600},{\"time\":1557529200,\"summary\":\"Clear throughout the day.\",\"icon\":\"clear-day\",\"sunriseTime\":1557552197,\"sunsetTime\":1557603685,\"moonPhase\":0.23,\"precipIntensity\":0.0007,\"precipIntensityMax\":0.0044,\"precipIntensityMaxTime\":1557529200,\"precipProbability\":0.08,\"precipType\":\"rain\",\"temperatureHigh\":71.5,\"temperatureHighTime\":1557583200,\"temperatureLow\":56.73,\"temperatureLowTime\":1557637200,\"apparentTemperatureHigh\":71.5,\"apparentTemperatureHighTime\":1557583200,\"apparentTemperatureLow\":56.73,\"apparentTemperatureLowTime\":1557637200,\"dewPoint\":52.75,\"humidity\":0.72,\"pressure\":1021.18,\"windSpeed\":7.11,\"windGust\":17.07,\"windGustTime\":1557594000,\"windBearing\":351,\"cloudCover\":0.04,\"uvIndex\":9,\"uvIndexTime\":1557576000,\"visibility\":10,\"ozone\":319.31,\"temperatureMin\":55.32,\"temperatureMinTime\":1557550800,\"temperatureMax\":71.5,\"temperatureMaxTime\":1557583200,\"apparentTemperatureMin\":55.32,\"apparentTemperatureMinTime\":1557550800,\"apparentTemperatureMax\":71.5,\"apparentTemperatureMaxTime\":1557583200},{\"time\":1557615600,\"summary\":\"Partly cloudy throughout the day.\",\"icon\":\"partly-cloudy-day\",\"sunriseTime\":1557638535,\"sunsetTime\":1557690146,\"moonPhase\":0.27,\"precipIntensity\":0.0001,\"precipIntensityMax\":0.0001,\"precipIntensityMaxTime\":1557644400,\"precipProbability\":0.01,\"precipType\":\"rain\",\"temperatureHigh\":83.88,\"temperatureHighTime\":1557673200,\"temperatureLow\":65.6,\"temperatureLowTime\":1557723600,\"apparentTemperatureHigh\":83.88,\"apparentTemperatureHighTime\":1557673200,\"apparentTemperatureLow\":65.6,\"apparentTemperatureLowTime\":1557723600,\"dewPoint\":52.94,\"humidity\":0.56,\"pressure\":1019.34,\"windSpeed\":4.88,\"windGust\":21.64,\"windGustTime\":1557644400,\"windBearing\":55,\"cloudCover\":0.3,\"uvIndex\":8,\"uvIndexTime\":1557662400,\"visibility\":10,\"ozone\":298.49,\"temperatureMin\":56.73,\"temperatureMinTime\":1557637200,\"temperatureMax\":83.88,\"temperatureMaxTime\":1557673200,\"apparentTemperatureMin\":56.73,\"apparentTemperatureMinTime\":1557637200,\"apparentTemperatureMax\":83.88,\"apparentTemperatureMaxTime\":1557673200},{\"time\":1557702000,\"summary\":\"Overcast throughout the day.\",\"icon\":\"cloudy\",\"sunriseTime\":1557724875,\"sunsetTime\":1557776606,\"moonPhase\":0.31,\"precipIntensity\":0,\"precipIntensityMax\":0.0001,\"precipIntensityMaxTime\":1557770400,\"precipProbability\":0,\"temperatureHigh\":85.22,\"temperatureHighTime\":1557752400,\"temperatureLow\":64.98,\"temperatureLowTime\":1557810000,\"apparentTemperatureHigh\":85.22,\"apparentTemperatureHighTime\":1557752400,\"apparentTemperatureLow\":64.98,\"apparentTemperatureLowTime\":1557810000,\"dewPoint\":57.11,\"humidity\":0.54,\"pressure\":1015.29,\"windSpeed\":3.49,\"windGust\":16.86,\"windGustTime\":1557727200,\"windBearing\":48,\"cloudCover\":0.94,\"uvIndex\":5,\"uvIndexTime\":1557748800,\"visibility\":10,\"ozone\":291.51,\"temperatureMin\":65.6,\"temperatureMinTime\":1557723600,\"temperatureMax\":85.22,\"temperatureMaxTime\":1557752400,\"apparentTemperatureMin\":65.6,\"apparentTemperatureMinTime\":1557723600,\"apparentTemperatureMax\":85.22,\"apparentTemperatureMaxTime\":1557752400}]},\"offset\":1}";
        JsonObject expResultObj = new JsonParser().parse(expResultStr).getAsJsonObject();
        JsonArray expResult = expResultObj.getAsJsonObject("daily").getAsJsonArray("data");
        // act
        JsonArray result = instance.getWeatherForecast(path, headers);
        // assert
        assertThat(result.size()).isEqualTo(expResult.size());
        assertThat(result.get(0).getAsJsonObject().keySet()).isEqualTo(expResult.get(0).getAsJsonObject().keySet());
    }
    
}
